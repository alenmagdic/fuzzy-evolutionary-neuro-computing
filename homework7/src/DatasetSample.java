public class DatasetSample {
    private double[] inputValues;
    private double[] outputValues;

    public DatasetSample(double[] inputValues, double[] outputValues) {
        this.inputValues = inputValues;
        this.outputValues = outputValues;
    }

    public double[] getInputValues() {
        return inputValues;
    }

    public void setInputValues(double[] inputValues) {
        this.inputValues = inputValues;
    }

    public double[] getOutputValues() {
        return outputValues;
    }

    public void setOutputValues(double[] outputValues) {
        this.outputValues = outputValues;
    }
}
