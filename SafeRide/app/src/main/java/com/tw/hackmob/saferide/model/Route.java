package com.tw.hackmob.saferide.model;

/**
 * Created by fjmartins on 4/8/2017.
 */

public class Route {

    private String owner;
    private String time;
    private Location from;
    private Location to;

    public Route() {}

    public Route(Location from, Location to) {
        this.from = from;
        this.to = to;
    }

    public Location getFrom() {
        return from;
    }

    public void setFrom(Location from) {
        this.from = from;
    }

    public Location getTo() {
        return to;
    }

    public void setTo(Location to) {
        this.to = to;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
