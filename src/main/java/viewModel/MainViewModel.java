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
    //TODO: Настроить:
    // 1) Создание фигуры
    // 2) Сохранение отредактированной/созданной фигуры
    // 3) Отрисовку одной/всех фигур
    @Command
    public void logout() {
        HttpConnector.logout();
        Executions.sendRedirect("/zul/login.zul");
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
        area = shapeService.calculateArea(_id);
    }

    @Command
    @NotifyChange("shapeList")
    public void moveShape(@BindingParam("_id") String _id, @BindingParam("x") String x, @BindingParam("y") String y) {
        if (_id != null && !_id.isEmpty()) {
            shapeService.moveShape(_id, x, y);
        }
        findAll();
    }

    @Command
    @NotifyChange("shapeList")
    public void rollShape(@BindingParam("_id") String _id, @BindingParam("angle") String angle) {
        if (_id != null && !_id.isEmpty()) {
            shapeService.rollShape(_id, angle);
        }
        findAll();
    }

    @Command
    @NotifyChange("shapeList")
    public void scaleShape(@BindingParam("_id") String _id, @BindingParam("scale") String scale) {
        if (_id != null && !_id.isEmpty()) {
            shapeService.scaleShape(_id, scale);
        }
        findAll();
    }

    @Command
    @NotifyChange("shapeList")
    public void delete(@BindingParam("_id") String _id) {
        if (_id != null && !_id.isEmpty()) {
            deleteResult = shapeService.deleteShape(_id);
        }
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
