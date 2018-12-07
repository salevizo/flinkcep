package hes.cs63.CEPMonitor.CourseHeadDiff;

import java.util.LinkedList;
import java.util.Objects;

public class vesselsInDanger {

    private int mmsi;
    private float lon;
    private float lat;
    private float course;
    private float heading;
    private int timestamp;
    private LinkedList<vesselsInDanger> msgs=new LinkedList<vesselsInDanger>();

    public vesselsInDanger(int mmsi, float heading,float course, float lon, float lat,  int timestamp) {
        this.mmsi = mmsi;
        this.lon = lon;
        this.lat = lat;
        this.course = course;
        this.heading = heading;
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        vesselsInDanger that = (vesselsInDanger) o;
        return mmsi == that.mmsi &&
                Float.compare(that.lon, lon) == 0 &&
                Float.compare(that.lat, lat) == 0 &&
                Float.compare(that.course, course) == 0 &&
                Float.compare(that.heading, heading) == 0 &&
                timestamp == that.timestamp &&
                Objects.equals(msgs, that.msgs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mmsi, lon, lat, course, heading, timestamp, msgs);
    }

    public int getMmsi() {
        return mmsi;
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

    public float getCourse() {
        return course;
    }

    public void setCourse(float course) {
        this.course = course;
    }

    public float getHeading() {
        return heading;
    }

    public void setHeading(float heading) {
        this.heading = heading;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public LinkedList<vesselsInDanger> getMsgs() {
        return msgs;
    }

    public void setMsgs(LinkedList<vesselsInDanger> msgs) {
        this.msgs = msgs;
    }

    public String findHeading(){
        String returnStr="----------------------------"+mmsi+"-------------------------------------------------";
        for(vesselsInDanger e:this.msgs){
            returnStr=returnStr+"\n"+"Dangerous Route{" +
                    "mmsi=" + e.getMmsi() +
                    ", heading=" + e.getHeading() +
                    ", lon=" + e.getLon() +
                    ", lat=" + e.getLat() +
                    ", course=" + e.getCourse() +
                    ", timestamp=" + e.getTimestamp()+
                    '}';

        }
        returnStr=returnStr+"\n"+"Dangerous Route{" +
                "mmsi=" + getMmsi() +
                ", heading=" + getHeading() +
                ", lon=" + getLon() +
                ", lat=" + getLat() +
                ", course=" + getCourse() +
                ", timestamp=" + getTimestamp()+
                '}';
        returnStr=returnStr+"\n"+"----------------------------"+mmsi+"-------------------------------------------------";
        return returnStr;
    }
}
