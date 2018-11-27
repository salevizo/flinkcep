package hes.cs63.CEPMonitor;

import hes.cs63.CEPMonitor.receivedClasses.GapMessage;
import org.apache.flink.streaming.api.functions.AssignerWithPunctuatedWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;

public class WatermarksGaps implements AssignerWithPunctuatedWatermarks<GapMessage> {
    @Override
    public long extractTimestamp(GapMessage event, long previousElementTimestamp) {
        return event.getGapEnd();
    }

    @Override
    public Watermark checkAndGetNextWatermark(GapMessage event, long extractedTimestamp) {
        // simply emit a watermark with every event
        return new Watermark(extractedTimestamp);
    }
}