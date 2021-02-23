package viewModel;

import model.ShapeTypes;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.ListModelList;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CreateViewModel {
    private CreateService createService = new CreateService();
    private List<Double> params = new ArrayList<>();
    private List<ShapeTypes> shapeTypeList = new ListModelList<ShapeTypes>(ShapeTypes.values());
    private ShapeTypes selectedShapeType;
    private boolean rectangleParam = false;
    private boolean circleParam = false;
    private boolean notCircle = true;

    @Init
    public void init() {
      setSelectedShapeType(ShapeTypes.TRIANGLE);
    }

    @Command
    @NotifyChange("params")
    public void addShape(@BindingParam("x1") String x1, @BindingParam("y1") String y1,
                         @BindingParam("x2") String x2, @BindingParam("y2") String y2,
                         @BindingParam("x3") String x3, @BindingParam("y3") String y3,
                         @BindingParam("x4") String x4, @BindingParam("y4") String y4,
                         @BindingParam("radius") String radius) {
        params.clear();
        List<String> paramsAsString = new ArrayList<>();
        paramsAsString.add(x1);
        paramsAsString.add(y1);
        paramsAsString.add(x2);
        paramsAsString.add(y2);
        paramsAsString.add(x3);
        paramsAsString.add(y3);
        paramsAsString.add(x4);
        paramsAsString.add(y4);
        paramsAsString.add(radius);
        for (String param : paramsAsString) {
            if (param != null && !param.isEmpty()) {
                params.add(Double.parseDouble(param.replace(',', '.')));
            }
        }
        createService.create(selectedShapeType, params);
        closeWithoutSave();
    }

    @Command
    @NotifyChange({"rectangleParam","circleParam","notCircle"})
    public void shapeIsVisible() {
        if (ShapeTypes.CIRCLE.equals(selectedShapeType)) {
            rectangleParam = false;
            circleParam = true;
        } else if (ShapeTypes.RECTANGLE.equals(selectedShapeType)) {
            rectangleParam = true;
            circleParam = false;
        } else if (ShapeTypes.TRIANGLE.equals(selectedShapeType)) {
            rectangleParam = false;
            circleParam = false;
        }
        notCircle = !circleParam;
    }

    @Command
    public void closeWithoutSave() {
        Executions.sendRedirect("/zul/main.zul");
    }

    public Validator doubleValidator() {
        return new AbstractValidator() {
            @Override
            public void validate(ValidationContext ctx) {
                String pat = "([\\s]*)?([0-9]+([,|.][0-9]+)?)?";
                String str = (String) ctx.getProperty().getValue();
                Pattern pattern = Pattern.compile(pat);
                if (str != null && !str.isEmpty()) {
                    Matcher matcher = pattern.matcher(str);
                    if (matcher.matches()) {
                        ctx.isValid();
                    }
                } else {
                    ctx.setInvalid();
                }
            }
        };
    }

    public String getParams() {
        return params.toString();
    }

    public List<ShapeTypes> getShapeTypeList() {
        return shapeTypeList;
    }

    public void setShapeTypeList(List<ShapeTypes> shapeTypeList) {
        this.shapeTypeList = shapeTypeList;
    }

    public ShapeTypes getSelectedShapeType() {
        return selectedShapeType;
    }

    public void setSelectedShapeType(ShapeTypes selectedShapeType) {
        this.selectedShapeType = selectedShapeType;
    }

    public boolean isRectangleParam() {
        return rectangleParam;
    }

    public void setRectangleParam(boolean rectangleParam) {
        this.rectangleParam = rectangleParam;
    }

    public boolean isCircleParam() {
        return circleParam;
    }

    public void setCircleParam(boolean circleParam) {
        this.circleParam = circleParam;
    }

    public boolean isNotCircle() {
        return notCircle;
    }

    public void setNotCircle(boolean notCircle) {
        this.notCircle = notCircle;
    }
}
