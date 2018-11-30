package hes.cs63.CEPMonitor.receivedClasses;

import com.google.gson.annotations.SerializedName;

public class courseHead {
    @SerializedName("MMSI")
    private int mmsi;
    @SerializedName("Course")
    private float course;
    @SerializedName("Heading")
    private float heading;
    @SerializedName("Timestamp")
    private int timestamp;
    @SerializedName("Lat")
    private float lat;
    @SerializedName("Lon")
    private float lon;



    public courseHead(int mmsi, float heading, float course, float lon, float lat, int timestamp) {
        this.mmsi = mmsi;
        this.heading = heading;
        this.course = course;
        this.lon = lon;
        this.lat = lat;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "courseHead{" +
                "mmsi=" + mmsi +
                ", course=" + course +
                ", heading=" + heading +
                ", timestamp=" + timestamp +
                ", lat=" + lat +
                ", lon=" + lon +
                '}';
    }

    public int getMmsi() {
        return mmsi;
    }

    public void setCourse(float course) {
        this.course = course;
    }

    public float getCourse() {
        return course;
    }

    public void setHeading(float heading) {
        this.heading = heading;
    }

    public float getHeading() {
        return heading;
    }

    public void setMmsi(int mmsi) {
        this.mmsi = mmsi;
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

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }
    public courseHead getObj(){
        return this;
    }
}
