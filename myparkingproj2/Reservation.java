package com.example.myparkingproj2;
public class Reservation {
    private String spotId;
    private String date;
    private String startTime;
    private String endTime;
    private String userId;

    // Constructor
    public Reservation() {
    }

    public String getSpotId() {
        return spotId;
    }

    public void setSpotId(String spotId) {
        this.spotId = spotId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getUserId() { // Getter for userId
        return userId;
    }

    public void setUserId(String userId) { // Setter for userId
        this.userId = userId;
    }

}
