package hes.cs63.CEPMonitor.Fishing;
import com.github.davidmoten.geo.GeoHash;

public class SuspiciousFishing {

    private Integer mmsi;
    private float gapStartLot;
    private float gapStartLat;
    private float gapEndLot;
    private float gapEndLat;
    private String geoHash;
    private int gapStart;
    private int gapEnd;


    public SuspiciousFishing(Integer mmsi, float gapStartLot, float gapStartLat, float gapEndLot, float gapEndLat, String geoHash, int gapStart, int gapEnd) {
        this.mmsi = mmsi;
        this.gapStartLot = gapStartLot;
        this.gapStartLat = gapStartLat;
        this.gapEndLot = gapEndLot;
        this.gapEndLat = gapEndLat;
        this.geoHash = geoHash;
        this.gapStart = gapStart;
        this.gapEnd = gapEnd;
    }


    public String findFishing() {
        return "SuspiciousFishing{" +
                "mmsi=" + mmsi +
                ", gapStart=" + gapStart +"{"+
                "  gapStartLot=" + gapStartLot +
                ", gapStartLat=" + gapStartLat +"} S"+
                ", gapEnd=" + gapEnd +"{"+
                "  gapEndLot=" + gapEndLot +
                ", gapEndLat=" + gapEndLat +"} "+
                ", geoHash='" + geoHash + '\'' +


                '}';
    }

    public int getGapStart() {
        return gapStart;
    }

    public void setGapStart(int gapStart) {
        this.gapStart = gapStart;
    }

    public int getGapEnd() {
        return gapEnd;
    }

    public void setGapEnd(int gapEnd) {
        this.gapEnd = gapEnd;
    }

    public Integer getMmsi() {
        return mmsi;
    }

    public void setMmsi(Integer mmsi) {
        this.mmsi = mmsi;
    }

    public float getGapStartLot() {
        return gapStartLot;
    }

    public void setGapStartLot(float gapStartLot) {
        this.gapStartLot = gapStartLot;
    }

    public float getGapStartLat() {
        return gapStartLat;
    }

    public void setGapStartLat(float gapStartLat) {
        this.gapStartLat = gapStartLat;
    }

    public float getGapEndLot() {
        return gapEndLot;
    }

    public void setGapEndLot(float gapEndLot) {
        this.gapEndLot = gapEndLot;
    }

    public float getGapEndLat() {
        return gapEndLat;
    }

    public void setGapEndLat(float gapEndLat) {
        this.gapEndLat = gapEndLat;
    }

    public String getGeoHash() {
        return geoHash;
    }

    public void setGeoHash(String geoHash) {
        this.geoHash = geoHash;
    }
    public SuspiciousFishing getGapObj(){
        return this;
    }
}
