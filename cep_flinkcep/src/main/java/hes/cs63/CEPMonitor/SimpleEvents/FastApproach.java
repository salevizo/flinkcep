package hes.cs63.CEPMonitor.SimpleEvents;

import hes.cs63.CEPMonitor.receivedClasses.AccelerationMessage;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.IterativeCondition;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.windowing.time.Time;

import com.github.davidmoten.geo.GeoHash;

import java.util.List;
import java.util.Map;
import java.util.*;


import java.io.BufferedReader;
import java.io.FileReader;


public class FastApproach {
	
public static Float distanceFor2vessels=Float.valueOf("0");

	 public static Boolean distance(Float lon1, Float lat1, ArrayList<ArrayList<Float>> listOfPorts) {
		 for (int i=0; i<listOfPorts.size(); i++) {
		  Float lat2 = listOfPorts.get(i).get(0);
			  Float lon2= listOfPorts.get(i).get(1);
				 Float theta = lon1 - lon2;
				 Double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
					dist = Math.acos(dist);
					dist = Math.toDegrees(dist);
					dist = dist * 60 * 1.1515;
					
						dist = dist * 1.609344;
			    //20km away form port
			    if (dist<20) {
			    	return true;
			    }
		 }
		 return false; 
	 }

    public static Pattern<AccelerationMessage, ?> patternFastApproach(){
        Pattern<AccelerationMessage, ?> FastApproachPattern = Pattern.<AccelerationMessage>begin("Vessel_1")
                .subtype(AccelerationMessage.class)
                .followedBy("Vessel_2")
                .subtype(AccelerationMessage.class)
                .where(new IterativeCondition<AccelerationMessage>() {
                    @Override
                    public boolean filter(AccelerationMessage event, Context<AccelerationMessage> ctx) throws Exception {
                        for (AccelerationMessage ev : ctx.getEventsForPattern("Vessel_1")) {
                        	
                            if(( distance(ev.getLon(), ev.getLat(), readcsv()))==false && ( distance(event.getLon(), event.getLat(), readcsv()))==false
							&& ev.getGeoHash().equals(event.getGeoHash()==true)){
                            	return true;
                            }
                            else{
                                return false;
                            }
                        }
                        return false;
                }})
                .within(Time.seconds(10));
        return FastApproachPattern;
    }

    public static DataStream<SuspiciousFastApproach> fastApproachDatastream(PatternStream<AccelerationMessage> patternStream){
        DataStream<SuspiciousFastApproach>  fastApproach = patternStream.select(new PatternSelectFunction<AccelerationMessage, SuspiciousFastApproach>() {
            @Override
            public SuspiciousFastApproach select(Map<String,List<AccelerationMessage>> pattern) throws Exception {
            	AccelerationMessage vessel_1 = (AccelerationMessage) pattern.get("Vessel_1").get(0);
                AccelerationMessage vessel_2 = (AccelerationMessage) pattern.get("Vessel_2").get(0);
                return new SuspiciousFastApproach(vessel_1.getMmsi(),vessel_2.getMmsi(),vessel_1.getGeoHash(),vessel_2.getGeoHash(),vessel_1.getAccelerationStart(),vessel_1.getAccelerationEnd());
            }
        });

        return fastApproach;
    }
}
