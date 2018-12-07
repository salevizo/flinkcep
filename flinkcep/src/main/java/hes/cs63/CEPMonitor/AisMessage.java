package hes.cs63.CEPMonitor;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class AisMessage {
    @SerializedName("mmsi")
    private int mmsi;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AisMessage that = (AisMessage) o;
        return mmsi == that.mmsi &&
                status == that.status &&
                Float.compare(that.turn, turn) == 0 &&
                Float.compare(that.course, course) == 0 &&
                Float.compare(that.speed, speed) == 0 &&
                Float.compare(that.heading, heading) == 0 &&
                Float.compare(that.lon, lon) == 0 &&
                Float.compare(that.lat, lat) == 0 &&
                t == that.t;
    }

    @Override
    public int hashCode() {
        return Objects.hash(mmsi, status, turn, course, speed, heading, lon, lat, t);
    }

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
