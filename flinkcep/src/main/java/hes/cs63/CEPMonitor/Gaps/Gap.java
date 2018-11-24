package hes.cs63.CEPMonitor.Gaps;

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
    private static int gapTime=120;
    private static int geoHashLen=5;
    private static int patternTime=1200;
    public static Pattern<AisMessage, ?> patternGap(){
        Pattern<AisMessage, ?> rendezvouzPattern = Pattern.<AisMessage>begin("gap_start")
                .subtype(AisMessage.class)
                .next("gap_end")
                .subtype(AisMessage.class)
                .where(new IterativeCondition<AisMessage>() {
                    @Override
                    public boolean filter(AisMessage event, Context<AisMessage> ctx) throws Exception {
                        for (AisMessage ev : ctx.getEventsForPattern("gap_start")) {
                            //VARIABLE THINK:TIME
                            if((event.getT()-ev.getT())>gapTime && (event.getT()-ev.getT())>0){
                                return true;
                            }
                            else{
                                return false;
                            }
                        }
                        return false;
                }})
                .within(Time.seconds(patternTime));
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
                String geoHash= GeoHash.encodeHash(gap_end.getLat(),gap_end.getLon(),geoHashLen);
                return new SuspiciousGap(gap_start.getMmsi(),gap_end.getLat(),gap_end.getLon(),gap_start.getT(),gap_end.getT(),geoHash);
            }
        });

        return rendezvouz;
    }
}
