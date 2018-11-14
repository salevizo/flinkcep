package hes.cs63.CEPMonitor.SimpleEvents;

import java.util.LinkedList;


public class SuspiciousGap {

    private Integer mmsi;
    private Integer gapStart;
    private Integer gapEnd;
    private Integer thresholdGap=600;

    public SuspiciousGap(Integer mmsi_, Integer gapStart_,Integer gapEnd_){
        this.mmsi = mmsi_;
        this.gapEnd=gapEnd_;
        this.gapStart=gapStart_;
    }

    public Integer getMmsi() {
        return mmsi;
    }

    public void setMmsi(Integer mmsi) {
        this.mmsi = mmsi;
    }

    public Integer getGapStart() {
        return gapStart;
    }

    public void setGapStart(Integer gapStart) {
        this.gapStart = gapStart;
    }

    public Integer getGapEnd() {
        return gapEnd;
    }

    public void setGapEnd(Integer gapEnd) {
        this.gapEnd = gapEnd;
    }

    public String findGap(){
        int gap=gapEnd-gapStart;
        return "Suspicious Gap : { MMSI : " + getMmsi()+", Gap Start : "+getGapStart()+" , Gap End : "+getGapEnd()+" , Gap Time : "+gap+" }";
    }
}
