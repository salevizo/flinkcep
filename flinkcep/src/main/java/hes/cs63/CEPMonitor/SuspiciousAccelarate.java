package hes.cs63.CEPMonitor;

import java.util.LinkedList;

/**
 * Created by sahbi on 5/8/16.
 */
public class SuspiciousAccelarate {

    private Integer mmsi;
    private LinkedList<Float> speed;




    public SuspiciousAccelarate(Integer mmsi, LinkedList<Float> speed) {
        this.mmsi = mmsi;
        this.speed = speed;
    }

    public Integer getMmsi() {
        return mmsi;
    }

    public void setMmsi(Integer mmsi) {
        this.mmsi = mmsi;
    }

    public LinkedList<Float> getTSpeed() {
        return speed;
    }

    public void setAcceleration(LinkedList<Float> speed) {
        this.speed = speed;
    }

    public SuspiciousAccelarate() {
        this(0,new LinkedList<Float>());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SuspiciousAccelarate) {
            SuspiciousAccelarate other = (SuspiciousAccelarate) obj;
            return mmsi == other.mmsi && speed == other.speed;
        } else {
            return false;
        }
    }

    public String zigNzag(){
        boolean suspiciousAccel=false;
        
        if(
                Math.abs(this.speed.get(0)-this.speed.get(1))>=105 //105KNOTS
                && Math.abs((this.speed.get(1)-this.speed.get(2)))>=105
        ){
        	suspiciousAccel=true;
        }
        
        
        return "Ship : "+this.getMmsi()+"  suspiciousAccelarate : "+suspiciousAccel+" .";
    }

    @Override
    public String toString() {
        return "Ship Acceleration : { MMSI : " + getMmsi()+"}";
    }

}
