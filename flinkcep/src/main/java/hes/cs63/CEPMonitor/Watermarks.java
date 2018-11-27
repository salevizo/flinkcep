package hes.cs63.CEPMonitor;

import hes.cs63.CEPMonitor.AisMessage;
import org.apache.flink.streaming.api.functions.AssignerWithPunctuatedWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;

public class Watermarks implements AssignerWithPunctuatedWatermarks<AisMessage> {
    @Override
    public long extractTimestamp(AisMessage event, long previousElementTimestamp) {
        return event.getT();
    }

    @Override
    public Watermark checkAndGetNextWatermark(AisMessage event, long extractedTimestamp) {
        // simply emit a watermark with every event
        return new Watermark(extractedTimestamp);
    }
}