package hes.cs63.CEPMonitor.Gaps;

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

public class Gap {
    private static int gapTime=300;
    private static int geoHashLen=5;
    private static int patternTime=10;

    public  static HashSet <String> listOfPorts=ports();

    public static HashSet<String> ports(){
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
                gHash=GeoHash.encodeHash(Float.valueOf(coordinates[0]),Float.valueOf(coordinates[1]),geoHashLen);
                listOfPorts.add(gHash);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listOfPorts;
    }

    public static Pattern<AisMessage, ?> patternGap(){
        Pattern<AisMessage, ?> rendezvouzPattern = Pattern.<AisMessage>begin("gap_start", AfterMatchSkipStrategy.skipPastLastEvent())
                .followedBy("gap_end")
                .where(new IterativeCondition<AisMessage>() {
                           @Override
                           public boolean filter(AisMessage event, Context<AisMessage> ctx) {
                               try {
                                   for (AisMessage ev : ctx.getEventsForPattern("gap_start")) {
                                       //VARIABLE THINK:TIME
                                       if ((event.getT() - ev.getT()) > gapTime && (event.getT() - ev.getT()) > 0
                                       && listOfPorts.contains(GeoHash.encodeHash(event.getLat(), event.getLon(), geoHashLen)) == false) {
                                           return true;
                                       } else {
                                           return false;
                                       }
                                   }
                                   return false;
                               } catch (Exception e) {
                                   System.out.println("Ignore exception...");
                                   if (ctx == null) {
                                       System.out.println("NULL");
                                   } else {
                                       System.out.println("NOT NULL");
                                   }
                                   try {
                                       if (ctx.getEventsForPattern("gap_start") == null) {
                                           System.out.println("Definitely Null");
                                       }
                                   } catch (Exception e1) {
                                       System.out.println("Definitely not Null");
                                       return false;
                                   }


                                   e.printStackTrace();
                                   return false;
                               }
                               }

                           }

                );

        return rendezvouzPattern;
    }

    public static DataStream<SuspiciousGap> suspiciousGapsStream(PatternStream<AisMessage> patternStream){
        DataStream<SuspiciousGap>  rendezvouz  = patternStream.select(new PatternSelectFunction<AisMessage, SuspiciousGap>() {
            @Override
            public SuspiciousGap select(Map<String,List<AisMessage>> pattern) throws Exception {
                AisMessage gap_start = (AisMessage) pattern.get("gap_start").get(0);
                AisMessage gap_end = (AisMessage) pattern.get("gap_end").get(0);

                LinkedList<Float> tempList=new LinkedList<Float>();
                tempList.add(Math.abs((gap_start.getTurn())));
                String geoHash= GeoHash.encodeHash(gap_end.getLat(),gap_end.getLon(),geoHashLen);
                return new SuspiciousGap(gap_start.getMmsi(),gap_end.getLat(),gap_end.getLon(),gap_start.getT(),gap_end.getT(),geoHash);
            }
        });

        return rendezvouz;
    }
}
