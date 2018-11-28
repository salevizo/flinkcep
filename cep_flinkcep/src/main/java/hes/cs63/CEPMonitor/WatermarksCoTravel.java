package hes.cs63.CEPMonitor;

import hes.cs63.CEPMonitor.receivedClasses.CoTravelInfo;
import hes.cs63.CEPMonitor.receivedClasses.GapMessage;
import org.apache.flink.streaming.api.functions.AssignerWithPunctuatedWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;

public class WatermarksCoTravel implements AssignerWithPunctuatedWatermarks<CoTravelInfo> {
    @Override
    public long extractTimestamp(CoTravelInfo event, long previousElementTimestamp) {
        return event.getTimestamp();
    }

    @Override
    public Watermark checkAndGetNextWatermark(CoTravelInfo event, long extractedTimestamp) {
        // simply emit a watermark with every event
        return new Watermark(extractedTimestamp);
    }
}