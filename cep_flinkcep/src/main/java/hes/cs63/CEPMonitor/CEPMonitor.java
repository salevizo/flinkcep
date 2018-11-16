package hes.cs63.CEPMonitor;

import hes.cs63.CEPMonitor.SimpleEvents.RendezVouz;
import hes.cs63.CEPMonitor.SimpleEvents.SuspiciousRendezVouz;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.core.fs.FileSystem.WriteMode;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.IngestionTimeExtractor;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer09;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer09;
import org.apache.flink.streaming.util.serialization.KeyedSerializationSchema;

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
        System.out.println("LOCO2");
        // Input stream of monitoring events
        DataStream<GapMessage> messageStream = env
                .addSource(new FlinkKafkaConsumer09<>(
                                    parameterTool.getRequired("topic"),
                                    new GapMessageDeserializer(),
                                    parameterTool.getProperties()))
                .assignTimestampsAndWatermarks(new IngestionTimeExtractor<>());


        DataStream<GapMessage> partitionedInput = messageStream.keyBy(
                new KeySelector<GapMessage, Integer>() {
                    @Override
                    public Integer getKey(GapMessage value) throws Exception {
                        return value.getMmsi();
                    }
        });



        Pattern<GapMessage, ?> alarmPattern = RendezVouz.patternGap();
        PatternStream<GapMessage> patternStream = CEP.pattern(partitionedInput,alarmPattern);
        DataStream<SuspiciousRendezVouz> alarms = RendezVouz.alarmsGap(patternStream);
        alarms.map(v -> v.findGap()).writeAsText("/home/cer/Desktop/rendezvouz.txt", WriteMode.OVERWRITE);



        
        env.execute("Suspicious RendezVouz");

    }
}