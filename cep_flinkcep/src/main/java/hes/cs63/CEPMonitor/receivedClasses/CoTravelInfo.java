package hes.cs63.CEPMonitor.receivedClasses;

import com.google.gson.annotations.SerializedName;

public class CoTravelInfo {
    @SerializedName("MMSI_1")
    private int mmsi_1;
    @SerializedName("MMSI_2")
    private int mmsi_2;
    @SerializedName("Lon_1")
    private float lon1;
    @SerializedName("lat_1")
    private float lat1;
    @SerializedName("Lon_2")
    private float lon2;
    @SerializedName("lat_2")
    private float lat2;
    @SerializedName("time")
    private int timestamp;

    public CoTravelInfo(int mmsi_1, int mmsi_2, float lon1, float lat1, float lon2, float lat2, int timestamp) {
        this.mmsi_1 = mmsi_1;
        this.mmsi_2 = mmsi_2;
        this.lon1 = lon1;
        this.lat1 = lat1;
        this.lon2 = lon2;
        this.lat2 = lat2;
        this.timestamp = timestamp;
    }

    public int getMmsi_1() {
        return mmsi_1;
    }

    public void setMmsi_1(int mmsi_1) {
        this.mmsi_1 = mmsi_1;
    }

    public int getMmsi_2() {
        return mmsi_2;
    }

    public void setMmsi_2(int mmsi_2) {
        this.mmsi_2 = mmsi_2;
    }

    public float getLon1() {
        return lon1;
    }

    public void setLon1(float lon1) {
        this.lon1 = lon1;
    }

    public float getLat1() {
        return lat1;
    }

    public void setLat1(float lat1) {
        this.lat1 = lat1;
    }

    public float getLon2() {
        return lon2;
    }

    public void setLon2(float lon2) {
        this.lon2 = lon2;
    }

    public float getLat2() {
        return lat2;
    }

    public void setLat2(float lat2) {
        this.lat2 = lat2;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public CoTravelInfo getSuspiciousCoTravelInfo(){
        return this;
    }
}
