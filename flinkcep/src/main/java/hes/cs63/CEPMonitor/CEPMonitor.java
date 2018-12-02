package hes.cs63.CEPMonitor;


import hes.cs63.CEPMonitor.Acceleration.SpeedNearPort;
import hes.cs63.CEPMonitor.Acceleration.SuspiciousSpeedNearPort;
import hes.cs63.CEPMonitor.Fishing.IllegalFishing;
import hes.cs63.CEPMonitor.Fishing.SuspiciousFishing;
import hes.cs63.CEPMonitor.Gaps.Gap;
import hes.cs63.CEPMonitor.Gaps.GapMessageSerializer;
import hes.cs63.CEPMonitor.Gaps.SuspiciousGap;
import hes.cs63.CEPMonitor.SpeedVesselType.SpeedVesselType;
import hes.cs63.CEPMonitor.SpeedVesselType.SuspiciousSpeedVesselType;
import hes.cs63.CEPMonitor.VesselsCoTravel.coTravelInfo;
import hes.cs63.CEPMonitor.VesselsCoTravel.coTravel;
import hes.cs63.CEPMonitor.VesselsCoTravel.coTravelSerializer;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer09;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer09;


import hes.cs63.CEPMonitor.FalseType.FalseType;
import hes.cs63.CEPMonitor.FalseType.SuspiciousMovement;
import hes.cs63.CEPMonitor.Loitering.Loitering;
import hes.cs63.CEPMonitor.Loitering.SuspiciousLoitering;
import hes.cs63.CEPMonitor.LongTermStops.Longtermstop;
import hes.cs63.CEPMonitor.LongTermStops.SuspiciousLongStop;
import hes.cs63.CEPMonitor.PackagePicking.Packagepick;
import hes.cs63.CEPMonitor.PackagePicking.SuspiciousPackage;


import java.util.Properties;

import hes.cs63.CEPMonitor.CoGHeading.*;

public class CEPMonitor {

    public static void main(String[] args) throws Exception {
    	
    	String sql = "SELECT * FROM `stackoverflow`";
        System.getenv("APP_HOME");
        StreamExecutionEnvironment env =
                StreamExecutionEnvironment.getExecutionEnvironment();

        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        Properties props=parameterTool.getProperties();
        env.enableCheckpointing(1000).
                setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        // Input stream of monitoring events
        DataStream<AisMessage> messageStream = env
                .addSource(new FlinkKafkaConsumer09<>(
                                    parameterTool.getRequired("INPUT"),
                                    new AisMessageDeserializer(),
                                   props))
                .assignTimestampsAndWatermarks(new Watermarks());

        DataStream<AisMessage> partitionedInput = messageStream.keyBy(
                new KeySelector<AisMessage, Integer>() {
                    @Override
                    public Integer getKey(AisMessage value) throws Exception {
                        return value.getMmsi();
                    }
        });

        DataStream<AisMessage> nonPartitionedInput = messageStream;
        ///////////////////////////////////Gaps in the messages of a single vessell////////////////////////////////////////////




        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        ///////////////////////////////////Gaps in the messages of a single vessell////////////////////////////////////////////
        Pattern<AisMessage, ?> gapPattern = Gap.patternGap();
        PatternStream<AisMessage> patternGapStream = CEP.pattern(partitionedInput,gapPattern);
        DataStream<SuspiciousGap> gaps = Gap.suspiciousGapsStream(patternGapStream);

        final SingleOutputStreamOperator<SuspiciousGap> topic_2_gap = gaps.map(v -> v.getGapObj()).uid("Suspicious");

        FlinkKafkaProducer09<SuspiciousGap> gapProducer = new FlinkKafkaProducer09<SuspiciousGap>(

                parameterTool.getRequired("OUT_GAP"),    // target topic
                new GapMessageSerializer(),
                parameterTool.getProperties());   // serialization schema

        topic_2_gap.addSink(gapProducer);
        
        
        ///////////////////////////////////Gaps in the messages of a single vessell////////////////////////////////////////////

        ///////////////////////////////////Pairs of Vessels moving closely////////////////////////////////////////////
        Pattern<AisMessage, ?> coTravelPattern = coTravel.patternCoTravel();
        PatternStream<AisMessage> patternCoTravelStream = CEP.pattern(nonPartitionedInput,coTravelPattern);
        DataStream<coTravelInfo> coTravel = hes.cs63.CEPMonitor.VesselsCoTravel.coTravel.suspiciousCoTravelStream(patternCoTravelStream);
        final SingleOutputStreamOperator<coTravelInfo> topic_2_co = coTravel.map(v -> v.getSuspiciousCoTravelInfo()).uid("Co Travelling");
        FlinkKafkaProducer09<coTravelInfo> coProducer = new FlinkKafkaProducer09<coTravelInfo>(
                parameterTool.getRequired("OUT_COTRAVEL"),    // target topic
                new coTravelSerializer(),
                parameterTool.getProperties());
        topic_2_co.addSink(coProducer);

        ///////////////////////////////////Pairs of Vessels moving closely////////////////////////////////////////////
        
        

        //////////////////////////////////Fast Approach//////////////////////////////////////////////////////////////

        Pattern<AisMessage, ?> Accelarationattern= SpeedNearPort.patternSpeedNearPort();
		PatternStream<AisMessage> patternSAccelarationStream = CEP.pattern(nonPartitionedInput,Accelarationattern);
		DataStream<SuspiciousSpeedNearPort> accelerations = SpeedNearPort.suspiciousSpeedNearPortStream(patternSAccelarationStream);

		accelerations.map(v -> v.findShip()).writeAsText("/home/cer/Desktop/temp/speed_near_port.txt", FileSystem.WriteMode.OVERWRITE);

		
        //////////////////////////////////Fast Approach//////////////////////////////////////////////////////////////



        //////////////////////////////////Fishing//////////////////////////////////////////////////////////////
        Pattern<AisMessage, ?> fishingPattern= IllegalFishing.patternFishing();
        PatternStream<AisMessage> patternFishingStream = CEP.pattern(partitionedInput,fishingPattern);
        DataStream<SuspiciousFishing> fishing = IllegalFishing.suspiciousFishingStream(patternFishingStream);
        fishing.map(v -> v.findFishing()).writeAsText("/home/cer/Desktop/temp/fishing.txt", FileSystem.WriteMode.OVERWRITE).uid("Fishing ");
        //////////////////////////////////Fishing//////////////////////////////////////////////////////////////


        //////////////////FALSE TYPE///////////////////////////////////////////////////////////////////////////////////////////

        Pattern<AisMessage, ?> SuspiciousTypePattern= FalseType.patternFalseType();
        PatternStream<AisMessage> patternFalseTypetream = CEP.pattern(partitionedInput,SuspiciousTypePattern);
        DataStream<SuspiciousMovement> falsetypes = FalseType.suspiciousTypeStream(patternFalseTypetream);

        falsetypes.map(v -> v.movement()).writeAsText("/home/cer/Desktop/temp/false_speed.txt", FileSystem.WriteMode.OVERWRITE);
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //////////////////LOITERING/////////////////////////////////////////////////////////////////////////////////////////////

        Pattern<AisMessage, ?> LoiteringPattern= Loitering.patternLoitering();
        PatternStream<AisMessage> LoiteringStream = CEP.pattern(partitionedInput,LoiteringPattern);
        DataStream<SuspiciousLoitering> loitering = Loitering.Loitering_Stream(LoiteringStream);

        loitering.map(v -> v.SuspiciousLoitering()).writeAsText("/home/cer/Desktop/temp/loitering.txt", FileSystem.WriteMode.OVERWRITE);
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        //////////////////LONG STOP OF VESSEL////////////////////////////////////////////////////////////////////////////////////

        Pattern<AisMessage, ?> LongStopPattern= Longtermstop.patternLongStop();
        PatternStream<AisMessage> LongStopStream = CEP.pattern(partitionedInput,LongStopPattern);
        DataStream<SuspiciousLongStop> longstop = Longtermstop.LongStop_Stream(LongStopStream);

        longstop.map(v -> v.getSuspiciousLongStop()).writeAsText("/home/cer/Desktop/temp/longstop.txt", FileSystem.WriteMode.OVERWRITE);
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        //////////////////FROM TOPIC//////////////PACKAGE PICKING///////////////////////////////////////////////////////////////////////

        /*Pattern<SuspiciousLongStop, ?> PackgePickPattern= Packagepick.patternPackagePicking();
        PatternStream<SuspiciousLongStop> PackagePickStream = CEP.pattern(longstop,PackgePickPattern);
        DataStream<SuspiciousPackage> pack = Packagepick.package_pick_Stream(PackagePickStream);

        pack.map(v -> v.getSuspiciousPackage()).writeAsText("/home/cer/Desktop/temp/packages.txt", FileSystem.WriteMode.OVERWRITE);*/
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        ///////////////////////////////////Suspicious speed in the messages of a single vessell////////////////////////////////////////////

        Pattern<AisMessage, ?> suspiciousSpeedPattern = SpeedVesselType.patternSpeedVesselType();
        PatternStream<AisMessage> patternsuspiciousSpeedStream= CEP.pattern(partitionedInput,suspiciousSpeedPattern);
        DataStream<SuspiciousSpeedVesselType> suspiciousspeed = SpeedVesselType.suspiciousSpeedVesselTypeStream(patternsuspiciousSpeedStream);
        suspiciousspeed.map(v -> v.findSpeed()).writeAsText("/home/cer/Desktop/temp/suspicious_speed.csv", FileSystem.WriteMode.OVERWRITE).uid("Speed");

        ///////////////////////////////////Suspicious heading in the messages of a single vessell////////////////////////////////////////////

        Pattern<AisMessage, ?> suspiciousHeadingPattern = CourseHeading.patternSpaciousHeading();
        PatternStream<AisMessage> patternsuspiciouHeadingStream= CEP.pattern(partitionedInput,suspiciousHeadingPattern);
        DataStream<SuspiciousCourseHeading> suspiciousHeading = CourseHeading.suspiciousSpeedVesselTypeStream(patternsuspiciouHeadingStream);
        final SingleOutputStreamOperator<SuspiciousCourseHeading> topic_2_course = suspiciousHeading.map(v -> v.getObj());

        FlinkKafkaProducer09<SuspiciousCourseHeading> courseProducer = new FlinkKafkaProducer09<SuspiciousCourseHeading>(
                parameterTool.getRequired("OUT_COURSE"),    // target topic
                new CourseDiffToHeadSerializer(),
                parameterTool.getProperties());   // serialization schema

        topic_2_course.addSink(courseProducer);

        env.execute("Trajectory events");

    }
}
