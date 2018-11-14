package hes.cs63.CEPMonitor;

import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.core.fs.FileSystem.WriteMode;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.IngestionTimeExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer09;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by sahbi on 5/7/16.
 */
public class CEPMonitor {

    public static void main(String[] args) throws Exception {
        System.getenv("APP_HOME");
        StreamExecutionEnvironment env =
                StreamExecutionEnvironment.getExecutionEnvironment();

        ParameterTool parameterTool = ParameterTool.fromArgs(args);

        // Use ingestion time => TimeCharacteristic == EventTime + IngestionTimeExtractor
        env.enableCheckpointing(1000).
            setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        System.out.println("LOCO");
        // Input stream of monitoring events
        DataStream<AisMessage> messageStream = env
                .addSource(new FlinkKafkaConsumer09<>(
                                    parameterTool.getRequired("topic"),
                                    new AisMessageDeserializer(),
                                    parameterTool.getProperties()))
                .assignTimestampsAndWatermarks(new IngestionTimeExtractor<>());


        DataStream<AisMessage> partitionedInput = messageStream.keyBy(
                new KeySelector<AisMessage, Integer>() {
                    @Override
                    public Integer getKey(AisMessage value) throws Exception {
                        return value.getMmsi();
                    }
        });



       

  

       //ZIGZAG
        Pattern<AisMessage, ?> alarmPatternZigZag = CEPFunction.patternZigZag();
        // Create a pattern stream from alarmPattern
        PatternStream<AisMessage> patternStreamZigZag = CEP.pattern(partitionedInput, alarmPatternZigZag);
        // Generate risk warnings for each matched alarm pattern
        DataStream<SuspiciousTurn> alarmsZigZag = CEPFunction.alarmsZigZag(patternStreamZigZag);

        
        
        //SuspiciousAccelarate
        Pattern<AisMessage, ?> alarmPatternSuspiciousAccelarate = CEPFunction.patternSuspiciousAccelarate();
        // Create a pattern stream from alarmPattern
        PatternStream<AisMessage> patternStreamSuspiciousAccelarate = CEP.pattern(partitionedInput, alarmPatternSuspiciousAccelarate);
        // Generate risk warnings for each matched alarm pattern
        DataStream<SuspiciousAccelarate> alarmsSuspiciousAccelarate = CEPFunction.alarmsSuspiciousAccelarate(patternStreamSuspiciousAccelarate);
        
        
        //SuspiciousGap
        Pattern<AisMessage, ?> alarmPatternSuspiciousGap = CEPFunction.patternSuspiciousGap();
        // Create a pattern stream from alarmPattern
        PatternStream<AisMessage> patternStreamSuspiciousGap = CEP.pattern(partitionedInput, alarmPatternSuspiciousGap);
        // Generate risk warnings for each matched alarm pattern
        DataStream<SuspiciousGap> alarmsSuspiciousGap= CEPFunction.alarmsSuspiciousGap(patternStreamSuspiciousGap);
        
        
        
        //Pause
        Pattern<AisMessage, ?> alarmPatternPause = CEPFunction.patternSuspiciousGap();
        // Create a pattern stream from alarmPattern
        PatternStream<AisMessage> patternStreamPause = CEP.pattern(partitionedInput, alarmPatternPause);
        // Generate risk warnings for each matched alarm pattern
        DataStream<Pause> alarmsPause= CEPFunction.alarmsPause(patternStreamPause);
        
        
        alarmsZigZag.map(v -> v.zigNzag()).writeAsText("/home/cer/Desktop/zigzag.txt", WriteMode.OVERWRITE);   
        alarmsSuspiciousAccelarate.map(v -> v.highAccelarate()).writeAsText("/home/cer/Desktop/highAccelarate.txt", WriteMode.OVERWRITE);   
        alarmsSuspiciousGap.map(v -> v.gap()).writeAsText("/home/cer/Desktop/gap.txt", WriteMode.OVERWRITE);   
        alarmsPause.map(v -> v.pause()).writeAsText("/home/cer/Desktop/pause.txt", WriteMode.OVERWRITE);   
        
       
        
        messageStream.map(v -> v.toString()).print();
        env.execute("Flink ICU CEP monitoring job");

    }
}
