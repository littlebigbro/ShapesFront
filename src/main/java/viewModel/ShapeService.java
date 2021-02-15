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
        if (keyword==null || "".equals(keyword)){
            result = shapeList;
        }else{
            for (Shape shape: shapeList){
                if (shape.getName().toLowerCase().contains(keyword.toLowerCase())
                        ||shape.getRuName().toLowerCase().contains(keyword.toLowerCase())
                        || String.valueOf(shape.getId()).contains(keyword.toLowerCase())){
                    result.add(shape);
                }
            }
        }
        return result;
    }

    public String calculateArea(Shape shape) {
        return HttpConnector.calcShapeArea(Converter.shapeToJSON(shape));
    }

    public boolean deleteShape(String _id) {
        return HttpConnector.deleteShape(_id);
    }
}
