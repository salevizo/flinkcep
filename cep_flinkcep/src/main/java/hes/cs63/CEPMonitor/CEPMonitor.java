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
import org.apache.flink.streaming.api.functions.timestamps.AscendingTimestampExtractor;
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

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        DataStream<GapMessage> gapMessageStream = env
                .addSource(new FlinkKafkaConsumer09<>(
                                    parameterTool.getRequired("topic_gap"),
                                    new GapMessageDeserializer(),
                                    parameterTool.getProperties()))
                .assignTimestampsAndWatermarks(new WatermarksGaps());

        DataStream<GapMessage> gapNonPartitionedInput = gapMessageStream;
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /*DataStream<CoTravelInfo> coTravelMessageStream = env
                .addSource(new FlinkKafkaConsumer09<>(
                        parameterTool.getRequired("topic_co"),
                        new CoTravelDeserializer(),
                        parameterTool.getProperties()));
                //.assignTimestampsAndWatermarks(new IngestionTimeExtractor<>());


        DataStream<CoTravelInfo> coTravelPartitionedInput = coTravelMessageStream.keyBy(
                new KeySelector<CoTravelInfo, Integer>() {
                    @Override
                    public Integer getKey(CoTravelInfo value) throws Exception {
                        return value.getMmsi_1();
                    }
                });
                */
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
      


         
        ///////////////////////////////////Gaps in the messages of a single vessell////////////////////////////////////////////
        Pattern<GapMessage, ?> rendezvouzPattern = RendezVouz.patternRendezvouz();
        PatternStream<GapMessage> rendezvouzPatternStream = CEP.pattern(gapNonPartitionedInput,rendezvouzPattern);
        DataStream<SuspiciousRendezVouz> rendezvouzStream = RendezVouz.rendevouzDatastream(rendezvouzPatternStream);
        rendezvouzStream.map(v -> v.findGap()).writeAsText("/home/cer/Desktop/temp/rendezvouz.txt", WriteMode.OVERWRITE);
      
        ///////////////////////////////////Gaps in the messages of a single vessell////////////////////////////////////////////
	
	    //////////////////////////////////Co travelling vessels////////////////////////////////////////////
        /*Pattern<CoTravelInfo, ?>coTravelpattern = coTravellingVessels.patternSuspiciousCoTravel();
        PatternStream<CoTravelInfo> coTravelPatternStream = CEP.pattern(coTravelPartitionedInput,coTravelpattern);
        DataStream<SuspiciousCoTravellingVessels> coTravelStream = coTravellingVessels.coTravellingDatastream(coTravelPatternStream);
        coTravelStream.map(v -> v.findVessels()).writeAsText("/home/cer/Desktop/temp/cotravel.txt", WriteMode.OVERWRITE);
	    coTravelStream.map(v -> v.findVesselsQGIS()).writeAsText("/home/cer/temp/Desktop/cotravelQGIS.csv", WriteMode.OVERWRITE);
	    */
        //////////////////////////////////Co travelling vessels////////////////////////////////////////////

    
        //messageStream.map(v -> v.toString()).print();
        env.execute("Suspicious RendezVouz");

    }
}
