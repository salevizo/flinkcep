package hes.cs63.CEPMonitor.SimpleEvents;
import com.github.davidmoten.geo.GeoHash;
import com.github.davidmoten.geo.GeoHash.*;
import java.util.LinkedList;

public class SuspiciousRendezVouz {

    private Integer mmsi_1;
    private Integer mmsi_2;
    private String geoHash;
    private Integer gapEnd_1;
    private Integer gapEnd_2;

    public SuspiciousRendezVouz(Integer mmsi_1, Integer mmsi_2, String geoHash_, Integer gapEnd_1, Integer gapEnd_2){
        this.mmsi_1 = mmsi_1;
        this.mmsi_2=mmsi_2;
        this.geoHash=geoHash_;
        this.gapEnd_1=gapEnd_1;
        this.gapEnd_2=gapEnd_2;

    }

    public Integer getMmsi_1() {
        return mmsi_1;
    }

    public void setMmsi_1(Integer mmsi_1) {
        this.mmsi_1 = mmsi_1;
    }

    public Integer getMmsi_2() {
        return mmsi_2;
    }

    public void setMmsi_2(Integer mmsi_2) {
        this.mmsi_2 = mmsi_2;
    }

    public String getGeoHash() {
        return geoHash;
    }

    public void setGeoHash(String geoHash) {
        this.geoHash = geoHash;
    }

    public Integer getGapEnd_1() {
        return gapEnd_1;
    }

    public void setGapEnd_1(Integer gapEnd_1) {
        this.gapEnd_1 = gapEnd_1;
    }

    public Integer getGapEnd_2() {
        return gapEnd_2;
    }

    public void setGapEnd_2(Integer gapEnd_2) {
        this.gapEnd_2 = gapEnd_2;
    }

    public String findGap(){
        return "Suspicious RendezVous : { Vessel_1 : " + mmsi_1+", Vessel_2 : "+mmsi_2+" , Gap_End_1 : "+gapEnd_1+" , Gap_End_1 : "+gapEnd_2+" GeoHash : "+geoHash+" }";
    }
}