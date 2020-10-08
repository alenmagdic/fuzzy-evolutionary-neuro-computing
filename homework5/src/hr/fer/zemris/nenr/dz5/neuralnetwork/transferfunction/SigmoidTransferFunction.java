package hr.fer.zemris.nenr.dz5.neuralnetwork.transferfunction;

public class SigmoidTransferFunction implements TransferFunction {

    @Override
    public double calculate(double x) {
        return 1/(1+Math.exp(-x));
    }
}
