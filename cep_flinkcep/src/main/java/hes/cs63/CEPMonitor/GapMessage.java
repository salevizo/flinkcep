package hes.cs63.CEPMonitor;

import com.google.gson.annotations.SerializedName;


public class GapMessage {
    @SerializedName("MMSI")
    private Integer mmsi;

    @SerializedName("GapStart")
    private Integer GapStart;

    @SerializedName("GapEnd")
    private Integer GapEnd;

    @SerializedName("GeoHash")
    private String GeoHash;

    public Integer getMmsi() {
        return mmsi;
    }

    public void setMmsi(Integer mmsi) {
        this.mmsi = mmsi;
    }

    public Integer getGapStart() {
        return GapStart;
    }

    public void setGapStart(Integer gapStart) {
        GapStart = gapStart;
    }

    public Integer getGapEnd() {
        return GapEnd;
    }

    public void setGapEnd(Integer gapEnd) {
        GapEnd = gapEnd;
    }


    public String getGeoHash() {
        return GeoHash;
    }

    public void setGeoHash(String geoHash) {
        GeoHash = geoHash;
    }
}
