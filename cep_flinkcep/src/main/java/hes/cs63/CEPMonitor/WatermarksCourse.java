package hes.cs63.CEPMonitor;

import hes.cs63.CEPMonitor.receivedClasses.CoTravelInfo;
import hes.cs63.CEPMonitor.receivedClasses.courseHead;
import org.apache.flink.streaming.api.functions.AssignerWithPunctuatedWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;

public class WatermarksCourse implements AssignerWithPunctuatedWatermarks<courseHead> {
    @Override
    public long extractTimestamp(courseHead event, long previousElementTimestamp) {
        return event.getTimestamp();
    }

    @Override
    public Watermark checkAndGetNextWatermark(courseHead event, long extractedTimestamp) {
        // simply emit a watermark with every event
        return new Watermark(extractedTimestamp);
    }
}