package hes.cs63.CEPMonitor;

import hes.cs63.CEPMonitor.Gaps.Gap;
import hes.cs63.CEPMonitor.Gaps.SuspiciousGap;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.IngestionTimeExtractor;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer09;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer09;


//import hes.cs63.CEPMonitor.SimpleEvents.Acceleration;
//import hes.cs63.CEPMonitor.SimpleEvents.SuspiciousAcceleration;
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




       

        ///////////////////////////////////Gaps in the messages of a single vessell////////////////////////////////////////////
        Pattern<AisMessage, ?> gapPattern = Gap.patternGap();
        PatternStream<AisMessage> patternGapStream = CEP.pattern(partitionedInput,gapPattern);
        DataStream<SuspiciousGap> gaps = Gap.suspiciousGapsStream(patternGapStream);
        final SingleOutputStreamOperator<SuspiciousGap> topic_2 = gaps.map(v -> v.getGapObj());
        FlinkKafkaProducer09<SuspiciousGap> gapProducer = new FlinkKafkaProducer09<SuspiciousGap>(

                parameterTool.getRequired("topic_output"),    // target topic
                new GapMessageSerializer(),
                parameterTool.getProperties());   // serialization schema


        topic_2.addSink(gapProducer);
      

        ///////////////////////////////////Vessels moving closely for a lot of time////////////////////////////////////////////
        /*Pattern<AisMessage, ?> gapPattern = Gap.patternGap();
        PatternStream<AisMessage> patternGapStream = CEP.pattern(partitionedInput,gapPattern);
        DataStream<SuspiciousGap> gaps = Gap.suspiciousGapsStream(patternGapStream);
        final SingleOutputStreamOperator<SuspiciousGap> topic_2 = gaps.map(v -> v.getGapObj());
        FlinkKafkaProducer09<SuspiciousGap> gapProducer = new FlinkKafkaProducer09<SuspiciousGap>(
                parameterTool.getRequired("topic_output"),    // target topic
                new GapMessageSerializer(),
                parameterTool.getProperties());   // serialization schema

        topic_2.addSink(gapProducer);*/
    
        
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////      
        
        
///////////////////////////////////High acceleration in a single vessel////////////////////////////////////////////
    /*
		Pattern<AisMessage, ?> alarmPatternAccelaration= Acceleration.patternAcceleration();
		PatternStream<AisMessage> patternStreamAccelaration = CEP.pattern(partitionedInput,alarmPatternAccelaration);
		DataStream<SuspiciousAcceleration> alarmsAcceleration = Acceleration.alarmsAcceleration(patternStreamAccelaration);

		final SingleOutputStreamOperator<SuspiciousAcceleration> process1 = alarmsAcceleration.map(v -> v.findAccelerationObj());

		FlinkKafkaProducer09<SuspiciousAcceleration> myProducerAcceleration = new FlinkKafkaProducer09<SuspiciousAcceleration>(
		        parameterTool.getRequired("topic_output"),    // target topic
		        new AccelerationMessageSerializer(),
		        parameterTool.getProperties());   // serialization schema
		
				System.out.println("High acceleration in a single vessel");
		        process1.addSink(myProducerAcceleration);
		        */
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////  
        
        
      
        
        env.execute("Trajentory evens");

    }
}
