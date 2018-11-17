package com.tarena.lbstest.gson;

/**
 * Author: Administrator
 * Created by: ModelGenerator on 2018/11/10/010
 */
public class PointsItem {
    private long loc_time;
    private double latitude;
    private double longitude;
    private String createTime;
    private double speed;
    private int direction;
    public long getLoc_time() {
        return loc_time;
    }

    public void setLoc_time(long loc_time) {
        this.loc_time = loc_time;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }
}