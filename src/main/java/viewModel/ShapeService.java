package viewModel;

import Utils.Converter;
import Utils.HttpConnector;
import model.Shape;

import java.util.LinkedList;
import java.util.List;

public class ShapeService {
    private List<Shape> shapeList = Converter.jsonToShapes(HttpConnector.getAll());

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

    private void updateShapeList(String resultJson) {
        Shape newShape = Converter.jsonToShapes(resultJson).get(0);
        for(int i = 0; i < shapeList.size(); i++) {
            if (newShape.get_id().equals(shapeList.get(i).get_id())) {
                shapeList.remove(shapeList.get(i));
            }
        }
        shapeList.add(newShape);
    }

    private Shape findShapeInListBy_id(String _id) {
        for (Shape shape : shapeList) {
            if (_id.equals(shape.get_id())) {
                return shape;
            }
        }
        return null;
    }

}
