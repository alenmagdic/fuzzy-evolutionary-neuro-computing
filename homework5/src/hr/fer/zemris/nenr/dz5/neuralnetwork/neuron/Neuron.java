package hr.fer.zemris.nenr.dz5.neuralnetwork.neuron;

import hr.fer.zemris.nenr.dz5.neuralnetwork.networklayer.INeuralNetworkLayer;
import hr.fer.zemris.nenr.dz5.neuralnetwork.transferfunction.TransferFunction;

import java.util.Arrays;

public class Neuron implements INeuron{
    private TransferFunction transferFunction;
    private double[] inputWeights; //na indeksu nula je pomocna tezina
    private double calculatedOutput;
    private INeuralNetworkLayer nextLayer;

    public Neuron(int previousLayerSize,TransferFunction transferFunction) {
        inputWeights = new double[previousLayerSize+1];
        Arrays.fill(inputWeights,1);
        this.transferFunction = transferFunction;
    }

    public void setInputWeight(int index, double value) {
        inputWeights[index] = value;
    }

    public double getInputWeight(int index) {
        return inputWeights[index];
    }

    public double calculateOutput(INeuralNetworkLayer previousLayer) {
        double result = inputWeights[0];
        for(int i = 1; i < inputWeights.length; i++) {
            result += previousLayer.getNeuron(i-1).getOutput()*inputWeights[i];
        }
        calculatedOutput = transferFunction.calculate(result);
        return calculatedOutput;
    }

    @Override
    public double getOutput() {
        return calculatedOutput;
    }

    @Override
    public void setNextLayer(INeuralNetworkLayer nextLayer) {
        this.nextLayer = nextLayer;
    }

    @Override
    public INeuralNetworkLayer getNextLayer() {
        return nextLayer;
    }
}
