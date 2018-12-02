package hes.cs63.CEPMonitor.Loitering;
import com.github.davidmoten.geo.GeoHash;

public class SuspiciousLoitering {
    private Integer mmsi;
    private float speed;
    private float lat;
    private float lon;
    private Integer timestamp;

    public SuspiciousLoitering(int mmsi, float speed, float lon, float lat) {
        this.mmsi = mmsi;
        this.speed = speed;
        this.lon = lon;
        this.lat = lat;


    }

    public String findLoitering() {
        return "findLoitering{" +
                "mmsi=" + mmsi +
                "speed=" + speed +
                ", gapStart=" + lat +"{"+
                "  gapStartLot=" + lon +
                '}';
    }


    public Float getSpeed() {
        return speed;
    }

    public void setSpeeds(Float acceleration) {
        this.speed = acceleration;
    }



    public Integer getMmsi() {
        return mmsi;
    }

    public void setMmsi(Integer mmsi) {
        this.mmsi = mmsi;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float Lat) {
        this.lat = Lat;
    }


//
//    public String getGeoHash() {
//        return geoHash;
//    }
//
//    public void setGeoHash(String geoHash) {
//        this.geoHash = geoHash;
//    }

    public String SuspiciousLoitering(){
        return "Suspicious Loitering : { Vessel : " + mmsi+" " +lat+" " +lon+"}";

    }
}
