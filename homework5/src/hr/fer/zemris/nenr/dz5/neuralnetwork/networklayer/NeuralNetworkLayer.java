package hr.fer.zemris.nenr.dz5.neuralnetwork.networklayer;

import hr.fer.zemris.nenr.dz5.neuralnetwork.neuron.INeuron;
import hr.fer.zemris.nenr.dz5.neuralnetwork.neuron.Neuron;
import hr.fer.zemris.nenr.dz5.neuralnetwork.transferfunction.TransferFunction;

public class NeuralNetworkLayer implements INeuralNetworkLayer {
    private Neuron[] neurons;

    public NeuralNetworkLayer(int numberOfNeurons, int previousLayerSize, TransferFunction transferFunction) {
        neurons = new Neuron[numberOfNeurons];
        for(int i = 0 ; i< numberOfNeurons; i++) {
            neurons[i] = new Neuron(previousLayerSize,transferFunction);
        }
    }

    public void calculateOutputs(INeuralNetworkLayer previousLayer) {
        for(int i = 0 ; i < neurons.length ; i++) {
            neurons[i].calculateOutput(previousLayer);
        }
    }

    @Override
    public int size() {
        return neurons.length;
    }

    @Override
    public INeuron getNeuron(int index) {
        return neurons[index];
    }
}
