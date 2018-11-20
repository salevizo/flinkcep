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

    public static HashSet<String> readCsv(){
        listOfPorts = new HashSet<String>();
        String csvFile = "/home/cer/Desktop/cer_2/flinkcep/producer/wpi.csv";
        String line = "";
        String cvsSplitBy = ",";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String gHash="";
            while ((line = br.readLine()) != null) {
                // use comma as separator
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
        Pattern<AisMessage, ?> alarmPattern = Pattern.<AisMessage>begin("accelaration_start")
                .subtype(AisMessage.class)
                .oneOrMore()
                .followedBy("speed change")
                .where(new IterativeCondition<AisMessage>() {
                    @Override
                    public boolean filter(AisMessage event, Context<AisMessage> ctx) throws Exception {
                        System.out.println("accelaration");
                        for (AisMessage ev : ctx.getEventsForPattern("accelaration_start")) {
                            //20KNOTS bigger speed at 1 min
                            //TODO CHECK IF THE GEO IS IN PORT, AS IT IS NORMAL A HIGHT ACCELARATION
                            //TO CHANGE IT
                            String h=GeoHash.encodeHash(event.getLat(),event.getLon(),6);
                            if(listOfPorts==null){
                                System.out.println("eimai nuuuuuuul1992");


                            }
                            else{
                                System.out.println("SIZEI="+listOfPorts.size());
                            }
                            System.out.println("HASHISH="+h);
                            System.out.println("1st="+(Math.abs(ev.getSpeed()-event.getSpeed())>=20)+"2nd:"+((event.getT()-ev.getT())<50) +"3d="+listOfPorts.contains(GeoHash.encodeHash(event.getLat(),event.getLon(),6)));
                            if(((event.getSpeed()-ev.getSpeed())>=20 &&
                                    (event.getT()-ev.getT())<50) &&
                                     listOfPorts.contains(GeoHash.encodeHash(event.getLat(),event.getLon(),6))==false
                            && ev.getMmsi()==event.getMmsi()){
                                System.out.println("ACCEPTED"+event.getT());
                                return true;
                            }
                        }
                        return false;
                    }})
                .followedBy("accelaration_end")
                .where(new IterativeCondition<AisMessage>() {
                    @Override
                    public boolean filter(AisMessage event, Context<AisMessage> ctx) throws Exception {
                        System.out.println("accelaration_end");
                        ArrayList<AisMessage> aises=Lists.newArrayList(ctx.getEventsForPattern("speed change"));
                        AisMessage ship=null;
                        for (AisMessage ev : ctx.getEventsForPattern("speed change")) {
                            System.out.println("VRAKI="+ev.getT());
                            ship=ev;
                        }

                        for (AisMessage ev : ctx.getEventsForPattern("accelaration_start")) {
                        	//20KNOTS bigger speed at 1 min
                            System.out.println("INTERMEDIATE SHIPS ="+ev.getT());
                            System.out.println("SOFI="+GeoHash.encodeHash(ev.getLat(),ev.getLon(),6).equals(GeoHash.encodeHash(ship.getLat(),ship.getLon(),6)));
                            System.out.println("GIANN="+(Math.abs(ev.getT()-ship.getT())<180));
                            if(((GeoHash.encodeHash(ev.getLat(),ev.getLon(),6).equals(GeoHash.encodeHash(ship.getLat(),ship.getLon(),6)))==true
                            && Math.abs(ev.getT()-ship.getT())<180)){
                                System.out.println("MOUNI="+ev.getT()+"POUTSA="+event.getT());
                                return true;
                            }
                        }
                        return false;

                }})
                .within(Time.seconds(10));
        return alarmPattern;
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
