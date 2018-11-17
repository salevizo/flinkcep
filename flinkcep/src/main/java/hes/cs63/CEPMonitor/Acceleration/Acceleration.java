package hes.cs63.CEPMonitor.Acceleration;

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

public class Acceleration {
    public static Pattern<AisMessage, ?> patternAcceleration(){
        Pattern<AisMessage, ?> alarmPattern = Pattern.<AisMessage>begin("accelaration_start")
                .subtype(AisMessage.class)
                .followedBy("accelaration_end")
                .subtype(AisMessage.class)
                .where(new IterativeCondition<AisMessage>() {
                    @Override
                    public boolean filter(AisMessage event, Context<AisMessage> ctx) throws Exception {
                        System.out.println("accelaration");
                        for (AisMessage ev : ctx.getEventsForPattern("accelaration_start")) {
                        	//20KNOTS bigger speed at 1 min 
                        	//TODO CHECK IF THE GEO IS IN PORT, AS IT IS NORMAL A HIGHT ACCELARATION
                      //TO CHANGE IT
                        	
                        if (ev.getT()>event.getT()) {
                        	if(((ev.getSpeed()-event.getSpeed())>=10 && (ev.getT()-event.getT())<50)){
                            	
                                
                                    return true;
                                }
                        }else if (ev.getT()<=event.getT()) {
                        	if(((event.getSpeed()-ev.getSpeed())>=10 && (event.getT()-ev.getT())<50)){
                            	
                                
                                    return true;
                                }
                        }
                        	
                            else{
                               return false;
                            }
                        }
                        return false;
                }})
                .within(Time.seconds(10));
        return alarmPattern;
    }

    public static DataStream<SuspiciousAcceleration> suspiciousAccelerationsStream(PatternStream<AisMessage> patternStream){
        DataStream<SuspiciousAcceleration>  alarms = patternStream.select(new PatternSelectFunction<AisMessage, SuspiciousAcceleration>() {
            @Override
            public SuspiciousAcceleration select(Map<String,List<AisMessage>> pattern) throws Exception {
                AisMessage accelaration_start = (AisMessage) pattern.get("accelaration_start").get(0);
                AisMessage accelaration_end = (AisMessage) pattern.get("accelaration_end").get(0);

                LinkedList<Float> tempList=new LinkedList<Float>();
                tempList.add(Math.abs((accelaration_start.getSpeed())));

                String geoHash= GeoHash.encodeHash(accelaration_start.getLat(),accelaration_start.getLon());     
               return new SuspiciousAcceleration(accelaration_start.getMmsi(),accelaration_start.getLon(),accelaration_start.getLat(),accelaration_start.getSpeed(),accelaration_end.getSpeed(), geoHash,accelaration_start.getT(),accelaration_end.getT());
            
            
            
            }
        });

        return alarms;
    }
}
