package hes.cs63.CEPMonitor.Gaps;
import com.github.davidmoten.geo.GeoHash;

public class SuspiciousGap {

    private int mmsi;
    private float lon;
    private float lat;
    private int gapStart;
    private int gapEnd;
    private String geoHash;

    public SuspiciousGap(int mmsi_,float lon_,float lat_,Integer gapStart_,Integer gapEnd_,String geoHash){
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

    public int getMmsi() {
        return mmsi;
    }

    public void setMmsi(int mmsi) {
        this.mmsi = mmsi;
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

    public void setGapEnd(Integer gapEnd) {
        this.gapEnd = gapEnd;
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

    /*public String findGap(){
        int gap=gapEnd-gapStart;
        //String geoHash=GeoHash.encodeHash(getLat(),getLon(),4);
        System.out.println("Writing this:"+"Suspicious Gap : { MMSI : " + getMmsi()+", GapStart : "+getGapStart()+" , GapEnd : "+getGapEnd()+" , GapTime : "+gap+" GeoHash : "+geoHash+" }");
        return "Suspicious Gap : { MMSI : " + getMmsi()+", GapStart : "+getGapStart()+" , GapEnd : "+getGapEnd()+" , GapTime : "+gap+" GeoHash : "+geoHash+" }";
    }*/
    public SuspiciousGap getGapObj(){
        return this;
    }

}
