package hes.cs63.CEPMonitor;

import hes.cs63.CEPMonitor.Acceleration.AccelerationMessageSerializer;
import hes.cs63.CEPMonitor.Gaps.Gap;
import hes.cs63.CEPMonitor.Gaps.GapMessageSerializer;
import hes.cs63.CEPMonitor.Gaps.SuspiciousGap;
import hes.cs63.CEPMonitor.VesselsCoTravel.coTravelInfo;
import hes.cs63.CEPMonitor.VesselsCoTravel.coTravel;
import hes.cs63.CEPMonitor.VesselsCoTravel.coTravelSerializer;
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

import hes.cs63.CEPMonitor.Acceleration.Acceleration;
import hes.cs63.CEPMonitor.Acceleration.SuspiciousAcceleration;


public class CEPMonitor {

    public static void main(String[] args) throws Exception {
    	
    	String sql = "SELECT * FROM `stackoverflow`";
        //Acceleration.readcsv();
        System.getenv("APP_HOME");
        StreamExecutionEnvironment env =
                StreamExecutionEnvironment.getExecutionEnvironment();

        ParameterTool parameterTool = ParameterTool.fromArgs(args);

        // Use ingestion time => TimeCharacteristic == EventTime + IngestionTimeExtractor
        env.enableCheckpointing(1000).
            setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

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

        DataStream<AisMessage> nonPartitionedInput = messageStream;
        ///////////////////////////////////Gaps in the messages of a single vessell////////////////////////////////////////////
        Pattern<AisMessage, ?> gapPattern = Gap.patternGap();
        PatternStream<AisMessage> patternGapStream = CEP.pattern(partitionedInput,gapPattern);
        DataStream<SuspiciousGap> gaps = Gap.suspiciousGapsStream(patternGapStream);

        final SingleOutputStreamOperator<SuspiciousGap> topic_2_gap = gaps.map(v -> v.getGapObj());

        FlinkKafkaProducer09<SuspiciousGap> gapProducer = new FlinkKafkaProducer09<SuspiciousGap>(

                parameterTool.getRequired("topic_output_gap"),    // target topic
                new GapMessageSerializer(),
                parameterTool.getProperties());   // serialization schema

        topic_2_gap.addSink(gapProducer);
        ///////////////////////////////////Gaps in the messages of a single vessell////////////////////////////////////////////

        ///////////////////////////////////Pairs of Vessels moving closely////////////////////////////////////////////
        Pattern<AisMessage, ?> coTravelPattern = coTravel.patternCoTravel();
        PatternStream<AisMessage> patternCoTravelStream = CEP.pattern(nonPartitionedInput,coTravelPattern);
        DataStream<coTravelInfo> coTravel = hes.cs63.CEPMonitor.VesselsCoTravel.coTravel.suspiciousCoTravelStream(patternCoTravelStream);

        final SingleOutputStreamOperator<coTravelInfo> topic_2_co = coTravel.map(v -> v.getSuspiciousCoTravelInfo());

        FlinkKafkaProducer09<coTravelInfo> coProducer = new FlinkKafkaProducer09<coTravelInfo>(
                parameterTool.getRequired("topic_output_co"),    // target topic
                new coTravelSerializer(),
                parameterTool.getProperties());   // serialization schema

        topic_2_co.addSink(coProducer);
        ///////////////////////////////////Pairs of Vessels moving closely////////////////////////////////////////////

/*
       //ZIGZAG
        Pattern<AisMessage, ?> alarmPatternZigZag = CEPFunction.patternZigZag();
        // Create a pattern stream from alarmPattern
        PatternStream<AisMessage> patternStreamZigZag = CEP.pattern(partitionedInput, alarmPatternZigZag);
        // Generate risk warnings for each matched alarm pattern
        DataStream<SuspiciousTurn> alarmsZigZag = CEPFunction.alarmsZigZag(patternStreamZigZag);

        
*/       
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////      

    ///////////////////////////////////High acceleration in a single vessel////////////////////////////////////////////
        //Acceleration acc=new Acceleration();
        Pattern<AisMessage, ?> Accelarationattern= Acceleration.patternAcceleration();
		PatternStream<AisMessage> patternSAccelarationStream = CEP.pattern(nonPartitionedInput,Accelarationattern);
		DataStream<SuspiciousAcceleration> accelerations = Acceleration.suspiciousAccelerationsStream(patternSAccelarationStream);

		final SingleOutputStreamOperator<SuspiciousAcceleration> topic_2_acc = accelerations.map(v -> v.findAccelerationObj());

		FlinkKafkaProducer09<SuspiciousAcceleration> AccelerationProducer = new FlinkKafkaProducer09<SuspiciousAcceleration>(
		        parameterTool.getRequired("topic_output_acc"),    // target topic
		        new AccelerationMessageSerializer(),
		        parameterTool.getProperties());   // serialization schema
		
				
        topic_2_acc.addSink(AccelerationProducer);

    ///////////////////////////////////High acceleration in a single vessel////////////////////////////////////////////

        
        
      
        
        env.execute("Trajentory evens");

    }
}
