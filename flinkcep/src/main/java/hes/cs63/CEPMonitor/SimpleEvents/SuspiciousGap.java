package hes.cs63.CEPMonitor.SimpleEvents;
import com.github.davidmoten.geo.GeoHash;
import com.github.davidmoten.geo.GeoHash.*;
import hes.cs63.CEPMonitor.GapMessageSerializer;
import org.apache.flink.api.java.tuple.Tuple2;

import java.util.LinkedList;

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

    public String findGap(){
        int gap=gapEnd-gapStart;
        String geoHash=GeoHash.encodeHash(this.lat,this.lon,4);
        return "Suspicious Gap : { MMSI : " + getMmsi()+", GapStart : "+getGapStart()+" , GapEnd : "+getGapEnd()+" , GapTime : "+gap+" GeoHash : "+geoHash+" }";
    }
    /*
    public byte[] findGapSer(){
        int gap=gapEnd-gapStart;
        String geoHash=GeoHash.encodeHash(this.lat,this.lon,4);
        Tuple2 tuple=new Tuple2(null,this);
        return new GapMessageSerializer().serializeValue(tuple);
    }
    */
    public SuspiciousGap findGapObj(){
        return this;
    }
}
