package hes.cs63.CEPMonitor.VesselsCoTravel;

public class coTravelInfo {

    private Integer mmsi_1;
    private Integer mmsi_2;
    private float lon1;
    private float lat1;
    private float lon2;
    private float lat2;
    private Integer timestamp;

    public coTravelInfo(Integer mmsi_1, Integer mmsi_2, float lon1, float lat1, float lon2, float lat2, Integer timestamp) {
        this.mmsi_1 = mmsi_1;
        this.mmsi_2 = mmsi_2;
        this.lon1 = lon1;
        this.lat1 = lat1;
        this.lon2 = lon2;
        this.lat2 = lat2;
        this.timestamp = timestamp;
    }

    public Integer getMmsi_1() {
        return mmsi_1;
    }

    public void setMmsi_1(Integer mmsi_1) {
        this.mmsi_1 = mmsi_1;
    }

    public Integer getMmsi_2() {
        return mmsi_2;
    }

    public void setMmsi_2(Integer mmsi_2) {
        this.mmsi_2 = mmsi_2;
    }

    public float getLon1() {
        return lon1;
    }

    public void setLon1(float lon1) {
        this.lon1 = lon1;
    }

    public float getLat1() {
        return lat1;
    }

    public void setLat1(float lat1) {
        this.lat1 = lat1;
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

    public Integer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

    public coTravelInfo getSuspiciousCoTravelInfo(){
        return this;
        //return "Travelling Vessels:Vessel_1:"+mmsi_1+" Longitude_1:"+lon1+" Latitude_1:"+lat1+" Vessel_2:"+mmsi_2;
    }
}
