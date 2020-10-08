package hr.fer.zemris.nenr.dz5.neuralnetwork.neuron;

import hr.fer.zemris.nenr.dz5.neuralnetwork.networklayer.INeuralNetworkLayer;

public class InputNeuron implements INeuron{
    private double output;
    private INeuralNetworkLayer nextLayer;

    public void setOutput(double output) {
        this.output = output;
    }

    @Override
    public void setNextLayer(INeuralNetworkLayer nextLayer) {
        this.nextLayer = nextLayer;
    }

    @Override
    public INeuralNetworkLayer getNextLayer() {
        return nextLayer;
    }

    @Override
    public double getOutput() {
        return output;
    }
}
