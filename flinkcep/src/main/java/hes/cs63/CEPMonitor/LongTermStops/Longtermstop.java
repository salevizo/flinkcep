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

public class Longtermstop {

    public static Pattern<AisMessage, ?> patternLongStop() {
        //CHECK PORTS
        List<String> Ports = new ArrayList<String>();
        String csvFile ="/home/gtsiatsios/Documents/msc/yea_cep/flinkicu/src/main/java/hes/cs63/CEPMonitor/wpi.csv";
        String line = "";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] coords = line.split(",");

                System.out.println("Coords [lat= " + coords[0] + " , Lon=" + coords[1] + "]");
                String geoHash1=GeoHash.encodeHash(Double.parseDouble(coords[0]),Double.parseDouble(coords[1]),6);
                System.out.println(geoHash1);
                Ports.add(geoHash1);


            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        int longterm = 60;
        Pattern<AisMessage, ?> LongTermStopPattern = Pattern.<AisMessage>begin("stop", AfterMatchSkipStrategy.skipPastLastEvent())
                .subtype(AisMessage.class)
                .where(new SimpleCondition<AisMessage>() {
                    @Override
                    public boolean filter(AisMessage event) throws Exception {
                        boolean near_ports = false;
                        for(String str: Ports) {
                            String ship_geohash = GeoHash.encodeHash(event.getLat(),event.getLon(),6);
                            if(str.equals(ship_geohash))
                                near_ports = true;
                            //System.out.printf("Ship near Port\n");

                        }
                        //System.out.printf("ships: %d %d %f\n", event.getMmsi(), event.getT(),event.getSpeed());

                        if((event.getSpeed() <1 && near_ports == false)){
                            //low speed away from ports ---> pause
                           // System.out.printf("Pause by ship %s %d %f\n", event.getMmsi(), event.getT(),event.getSpeed());


                            return true;
                        }
                        return false;
                    }
                })
                //.oneOrMore()
                //.timesOrMore(2)
                .followedBy("stop_ends")
                //.subtype(AisMessage.class)
                .where(new IterativeCondition<AisMessage>() {

                    @Override
                    public boolean filter(AisMessage event, Context<AisMessage> ctx) throws Exception {
                        //boolean near_ports = false;
//                        for(String str: Ports) {
//                            String ship_geohash = GeoHash.encodeHash(event.getLat(),event.getLon(),6);
//                            if(str.equals(ship_geohash))
//                                near_ports = true;
//                            //System.out.printf("Ship near Port\n");
//
//                        }
                        for (AisMessage ev : ctx.getEventsForPattern("stop")) {
                                String geoHash1=GeoHash.encodeHash(event.getLat(),event.getLon(),6);
                            if ( ev.getMmsi() == event.getMmsi()) {
                                //System.out.printf("events ship %s %d %s %f?\n",event.getMmsi(),event.getT(),geoHash1,event.getSpeed());


                                String geoHash2=GeoHash.encodeHash(ev.getLat(),ev.getLon(),6);
                                //System.out.printf("eventszssss ship %s %d %s %f?\n",ev.getMmsi(),ev.getT(),geoHash2,ev.getSpeed());

                                // m conservative events ev.getSpeed() < 2
                                if((geoHash1.equals(geoHash2)) && (event.getSpeed()<2) && ev.getSpeed()<2  && (Math.abs(event.getT() - ev.getT()) > longterm)){
                                   System.out.printf("LongStop %s %d %f\n?", event.getMmsi(), event.getT(),event.getSpeed());

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
                    }})
//                .followedBy("end_of")
//                .where(new SimpleCondition<AisMessage>() {
//                    @Override
//                    public boolean filter(AisMessage aisMessage) throws Exception {
//                        if(aisMessage.getSpeed()>2){
//                            String geoHash2=GeoHash.encodeHash(aisMessage.getLat(),aisMessage.getLon(),6);
//
//                            System.out.printf("eventszssss ship %s %d %s %f?\n",aisMessage.getMmsi(),aisMessage.getT(),geoHash2,aisMessage.getSpeed());
//
//                        }
//                        else {
//                            return false;
//                        }
//                        return false;
//                    }
//                })
                .within(Time.seconds(200));
        return LongTermStopPattern;
    }

    public static DataStream<SuspiciousLongStop> LongStop_Stream(PatternStream<AisMessage> patternStream) {
        DataStream<SuspiciousLongStop>  alarms  = patternStream.select(new PatternSelectFunction<AisMessage, SuspiciousLongStop>() {
            @Override
            public SuspiciousLongStop select(Map<String,List<AisMessage>> pattern) throws Exception {
                AisMessage stop_vessel = (AisMessage) pattern.get("stop").get(0);
                AisMessage stop_end_vessel = (AisMessage) pattern.get("stop_ends").get(0);
              //  AisMessage stop_ends_vessel = (AisMessage) pattern.get("end_of").get(0);


                String geohash= GeoHash.encodeHash(stop_end_vessel.getLat(),stop_end_vessel.getLon(),6);


                return new SuspiciousLongStop(stop_end_vessel.getMmsi(),stop_end_vessel.getLon(),stop_end_vessel.getLat(),stop_vessel.getT(),stop_end_vessel.getT(),geohash);
            }
        });
        return alarms;
    }

}
