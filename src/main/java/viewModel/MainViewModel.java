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
    private boolean editBox = false;
    private boolean editBtn = false;
    private boolean deleteBtn = false;
    private boolean createBtn = true;
    private String isVisibleParam = "default";

    //TODO: Настроить:
    // 1) Отрисовку одной/всех фигур
    // 2) Настроить валидатор значений
    // 3) Очищать параметры для
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
    @NotifyChange("selectedShape")
    public void moveShape(@BindingParam("_id") String _id, @BindingParam("x") String x, @BindingParam("y") String y) {
        if (_id != null && !_id.isEmpty()) {
            shapeService.moveShape(_id, x, y);
        }
        selectedShape = shapeService.updateSelectedShape(selectedShape);
    }

    @Command
    @NotifyChange("selectedShape")
    public void rollShape(@BindingParam("_id") String _id, @BindingParam("angle") String angle) {
        if (_id != null && !_id.isEmpty()) {
            shapeService.rollShape(_id, angle);
        }
        selectedShape = shapeService.updateSelectedShape(selectedShape);
    }

    @Command
    @NotifyChange("selectedShape")
    public void scaleShape(@BindingParam("_id") String _id, @BindingParam("scale") String scale) {
        if (_id != null && !_id.isEmpty()) {
            shapeService.scaleShape(_id, scale);
        }
        selectedShape = shapeService.updateSelectedShape(selectedShape);
    }

    @Command
    @NotifyChange("shapeList")
    public void delete(@BindingParam("_id") String _id) {
        if (_id != null && !_id.isEmpty()) {
            deleteResult = shapeService.deleteShape(_id);
        }
        findAll();
    }

    @Command
    @NotifyChange({"createBtn", "editBtn", "editBox", "deleteBtn"})
    public void isVisible(@BindingParam("param") String param) {
        if ("createBtn".equalsIgnoreCase(param)) {
            createBtn = true;
            deleteBtn = false;
            editBtn = false;
            editBox = false;
        } else if ("resultTable".equalsIgnoreCase(param)) {
            createBtn = true;
            deleteBtn = true;
            editBtn = true;
            editBox = false;
        } else if ("editBox".equalsIgnoreCase(param)) {
            createBtn = true;
            deleteBtn = true;
            editBtn = false;
            editBox = true;
        } else if ("topPanel".equalsIgnoreCase(param)) {
            createBtn = true;
            deleteBtn = false;
            editBtn = false;
            editBox = false;
        } else if ("default".equalsIgnoreCase(param)) {
            if ("default".equalsIgnoreCase(isVisibleParam)) {
                createBtn = true;
                deleteBtn = false;
                editBtn = false;
                editBox = false;
            } else {
                param = isVisibleParam;
            }
        }
        isVisibleParam = param;
    }

    @Command
    @NotifyChange("params")
    public void saveShape(@BindingParam("_id") String _id) {
        shapeService.saveUpdatedShape(_id);
        findAll();
    }

    public boolean isCreateBtn() {
        return createBtn;
    }

    public void setCreateBtn(boolean createBtn) {
        this.createBtn = createBtn;
    }

    public boolean isEditBtn() {
        return editBtn;
    }

    public void setEditBtn(boolean editBtn) {
        this.editBtn = editBtn;
    }

    public boolean isEditBox() {
        return editBox;
    }

    public void setEditBox(boolean editBox) {
        this.editBox = editBox;
    }

    public boolean isDeleteBtn() {
        return deleteBtn;
    }

    public void setDeleteBtn(boolean deleteBtn) {
        this.deleteBtn = deleteBtn;
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
