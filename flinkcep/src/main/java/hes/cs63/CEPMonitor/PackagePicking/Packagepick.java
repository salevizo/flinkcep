package hes.cs63.CEPMonitor.PackagePicking;


import com.github.davidmoten.geo.GeoHash;
import hes.cs63.CEPMonitor.AisMessage;
import hes.cs63.CEPMonitor.FalseType.SuspiciousMovement;
import hes.cs63.CEPMonitor.LongTermStops.SuspiciousLongStop;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.IterativeCondition;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.util.List;
import java.util.Map;

public class Packagepick {
    public static Pattern<SuspiciousLongStop, ?> patternPackagePicking() {
        Pattern<SuspiciousLongStop, ?> alarmPattern = Pattern.<SuspiciousLongStop>begin("first")
                .where(new SimpleCondition<SuspiciousLongStop>() {
                    @Override
                    public boolean filter(SuspiciousLongStop suspiciousLongStop) throws Exception {
                        if(suspiciousLongStop.getMmsi()>0) {
                            //System.out.printf("ship ship %s %s ?\n", suspiciousLongStop.getMmsi(), suspiciousLongStop.getGapEnd());
                            return true;
                        }
                        else{
                            return false;
                        }
                    }
                })
                .followedBy("picking")
                .where(new IterativeCondition<SuspiciousLongStop>() {

                    @Override
                    public boolean filter(SuspiciousLongStop event, Context<SuspiciousLongStop> ctx) throws Exception {
                        String geoHash1= GeoHash.encodeHash(event.getLat(),event.getLon(),6);
                        //System.out.printf("candidates ship %s %d %s?\n",event.getMmsi(),event.getGapEnd(),geoHash1);
                        for (SuspiciousLongStop ev : ctx.getEventsForPattern("first")) {

                            if ( ev.getMmsi() != event.getMmsi()) {

                                //System.out.printf("candidate ship %s %d %s?\n",event.getMmsi(),event.getGapEnd(),geoHash1);
                                String geoHash2=GeoHash.encodeHash(ev.getLat(),ev.getLon(),6);
                                //System.out.printf("ship ship %s %s %s?\n",ev.getMmsi(),ev.getGapEnd(),geoHash2);
                                // m conservative events ev.getSpeed() < 2
                                if((geoHash1.equals(geoHash2)) && (event.getGapEnd() - ev.getGapEnd())<60){
                                    return true;
                                }
                                else{
                                    return false;
                                }
                            }
                            else {
                                return false;
                            }

                        }
                        return false;
                    }})
                .within(Time.seconds(100));
        return alarmPattern;

    }

    public static DataStream<SuspiciousPackage> package_pick_Stream(PatternStream<SuspiciousLongStop> patternStream) {
                DataStream<SuspiciousPackage>  spackage  = patternStream.select(new PatternSelectFunction<SuspiciousLongStop, SuspiciousPackage>() {
            @Override
            public SuspiciousPackage select(Map<String, List<SuspiciousLongStop>> pattern) throws Exception {
                SuspiciousLongStop stop_vessel = (SuspiciousLongStop) pattern.get("first").get(0);
                SuspiciousLongStop pick_vessel = (SuspiciousLongStop) pattern.get("picking").get(0);


                return new SuspiciousPackage(stop_vessel.getMmsi(),pick_vessel.getMmsi(),stop_vessel.getLon(),stop_vessel.getLat(),pick_vessel.getLon(),pick_vessel.getLat(),stop_vessel.getGapEnd());
            }
        });

        return spackage;
    }



}
