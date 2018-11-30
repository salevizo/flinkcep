package hes.cs63.CEPMonitor.SpeedVesselType;
import com.github.davidmoten.geo.GeoHash;

public class SuspiciousSpeedVesselType {

    private int mmsi;
    private float speed;


    public SuspiciousSpeedVesselType(int mmsi_,float speed_){
        this.mmsi = mmsi_;
        this.speed=speed_;
    }

    
    public int getMmsi() {
        return mmsi;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getSpeed() {
        return speed;
    }

    public String findSpeed(){
     
        //System.out.println("Writing this:"+"Suspicious speed : { MMSI : " + getMmsi()+", speed : "+getSpeed()+" }");
        return "Suspicious Speed :{ MMSI : " + getMmsi()+", speed : "+getSpeed()+" }";
    }
   

}
