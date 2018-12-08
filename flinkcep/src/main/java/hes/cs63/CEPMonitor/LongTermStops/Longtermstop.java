package hes.cs63.CEPMonitor.LongTermStops;

import com.github.davidmoten.geo.GeoHash;
import hes.cs63.CEPMonitor.AisMessage;
import hes.cs63.CEPMonitor.Loitering.SuspiciousLoitering;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.nfa.aftermatch.AfterMatchSkipStrategy;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.IterativeCondition;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Longtermstop {

    public  static HashSet <String> listOfPorts=ports();

    public static HashSet<String> ports(){
        listOfPorts = new HashSet<String>();
        String csvFile = "/home/cer/Desktop/cer/flinkcep/flinkcep/producer/wpi.csv";
        String line = "";
        String cvsSplitBy = ",";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String gHash="";
            while ((line = br.readLine()) != null) {
                String[] coordinates = line.split(cvsSplitBy);
                gHash=GeoHash.encodeHash(Float.valueOf(coordinates[0]),Float.valueOf(coordinates[1]),6);
                listOfPorts.add(gHash);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listOfPorts;
    }

    public static Pattern<AisMessage, ?> patternLongStop() {
        //CHECK PORTS




        int longterm = 3600;
        int time_window=7200;
        Pattern<AisMessage, ?> LongTermStopPattern = Pattern.<AisMessage>begin("start", AfterMatchSkipStrategy.skipPastLastEvent())

                .next("stop")
                .where(new SimpleCondition<AisMessage>() {
                    @Override
                    public boolean filter(AisMessage event) throws Exception {

                        if((event.getSpeed() <1 && listOfPorts.contains(GeoHash.encodeHash(event.getLat(), event.getLon(), 8)) == false)){
                            
                            String geoHash1=GeoHash.encodeHash(event.getLat(),event.getLon(),8);

                            

                            return true;
                        }
                        return false;
                    }
                })
           
                .followedBy("stop_ends")
           
                .where(new IterativeCondition<AisMessage>() {

                    @Override
                    public boolean filter(AisMessage event, Context<AisMessage> ctx) throws Exception {
                        for (AisMessage ev : ctx.getEventsForPattern("stop")) {
                            if ( ev.getMmsi() == event.getMmsi()) {

                                String geoHash1=GeoHash.encodeHash(event.getLat(),event.getLon(),6);
                            
                                String geoHash2=GeoHash.encodeHash(ev.getLat(),ev.getLon(),6);

                                if((geoHash1.equals(geoHash2)) && (event.getSpeed()<2) && ev.getSpeed()<2  && (Math.abs(event.getT() - ev.getT()) > 1800)){
                                   System.out.printf("LongStop %s %d %f %s\n?", event.getMmsi(), event.getT(),event.getSpeed(),geoHash1);

                                    return true;
                                }
                                else{
                                    return false;
                                }
                            }
                            else {
                                return false;
                            }

                        }
                        return false;
                    }});
              
        return LongTermStopPattern;
    }

    public static DataStream<SuspiciousLongStop> LongStop_Stream(PatternStream<AisMessage> patternStream) {
        DataStream<SuspiciousLongStop>  alarms  = patternStream.select(new PatternSelectFunction<AisMessage, SuspiciousLongStop>() {
            @Override
            public SuspiciousLongStop select(Map<String,List<AisMessage>> pattern) throws Exception {
                AisMessage stop_vessel = (AisMessage) pattern.get("stop").get(0);
                AisMessage stop_end_vessel = (AisMessage) pattern.get("stop_ends").get(0);
                //AisMessage stop_ends_vessel = (AisMessage) pattern.get("end_of").get(0);


                String geohash= GeoHash.encodeHash(stop_end_vessel.getLat(),stop_end_vessel.getLon(),6);


                return new SuspiciousLongStop(stop_end_vessel.getMmsi(),stop_end_vessel.getLon(),stop_end_vessel.getLat(),stop_vessel.getT(),stop_end_vessel.getT(),geohash);
            }
        });
        return alarms;
    }

}

