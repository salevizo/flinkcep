package hes.cs63.CEPMonitor.VesselsCoTravel;

import com.github.davidmoten.geo.GeoHash;
import hes.cs63.CEPMonitor.AisMessage;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.IterativeCondition;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.util.List;
import java.util.Map;

public class coTravel {
    public static Pattern<AisMessage, ?> patternCoTravel(){
        Pattern<AisMessage, ?> coTravelPattern = Pattern.<AisMessage>begin("vessel_1")
                .subtype(AisMessage.class)
                .where(
                        new SimpleCondition<AisMessage>() {
                            @Override
                            public boolean filter(AisMessage event) {
                                System.out.println("YAYA19921="+event.getMmsi());
                                return true;
                            }
                        }
                ).followedByAny("vessel_2")
                .subtype(AisMessage.class)
                .where(new IterativeCondition<AisMessage>() {
                    @Override
                    public boolean filter(AisMessage event, Context<AisMessage> ctx) throws Exception {
                        int base=event.getT();
                        int time=0;
                        for (AisMessage ev : ctx.getEventsForPattern("vessel_1")) {
                            String geoHash1=GeoHash.encodeHash(ev.getLat(),ev.getLon(),4);
                            String geoHash2=GeoHash.encodeHash(event.getLat(),event.getLon(),4);
                            System.out.println("YAYA19922:geoHash="+geoHash1+"|"+geoHash2+"||[TIMESTAMPS="+ev.getT()+"__"+event.getT()+"__"+Math.abs(ev.getT()-event.getT())+"]|||"+ev.getSpeed()+"|||"+event.getSpeed()+"--|"+time+"..(2)"+event.getMmsi()+"(1)"+ev.getMmsi()+"--|");
                            System.out.println("YAYA19923:|"+(Math.abs(ev.getT()-event.getT())<30)+"-"+(geoHash1.equals(geoHash2)==true)+"-"+(ev.getSpeed()>1)+"-"+(ev.getSpeed()>1)+"|");
                            if(Math.abs(ev.getT()-event.getT())<30
                            && geoHash1.equals(geoHash2)==true
                            && ev.getSpeed()>1
                            && event.getSpeed()>1
                            && ev.getMmsi()!=event.getMmsi()){
                                return true;
                            }
                            else{
                                return false;
                            }
                        }
                        return false;
                }})
                .within(Time.seconds(10));
        return coTravelPattern;
    }

    public static DataStream<coTravelInfo> suspiciousCoTravelStream(PatternStream<AisMessage> patternStream){
        DataStream<coTravelInfo>  coTravel = patternStream.select(new PatternSelectFunction<AisMessage, coTravelInfo>() {
            @Override
            public coTravelInfo select(Map<String,List<AisMessage>> pattern) throws Exception {
                AisMessage vessel_1 = (AisMessage) pattern.get("vessel_1").get(0);
                AisMessage vessel_2 = (AisMessage) pattern.get("vessel_2").get(0);

                return new coTravelInfo(vessel_1.getMmsi(),vessel_2.getMmsi(),vessel_1.getLon(),vessel_1.getLat(),vessel_2.getLon(),vessel_2.getLat(),vessel_2.getT());
            }
        });

        return coTravel;
    }
}
