package viewModel;

import java.util.List;

import Utils.HttpConnector;
import model.Shape;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.ListModelList;

public class MainViewModel {
    private ShapeService shapeService = new ShapeService();
    private List<Shape> shapeList = new ListModelList<Shape>();
    private Shape selectedShape;
    private String keyword;

    private boolean deleteResult = false;
    private String area = "0.0";

    @Command
    public void logout() {
        HttpConnector.logout();
        Executions.sendRedirect("/login.zul");
    }

    @Command
    public void search() {
        shapeList.clear();
        shapeList.addAll(shapeService.search(keyword));
    }

    @Command
    @NotifyChange("shapeList")
    public void findAll() {
        shapeList.clear();
        shapeList.addAll(shapeService.findAll());
    }

    @Command
    @NotifyChange("area")
    public void calculateArea(@BindingParam("_id") String _id) {
        for (Shape shape : shapeService.findAll()) {
            if (_id.equals(shape.get_id())) {
                area = shapeService.calculateArea(shape);
                break;
            }
        }
    }


    @Command
    public void create() {

    }

    @Command
    public void view() {

    }

    @Command
    public void edit() {

    }

    @Command
    public void delete(@BindingParam("_id") String _id) {
        deleteResult = shapeService.deleteShape(_id);
        findAll();
    }

    public List<Shape> getShapeList() {
        findAll();
        return shapeList;
    }

    public Shape getSelectedShape() {
        return selectedShape;
    }

    public void setSelectedShape(Shape selectedShape) {
        this.selectedShape = selectedShape;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getArea() {
        return area;
    }

    public boolean isDeleteResult() {
        return deleteResult;
    }
}
