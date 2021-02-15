package viewModel;

import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.Executions;

import java.util.ArrayList;
import java.util.List;

public class CreateViewModel {
    private double x;
    private double y;
    private double radius;
    private List<Double> params;

    @Command
    public void addShape() {

        closeWithoutSave();
    }

    @Command
    public void closeWithoutSave() {
        Executions.sendRedirect("/main.zul");
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

    public List<Double> getParams() {
        return params;
    }
}
