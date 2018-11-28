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
        String value = "{\"MMSI\": "+ Integer.toString(element.getMmsi())+ ","+
                "\"GapStart\": "+ Integer.toString(element.getGapStart()) + "," +
                " \"GapEnd\": "+ Integer.toString(element.getGapEnd()) + ","+
                " \"GeoHash\": "+ element.getGeoHash()+","+
                " \"Lat\": "+ Float.toString(element.getLat()) + ","+
                " \"Lon\": "+ Float.toString(element.getLon())+

        "}";
        return value.getBytes();
    }

    @Override
    public String getTargetTopic(SuspiciousGap element) {
        // use always the default topic
        return "DEMOCP22";
    }
}