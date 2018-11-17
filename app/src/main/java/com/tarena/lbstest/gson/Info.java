package com.tarena.lbstest.gson;

/**
 * Author: Administrator
 * Created by: ModelGenerator on 2018/11/10/010
 */
public class Info {
    private int status;
    private String message;
    private int total;
    private int size;
    private double distance;
    private int tollDistance;
    private StartPoint startPoint;
    private EndPoint endPoint;
    private PointsItem[] points;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getTollDistance() {
        return tollDistance;
    }

    public void setTollDistance(int tollDistance) {
        this.tollDistance = tollDistance;
    }

    public StartPoint getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(StartPoint startPoint) {
        this.startPoint = startPoint;
    }

    public EndPoint getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(EndPoint endPoint) {
        this.endPoint = endPoint;
    }

    public PointsItem[] getPoints() {
        return points;
    }

    public void setPoints(PointsItem[] points) {
        this.points = points;
    }
}