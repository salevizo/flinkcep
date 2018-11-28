package hes.cs63.CEPMonitor.SpeedVesselType;

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
import java.util.Arrays;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
public class SpeedVesselType {


	public  static HashMap<String, String[]> listOfVesselsType=vesselTypes();
	public  static HashMap<String, String[]> listOfVesselsMaxMinSpeed=vesselMaxMinSpeed();


	public static HashMap<String, String[]> vesselMaxMinSpeed(){
		listOfVesselsMaxMinSpeed = new HashMap<>();
		String csvFile = "/home/cer/Desktop/cer_2/flinkcep/flinkcep/producer/maxSpeedVesselType.csv";
		String line = "";
		String cvsSplitBy = ",";

		try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {


			String type="";
			String all_lists="";
			while ((line = br.readLine()) != null) {
				String[] context = line.split(cvsSplitBy);
				type = context[0];
				
				String speeds[] = new String[2];
				speeds[0]=context[1];
				speeds[1]=context[2];
				
				listOfVesselsMaxMinSpeed.put(type,speeds);   
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		System.out.print("listOfVesselsMaxMinSpeed: " + listOfVesselsMaxMinSpeed.size());
		return listOfVesselsMaxMinSpeed;
	}

	public static HashMap<String, String[]> vesselTypes(){
		listOfVesselsType = new HashMap<>();
		String csvFile = "/home/cer/Desktop/cer_2/flinkcep/flinkcep/producer/vessel_type.csv";
		String line = "";
		String cvsSplitBy = ",";

		try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

			String type="";
			String all_lists="";
			while ((line = br.readLine()) != null) {
				
				String[] context = line.split(",\"");
				type = context[0];
				all_lists = context[1];
				all_lists = all_lists.replaceAll("[^a-zA-Z0-9,]+","");
			
				String vessels[] = all_lists.split(",");
				listOfVesselsType.put(type,vessels);   
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("listOfVesselsType: "+ listOfVesselsType.size());
			
		return listOfVesselsType;
	}

	//to followed de xerw an to thelei
	public static Pattern<AisMessage, ?> patternSpeedVesselType(){
		Pattern<AisMessage, ?> spaciousSpeedPattern = Pattern.<AisMessage>begin("speed_spacicious_start")
				.where( new SimpleCondition<AisMessage>() {
					@Override
					public boolean filter(AisMessage ev) throws Exception {
						
							String mmsi_= String.valueOf(ev.getMmsi());
							String type="";

							for (Object o : listOfVesselsType.keySet()) {
								if (Arrays.asList(listOfVesselsType.get(o)).contains(mmsi_))
								 {
									type = o.toString();
								}
							}
							//we have info about the vessel type
							if (type.equals("")==false) {
								String speed [] = listOfVesselsMaxMinSpeed.get(type);
								if ( ((ev.getSpeed()<Float.valueOf(speed[0]))
										|| (ev.getSpeed()>Float.valueOf(speed[1]))) ) {

									return true;
								} else {
									return false;
								}
							}else {
								return false;
							}

						
						
					}

				}

						);

		return spaciousSpeedPattern;
	}

	public static DataStream<SuspiciousSpeedVesselType> suspiciousSpeedVesselTypeStream(PatternStream<AisMessage> patternStream){
        DataStream<SuspiciousSpeedVesselType>  rendezvouz  = patternStream.select(new PatternSelectFunction<AisMessage, SuspiciousSpeedVesselType>() {
            @Override
            public SuspiciousSpeedVesselType select(Map<String,List<AisMessage>> pattern) throws Exception {
                AisMessage speed_spacicious_start = (AisMessage) pattern.get("speed_spacicious_start").get(0);
              
                return new SuspiciousSpeedVesselType(speed_spacicious_start.getMmsi(),speed_spacicious_start.getSpeed());
            }
        });

        return rendezvouz;
    }
}
