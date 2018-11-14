package hes.cs63.CEPMonitor;

import java.util.LinkedList;

/**
 * Created by sahbi on 5/8/16.
 */
public class SuspiciousGap {

    private Integer mmsi;
    private LinkedList<Integer> t;




    public SuspiciousGap(Integer mmsi, LinkedList<Integer> t) {
        this.mmsi = mmsi;
        this.t = t;
    }

    public Integer getMmsi() {
        return mmsi;
    }

    public void setMmsi(Integer mmsi) {
        this.mmsi = mmsi;
    }

    public LinkedList<Integer> getTtime() {
        return t;
    }

    public void setATime(LinkedList<Integer> t) {
        this.t = t;
    }

    public SuspiciousGap() {
        this(0,new LinkedList<Integer>());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SuspiciousGap) {
            SuspiciousGap other = (SuspiciousGap) obj;
            return mmsi == other.mmsi && t == other.t;
        } else {
            return false;
        }
    }

    public String gap(){
        boolean gap=false;
        
        if(
                Math.abs(this.t.get(0)-this.t.get(1))>=600 //10 min
                && Math.abs((this.t.get(1)-this.t.get(2)))>=600
        ){
        	gap=true;
        }
        
        
        return "Ship : "+this.getMmsi()+"  gap : "+gap+" .";
    }

    @Override
    public String toString() {
        return "Ship Acceleration : { MMSI : " + getMmsi()+"}";
    }

}
