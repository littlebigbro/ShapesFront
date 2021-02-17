package viewModel;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;

import java.util.*;

public class CreateViewModel {
    private double x;
    private double y;
    private double radius;
    private List<Double> params = new ArrayList<>();

    @Command
    public void addShape() {
        closeWithoutSave();
    }

    @Command
    @NotifyChange
    public void addParamToParams(@BindingParam("cm.x") String param) {
        this.params.add(Double.parseDouble(param));
    }

    @Command
    public void closeWithoutSave() {
        Executions.sendRedirect("/zul/main.zul");
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    @NotifyChange("x")
    public String getParams() {
        return params.toString();
    }
}
