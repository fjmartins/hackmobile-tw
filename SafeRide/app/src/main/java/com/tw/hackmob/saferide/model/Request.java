package com.tw.hackmob.saferide.model;

/**
 * Created by phgm on 09/04/2017.
 */

public class Request {
    private String uid;
    private User userRequest;
    private User userOwner;
    private String userOwnerUid;
    private Route route;
    private int status = 0;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public User getUserRequest() {
        return userRequest;
    }

    public void setUserRequest(User userRequest) {
        this.userRequest = userRequest;
    }

    public User getUserOwner() {
        return userOwner;
    }

    public void setUserOwner(User userOwner) {
        this.userOwner = userOwner;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUserOwnerUid() {
        return userOwnerUid;
    }

    public void setUserOwnerUid(String userOwnerUid) {
        this.userOwnerUid = userOwnerUid;
    }
}
