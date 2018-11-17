package hes.cs63.CEPMonitor;

import hes.cs63.CEPMonitor.Gaps.Gap;
import hes.cs63.CEPMonitor.Gaps.SuspiciousGap;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.core.fs.FileSystem.WriteMode;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.IngestionTimeExtractor;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer09;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer09;

import com.github.davidmoten.geo.GeoHash;

import hes.cs63.CEPMonitor.Acceleration.Acceleration;
import hes.cs63.CEPMonitor.Acceleration.SuspiciousAcceleration;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class CEPMonitor {

    public static void main(String[] args) throws Exception {
    	
    	
    	
    	String sql = "SELECT * FROM `stackoverflow`";
    
    
        System.getenv("APP_HOME");
        StreamExecutionEnvironment env =
                StreamExecutionEnvironment.getExecutionEnvironment();

        ParameterTool parameterTool = ParameterTool.fromArgs(args);

        // Use ingestion time => TimeCharacteristic == EventTime + IngestionTimeExtractor
        env.enableCheckpointing(1000).
            setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        System.out.println("LOCO0000");
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
        Pattern<AisMessage, ?> Accelarationattern= Acceleration.patternAcceleration();
        System.out.println("LOCO1");
		PatternStream<AisMessage> patternSAccelarationStream = CEP.pattern(partitionedInput,Accelarationattern);
		  System.out.println("LOCO2");
		DataStream<SuspiciousAcceleration> accelerations = Acceleration.suspiciousAccelerationsStream(patternSAccelarationStream);
		  System.out.println("LOCO3");
		final SingleOutputStreamOperator<SuspiciousAcceleration> topic_22 = accelerations.map(v -> v.findAccelerationObj());
		  accelerations.map(v -> v.findAccelerationObjToString()).writeAsText("/home/cer/Desktop/GAMW.txt", WriteMode.OVERWRITE);
	
		  System.out.println("LOCO4");
		
	
		FlinkKafkaProducer09<SuspiciousAcceleration> AccelerationProducer = new FlinkKafkaProducer09<SuspiciousAcceleration>(
		        parameterTool.getRequired("topic_output"),    // target topic
		        new AccelerationMessageSerializer(),
		        parameterTool.getProperties());   // serialization schema
		
				
		        topic_22.addSink(AccelerationProducer);
		       
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////  
        
        
      
        
        env.execute("Trajentory evens");

    }
}
