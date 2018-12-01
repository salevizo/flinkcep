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
    private static int gapTime=600;
    private static int geoHashLen=6;

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
                .next("gap_end")
                .where(new IterativeCondition<AisMessage>() {
                           @Override
                           public boolean filter(AisMessage event, Context<AisMessage> ctx) throws Exception {
                                   for (AisMessage ev : ctx.getEventsForPattern("gap_start")) {
                                       //System.out.println("EMPIKA");
                                       if ((event.getT() - ev.getT()) > gapTime
                                       && listOfPorts.contains(GeoHash.encodeHash(event.getLat(), event.getLon(), geoHashLen)) == false) {
                                           return true;
                                       } else {
                                           return false;
                                       }
                                   }
                                   return false;
                               }

                           }

                )
                .within(Time.seconds(900));

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
