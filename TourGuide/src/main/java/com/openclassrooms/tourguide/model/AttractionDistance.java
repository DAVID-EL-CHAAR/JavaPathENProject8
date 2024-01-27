package com.openclassrooms.tourguide.model;

import gpsUtil.location.Attraction;

public class AttractionDistance {
    private final Attraction attraction;
    private final double distance;

    public AttractionDistance(Attraction attraction, double distance) {
        this.attraction = attraction;
        this.distance = distance;
    }

    public Attraction getAttraction() {
        return attraction;
    }

    public double getDistance() {
        return distance;
    }
}
