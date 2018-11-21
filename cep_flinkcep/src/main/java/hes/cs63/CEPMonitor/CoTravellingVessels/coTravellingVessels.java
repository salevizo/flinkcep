package hes.cs63.CEPMonitor.CoTravellingVessels;


import hes.cs63.CEPMonitor.receivedClasses.CoTravelInfo;
import hes.cs63.CEPMonitor.receivedClasses.GapMessage;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
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
    static int coTravelTime=30;
    static int coTravellingTotalTime=60;
    static int patternTime=10;
    public static Pattern<CoTravelInfo, ?> patternSuspiciousCoTravel(){
        Pattern<CoTravelInfo, ?> coTravelattern = Pattern.<CoTravelInfo>begin("msg_1")
                .subtype(CoTravelInfo.class)
                .oneOrMore()
                .followedBy("msg_2")
                .where(new IterativeCondition<CoTravelInfo>() {
                    @Override
                    public boolean filter(CoTravelInfo event, Context<CoTravelInfo> ctx) throws Exception {
                        int base=event.getTimestamp();
                        int currTime=event.getTimestamp();
                        List<CoTravelInfo> l=Lists.newArrayList(ctx.getEventsForPattern("msg_1"));
                        LinkedList<String> s=new LinkedList<String>();
                        for (CoTravelInfo ev : Lists.reverse(l)) {
                            //System.out.println("ALEKARAS112="+event.getMmsi_1()+"-"+ev.getMmsi_2()+"-"+ev.getTimestamp()+"-(base event)"+base+"-(previterationEvenTime)"+currTime);
                            //System.out.println("LENGTH IS1 ="+l.size());
                            //allagi seiras,prota na mpei to mmsi check

                            if((currTime-ev.getTimestamp())<coTravelTime && (currTime-ev.getTimestamp())>0) {
                                if (event.getMmsi_2() == ev.getMmsi_2()) {
                                    if ((base - ev.getTimestamp()) > coTravellingTotalTime) {
                                        //String f = "";
                                        //f = f + ev.getTimestamp();
                                        /*for (String m : s) {
                                            f = f + "-" + m;
                                        }

                                        System.out.println("ACCEPTED MALAKA=" + f);
                                        System.out.println("ALEKARAS123=" + event.getMmsi_1() + "-" + ev.getMmsi_2());*/
                                        return true;
                                    } else {
                                        //System.out.println("ELSE INNER");
                                        s.add(Float.toString(ev.getTimestamp()));
                                        currTime = ev.getTimestamp();
                                    }
                                }
                            }

                            else{
                                //isos prepei na vgei giati tha akirwnei an vrw ena akiro id endiamesa
                                //System.out.println("ELSE OUTER");
                                return false;
                            }
                        }
                        return false;
                    }})
                .within(Time.seconds(patternTime));
        return coTravelattern;
    }

    public static DataStream<SuspiciousCoTravellingVessels> coTravellingDatastream(PatternStream<CoTravelInfo> patternStream){
        DataStream<SuspiciousCoTravellingVessels>  rendezvouz = patternStream.select(new PatternSelectFunction<CoTravelInfo, SuspiciousCoTravellingVessels>() {
            @Override
            public SuspiciousCoTravellingVessels select(Map<String,List<CoTravelInfo>> pattern) throws Exception {
                //SuspiciousCoTravellingVessels vessel_1 = (SuspiciousCoTravellingVessels) pattern.get("Vessel_1").get(0);
                CoTravelInfo msg = (CoTravelInfo) pattern.get("msg_2").get(0);
                //System.out.println("RETURNING="+msg.getMmsi_1()+"\\"+msg.getMmsi_2()+"\\"+msg.getLon1()+"\\"+msg.getTimestamp());
                return new SuspiciousCoTravellingVessels(msg.getMmsi_1(),msg.getMmsi_2(),msg.getLon1(),msg.getLat1(),msg.getLon2(),msg.getLon2(),msg.getTimestamp());
            }
        });

        return rendezvouz;
    }
}
