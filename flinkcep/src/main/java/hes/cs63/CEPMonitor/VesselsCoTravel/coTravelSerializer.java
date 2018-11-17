package hes.cs63.CEPMonitor.VesselsCoTravel;

import org.apache.flink.streaming.util.serialization.KeyedSerializationSchema;

public class coTravelSerializer implements KeyedSerializationSchema<coTravelInfo> {
    @Override
    public byte[] serializeKey(coTravelInfo element) {
        if(element.getMmsi_1()<element.getMmsi_2()){
            return ("\"" + element.getMmsi_1() + "\"").getBytes();
        }
        else{
            return ("\"" + element.getMmsi_2() + "\"").getBytes();
        }
    }

    @Override
    public byte[] serializeValue(coTravelInfo element) {
        String value="";
        if(element.getMmsi_1()<element.getMmsi_2()){
            value = "{\"MMSI_1\": "+ element.getMmsi_1().toString() + ","+
                    "\"Lon_1\": "+ element.getLon1()+ "," +
                    " \"lat_1\": "+ element.getLat1()+ ","+
                    "\"MMSI_2\": "+ element.getMmsi_2().toString() + ","+
                    "\"Lon_2\": "+ element.getLon2()+ "," +
                    " \"lat_2\": "+ element.getLat2() + ","+
                    " \"time\": "+ element.getTimestamp()+
                    "}";
        }
        else{
            value = "{\"MMSI_1\": "+ element.getMmsi_2().toString() + ","+
                    "\"Lon_1\": "+ element.getLon2()+ "," +
                    " \"lat_1\": "+ element.getLat2()+ ","+
                    "\"MMSI_2\": "+ element.getMmsi_1().toString() + ","+
                    "\"Lon_2\": "+ element.getLon1()+ "," +
                    " \"lat_2\": "+ element.getLat1()+ ","+
                    " \"time\": "+ element.getTimestamp()+
                    "}";
        }
        System.out.println("AVGEROS1992="+value);
        return value.getBytes();
    }

    @Override
    public String getTargetTopic(coTravelInfo element) {
        // use always the default topic
        return "DEMOCP_CO";
    }
}