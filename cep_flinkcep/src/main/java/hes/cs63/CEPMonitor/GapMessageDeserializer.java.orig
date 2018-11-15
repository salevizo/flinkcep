package hes.cs63.CEPMonitor;

import com.google.gson.Gson;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.util.serialization.KeyedDeserializationSchema;
import static org.apache.flink.api.java.typeutils.TypeExtractor.getForClass;

import java.io.IOException;

/**
 * Created by sahbi on 5/8/16.
 */
public class GapMessageDeserializer implements KeyedDeserializationSchema<GapMessage> {
    private Gson gson;
    @Override
    public GapMessage deserialize(byte[] messageKey,
                                   byte[] message,
                                   String topic,
                                   int partition,
                                   long offset) throws IOException {
        if (gson == null) {
            gson = new Gson();
        }
        GapMessage m = gson.fromJson(new String(message), GapMessage.class);
        return m;
    }

    @Override
    public boolean isEndOfStream(GapMessage nextElement) {
        return false;
    }

    @Override
    public TypeInformation<GapMessage> getProducedType() {
        return getForClass(GapMessage.class);
    }
}