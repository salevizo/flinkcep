package hes.cs63.CEPMonitor.CourseHeadDiff;

import java.util.LinkedList;

public class vesselsInDanger {

    private Integer mmsi;
    private float lon;
    private float lat;
    private float course;
    private float heading;
    private Integer timestamp;
    private LinkedList<vesselsInDanger> msgs=new LinkedList<vesselsInDanger>();

    public vesselsInDanger(Integer mmsi, float heading,float course, float lon, float lat,  Integer timestamp) {
        this.mmsi = mmsi;
        this.lon = lon;
        this.lat = lat;
        this.course = course;
        this.heading = heading;
        this.timestamp = timestamp;
    }

    public Integer getMmsi() {
        return mmsi;
    }

    public void setMmsi(Integer mmsi) {
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

    public Integer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp) {
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
