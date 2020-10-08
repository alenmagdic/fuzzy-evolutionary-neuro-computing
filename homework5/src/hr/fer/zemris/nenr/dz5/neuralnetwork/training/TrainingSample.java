package hr.fer.zemris.nenr.dz5.neuralnetwork.training;

public class TrainingSample {
    private double[] inputValues;
    private double[] outputValues;

    public TrainingSample(double[] inputValues,double[] outputValues) {
        this.inputValues = inputValues;
        this.outputValues = outputValues;
    }

    public double[] getInputValues() {
        return inputValues;
    }

    public double[] getOutputValues() {
        return outputValues;
    }
}
