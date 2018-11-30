package hes.cs63.CEPMonitor.CoGHeading;
import com.github.davidmoten.geo.GeoHash;

import java.util.LinkedList;

public class SuspiciousCourseHeading {

    private int mmsi;
    private float heading;
    private float course;
    private float lon;
    private float lat;
    private int timestamp;


    public SuspiciousCourseHeading(int mmsi, float heading, float course, float lon, float lat, int timestamp) {
        this.mmsi = mmsi;
        this.heading = heading;
        this.course = course;
        this.lon = lon;
        this.lat = lat;
        this.timestamp = timestamp;
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
    public SuspiciousCourseHeading getObj(){
        return this;
    }
}
