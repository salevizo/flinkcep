package hes.cs63.CEPMonitor.SimpleEvents;

import com.github.davidmoten.geo.GeoHash;
import hes.cs63.CEPMonitor.AisMessage;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.IterativeCondition;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.windowing.time.Time;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Gap {
    public static Pattern<AisMessage, ?> patternGap(){
        Pattern<AisMessage, ?> alarmPattern = Pattern.<AisMessage>begin("gap_start")
                .subtype(AisMessage.class)
                .followedBy("gap_end")
                .subtype(AisMessage.class)
                .where(new IterativeCondition<AisMessage>() {
                    @Override
                    public boolean filter(AisMessage event, Context<AisMessage> ctx) throws Exception {
                        System.out.println("ISILTHA");
                        for (AisMessage ev : ctx.getEventsForPattern("gap_start")) {
                            if(ev.getT()-event.getT()>600){
                                return true;
                            }
                            else{
                                System.out.println("NON FOR :"+ev.getT()+"|||"+ev.getMmsi()+"|||"+event.getT()+"|||"+event.getMmsi());
                                return false;
                            }
                        }
                        return false;
                }})
                .within(Time.seconds(10));
        return alarmPattern;
    }

    public static DataStream<SuspiciousGap> alarmsGap(PatternStream<AisMessage> patternStream){
        DataStream<SuspiciousGap>  alarms = patternStream.select(new PatternSelectFunction<AisMessage, SuspiciousGap>() {
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

        return alarms;
    }
}
