package hr.fer.zemris.nenr.dz5.neuralnetwork.networklayer;

import hr.fer.zemris.nenr.dz5.neuralnetwork.neuron.INeuron;

public interface INeuralNetworkLayer {
    int size();
    INeuron getNeuron(int index);
}
