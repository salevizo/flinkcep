package hes.cs63.CEPMonitor.Acceleration;

import com.github.davidmoten.geo.GeoHash;
import hes.cs63.CEPMonitor.AisMessage;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.nfa.aftermatch.AfterMatchSkipStrategy;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class SpeedNearPort {
    static int indexNearPorts=6;
    static int maxSpeed=5;
    static int patternTime=600;
    public  static HashSet <String> listOfPorts=readCsv();
    
    public static HashSet<String> readCsv(){
        listOfPorts = new HashSet<String>();
        String csvFile = "/home/cer/Desktop/cer/flinkcep/flinkcep/producer/wpi.csv";
        String line = "";
        String cvsSplitBy = ",";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String gHash="";
            while ((line = br.readLine()) != null) {
                String[] coordinates = line.split(cvsSplitBy);
                gHash=GeoHash.encodeHash(Float.valueOf(coordinates[0]),Float.valueOf(coordinates[1]),indexNearPorts);
                listOfPorts.add(gHash);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listOfPorts;
    }

    public static Pattern<AisMessage, ?> patternSpeedNearPort(){
        Pattern<AisMessage, ?> fastForwardPattern = Pattern.<AisMessage>begin("acceleration_start", AfterMatchSkipStrategy.skipPastLastEvent())
                .where(new SimpleCondition<AisMessage>() {
                    @Override
                    public boolean filter(AisMessage event) throws Exception {
                            if (listOfPorts.contains(GeoHash.encodeHash(event.getLat(), event.getLon(), indexNearPorts))== true
                            && event.getSpeed()>maxSpeed) {
                                return true;
                            }
                        return false;
                    }})
                .times(10)
                .consecutive()
                .within(Time.seconds(patternTime));
        return fastForwardPattern;
    }

    public static DataStream<SuspiciousSpeedNearPort> suspiciousSpeedNearPortStream(PatternStream<AisMessage> patternStream){
        DataStream<SuspiciousSpeedNearPort>  alarms = patternStream.select(new PatternSelectFunction<AisMessage, SuspiciousSpeedNearPort>() {
            @Override
            public SuspiciousSpeedNearPort select(Map<String,List<AisMessage>> pattern) throws Exception {
                SuspiciousSpeedNearPort temp=null;
                for(int i=0;i<pattern.get("acceleration_start").size();i++) {
                    if(i==0){
                        AisMessage in_msg = pattern.get("acceleration_start").get(i);
                        temp=new SuspiciousSpeedNearPort(in_msg.getMmsi(),in_msg.getLon(),in_msg.getLat(),in_msg.getT());
                    }
                    else {
                        AisMessage in_msg = pattern.get("acceleration_start").get(i);
                        temp.getMsgs().add(new SuspiciousSpeedNearPort(in_msg.getMmsi(),in_msg.getLon(),in_msg.getLat(),in_msg.getT()));
                    }
                }
               return temp;
            }
        });

        return alarms;
    }
}
