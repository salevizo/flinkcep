package hes.cs63.CEPMonitor.Acceleration;

import java.util.LinkedList;
import java.util.Objects;

public class SuspiciousSpeedNearPort {

    private int mmsi;
    private float lon;
    private float lat;
    private int t;
    private LinkedList<SuspiciousSpeedNearPort> msgs=new LinkedList<SuspiciousSpeedNearPort>();
    public SuspiciousSpeedNearPort(int mmsi_, float lon_, float lat_, int t){
        this.mmsi = mmsi_;
        this.lon=lon_;
        this.lat=lat_;
        this.t=t;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SuspiciousSpeedNearPort that = (SuspiciousSpeedNearPort) o;
        return mmsi == that.mmsi &&
                Float.compare(that.lon, lon) == 0 &&
                Float.compare(that.lat, lat) == 0 &&
                t == that.t;
    }

    @Override
    public int hashCode() {
        return Objects.hash(mmsi, lon, lat, t, msgs);
    }

    public LinkedList<SuspiciousSpeedNearPort> getMsgs() {
        return msgs;
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

    public int getT() {
        return t;
    }

    public void setT(int t) {
        this.t = t;
    }

    public String findShip(){
        String returnStr="----------------------------"+mmsi+"-------------------------------------------------";
        for(SuspiciousSpeedNearPort e:this.msgs){
            returnStr=returnStr+"\n"+"SuspiciousSpeedNearPort{" +
                    "mmsi_1=" + e.getMmsi() +
                    ", lon=" + e.getLon() +
                    ", lat=" + e.getLat() +
                    ", timestamp=" + e.getT()+
                    '}';

        }
        returnStr=returnStr+"\n"+"SuspiciousSpeedNearPort{" +
                "mmsi_1=" + getMmsi() +
                ", lon=" + getLon() +
                ", lat=" + getLat() +
                ", timestamp=" + getT()+
                '}';
        returnStr=returnStr+"\n"+"----------------------------"+mmsi+"-------------------------------------------------";
        return returnStr;
    }
}
