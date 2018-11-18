package hes.cs63.CEPMonitor.Acceleration;

import hes.cs63.CEPMonitor.Acceleration.SuspiciousAcceleration;
import org.apache.flink.streaming.util.serialization.KeyedSerializationSchema;

public class AccelerationMessageSerializer implements KeyedSerializationSchema<SuspiciousAcceleration> {
    @Override
    public byte[] serializeKey(SuspiciousAcceleration element) {
        return ("\"" + element.getMmsi() + "\"").getBytes();
    }

    @Override
    public byte[] serializeValue(SuspiciousAcceleration element) {
        String value = "{\"MMSI\": "+ element.getMmsi().toString() + ","+
                "\"AccelerationStart\": "+ element.getAccelerationStart().toString() + "," +
                " \"AccelerationEnd\": "+ element.getAccelerationEnd().toString() + ","+
                " \"GeoHash\": "+ element.getGeoHash()+

        "}";
        return value.getBytes();
    }

    @Override
    public String getTargetTopic(SuspiciousAcceleration element) {
        // use always the default topic
        return "DEMOCP2";
    }
}