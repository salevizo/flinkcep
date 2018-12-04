package hes.cs63.CEPMonitor.CourseHeadDiff;

import com.github.davidmoten.geo.GeoHash;


import hes.cs63.CEPMonitor.CourseHeadDiff.vesselsInDanger;
import hes.cs63.CEPMonitor.receivedClasses.courseHead;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.nfa.aftermatch.AfterMatchSkipStrategy;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.IterativeCondition;
import org.apache.flink.shaded.guava18.com.google.common.collect.Lists;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.cep.pattern.conditions.IterativeCondition.Context;
public class courseHeadDiff {

    static int messagesTimeGap=20;
    static int problematicRouteTime=120;

    public static Pattern<courseHead, ?> patternSpaciousHeading(){
        Pattern<courseHead, ?> spaciousHeading = Pattern.<courseHead>begin("suspicious_heading_start", AfterMatchSkipStrategy.skipPastLastEvent())
                .oneOrMore()
                .followedBy("suspicious_heading_stop")
                .where(new IterativeCondition<courseHead>() {
                    @Override
                    public boolean filter(courseHead event, Context<courseHead> ctx) throws Exception {
                        int base = event.getTimestamp();
                        int currTime = event.getTimestamp();
                        List<courseHead> l = Lists.newArrayList(ctx.getEventsForPattern("suspicious_heading_start"));
                        for (courseHead ev : Lists.reverse(l)) {
                            if ((currTime - ev.getTimestamp()) < messagesTimeGap) {
                                if ((base - ev.getTimestamp()) > problematicRouteTime) {
                                    return true;
                                } else {
                                    currTime = ev.getTimestamp();
                                }
                            } else {
                                return false;
                            }
                        }
                        return false;
                    }

                })
                .within(Time.seconds(300));

        return spaciousHeading;
    }



    public static DataStream<vesselsInDanger> suspiciousSpeedVesselTypeStream(PatternStream<courseHead> patternStream){
        DataStream<vesselsInDanger>  rendezvouz  = patternStream.select(new PatternSelectFunction<courseHead, vesselsInDanger>() {
            @Override
            public vesselsInDanger select(Map<String,List<courseHead>> pattern) throws Exception {
                courseHead suspicious_heading_start = (courseHead) pattern.get("suspicious_heading_stop").get(0);
                vesselsInDanger suspHead=new vesselsInDanger(suspicious_heading_start.getMmsi(),
                        suspicious_heading_start.getHeading(),suspicious_heading_start.getCourse(),
                        suspicious_heading_start.getLon(),suspicious_heading_start.getLat(),suspicious_heading_start.getTimestamp());


                for(int i=0;i<pattern.get("suspicious_heading_start").size();i++) {
                    courseHead in_msg = pattern.get("suspicious_heading_start").get(i);
                    suspHead.getMsgs().add(new vesselsInDanger(in_msg.getMmsi(),
                            in_msg.getHeading(), in_msg.getCourse(),
                            in_msg.getLon(), in_msg.getLat(), in_msg.getTimestamp()));
                }
                return suspHead;
            }
        });

        return rendezvouz;
    }
}
