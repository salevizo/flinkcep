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
import java.util.List;
import java.util.Map;


public class Loitering {
    public static Pattern<AisMessage, ?> patternLoitering() {
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

        Pattern<AisMessage, ?> Loitering = Pattern.<AisMessage>begin("stop")
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

                        if((event.getSpeed() >2.87 && near_ports == false)){
                            if(event.getSpeed() < 8){
                                //low speed away from ports
                                System.out.printf("moving with low_speed by ship %s %d %f\n?", event.getMmsi(), event.getT(),event.getSpeed());

                                return true;}
                            else{
                                return false;
                            }
                        }
                        return false;
                    }
                })
                //.oneOrMore() //times could be also used
                .followedByAny("stop_ends")
                .where(new IterativeCondition<AisMessage>() {

                    @Override
                    public boolean filter(AisMessage event, Context<AisMessage> ctx) throws Exception {
                        boolean near_ports = false;
//                        for(String str: Ports) {
//                            String ship_geohash = GeoHash.encodeHash(event.getLat(),event.getLon(),6);
//                            if(str.equals(ship_geohash))
//                                near_ports = true;
//                            //System.out.printf("Ship near Port\n");
//
//                        }
                        for (AisMessage ev : ctx.getEventsForPattern("stop")) {
                            String geoHash1=GeoHash.encodeHash(event.getLat(),event.getLon(),6);
                            // System.out.printf("events ship %s %d %s %f?\n",event.getMmsi(),event.getT(),geoHash1,event.getSpeed());
                            if ( ev.getMmsi() == event.getMmsi()) {


                                String geoHash2=GeoHash.encodeHash(ev.getLat(),ev.getLon(),6);
                                //System.out.printf("eventszssss ship %s %d %s %f?\n",ev.getMmsi(),ev.getT(),geoHash2,ev.getSpeed());
                                // added ev.speed() < 8
                                if((geoHash1.equals(geoHash2)) && ev.getSpeed()< 8 && (event.getSpeed()< 8 && (event.getT()-ev.getT()>60))){
                                    if(event.getSpeed()>2.87 && ev.getSpeed() > 2.87){
                                        System.out.printf("Loitering %s %d %f\n?", event.getMmsi(), event.getT(),event.getSpeed());

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
                    }})
                .within(Time.seconds(1800));


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
