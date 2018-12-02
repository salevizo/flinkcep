package hes.cs63.CEPMonitor.FalseType;
import hes.cs63.CEPMonitor.AisMessage;

import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.util.List;
import java.util.Map;


public class FalseType {
    public static Pattern<AisMessage, ?> patternFalseType() {

        Pattern<AisMessage, AisMessage> alarmPattern = Pattern.<AisMessage>begin("first")
                .where(new SimpleCondition<AisMessage>() {
                    @Override
                    public boolean filter(AisMessage event) {

                        if ((event.getStatus() == 1 || event.getStatus() == 5)) {
                            if (event.getSpeed() > 5) {
                                return true;
                            } else
                                return false;

                        } else {
                            return false;
                        }
                    }

                });
        return alarmPattern;
    }


    public static DataStream<SuspiciousMovement> suspiciousTypeStream(PatternStream<AisMessage> patternStream) {
        DataStream<SuspiciousMovement> alarms = patternStream.select(new PatternSelectFunction<AisMessage, SuspiciousMovement>() {
            @Override
            public SuspiciousMovement select(Map<String, List<AisMessage>> pattern) throws Exception {
                AisMessage first = (AisMessage) pattern.get("first").get(0);

                return new SuspiciousMovement(first.getMmsi(), first.getSpeed());
            }

        });
        return alarms;
    }
}