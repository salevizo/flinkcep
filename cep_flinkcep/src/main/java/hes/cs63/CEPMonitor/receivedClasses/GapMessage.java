package hes.cs63.CEPMonitor.receivedClasses;

import com.google.gson.annotations.SerializedName;


public class GapMessage {
    @SerializedName("MMSI")
    private int mmsi;

    @SerializedName("GapStart")
    private int GapStart;

    @SerializedName("GapEnd")
    private int GapEnd;

    @SerializedName("GeoHash")
    private String GeoHash;

    public int getMmsi() {
        return mmsi;
    }

    public void setMmsi(int mmsi) {
        this.mmsi = mmsi;
    }

    public int getGapStart() {
        return GapStart;
    }

    public void setGapStart(int gapStart) {
        GapStart = gapStart;
    }

    public int getGapEnd() {
        return GapEnd;
    }

    public void setGapEnd(int gapEnd) {
        GapEnd = gapEnd;
    }


    public String getGeoHash() {
        return GeoHash;
    }

    public void setGeoHash(String geoHash) {
        GeoHash = geoHash;
    }
}
