package hes.cs63.CEPMonitor.CoTravellingVessels;

import com.google.gson.annotations.SerializedName;

import java.util.LinkedList;
import java.util.Objects;

public class SuspiciousCoTravellingVessels {

    private int mmsi_1;
    private int mmsi_2;
    private float lon1;
    private float lat1;
    private float lon2;
    private float lat2;
    private int timestamp;
    private LinkedList<SuspiciousCoTravellingVessels> msgs;

    public SuspiciousCoTravellingVessels(int mmsi_1, int mmsi_2, float lon1, float lat1, float lon2, float lat2, int timestamp) {
        this.mmsi_1 = mmsi_1;
        this.mmsi_2 = mmsi_2;
        this.lon1 = lon1;
        this.lat1 = lat1;
        this.lon2 = lon2;
        this.lat2 = lat2;
        this.timestamp = timestamp;
        msgs=new LinkedList<SuspiciousCoTravellingVessels>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SuspiciousCoTravellingVessels that = (SuspiciousCoTravellingVessels) o;
        return mmsi_1 == that.mmsi_1 &&
                mmsi_2 == that.mmsi_2 &&
                Float.compare(that.lon1, lon1) == 0 &&
                Float.compare(that.lat1, lat1) == 0 &&
                Float.compare(that.lon2, lon2) == 0 &&
                Float.compare(that.lat2, lat2) == 0 &&
                timestamp == that.timestamp;
    }

    @Override
    public int hashCode() {
        return Objects.hash(mmsi_1, mmsi_2, lon1, lat1, lon2, lat2, timestamp, msgs);
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

    public LinkedList<SuspiciousCoTravellingVessels> getMsgs() {
        return msgs;
    }

    public void setMsgs(LinkedList<SuspiciousCoTravellingVessels> msgs) {
        this.msgs = msgs;
    }

    public String findVessels(){
        String returnStr="----------------------------"+mmsi_1+" with "+mmsi_2+"-------------------------------------------------";
        for(SuspiciousCoTravellingVessels e:this.msgs){
            returnStr=returnStr+"\n"+"SuspiciousCoTravellingVessels{" +
                "mmsi_1=" + e.getMmsi_1() +
                        ", mmsi_2=" + e.getMmsi_2() +
                        ", lon1=" + e.getLon1() +
                        ", lat1=" + e.getLat1() +
                        ", lon2=" + e.getLon2() +
                        ", lat2=" + e.getLat2() +
                        ", timestamp=" + e.getTimestamp()+
                        '}';

        }
        returnStr=returnStr+"\n"+"SuspiciousCoTravellingVessels{" +
                "mmsi_1=" + this.getMmsi_1() +
                ", mmsi_2=" + this.getMmsi_2() +
                ", lon1=" + this.getLon1() +
                ", lat1=" + this.getLat1() +
                ", lon2=" + this.getLon2() +
                ", lat2=" + this.getLat2() +
                ", timestamp=" + this.getTimestamp()+
                '}';
        returnStr=returnStr+"\n"+"----------------------------"+mmsi_1+" with "+mmsi_2+"-------------------------------------------------";
        return returnStr;
    }

    public String findVesselsQGIS(){
        return "" + mmsi_1+","+mmsi_2+","+timestamp+","+lon1+","+lat1+","+lon2+","+lat2+"}";
   }

}
