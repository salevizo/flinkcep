package hes.cs63.CEPMonitor.Deserializers;


import static org.apache.flink.api.java.typeutils.TypeExtractor.getForClass;

import java.io.IOException;

import hes.cs63.CEPMonitor.receivedClasses.AccelerationMessage;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.util.serialization.KeyedDeserializationSchema;

import com.google.gson.Gson;



public class AccelerationMessageDeserializer implements KeyedDeserializationSchema<AccelerationMessage> {

	 private Gson gson;
	    @Override
	    public AccelerationMessage deserialize(byte[] messageKey,
	                                   byte[] message,
	                                   String topic,
	                                   int partition,
	                                   long offset) throws IOException {
	        if (gson == null) {
	            gson = new Gson();
	        }
	        AccelerationMessage m = gson.fromJson(new String(message), AccelerationMessage.class);
	        return m;
	    }

	    @Override
	    public boolean isEndOfStream(AccelerationMessage nextElement) {
	        return false;
	    }

	    @Override
	    public TypeInformation<AccelerationMessage> getProducedType() {
	        return getForClass(AccelerationMessage.class);
	    }
}
