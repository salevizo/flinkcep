package hes.cs63.CEPMonitor.CoGHeading;

import com.github.davidmoten.geo.GeoHash;
import hes.cs63.CEPMonitor.AisMessage;


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
public class CourseHeading {

	static int minDiff=10;
	static int maxDiff=60;
	static double max_speed=48.6;
	static double min_speed=2.7;
	public static Pattern<AisMessage, ?> patternSpaciousHeading(){
		Pattern<AisMessage, ?> spaciousHeading = Pattern.<AisMessage>begin("suspicious_heading_start", AfterMatchSkipStrategy.skipPastLastEvent())
        .where(new SimpleCondition<AisMessage>() {
					@Override
					 public boolean filter(AisMessage event) throws Exception {
						float courseDiffToHead=(Math.abs(event.getHeading()-event.getCourse()));
						if (courseDiffToHead>minDiff && courseDiffToHead<maxDiff && event.getSpeed()>min_speed && event.getSpeed()<max_speed) {
							return true;
						} else {
							return false;
						}
					}

				});

		return spaciousHeading;
	}
	
	

	public static DataStream<SuspiciousCourseHeading> suspiciousSpeedVesselTypeStream(PatternStream<AisMessage> patternStream){
        DataStream<SuspiciousCourseHeading>  courseHeading  = patternStream.select(new PatternSelectFunction<AisMessage, SuspiciousCourseHeading>() {
            @Override
            public SuspiciousCourseHeading select(Map<String,List<AisMessage>> pattern) throws Exception {
				AisMessage msg = (AisMessage) pattern.get("suspicious_heading_start").get(0);
				return new SuspiciousCourseHeading(msg.getMmsi(),msg.getHeading(),msg.getCourse(),msg.getLon(),msg.getLat(),msg.getT());
            }
        });

        return courseHeading;
    }
}
