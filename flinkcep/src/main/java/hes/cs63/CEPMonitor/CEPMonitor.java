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


       //1st 
        Pattern<AisMessage, ?> alarmPattern = Pattern.<AisMessage>begin("first")
                .subtype(AisMessage.class)
                .where(new SimpleCondition<AisMessage>() {
                    @Override
                    public boolean filter(AisMessage event) {
                        return event.getStatus()!=8;
                    }
                })
                .followedBy("middle")
                .subtype(AisMessage.class)
                .where(new SimpleCondition<AisMessage>() {
                    @Override
                    public boolean filter(AisMessage event) {
                        System.out.println(event.getStatus() !=8);
                    return event.getStatus() !=8;
                    }
                })
                .followedBy("last")
                .subtype(AisMessage.class)
                .where(new SimpleCondition<AisMessage>() {
                    @Override
                    public boolean filter(AisMessage event) {
                        return event.getStatus() !=8;
                    }
                })
                .within(Time.seconds(10));

        // Create a pattern stream from alarmPattern
        PatternStream<AisMessage> patternStream = CEP.pattern(partitionedInput, alarmPattern);
        
        //SuspiciousTurn for each matched alarm pattern
        DataStream<SuspiciousTurn> alarms = patternStream.select(new PatternSelectFunction<AisMessage, SuspiciousTurn>() {
            @Override
            public SuspiciousTurn select(Map<String,List<AisMessage>> pattern) throws Exception {
                AisMessage first = (AisMessage) pattern.get("first").get(0);
                AisMessage last = (AisMessage) pattern.get("last").get(0);
                AisMessage middle = (AisMessage) pattern.get("middle").get(0);

                LinkedList<Float> tempList=new LinkedList<Float>();
                tempList.add(Math.abs((first.getTurn())));


                tempList.add(Math.abs((last.getTurn())));
                tempList.add(Math.abs((middle.getTurn())));
                return new SuspiciousTurn(first.getMmsi(),tempList);
            }
        });

        alarms.map(v -> v.zigNzag()).writeAsText(parameterTool.getRequired("out"), WriteMode.OVERWRITE);
        messageStream.map(v -> v.toString()).print();
        env.execute("Flink ICU CEP monitoring job");

    }
}
