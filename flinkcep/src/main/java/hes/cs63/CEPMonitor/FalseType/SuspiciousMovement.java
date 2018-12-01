package hes.cs63.CEPMonitor.FalseType;


/**
 * Created by sahbi on 5/8/16.
 */
public class SuspiciousMovement {
    private Integer mmsi;
    private float speeds;

    public SuspiciousMovement(Integer mmsi, Float speed) {
        this.mmsi = mmsi;
        this.speeds = speed;
    }
    public Integer getMmsi() {
        return mmsi;
    }
    public void setMmsi(Integer mmsi) {
        this.mmsi = mmsi;
    }
    public Float getSpeed() {
        return speeds;
    }
    public void setSpeeds(Float acceleration) {
        this.speeds = acceleration;
    }
    public SuspiciousMovement() {
        this(0, (float) 0.0);
    }
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SuspiciousMovement) {
            SuspiciousMovement other = (SuspiciousMovement) obj;
            return mmsi == other.mmsi && speeds == other.speeds;
        } else {
            return false;
        }
    }
    public String movement(){
        boolean movement=false;
        if(
                Math.abs(this.speeds)>=5

        ){
            movement=true;
        }
        return "Ship : "+this.getMmsi()+"  suspicious : "+movement+" "+this.speeds+".";
    }
    @Override
    public String toString() {
        return "Ship suspicious : { MMSI : " + getMmsi()+ getSpeed()+"}";
    }
}