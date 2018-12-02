package hes.cs63.CEPMonitor.CoTravellingVessels;


import hes.cs63.CEPMonitor.receivedClasses.CoTravelInfo;
import hes.cs63.CEPMonitor.receivedClasses.GapMessage;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.nfa.aftermatch.AfterMatchSkipStrategy;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.IterativeCondition;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.shaded.guava18.com.google.common.collect.Lists;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;




public class coTravellingVessels {
    static int coTravelTime=35;
    static int coTravellingTotalTime=300;
    public static Pattern<CoTravelInfo, ?> patternSuspiciousCoTravel(){
        Pattern<CoTravelInfo, ?> coTravelattern = Pattern.<CoTravelInfo>begin("msg_1",AfterMatchSkipStrategy.skipPastLastEvent())
                .subtype(CoTravelInfo.class)
                .oneOrMore()
                .followedBy("msg_2")
                .where(new IterativeCondition<CoTravelInfo>() {
                    @Override
                    public boolean filter(CoTravelInfo event, Context<CoTravelInfo> ctx) throws Exception{
                            int base = event.getTimestamp();
                            int currTime = event.getTimestamp();
                            List<CoTravelInfo> l = Lists.newArrayList(ctx.getEventsForPattern("msg_1"));
                            for (CoTravelInfo ev : Lists.reverse(l)) {
                                if ((currTime - ev.getTimestamp()) < coTravelTime) {
                                    if (event.getMmsi_2() == ev.getMmsi_2()) {
                                        if ((base - ev.getTimestamp()) > coTravellingTotalTime) {
                                            return true;
                                        } else {
                                            currTime = ev.getTimestamp();
                                        }
                                    }
                                } else {
                                    return false;
                                }
                            }
                            return false;
                    }})
                .within(Time.seconds(600));
        return coTravelattern;
    }

    public static DataStream<SuspiciousCoTravellingVessels> coTravellingDatastream(PatternStream<CoTravelInfo> patternStream){
        DataStream<SuspiciousCoTravellingVessels>  rendezvouz = patternStream.select(new PatternSelectFunction<CoTravelInfo, SuspiciousCoTravellingVessels>() {
            @Override
            public SuspiciousCoTravellingVessels select(Map<String,List<CoTravelInfo>> pattern) throws Exception {
                CoTravelInfo msg = (CoTravelInfo) pattern.get("msg_2").get(0);
                SuspiciousCoTravellingVessels coTravel=new SuspiciousCoTravellingVessels(msg.getMmsi_1(),msg.getMmsi_2(),msg.getLon1(),msg.getLat1(),msg.getLon2(),msg.getLon2(),msg.getTimestamp());

                for(int i=0;i<pattern.get("msg_1").size();i++){
                    CoTravelInfo in_msg=pattern.get("msg_1").get(i);
                    coTravel.getMsgs().add(new SuspiciousCoTravellingVessels(in_msg.getMmsi_1(),in_msg.getMmsi_2(),in_msg.getLon1(),in_msg.getLat1(),in_msg.getLon2(),in_msg.getLon2(),in_msg.getTimestamp()));
                }
                return coTravel;
            }
        });

        return rendezvouz;
    }
}
