package hes.cs63.CEPMonitor;

import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public  class CEPFunction {

	
	//ZIGZAG
    static Pattern<AisMessage, ?> patternZigZag(){
        Pattern<AisMessage, ?> alarmPattern = Pattern.<AisMessage>begin("first")
                .subtype(AisMessage.class)
                .where(new SimpleCondition<AisMessage>() {
                    @Override
                    public boolean filter(AisMessage event) {
                        return event.getStatus()!=8;
                    }
                })
                .followedBy("middle")
                .subtype(AisMessage.class)
                .where(new SimpleCondition<AisMessage>() {
                    @Override
                    public boolean filter(AisMessage event) {
                        System.out.println(event.getStatus() !=8);
                        return event.getStatus() !=8;
                    }
                })
                .followedBy("last")
                .subtype(AisMessage.class)
                .where(new SimpleCondition<AisMessage>() {
                    @Override
                    public boolean filter(AisMessage event) {
                        return event.getStatus() !=8;
                    }
                })
                .within(Time.seconds(10));
        return alarmPattern;
    }

    static DataStream<SuspiciousTurn> alarmsZigZag(PatternStream<AisMessage> patternStream){
        DataStream<SuspiciousTurn>  alarms = patternStream.select(new PatternSelectFunction<AisMessage, SuspiciousTurn>() {
            @Override
            public SuspiciousTurn select(Map<String,List<AisMessage>> pattern) throws Exception {
                AisMessage first = (AisMessage) pattern.get("first").get(0);
                AisMessage last = (AisMessage) pattern.get("last").get(0);
                AisMessage middle = (AisMessage) pattern.get("middle").get(0);

                LinkedList<Float> tempList=new LinkedList<Float>();
                tempList.add(Math.abs((first.getTurn())));


                tempList.add(Math.abs((last.getTurn())));
                tempList.add(Math.abs((middle.getTurn())));
                return new SuspiciousTurn(first.getMmsi(),tempList);
            }
        });
        return alarms;
    }

    
	//HIGH ACCELARATE
    static Pattern<AisMessage, ?> patternSuspiciousAccelarate(){
        Pattern<AisMessage, ?> alarmPattern = Pattern.<AisMessage>begin("first")
                .subtype(AisMessage.class)
                .where(new SimpleCondition<AisMessage>() {
                    @Override
                    public boolean filter(AisMessage event) {
                        return event.getSpeed()>=0;
                    }
                })
                .followedBy("middle")
                .subtype(AisMessage.class)
                .where(new SimpleCondition<AisMessage>() {
                    @Override
                    public boolean filter(AisMessage event) {
                        System.out.println(event.getStatus() !=8);
                        return event.getSpeed()!=0;
                    }
                })
                .followedBy("last")
                .subtype(AisMessage.class)
                .where(new SimpleCondition<AisMessage>() {
                    @Override
                    public boolean filter(AisMessage event) {
                    	return event.getSpeed()!=0;
                    }
                })
                .within(Time.seconds(10));
        return alarmPattern;
    }
    
    
    static DataStream<SuspiciousAccelarate> alarmsSuspiciousAccelarate(PatternStream<AisMessage> patternStream){
        DataStream<SuspiciousAccelarate>  alarms = patternStream.select(new PatternSelectFunction<AisMessage, SuspiciousAccelarate>() {
            @Override
            public SuspiciousAccelarate select(Map<String,List<AisMessage>> pattern) throws Exception {
                AisMessage first = (AisMessage) pattern.get("first").get(0);
                AisMessage last = (AisMessage) pattern.get("last").get(0);
                AisMessage middle = (AisMessage) pattern.get("middle").get(0);

                LinkedList<Float> tempList=new LinkedList<Float>();
                tempList.add(Math.abs((first.getSpeed())));


                tempList.add(Math.abs((last.getSpeed())));
                tempList.add(Math.abs((middle.getSpeed())));
                return new SuspiciousAccelarate(first.getMmsi(),tempList);
            }
        });
        return alarms;
    }
    
    
    //GAP FOR 10 MINS
    static Pattern<AisMessage, ?> patternSuspiciousGap(){
        Pattern<AisMessage, ?> alarmPattern = Pattern.<AisMessage>begin("first")
                .subtype(AisMessage.class)
                .where(new SimpleCondition<AisMessage>() {
                    @Override
                    public boolean filter(AisMessage event) {
                        return event.getT()>=0;
                    }
                })
                .followedBy("middle")
                .subtype(AisMessage.class)
                .where(new SimpleCondition<AisMessage>() {
                    @Override
                    public boolean filter(AisMessage event) {
                      return event.getT()>0;
                    }
                })
                .followedBy("last")
                .subtype(AisMessage.class)
                .where(new SimpleCondition<AisMessage>() {
                    @Override
                    public boolean filter(AisMessage event) {
                    	  return event.getT()>0;
                    }
                })
                .within(Time.seconds(10));
        return alarmPattern;
    }
    
    
    static DataStream<SuspiciousGap> alarmsSuspiciousGap(PatternStream<AisMessage> patternStream){
        DataStream<SuspiciousGap>  alarms = patternStream.select(new PatternSelectFunction<AisMessage, SuspiciousGap>() {
            @Override
            public SuspiciousGap select(Map<String,List<AisMessage>> pattern) throws Exception {
                AisMessage first = (AisMessage) pattern.get("first").get(0);
                AisMessage last = (AisMessage) pattern.get("last").get(0);
                AisMessage middle = (AisMessage) pattern.get("middle").get(0);

                LinkedList<Integer> tempList=new LinkedList<Integer>();
                tempList.add(Math.abs((first.getT())));

                tempList.add(Math.abs((last.getT())));
                tempList.add(Math.abs((middle.getT())));
                return new SuspiciousGap(first.getMmsi(),tempList);
            }
        });
       return alarms;   
       }
    
}
