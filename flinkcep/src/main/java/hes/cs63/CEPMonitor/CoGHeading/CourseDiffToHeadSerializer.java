package hes.cs63.CEPMonitor.CoGHeading;

import org.apache.flink.streaming.util.serialization.KeyedSerializationSchema;

public class CourseDiffToHeadSerializer implements KeyedSerializationSchema<SuspiciousCourseHeading> {
    @Override
    public byte[] serializeKey(SuspiciousCourseHeading element) {
        return ("\"" + element.getMmsi() + "\"").getBytes();
    }

    @Override
    public byte[] serializeValue(SuspiciousCourseHeading element) {
        String value = "{\"MMSI\": "+ Integer.toString(element.getMmsi())+ ","+
                "\"Course\": "+ Float.toString(element.getCourse()) + "," +
                " \"Heading\": "+ Float.toString(element.getHeading()) + ","+
                " \"Timestamp\": "+ Integer.toString(element.getTimestamp())+","+
                " \"Lat\": "+ Float.toString(element.getLat()) + ","+
                " \"Lon\": "+ Float.toString(element.getLon())+

        "}";
        return value.getBytes();
    }

    @Override
    public String getTargetTopic(SuspiciousCourseHeading element) {
        // use always the default topic
        return "COURSE";
    }

}