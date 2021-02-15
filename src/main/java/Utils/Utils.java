package Utils;

import model.Point;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static Point calculateCenter(List<Point> points) {
        double xC = 0;
        double yC = 0;
        for (Point point : points) {
            xC += point.getX();
            yC += point.getY();
        }
        return new Point(xC / points.size(), yC / points.size());
    }

    public static List<Point> toPoints(List<java.lang.Double> params) {
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < params.size() - 1; i = i + 2) {
            Point point = new Point(params.get(i), params.get(i + 1));
            points.add(point);
        }
        return points;
    }

    public static String getPointsAsString(List<Point> points) {
        StringBuilder pointCoords = new StringBuilder();
        for (Point point : points) {
            pointCoords.append("(").append(point.getX()).append(", ").append(point.getY()).append(") ");
        }
        return pointCoords.toString().trim();
    }

    /**
     * Возвращает округленное число типа double в формате #.##
     */
    public static double roundDouble(double param) {
        return BigDecimal.valueOf(param)
                .setScale(2, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
    }
}
