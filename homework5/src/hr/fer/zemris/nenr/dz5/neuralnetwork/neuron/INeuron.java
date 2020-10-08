package hr.fer.zemris.nenr.dz5.neuralnetwork.neuron;

import hr.fer.zemris.nenr.dz5.neuralnetwork.networklayer.INeuralNetworkLayer;

public interface INeuron {
    double getOutput();
    INeuralNetworkLayer getNextLayer();
    void setNextLayer(INeuralNetworkLayer nextLayer);
}
