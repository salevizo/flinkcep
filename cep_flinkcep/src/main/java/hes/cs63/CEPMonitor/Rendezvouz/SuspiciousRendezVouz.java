package hes.cs63.CEPMonitor.Rendezvouz;

import java.util.Objects;

public class SuspiciousRendezVouz {

    private int mmsi_1;
    private int mmsi_2;
    private String geoHash;
    private int gapEnd_1;
    private int gapEnd_2;
    private float lon1;
    private float lat1;
    private float lon2;
    private float lat2;
    
    //public SuspiciousRendezVouz(Integer mmsi_1, Integer mmsi_2, String geoHash_, Integer gapEnd_1, Integer gapEnd_2,float lon_, float lat_){ 
    public SuspiciousRendezVouz(int mmsi_1, int mmsi_2, String geoHash_, int gapEnd_1, int gapEnd_2,float lon1_,float lat1_,float lon2_,float lat2_){
        this.mmsi_1 = mmsi_1;
        this.mmsi_2=mmsi_2;
        this.geoHash=geoHash_;
        this.gapEnd_1=gapEnd_1;
        this.gapEnd_2=gapEnd_2;
        this.lon1=lon1_;
        this.lat1=lat1_;
        this.lon2=lon2_;
        this.lat2=lat2_;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SuspiciousRendezVouz that = (SuspiciousRendezVouz) o;
        return mmsi_1 == that.mmsi_1 &&
                mmsi_2 == that.mmsi_2 &&
                gapEnd_1 == that.gapEnd_1 &&
                gapEnd_2 == that.gapEnd_2 &&
                Float.compare(that.lon1, lon1) == 0 &&
                Float.compare(that.lat1, lat1) == 0 &&
                Float.compare(that.lon2, lon2) == 0 &&
                Float.compare(that.lat2, lat2) == 0 &&
                Objects.equals(geoHash, that.geoHash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mmsi_1, mmsi_2, geoHash, gapEnd_1, gapEnd_2, lon1, lat1, lon2, lat2);
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

    public String getGeoHash() {
        return geoHash;
    }

    public void setGeoHash(String geoHash) {
        this.geoHash = geoHash;
    }

    public int getGapEnd_1() {
        return gapEnd_1;
    }

    public void setGapEnd_1(int gapEnd_1) {
        this.gapEnd_1 = gapEnd_1;
    }

    public int getGapEnd_2() {
        return gapEnd_2;
    }

    public void setGapEnd_2(int gapEnd_2) {
        this.gapEnd_2 = gapEnd_2;
    }

    
    public float getLon() {
        return lon1;
    }

    public void setLon(float lon) {
        this.lon1 = lon;
    }

    public float getLat() {
        return lat1;
    }

    public void setLat(float lat) {
        this.lat1 = lat;
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

    public String findGap(){
        return "Suspicious RendezVous : { Vessel_1 : " + mmsi_1+", Vessel_2 : "+mmsi_2+" , Gap_End_1 : "+gapEnd_1+" , Gap_End_2 : "+gapEnd_2+
                ", GeoHash : "+geoHash+", Lon1 : "+lon1 +" , Lat1 : "+lat1+", Lon2 : "+lon2 +" , Lat2 : "+lat2+" }";
    }
    /*
    public String findGapQGIS(){
   return "" + mmsi_1+","+mmsi_2+","+gapEnd_1+","+gapEnd_2+","+geoHash+","+lon+","+lat+"";
    }
*/
}

