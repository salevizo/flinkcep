package hes.cs63.CEPMonitor.Deserializers;

import com.google.gson.Gson;
import hes.cs63.CEPMonitor.receivedClasses.CoTravelInfo;
import hes.cs63.CEPMonitor.receivedClasses.courseHead;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.util.serialization.KeyedDeserializationSchema;

import java.io.IOException;

import static org.apache.flink.api.java.typeutils.TypeExtractor.getForClass;

/**
 * Created by sahbi on 5/8/16.
 */
public class CourseHeadDeserializer implements KeyedDeserializationSchema<courseHead> {
    private Gson gson;
    @Override
    public courseHead deserialize(byte[] messageKey,
                                   byte[] message,
                                   String topic,
                                   int partition,
                                   long offset) throws IOException {
        if (gson == null) {
            gson = new Gson();
        }
        courseHead m = gson.fromJson(new String(message), courseHead.class);
        return m;
    }

    @Override
    public boolean isEndOfStream(courseHead nextElement) {
        return false;
    }

    @Override
    public TypeInformation<courseHead> getProducedType() {
        return getForClass(courseHead.class);
    }
}