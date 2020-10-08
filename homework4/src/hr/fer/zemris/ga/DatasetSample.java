package hr.fer.zemris.ga;

public class DatasetSample {
    private double x;
    private double y;
    private double functionValue;

    public DatasetSample(double x, double y, double functionValue) {
        this.x = x;
        this.y = y;
        this.functionValue = functionValue;
    }

    @Override
    public String toString() {
        return String.format("%f %f %f",x,y,functionValue);
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

    public double getFunctionValue() {
        return functionValue;
    }

    public void setFunctionValue(double functionValue) {
        this.functionValue = functionValue;
    }
}
