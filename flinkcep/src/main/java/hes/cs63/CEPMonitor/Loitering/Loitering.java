package hes.cs63.CEPMonitor.Loitering;

import com.github.davidmoten.geo.GeoHash;
import hes.cs63.CEPMonitor.AisMessage;
import hes.cs63.CEPMonitor.FalseType.SuspiciousMovement;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.IterativeCondition;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


public class Loitering {


    public  static HashSet<String> listOfPorts=ports();

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

    public static Pattern<AisMessage, ?> patternLoitering() {

        int ltrtime = 1800;
        int window= 3600;
        Pattern<AisMessage, ?> Loitering = Pattern.<AisMessage>begin("stop")
                .subtype(AisMessage.class)
                .where(new SimpleCondition<AisMessage>() {
                    @Override
                    public boolean filter(AisMessage event) throws Exception {
                        if((event.getSpeed() >2.87 && listOfPorts.contains(GeoHash.encodeHash(event.getLat(), event.getLon(), 6)) == false)){
                            if(event.getSpeed() < 8){
                                //low speed away from ports
                                String geohash1=GeoHash.encodeHash(event.getLat(),event.getLon(),6);

                                System.out.printf("moving with low_speed by ship %s %d %f %s\n?", event.getMmsi(), event.getT(),event.getSpeed(),geohash1);

                                return true;}
                            else{
                                return false;
                            }
                        }
                        return false;
                    }
                })
                .followedByAny("stop_ends")
                .where(new IterativeCondition<AisMessage>() {

                    @Override
                    public boolean filter(AisMessage event, Context<AisMessage> ctx) throws Exception {
                        for (AisMessage ev : ctx.getEventsForPattern("stop")) {
                            String geoHash1=GeoHash.encodeHash(event.getLat(),event.getLon(),6);
                            //System.out.printf("events ship %s %d %s %f?\n",event.getMmsi(),event.getT(),geoHash1,event.getSpeed());
                            if ( ev.getMmsi() == event.getMmsi()) {


                                String geoHash2=GeoHash.encodeHash(ev.getLat(),ev.getLon(),6);
                                if((geoHash1.equals(geoHash2)) && ev.getSpeed()< 8 && (event.getSpeed()< 8 && (event.getT()-ev.getT()>ltrtime))){
                                    if(event.getSpeed()>2.87 && ev.getSpeed() > 2.87){

                                        return true;
                                    }
                                    else{
                                        return false;
                                    }
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


        return Loitering;
    }


    public static DataStream<SuspiciousLoitering> Loitering_Stream(PatternStream<AisMessage> patternStream) {
        DataStream<SuspiciousLoitering> loitering_alarm = patternStream.select(new PatternSelectFunction<AisMessage, SuspiciousLoitering>() {
            @Override
            public SuspiciousLoitering select(Map<String, List<AisMessage>> pattern) throws Exception {
                AisMessage stop_vessel = (AisMessage) pattern.get("stop").get(0);
                AisMessage stop_end_vessel = (AisMessage) pattern.get("stop_ends").get(0);

                String geohash = GeoHash.encodeHash(stop_end_vessel.getLat(), stop_end_vessel.getLon(), 6);


                return new SuspiciousLoitering(stop_end_vessel.getMmsi(), stop_end_vessel.getSpeed(), stop_end_vessel.getLon(), stop_end_vessel.getLat());
            }
        });
        return loitering_alarm;
    }

}

