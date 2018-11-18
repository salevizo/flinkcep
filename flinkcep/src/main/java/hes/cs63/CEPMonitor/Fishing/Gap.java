package hes.cs63.CEPMonitor.Fishing;

import com.github.davidmoten.geo.GeoHash;
import hes.cs63.CEPMonitor.AisMessage;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.IterativeCondition;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Gap {
    public static Pattern<AisMessage, ?> patternGap(){
        Pattern<AisMessage, ?> rendezvouzPattern = Pattern.<AisMessage>begin("start")
                .subtype(AisMessage.class)
                .followedBy("change in heading")
                .subtype(AisMessage.class)
                .where(new IterativeCondition<AisMessage>() {
                    @Override
                    public boolean filter(AisMessage event, Context<AisMessage> ctx) throws Exception {
                        for (AisMessage ev : ctx.getEventsForPattern("start")) {
                            if(Math.abs(ev.getHeading()-event.getHeading())>60){
                                return true;
                            }
                            else{
                                return false;
                            }
                        }
                        return false;
                }})
                .subtype(AisMessage.class)
                .followedBy("gap_start")
                .followedBy("gap_end")
                .subtype(AisMessage.class)
                .where(new IterativeCondition<AisMessage>() {
                    @Override
                    public boolean filter(AisMessage event, Context<AisMessage> ctx) throws Exception {
                        for (AisMessage ev : ctx.getEventsForPattern("gap_start")) {
                            if(Math.abs(ev.getT()-event.getT())>60){
                                return true;
                            }
                            else{
                                return false;
                            }
                        }
                        return false;
                    }})
                .followedBy("change in heading")
                .within(Time.seconds(10));
        return rendezvouzPattern;
    }

    public static DataStream<SuspiciousGap> suspiciousGapsStream(PatternStream<AisMessage> patternStream){
        DataStream<SuspiciousGap>  rendezvouz  = patternStream.select(new PatternSelectFunction<AisMessage, SuspiciousGap>() {
            @Override
            public SuspiciousGap select(Map<String,List<AisMessage>> pattern) throws Exception {
                AisMessage gap_start = (AisMessage) pattern.get("gap_start").get(0);
                AisMessage gap_end = (AisMessage) pattern.get("gap_end").get(0);

                LinkedList<Float> tempList=new LinkedList<Float>();
                tempList.add(Math.abs((gap_start.getTurn())));
                String geoHash= GeoHash.encodeHash(gap_end.getLat(),gap_end.getLon());
                return new SuspiciousGap(gap_start.getMmsi(),gap_end.getLat(),gap_end.getLon(),gap_start.getT(),gap_end.getT(),geoHash);
            }
        });

        return rendezvouz;
    }
}
