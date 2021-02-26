package viewModel;

import Utils.Converter;
import Utils.HttpConnector;
import model.Shape;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ShapeService {
    private List<Shape> shapeList = new ArrayList<>(Converter.jsonToShapes(HttpConnector.getAll()));

    public ShapeService() {
    }

    public List<Shape> findAll() {
        return shapeList;
    }

    public List<Shape> search(String keyword) {
        shapeList = Converter.jsonToShapes(HttpConnector.getAll());
        List<Shape> result = new LinkedList<Shape>();
        if (keyword == null || "".equals(keyword)) {
            result = shapeList;
        } else {
            for (Shape shape : shapeList) {
                if (shape.getName().toLowerCase().contains(keyword.toLowerCase())
                        || shape.getRuName().toLowerCase().contains(keyword.toLowerCase())
                        || String.valueOf(shape.getId()).contains(keyword.toLowerCase())) {
                    result.add(shape);
                }
            }
        }
        return result;
    }

    public String calculateArea(String _id) {
        Shape shape = findShapeInListBy_id(_id);
        if (shape != null) {
            return HttpConnector.calcShapeArea(Converter.shapeToJSON(shape));
        }
        return "0.0";
    }

    public boolean deleteShape(String _id) {
        return HttpConnector.deleteShape(_id);
    }

    public void moveShape(String _id, String x, String y) {
        Shape shape = findShapeInListBy_id(_id);
        String json = Converter.shapeToJSON(shape);
        String[] params = {"json", json, "x", x, "y", y};
        String result = HttpConnector.moveShape(Converter.createMapping(params));
        updateShapeList(result);
    }

    public void rollShape(String _id, String angle) {
        Shape shape = findShapeInListBy_id(_id);
        String json = Converter.shapeToJSON(shape);
        String[] params = {"json", json, "angle", angle};
        String result = HttpConnector.rollShape(Converter.createMapping(params));
        updateShapeList(result);
    }

    public void scaleShape(String _id, String scale) {
        Shape shape = findShapeInListBy_id(_id);
        String json = Converter.shapeToJSON(shape);
        String[] params = {"json", json, "scale", scale};
        String result = HttpConnector.scaleShape(Converter.createMapping(params));
        updateShapeList(result);
    }

    public void saveUpdatedShape(String _id) {
        HttpConnector.saveUpdatedShape(Converter.shapeToJSON(findShapeInListBy_id(_id)));
    }

    public Shape getBy_id(String _id) {
        return Converter.jsonToShapes(HttpConnector.getById(_id)).get(0);
    }

    private void updateShapeList(String resultJson) {
        Shape newShape = Converter.jsonToShapes(resultJson).get(0);
        int index = -1;
        for (int i = 0; i < shapeList.size(); i++) {
            if (newShape.get_id().equals(shapeList.get(i).get_id())) {
                index = i;
                break;
            }
        }
        if (index >= 0) {
            shapeList.remove(index);
            shapeList.add(index, newShape);
        } else {
            shapeList.add(newShape);
        }
    }

    private Shape findShapeInListBy_id(String _id) {
        for (Shape shape : shapeList) {
            if (_id.equals(shape.get_id())) {
                return shape;
            }
        }
        return null;
    }

    public Shape updateSelectedShape(Shape shape) {
        return findShapeInListBy_id(shape.get_id());
    }
}
