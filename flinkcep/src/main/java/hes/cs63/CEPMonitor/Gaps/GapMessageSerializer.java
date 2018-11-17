package hes.cs63.CEPMonitor.Gaps;

import hes.cs63.CEPMonitor.Gaps.SuspiciousGap;
import org.apache.flink.streaming.util.serialization.KeyedSerializationSchema;

public class GapMessageSerializer implements KeyedSerializationSchema<SuspiciousGap> {
    @Override
    public byte[] serializeKey(SuspiciousGap element) {
        return ("\"" + element.getMmsi() + "\"").getBytes();
    }

    @Override
    public byte[] serializeValue(SuspiciousGap element) {
        String value = "{\"MMSI\": "+ element.getMmsi().toString() + ","+
                "\"GapStart\": "+ element.getGapStart().toString() + "," +
                " \"GapEnd\": "+ element.getGapEnd().toString() + ","+
                " \"GeoHash\": "+ element.getGeoHash()+

        "}";
        return value.getBytes();
    }

    @Override
    public String getTargetTopic(SuspiciousGap element) {
        // use always the default topic
        return "DEMOCP2";
    }
}