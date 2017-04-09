package com.tw.hackmob.saferide.model;

import android.graphics.Color;

import java.io.Serializable;
import java.util.Random;

/**
 * Created by fjmartins on 4/8/2017.
 */

public class Route implements Serializable {

    private User owner;
    private String time;
    private Location from;
    private Location to;
    private int color;

    public Route() {}

    public Route(Location from, Location to) {
        this.from = from;
        this.to = to;

        Random rnd = new Random();
        color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
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

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
