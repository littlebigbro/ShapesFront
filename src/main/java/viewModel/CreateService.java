package viewModel;

import Utils.Converter;
import Utils.HttpConnector;

import java.util.ArrayList;
import java.util.List;

import model.Point;
import model.Shape;
import model.ShapeTypes;

public class CreateService {
    public CreateService() {
    }

    public void create(ShapeTypes shapeType, List<Double> params) {
        Shape shape = new Shape();
        shape.setType(shapeType.toString());
        shape.setName(shapeType.toString());
        shape.setRuName(shapeType.getRuName());
        List<Point> pointList = new ArrayList<>();
        for (int i = 0; i < params.size(); i = i + 2) {
            if (i + 1 < params.size()) {
                pointList.add(new Point(params.get(i), params.get(i + 1)));
            } else {
                shape.setRadius(params.get(i));
            }
        }
        shape.setId(HttpConnector.getNewId());
        shape.setPoints(pointList);
        HttpConnector.createShape(Converter.shapeToJSON(shape));
    }
}
