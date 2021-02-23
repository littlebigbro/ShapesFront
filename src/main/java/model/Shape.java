package model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonAutoDetect
public class Shape {
    private static final long serialVersionUID = 1L;
    @JsonProperty("shapeType")
    private String name;
    private String ruName;
    private List<Point> points;
    private double radius = 0;
    private String type;
    private String _id;
    private int id;
    private Point center = new Point();
    @JsonIgnore
    private String description;
    @JsonIgnore
    private String area;

    public Shape() {
    }

    public Point getCenter() {
        return center;
    }

    public String getName() {
        return name;
    }

    public String getRuName() {
        return ruName;
    }

    public double getRadius() {
        return radius;
    }

    public List<Point> getPoints() {
        return points;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String get_id() {
        return _id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        StringBuilder params = new StringBuilder();
        if (!"круг".equalsIgnoreCase(this.ruName)) {
            for (int i = 0; i < points.size(); i++) {
                params.append("x = ").append(points.get(i).getX()).append(", y = ").append(points.get(i).getY());
                if (i + 1 != points.size()) {
                    params.append("; ");
                }
            }
        } else {
            params.append("Центр: x = ")
                    .append(points.get(0).getX())
                    .append(", y = ")
                    .append(points.get(0).getX())
                    .append(". Радиус = ")
                    .append(this.radius).append(".");
        }
        return params.toString();
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRuName(String ruName) {
        this.ruName = ruName;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
