package hes.cs63.CEPMonitor.SimpleEvents;
import com.github.davidmoten.geo.GeoHash;
import com.github.davidmoten.geo.GeoHash.*;
import java.util.LinkedList;

public class SuspiciousFastApproach {

    private Integer mmsi_1;
    private Integer mmsi_2;
    private String geoHash_1;
    private String geoHash_2;
    private Float speed_1;
    private Float speed_2; //the final speed of teh vessel that is going forward the other vessel


    public SuspiciousFastApproach(Integer mmsi_1, Integer mmsi_2, String geoHash_1,String geoHash_2, Float speed1_, Float speed2_){
        this.mmsi_1 = mmsi_1;
        this.mmsi_2=mmsi_2;
        this.geoHash_1=geoHash_1;
        this.geoHash_2=geoHash_2;
        this.speed_1=speed1_;
        this.speed_2=speed2_;
   

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

    public String getGeoHash_1() {
        return geoHash_1;
    }

    public void setGeoHash_1(String geoHash_1) {
        this.geoHash_1 = geoHash_1;
    }
    
    
    public String getGeoHash_2() {
        return geoHash_2;
    }

    public void setGeoHash_2(String geoHash_2) {
        this.geoHash_2= geoHash_2;
    }
    public Float getSpeed_1() {
        return speed_1;
    }

    public void setMmsi_1(Float speed_1) {
        this.speed_1 = speed_1;
    }
    
    public Float getSpeed_2() {
        return speed_2;
    }

    public void setMmsi_2(Float speed_2) {
        this.speed_2 = speed_1;
    }

    

    public String findFastApproach(){
        return "Suspicious RendezVous : { Vessel_1 : " + mmsi_1+", Vessel_2 : "+mmsi_2+"  GeoHash_1 : "+geoHash_1+" , GeoHash_2 : "+geoHash_2+" , Speed_1 : "+speed_1+ "  Speed_2 : "+speed_2+"}";
    }
}
