package hes.cs63.CEPMonitor;

import java.util.LinkedList;

/**
 * Created by sahbi on 5/8/16.
 */
public class SuspiciousTurn {

    private Integer mmsi;
    private LinkedList<Float> turns;




    public SuspiciousTurn(Integer mmsi, LinkedList<Float> acceleration) {
        this.mmsi = mmsi;
        this.turns = acceleration;
    }

    public Integer getMmsi() {
        return mmsi;
    }

    public void setMmsi(Integer mmsi) {
        this.mmsi = mmsi;
    }

    public LinkedList<Float> getTurns() {
        return turns;
    }

    public void setAcceleration(LinkedList<Float> acceleration) {
        this.turns = acceleration;
    }

    public SuspiciousTurn() {
        this(0,new LinkedList<Float>());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SuspiciousTurn) {
            SuspiciousTurn other = (SuspiciousTurn) obj;
            return mmsi == other.mmsi && turns == other.turns;
        } else {
            return false;
        }
    }

    public String zigNzag(){
        boolean zigNzag=false;
        if(
                Math.abs(this.turns.get(0)-this.turns.get(1))>=15
                && Math.abs((this.turns.get(1)-this.turns.get(2)))>=15
        ){
          zigNzag=true;
        }
        return "Ship : "+this.getMmsi()+"  zigNzag : "+zigNzag+" .";
    }

    @Override
    public String toString() {
        return "Ship Acceleration : { MMSI : " + getMmsi()+"}";
    }

}
