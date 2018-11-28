package hes.cs63.CEPMonitor.CoGHeading;

import com.github.davidmoten.geo.GeoHash;
import hes.cs63.CEPMonitor.AisMessage;


import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.nfa.aftermatch.AfterMatchSkipStrategy;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.IterativeCondition;
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
public class CourseHeading {



	
	public static Pattern<AisMessage, ?> patternSpaciousHeading(){
		Pattern<AisMessage, ?> spaciousHeading = Pattern.<AisMessage>begin("suspicious_heading_start")
				.where(new SimpleCondition<AisMessage>() {
					@Override
					public boolean filter(AisMessage ev) throws Exception {
						//to check the difference
						if  ((Math.abs(ev.getHeading()-ev.getCourse())> 30)) {

									return true;
								} else {
									return false;
								}
							

						}
					

				}

						);

		return spaciousHeading;
	}

	public static DataStream<SuspiciousCourseHeading> suspiciousSpeedVesselTypeStream(PatternStream<AisMessage> patternStream){
        DataStream<SuspiciousCourseHeading>  rendezvouz  = patternStream.select(new PatternSelectFunction<AisMessage, SuspiciousCourseHeading>() {
            @Override
            public SuspiciousCourseHeading select(Map<String,List<AisMessage>> pattern) throws Exception {
                AisMessage suspicious_heading_start = (AisMessage) pattern.get("suspicious_heading_start").get(0);
              
                return new SuspiciousCourseHeading(suspicious_heading_start.getMmsi(),suspicious_heading_start.getHeading(),suspicious_heading_start.getCourse());
            }
        });

        return rendezvouz;
    }
}
