package hes.cs63.CEPMonitor;

import com.google.gson.annotations.SerializedName;

public class AisMessage {
    @SerializedName("mmsi")
    private Integer mmsi;

    @SerializedName("status")
    private int status;

    @SerializedName("turn")
    private float turn;

    @SerializedName("course")
    private float course;

    @SerializedName("speed")
    private float speed;

    @SerializedName("heading")
    private float heading;

    @SerializedName("lon")
    private float lon;

    @SerializedName("lat")
    private float lat;

    @SerializedName("t")
    private int t;

    public int getMmsi() {
        return mmsi;
    }

    public void setMmsi(int mmsi) {
        this.mmsi = mmsi;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public float getTurn() {
        return turn;
    }

    public void setTurn(float turn) {
        this.turn = turn;
    }

    public float getCourse() {
        return course;
    }

    public void setCourse(float course) {
        this.course = course;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getHeading() {
        return heading;
    }

    public void setHeading(float heading) {
        this.heading = heading;
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

    public void setLat(float lat) {
        this.lat = lat;
    }

    public int getT() {
        return t;
    }

    public void setT(int t) {
        this.t = t;
    }

    public String toString(){
        return new String("Mmsi:"+this.getMmsi());
    }

}
