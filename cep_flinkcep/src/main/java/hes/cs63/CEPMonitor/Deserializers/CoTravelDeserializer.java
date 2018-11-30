package hes.cs63.CEPMonitor.Deserializers;

import com.google.gson.Gson;
import hes.cs63.CEPMonitor.receivedClasses.GapMessage;
import hes.cs63.CEPMonitor.receivedClasses.CoTravelInfo;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.util.serialization.KeyedDeserializationSchema;

import java.io.IOException;

import static org.apache.flink.api.java.typeutils.TypeExtractor.getForClass;

/**
 * Created by sahbi on 5/8/16.
 */
public class CoTravelDeserializer implements KeyedDeserializationSchema<CoTravelInfo> {
    private Gson gson;
    @Override
    public CoTravelInfo deserialize(byte[] messageKey,
                                   byte[] message,
                                   String topic,
                                   int partition,
                                   long offset) throws IOException {
        if (gson == null) {
            gson = new Gson();
        }
        CoTravelInfo m = gson.fromJson(new String(message), CoTravelInfo.class);
        return m;
    }

    @Override
    public boolean isEndOfStream(CoTravelInfo nextElement) {
        return false;
    }

    @Override
    public TypeInformation<CoTravelInfo> getProducedType() {
        return getForClass(CoTravelInfo.class);
    }
}