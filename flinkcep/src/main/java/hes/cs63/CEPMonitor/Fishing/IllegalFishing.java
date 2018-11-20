package hes.cs63.CEPMonitor.Fishing;

import com.github.davidmoten.geo.GeoHash;
import hes.cs63.CEPMonitor.AisMessage;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.IterativeCondition;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class IllegalFishing {
    static int headingChange=60;
    static int gapTime=600;
    static int patternTime=10;
    public static Pattern<AisMessage, ?> patternFishing(){
        Pattern<AisMessage, ?> fishingPattern = Pattern.<AisMessage>begin("start")
                .subtype(AisMessage.class)
                .followedBy("gap_start")
                .subtype(AisMessage.class)
                .where(new IterativeCondition<AisMessage>() {
                    @Override
                    public boolean filter(AisMessage event, Context<AisMessage> ctx) throws Exception {
                        for (AisMessage ev : ctx.getEventsForPattern("start")) {
                            if(Math.abs(ev.getHeading()-event.getHeading())>headingChange){
                                //System.out.println("CHANGE IN HEADING 1:"+event.getT());
                                return true;
                            }
                            else{
                                return false;
                            }
                        }
                        return false;
                }})
                .subtype(AisMessage.class)
                .followedBy("gap_end")
                .subtype(AisMessage.class)
                .where(new IterativeCondition<AisMessage>() {
                    @Override
                    public boolean filter(AisMessage event, Context<AisMessage> ctx) throws Exception {
                        for (AisMessage ev : ctx.getEventsForPattern("gap_start")) {
                            if(Math.abs(ev.getT()-event.getT())>gapTime){
                                //System.out.println("gap start:"+ev.getT());
                                //System.out.println("gap end:"+event.getT());

                                return true;
                            }
                            else{
                                return false;
                            }
                        }
                        return false;
                    }})
                .followedBy("change in heading  again")
                .subtype(AisMessage.class)
                .where(new IterativeCondition<AisMessage>() {
                    @Override
                    public boolean filter(AisMessage event, Context<AisMessage> ctx) throws Exception {
                        for (AisMessage ev : ctx.getEventsForPattern("gap_end")) {
                            //System.out.println("Final Event="+ev.getT());
                            if(Math.abs(ev.getHeading()-event.getHeading())>headingChange){
                                //System.out.println("change in heading again:"+event.getT());
                                return true;
                            }
                            else{
                                return false;
                            }
                        }
                        return false;
                    }})
                .within(Time.seconds(patternTime));
        return fishingPattern;
    }

    public static DataStream<SuspiciousFishing> suspiciousFishingStream(PatternStream<AisMessage> patternStream){
        DataStream<SuspiciousFishing>  rendezvouz  = patternStream.select(new PatternSelectFunction<AisMessage, SuspiciousFishing>() {
            @Override
            public SuspiciousFishing select(Map<String,List<AisMessage>> pattern) throws Exception {
                AisMessage gap_start = (AisMessage) pattern.get("gap_start").get(0);
                AisMessage gap_end = (AisMessage) pattern.get("gap_end").get(0);
                AisMessage change = (AisMessage) pattern.get("change in heading  again").get(0);

                LinkedList<Float> tempList=new LinkedList<Float>();
                tempList.add(Math.abs((gap_start.getTurn())));
                String geoHash= GeoHash.encodeHash(change.getLat(),change.getLon());
                return new SuspiciousFishing(gap_start.getMmsi(),gap_start.getLon(),gap_start.getLat(),gap_end.getLon(),gap_end.getLat(),geoHash,gap_start.getT(),gap_end.getT());
            }
        });

        return rendezvouz;
    }
}
