package hes.cs63.CEPMonitor.Acceleration;
import com.github.davidmoten.geo.GeoHash;
import com.github.davidmoten.geo.GeoHash.*;
import org.apache.flink.api.java.tuple.Tuple2;

import java.util.LinkedList;

public class SuspiciousAcceleration {

    private Integer mmsi;
    private float lon;
    private float lat;
    private Float accelerationStart;
    private Float accelerationEnd;
    private String geoHash;
    private Integer t_start;
    private Integer t_end;;
    private Integer thresholdTime=60;
    private Integer thresholdSpeed=100;
    public SuspiciousAcceleration(Integer mmsi_,Float lon_,Float lat_,Float accelerationStart_,Float accelerationEnd_,String geoHash, Integer t_start, Integer t_end){
        this.mmsi = mmsi_;
        this.lon=lon_;
        this.lat=lat_;
        this.accelerationStart=accelerationStart_;
        this.accelerationEnd=accelerationEnd_;
        this.geoHash=geoHash;
        this.t_start=t_start;
        this.t_end=t_end;

    }

    public String getGeoHash() {
        return geoHash;
    }

    public void setGeoHash(String geoHash) {
        this.geoHash = geoHash;
    }

    public Integer getMmsi() {
        return mmsi;
    }

    public void setMmsi(Integer mmsi) {
        this.mmsi = mmsi;
    }

    public Float getAccelerationStart() {
        return accelerationStart;
    }

    public void setAccelerationStart(Float accelerationStart) {
        this.accelerationStart = accelerationStart;
    }

    public Float getAccelerationEnd() {
        return accelerationEnd;
    }

    public void setAccelerationEnd(Float accelerationEnd) {
        this.accelerationEnd = accelerationEnd;
    }
    
    
    

    public Integer getT_start() {
        return t_start;
    }

    public void setT_start(Integer t_start) {
        this.t_start = t_start;
    }

    
    
    public Integer getT_end() {
        return t_end;
    }

    public void setT_end(Integer t_end) {
        this.t_end = t_end;
    }
    public String findAcceleration(){
        Float acceleration=accelerationEnd-accelerationEnd;
        String geoHash=GeoHash.encodeHash(this.lat,this.lon,4);
     return "Suspicious Acceleration : { MMSI : " + getMmsi()+", AccelerationStart : "+getAccelerationStart()+" , AccelerationEnd : "+getAccelerationEnd()+" , Acceleration : "+acceleration+" GeoHash : "+geoHash+"  , t_start :"+t_start+" t_end : "+t_end+" , lat :"+lat+" lon : "+lon+" }";
    }   
   
 public SuspiciousAcceleration findAccelerationObj(){
//      public String findAccelerationObj(){
  //   return "Suspicious SuspiciousAcceleration : { mmsi : " + mmsi+", accelerationStart : "+accelerationStart+" }";
        
    	
      return this;
    }
 
 
 public String findAccelerationObjToString(){
	  return "Suspicious Acceleration : { MMSI : " + getMmsi()+", AccelerationStart : "+getAccelerationStart()+" , AccelerationEnd : "+getAccelerationEnd()+" GeoHash : "+geoHash+"  , t_start :"+t_start+" t_end : "+t_end+" , lat :"+lat+" lon : "+lon+" }";
 
 }
}
