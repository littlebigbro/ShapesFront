package viewModel;

import java.util.List;

import Utils.HttpConnector;
import model.Point;
import model.Shape;
import model.ShapeTypes;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.*;

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
    private String chartOneType = "bubble";
    private String chartAllType = "line";
    private XYZModel chartOneModel = new SimpleXYZModel();
    private XYZModel chartAllModel = new SimpleXYZModel();
    private boolean drawAllBox = true;

    //TODO: Настроить:
    // 1) Настроить валидатор значений

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
        if (selectedShape == null) {
            drawAll();
        }
    }

    @Command
    @NotifyChange("selectedShape")
    public void updateSelectedShapeFromDB() {
        selectedShape = shapeService.getBy_id(selectedShape.get_id());
        drawShape();
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
        drawShape();
    }

    @Command
    @NotifyChange("selectedShape")
    public void rollShape(@BindingParam("_id") String _id, @BindingParam("angle") String angle) {
        if (_id != null && !_id.isEmpty()) {
            shapeService.rollShape(_id, angle);
        }
        selectedShape = shapeService.updateSelectedShape(selectedShape);
        drawShape();
    }

    @Command
    @NotifyChange("selectedShape")
    public void scaleShape(@BindingParam("_id") String _id, @BindingParam("scale") String scale) {
        if (_id != null && !_id.isEmpty()) {
            shapeService.scaleShape(_id, scale);
        }
        selectedShape = shapeService.updateSelectedShape(selectedShape);
        drawShape();
    }

    @Command
    @NotifyChange("params")
    public void saveShape(@BindingParam("_id") String _id) {
        shapeService.saveUpdatedShape(_id);
        findAll();
    }

    @Command
    public void delete(@BindingParam("_id") String _id) {
        if (_id != null && !_id.isEmpty()) {
            StringBuilder message = new StringBuilder();
            message.append("Вы уверены что хотите удалить фигуру, с параметрами: \n")
                    .append(selectedShape.getDescription()).append("?");
            Messagebox.setTemplate("/zul/mBox.zul");
            Messagebox.show(message.toString(),
                    "Удалить?", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION,
                    new EventListener() {
                        public void onEvent(Event e) {
                            if (Messagebox.ON_OK.equals(e.getName())) {
                                deleteResult = shapeService.deleteShape(_id);
                            }
                        }
                    }
            );
        }
        findAll();
    }

    @Command
    @NotifyChange({"createBtn", "editBtn",
            "deleteBtn", "editBox",
            "chartOneType", "chartOneModel",
            "chartAllType", "chartAllModel",
            "drawAllBox"})
    public void isVisible(@BindingParam("param") String param) {
        createBtn = true;
        if ("createBtn".equalsIgnoreCase(param)) {
            drawAllBox = true;
            deleteBtn = false;
            editBox = false;
            drawAll();
        } else if ("resultTable".equalsIgnoreCase(param)) {
            drawAllBox = false;
            deleteBtn = true;
            editBox = true;
            drawShape();
        } else if ("editBox".equalsIgnoreCase(param)) {
            drawAllBox = false;
            deleteBtn = true;
            editBox = true;
            drawShape();
        } else if ("topPanel".equalsIgnoreCase(param)) {
            drawAllBox = true;
            deleteBtn = false;
            editBox = false;
            drawAll();
        } else if ("default".equalsIgnoreCase(param)) {
            if ("default".equalsIgnoreCase(isVisibleParam)) {
                drawAllBox = true;
                deleteBtn = false;
                editBox = false;
                drawAll();
            } else {
                param = isVisibleParam;
            }
        }
        isVisibleParam = param;
    }

    public void drawShape() {
        chartOneModel.clear();
        chartOneModel.setAutoSort(false);
        chartOneType = "line";
        double firstX = selectedShape.getPoints().get(0).getX();
        double firstY = selectedShape.getPoints().get(0).getY();
        String series = String.valueOf(selectedShape.getId());
        double zCoord = 0.0; //Для двумерных фигур должна быть равна нулю, для круга равна радиусу
        if (ShapeTypes.CIRCLE.toString().equalsIgnoreCase(selectedShape.getName())) {
            chartOneModel.setAutoSort(true);
            chartOneType = "bubble";
            double radius = selectedShape.getRadius();
            //Добавляем невидимый круг в левой нижней координате
            //Необходим для того чтобы "выбранный" круг вообще рисовался
            chartOneModel.addValue(series, (firstX - radius - 1), (firstY - radius - 1), zCoord);
            //Добавляем "выбранный" круг
            chartOneModel.addValue(series, firstX, firstY, (radius * 2));
            //Добавляем невидимый круг в правой верхней координате
            //Необходим для того чтобы "выбранный" круг выравнивался по центру
            chartOneModel.addValue(series, (firstX + radius + 1), (firstY + radius + 1), zCoord);
        } else {
            //Для остальный фигур рисовка аналогична
            for (Point point : selectedShape.getPoints()) {
                chartOneModel.addValue(series, point.getX(), point.getY(), zCoord);
            }
            //добавляем дополнительную точку для постороения последней прямой
            chartOneModel.addValue(series, firstX, firstY, zCoord);
        }
    }

    private void drawAll() {
        double zCoord = 0.0;
        chartOneModel.clear();
        chartOneModel.setAutoSort(true);
        chartOneType = "bubble";
        chartAllModel.clear();
        chartAllModel.setAutoSort(false);
        chartAllType = "line";
        for (Shape shape : shapeList) {
            double firstX = shape.getPoints().get(0).getX();
            double firstY = shape.getPoints().get(0).getY();
            String series = String.valueOf(shape.getId());
            if (ShapeTypes.CIRCLE.toString().equalsIgnoreCase(shape.getName())) {
                double radius = shape.getRadius();
                chartOneModel.addValue(series, (firstX - radius - 1), (firstY - radius - 1), zCoord);
                chartOneModel.addValue(series, firstX, firstY, (radius * 2));
                chartOneModel.addValue(series, (firstX + radius + 1), (firstY + radius + 1), zCoord);
            } else {
                for (Point point : shape.getPoints()) {
                    chartAllModel.addValue(series, point.getX(), point.getY(), zCoord);
                }
                chartAllModel.addValue(series, firstX, firstY, zCoord);
            }
        }

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

    public String getChartOneType() {
        return chartOneType;
    }

    public XYZModel getChartOneModel() {
        return chartOneModel;
    }

    public void setChartOneModel(XYZModel chartOneModel) {
        this.chartOneModel = chartOneModel;
    }

    public XYZModel getChartAllModel() {
        return chartAllModel;
    }

    public void setChartAllModel(XYZModel chartAllModel) {
        this.chartAllModel = chartAllModel;
    }

    public String getChartAllType() {
        return chartAllType;
    }

    public boolean isDrawAllBox() {
        return drawAllBox;
    }
}
