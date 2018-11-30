package hes.cs63.CEPMonitor.CoGHeading;
import com.github.davidmoten.geo.GeoHash;

public class SuspiciousCourseHeading {

    private int mmsi;
    private float heading;
    private float course;


    public SuspiciousCourseHeading(int mmsi_,float heading_, float course_){
        this.mmsi = mmsi_;
        this.heading=heading_;
        this.course=course_;
    }

    
    public int getMmsi() {
        return mmsi;
    }

    public void setCourse(float course) {
        this.course = course;
    }

    public float getCourse() {
        return course;
    }
    
    public void setHeading(float heading) {
        this.heading = heading;
    }

    public float getHeading() {
        return heading;
    }

    public String findHeading(){
     
       // System.out.println("Writing this:"+"Suspicious heading : { MMSI : " + getMmsi()+", course : "+getCourse()+", heading : "+getHeading()+" }");
        return "Suspicious Heading :{ MMSI : " + getMmsi()+", course : "+getCourse()+", heading: "+ getHeading()+" }";
    }
   

}
