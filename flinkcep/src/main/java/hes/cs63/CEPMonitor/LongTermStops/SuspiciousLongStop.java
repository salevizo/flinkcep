package hes.cs63.CEPMonitor.LongTermStops;


public class SuspiciousLongStop {
    private int mmsi;
    private float lon;
    private float lat;
    private int gapStart;
    private int gapEnd;
    private String geoHash;

    public SuspiciousLongStop(int mmsi_,float lon_,float lat_,Integer gapStart_,Integer gapEnd_,String geoHash) {
        this.mmsi = mmsi_;
        this.lon=lon_;
        this.lat=lat_;
        this.gapEnd=gapEnd_;
        this.geoHash=geoHash;
        this.gapStart=gapStart_;
    }

    public String getGeoHash() {
        return geoHash;
    }

    public void setGeoHash(String geoHash) {
        this.geoHash = geoHash;
    }

    public int getMmsi() {
        return mmsi;
    }

    public void setMmsi(int mmsi) {
        this.mmsi = mmsi;
    }

    public int getGapStart() {
        return gapStart;
    }

    public void setGapStart(int gapStart) {
        this.gapStart = gapStart;
    }

    public int getGapEnd() {
        return gapEnd;
    }

    public void setGapEnd(Integer gapEnd) {
        this.gapEnd = gapEnd;
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


    public String getSuspiciousLongStop(){

        return "Vessel Long Stop:"+ getMmsi();
    }

    public String toString() {
        return "Ship suspicious : { MMSI : " + getMmsi()+ getGeoHash() + getGapStart() + getGapEnd() +"}";
    }
}
