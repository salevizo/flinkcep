package hes.cs63.CEPMonitor;
import hes.cs63.CEPMonitor.CoTravellingVessels.SuspiciousCoTravellingVessels;
import hes.cs63.CEPMonitor.CoTravellingVessels.coTravellingVessels;
import hes.cs63.CEPMonitor.Deserializers.CoTravelDeserializer;
import hes.cs63.CEPMonitor.Deserializers.GapMessageDeserializer;
import hes.cs63.CEPMonitor.Rendezvouz.RendezVouz;
import hes.cs63.CEPMonitor.Rendezvouz.SuspiciousRendezVouz;


import hes.cs63.CEPMonitor.receivedClasses.CoTravelInfo;
import hes.cs63.CEPMonitor.receivedClasses.GapMessage;
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

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        DataStream<GapMessage> gapMessageStream = env
                .addSource(new FlinkKafkaConsumer09<>(
                                    parameterTool.getRequired("topic_gap"),
                                    new GapMessageDeserializer(),
                                    parameterTool.getProperties()))
                .assignTimestampsAndWatermarks(new IngestionTimeExtractor<>());


        DataStream<GapMessage> gapPartitionedInput = gapMessageStream;
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        DataStream<CoTravelInfo> coTravelMessageStream = env
                .addSource(new FlinkKafkaConsumer09<>(
                        parameterTool.getRequired("topic_co"),
                        new CoTravelDeserializer(),
                        parameterTool.getProperties()))
                .assignTimestampsAndWatermarks(new IngestionTimeExtractor<>());


        DataStream<CoTravelInfo> coTravelPartitionedInput = coTravelMessageStream.keyBy(
                new KeySelector<CoTravelInfo, Integer>() {
                    @Override
                    public Integer getKey(CoTravelInfo value) throws Exception {
                        return value.getMmsi_1();
                    }
                });
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /*DataStream<AccelerationMessage> messageStreamFastApproach = env
                .addSource(new FlinkKafkaConsumer09<>(
                        parameterTool.getRequired("topic_acc"),
                        new AccelerationMessageDeserializer(),
                        parameterTool.getProperties()))
                .assignTimestampsAndWatermarks(new IngestionTimeExtractor<>());


        DataStream<AccelerationMessage> partitionedInputFastApproach  = messageStreamFastApproach.keyBy(
                new KeySelector<AccelerationMessage, Integer>() {
                    @Override
                    public Integer getKey(AccelerationMessage value) throws Exception {
                        return value.getMmsi();
                    }
                });

           */
        ///////////////////////////////////Gaps in the messages of a single vessell////////////////////////////////////////////
        Pattern<GapMessage, ?> rendezvouzPattern = RendezVouz.patternRendezvouz();
        PatternStream<GapMessage> rendezvouzPatternStream = CEP.pattern(gapPartitionedInput,rendezvouzPattern);
        DataStream<SuspiciousRendezVouz> rendezvouzStream = RendezVouz.rendevouzDatastream(rendezvouzPatternStream);
        rendezvouzStream.map(v -> v.findGap()).writeAsText("/home/cer/Desktop/rendezvouz.txt", WriteMode.OVERWRITE);
        ///////////////////////////////////Gaps in the messages of a single vessell////////////////////////////////////////////
	
	    //////////////////////////////////Co travelling vessels////////////////////////////////////////////
        Pattern<CoTravelInfo, ?>coTravelpattern = coTravellingVessels.patternSuspiciousCoTravel();
        PatternStream<CoTravelInfo> coTravelPatternStream = CEP.pattern(coTravelPartitionedInput,coTravelpattern);
        DataStream<SuspiciousCoTravellingVessels> coTravelStream = coTravellingVessels.coTravellingDatastream(coTravelPatternStream);
        coTravelStream.map(v -> v.findVessels()).writeAsText("/home/cer/Desktop/cotravel.txt", WriteMode.OVERWRITE);
	    //////////////////////////////////Co travelling vessels////////////////////////////////////////////

        ///////////////////////////////////High acceleration a single vessell////////////////////////////////////////////
        /*
        Pattern<AccelerationMessage, ?> fastApproachPattern = FastApproach.patternFastApproach();
        PatternStream<AccelerationMessage> fastForwardPatternStream = CEP.pattern(partitionedInputFastApproach,fastApproachPattern);
        DataStream<SuspiciousFastApproach> fastApproachStream = FastApproach.fastApproachDatastream(fastForwardPatternStream);
        fastApproachStream.map(v -> v.findFastApproach()).writeAsText("/home/cer/Desktop/fastApproach.txt", WriteMode.OVERWRITE);
        */
        ///////////////////////////////////High acceleration a single vessell////////////////////////////////////////////

        //messageStream.map(v -> v.toString()).print();
        env.execute("Suspicious RendezVouz");

    }
}
