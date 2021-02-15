package model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import Utils.Utils;

@JsonAutoDetect
public class Point {
    private double x;
    private double y;

    public Point() {
    }

    public Point(double x, double y) {
        this.x = Utils.roundDouble(x);
        this.y = Utils.roundDouble(y);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = Utils.roundDouble(x);
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = Utils.roundDouble(y);
    }
}
