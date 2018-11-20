package hes.cs63.CEPMonitor.Gaps;
import com.github.davidmoten.geo.GeoHash;

public class SuspiciousGap {

    private Integer mmsi;
    private float lon;
    private float lat;
    private Integer gapStart;
    private Integer gapEnd;
    private String geoHash;
    private Integer thresholdGap=600;

    public SuspiciousGap(Integer mmsi_,Float lon_,Float lat_,Integer gapStart_,Integer gapEnd_,String geoHash){
        this.mmsi = mmsi_;
        this.lon=lon_;
        this.lat=lat_;
        this.gapEnd=gapEnd_;
        this.geoHash=geoHash;
        this.gapStart=gapStart_;

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

    public Integer getGapStart() {
        return gapStart;
    }

    public void setGapStart(Integer gapStart) {
        this.gapStart = gapStart;
    }

    public Integer getGapEnd() {
        return gapEnd;
    }

    public void setGapEnd(Integer gapEnd) {
        this.gapEnd = gapEnd;
    }

    
    
    public Float getLon() {
        return lon;
    }

    public void setLon(Float lon) {
        this.lon = lon;
    }
    
    
    public Float getLat() {
        return lat;
    }

    public void setLat(Float lat) {
        this.lat = lat;
    }
    
    public String findGap(){
        int gap=gapEnd-gapStart;
        //String geoHash=GeoHash.encodeHash(getLat(),getLon(),4);
        return "Suspicious Gap : { MMSI : " + getMmsi()+", GapStart : "+getGapStart()+" , GapEnd : "+getGapEnd()+" , GapTime : "+gap+" GeoHash : "+geoHash+" }";
    }
    public SuspiciousGap getGapObj(){
        return this;
    }
}
