package hes.cs63.CEPMonitor.Accelaration;

import com.google.gson.annotations.SerializedName;


public class AccelerationMessage {
    @SerializedName("MMSI")
    private Integer mmsi;

    
    @SerializedName("AccelerationStart")
    private Float AccelerationStart;

    @SerializedName("AccelerationEnd")
    private Float AccelerationEnd;

    @SerializedName("Acceleration")
    private Float Acceleration;

    
    @SerializedName("GeoHash")
    private String GeoHash;
    
    
    @SerializedName("t_start")
    private Integer t_start;
    
    @SerializedName("t_end")
    private Integer t_end;
    
    @SerializedName("lat")
    private Float lat;
    
    @SerializedName("lon")
    private Float lon;
    

    
    
    
    public Integer getMmsi() {
        return mmsi;
    }

    public void setMmsi(Integer mmsi) {
        this.mmsi = mmsi;
    }

    
    public Integer getTstart() {
        return t_start;
    }

    public void setTend(Integer t_end) {
        this.t_end = t_start;
    }
    
    public Integer getTendt() {
        return t_end;
    }

    public void setTstart(Integer t_start) {
        this.t_start = t_start;
    }
    
    
    
    public Float getAccelerationStart() {
        return AccelerationStart;
    }

    public void setGAccelerationStart(Float acelerationStart) {
    	AccelerationStart = acelerationStart;
    }

    public Float getAccelerationEnd() {
        return AccelerationEnd;
    }

    public void setGAccelerationEnd(Float acelerationEnd) {
    	AccelerationEnd = acelerationEnd;
    }


    public String getGeoHash() {
        return GeoHash;
    }

    public void setGeoHash(String geoHash) {
        GeoHash = geoHash;
    }
    
    public void setAcceleration(Float acceleration) {
    	Acceleration = acceleration;
    }
    public Float getAcceleration() {
    	 return Acceleration;
    }
    
    
    public void setLat(Float lat) {
    	lat = lat;
    }
    public Float getLat() {
    	 return lat;
    }
    
    public void setLon(Float lon) {
    	lon = lon;
    }
    public Float getLon() {
    	 return lon;
    }
    
}
