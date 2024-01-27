package com.openclassrooms.tourguide.model;

public class AttractionInformationDto {
    private String attractionName;
    private double attractionLat;
    private double attractionLong;
    private double userLat;
    private double userLong;
    private double distance;
    private int rewardPoints;

  /*  public AttractionInformationDto(String attractionName, double attractionLat, double attractionLong,
                                 double userLat, double userLong, double distance, int rewardPoints) {
        this.attractionName = attractionName;
        this.attractionLat = attractionLat;
        this.attractionLong = attractionLong;
        this.userLat = userLat;
        this.userLong = userLong;
        this.distance = distance;
        this.rewardPoints = rewardPoints;
    }*/
    
    public AttractionInformationDto() {
    }

    // Getters
    public String getAttractionName() {
        return attractionName;
    }

    public double getAttractionLat() {
        return attractionLat;
    }

    public double getAttractionLong() {
        return attractionLong;
    }

    public double getUserLat() {
        return userLat;
    }

    public double getUserLong() {
        return userLong;
    }

    public double getDistance() {
        return distance;
    }

    public int getRewardPoints() {
        return rewardPoints;
    }

    // Setters
    public void setAttractionName(String attractionName) {
        this.attractionName = attractionName;
    }

    public void setAttractionLat(double attractionLat) {
        this.attractionLat = attractionLat;
    }

    public void setAttractionLong(double attractionLong) {
        this.attractionLong = attractionLong;
    }

    public void setUserLat(double userLat) {
        this.userLat = userLat;
    }

    public void setUserLong(double userLong) {
        this.userLong = userLong;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setRewardPoints(int rewardPoints) {
        this.rewardPoints = rewardPoints;
    }
}

