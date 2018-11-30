package hes.cs63.CEPMonitor.Acceleration;

import com.github.davidmoten.geo.GeoHash;
import hes.cs63.CEPMonitor.AisMessage;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.IterativeCondition;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.shaded.guava18.com.google.common.collect.Lists;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class Acceleration {
    public  static HashSet <String> listOfPorts=readCsv();
    static float accepted_acceleration=25/100;
    static int accelerationTime= Math.round(20/(accepted_acceleration));
    static int indexNearVessels=6;
    static int indexNearPorts=6;
    static int maxSpeed=20;
    static int patternTime=10;
    
    
    public static HashSet<String> readCsv(){
        listOfPorts = new HashSet<String>();
        String csvFile = "/home/cer/Desktop/cer_2/flinkcep/producer/wpi.csv";
        String line = "";
        String cvsSplitBy = ",";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String gHash="";
            while ((line = br.readLine()) != null) {
                // use comma as separator
            	//create the geohash for all ports of brittany
                String[] coordinates = line.split(cvsSplitBy);
                gHash=GeoHash.encodeHash(Float.valueOf(coordinates[0]),Float.valueOf(coordinates[1]),5);
                listOfPorts.add(gHash);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listOfPorts;
    }

    public static Pattern<AisMessage, ?> patternAcceleration(){
        Pattern<AisMessage, ?> fastForwardPattern = Pattern.<AisMessage>begin("accelaration_start")
                .oneOrMore()
                .followedBy("speed change")
                .where(new IterativeCondition<AisMessage>() {
                    @Override
                    public boolean filter(AisMessage event, Context<AisMessage> ctx) throws Exception {
                        //System.out.println("accelaration");
                        if(ctx!=null) {
                            if (ctx.getEventsForPattern("start") != null) {
                                for (AisMessage ev : ctx.getEventsForPattern("accelaration_start")) {

                                    //high acceleration in less than 80secs and not near port, dont count out of order events
                                    if ((event.getSpeed() - ev.getSpeed()) >= maxSpeed &&
                                            (event.getT() - ev.getT()) < accelerationTime &&
                                            (event.getT() - ev.getT()) > 0 &&
                                            listOfPorts.contains(GeoHash.encodeHash(event.getLat(), event.getLon(), indexNearPorts)) == false &&
                                            ev.getMmsi() == event.getMmsi()) {

                                        return true;
                                    }
                                }
                                return false;
                            } else {
                                return false;
                            }
                        }
                        else{
                            return false;
                        }
                    }})
                .followedBy("accelaration_end")
                .where(new IterativeCondition<AisMessage>() {
                    @Override
                    public boolean filter(AisMessage event, Context<AisMessage> ctx) throws Exception {
                 
                        ArrayList<AisMessage> aises=Lists.newArrayList(ctx.getEventsForPattern("speed change"));
                        AisMessage ship=null;
                      

                        for (AisMessage ev : ctx.getEventsForPattern("accelaration_start")) {
                        	 if(((GeoHash.encodeHash(ev.getLat(),ev.getLon(),indexNearVessels).equals(GeoHash.encodeHash(ship.getLat(),ship.getLon(),indexNearVessels)))==true
                            &&  ev.getT()-ship.getT()>0)){
                              
                                return true;
                            }
                        }
                        return false;

                }})
                .within(Time.seconds(patternTime));
        return fastForwardPattern;
    }

    public static DataStream<SuspiciousAcceleration> suspiciousAccelerationsStream(PatternStream<AisMessage> patternStream){
        DataStream<SuspiciousAcceleration>  alarms = patternStream.select(new PatternSelectFunction<AisMessage, SuspiciousAcceleration>() {
            @Override
            public SuspiciousAcceleration select(Map<String,List<AisMessage>> pattern) throws Exception {
                AisMessage accelaration_start = (AisMessage) pattern.get("accelaration_start").get(0);
                AisMessage accelaration_end = (AisMessage) pattern.get("accelaration_end").get(0);

                LinkedList<Float> tempList=new LinkedList<Float>();
                tempList.add(Math.abs((accelaration_start.getSpeed())));

                String geoHash= GeoHash.encodeHash(accelaration_start.getLat(),accelaration_start.getLon());     
               return new SuspiciousAcceleration(accelaration_start.getMmsi(),accelaration_start.getLon(),accelaration_start.getLat(),accelaration_start.getSpeed(),accelaration_end.getSpeed(), geoHash,accelaration_start.getT(),accelaration_end.getT());         
            }
        });

        return alarms;
    }
}
