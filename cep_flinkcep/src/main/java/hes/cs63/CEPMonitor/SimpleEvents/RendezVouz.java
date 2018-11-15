package hes.cs63.CEPMonitor.SimpleEvents;

import hes.cs63.CEPMonitor.GapMessage;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.IterativeCondition;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.util.List;
import java.util.Map;

public class RendezVouz {
    public static Pattern<GapMessage, ?> patternGap(){
        Pattern<GapMessage, ?> alarmPattern = Pattern.<GapMessage>begin("Vessel_1")
                .subtype(GapMessage.class)
                .followedBy("Vessel_2")
                .subtype(GapMessage.class)
                .where(new IterativeCondition<GapMessage>() {
                    @Override
                    public boolean filter(GapMessage event, Context<GapMessage> ctx) throws Exception {
                        System.out.println("GRAVALOS");
                        for (GapMessage ev : ctx.getEventsForPattern("Vessel_1")) {
                            if(Math.abs(ev.getGapEnd()-event.getGapEnd())<60
                            && ev.getGeoHash().equals(event.getGeoHash())){
                                return true;
                            }
                            else{
                                String t="LIAKOS1 :"+ev.getGeoHash()+"|||"+ev.getMmsi()+"|||"+ev.getGapEnd()+"|||"+event.getMmsi()+"|||"+event.getGeoHash()+"|||"+event.getGapEnd();
                                boolean gh=(ev.getGeoHash()==event.getGeoHash());
                                boolean ge=(Math.abs(ev.getGapEnd()-event.getGapEnd())<60);
                                System.out.println("LIAKOS11:"+gh+"2:"+ge);
                                System.out.println(t);
                                return false;
                            }
                        }
                        String t="LIAKOS2 :"+event.getMmsi()+"|||"+event.getGeoHash();
                        System.out.println(t);
                        return false;
                }})
                .within(Time.seconds(10));
        return alarmPattern;
    }

    public static DataStream<SuspiciousRendezVouz> alarmsGap(PatternStream<GapMessage> patternStream){
        DataStream<SuspiciousRendezVouz>  alarms = patternStream.select(new PatternSelectFunction<GapMessage, SuspiciousRendezVouz>() {
            @Override
            public SuspiciousRendezVouz select(Map<String,List<GapMessage>> pattern) throws Exception {
                GapMessage vessel_1 = (GapMessage) pattern.get("Vessel_1").get(0);
                GapMessage vessel_2 = (GapMessage) pattern.get("Vessel_2").get(0);
                return new SuspiciousRendezVouz(vessel_1.getMmsi(),vessel_2.getMmsi(),vessel_1.getGeoHash(),vessel_1.getGapEnd(),vessel_2.getGapEnd());
            }
        });

        return alarms;
    }
}
