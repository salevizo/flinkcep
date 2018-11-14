package hes.cs63.CEPMonitor;

import java.util.LinkedList;

/**
 * Created by sahbi on 5/8/16.
 */
public class Pause {

    private Integer mmsi;
    private LinkedList<Float> speed;




    public Pause(Integer mmsi, LinkedList<Float> speed) {
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

    public Pause() {
        this(0,new LinkedList<Float>());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Pause) {
            Pause other = (Pause) obj;
            return mmsi == other.mmsi && speed == other.speed;
        } else {
            return false;
        }
    }

    public String pause(){
        boolean pause=false;
        
        if(
                Math.abs(this.speed.get(0)-this.speed.get(1))<=1 //1KNOT
                && Math.abs((this.speed.get(1)-this.speed.get(2)))<=1
        ){
        	pause=true;
        }
        
        
        return "Ship : "+this.getMmsi()+"  Pause : "+pause+" .";
    }

    @Override
    public String toString() {
        return "Ship Pause : { MMSI : " + getMmsi()+"}";
    }

}
