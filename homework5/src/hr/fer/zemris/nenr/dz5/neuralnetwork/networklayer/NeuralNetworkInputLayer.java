package hr.fer.zemris.nenr.dz5.neuralnetwork.networklayer;

import hr.fer.zemris.nenr.dz5.neuralnetwork.neuron.InputNeuron;

public class NeuralNetworkInputLayer implements INeuralNetworkLayer {
    private InputNeuron[] neurons;

    public NeuralNetworkInputLayer(int numberOfNeurons) {
        neurons = new InputNeuron[numberOfNeurons];
        for(int i = 0 ; i< numberOfNeurons; i++) {
            neurons[i] = new InputNeuron();
        }
    }

    public void setOutputs(double[] outputs) {
        for(int i = 0 ; i < neurons.length; i++) {
            neurons[i].setOutput(outputs[i]);
        }
    }

    @Override
    public int size() {
        return neurons.length;
    }

    @Override
    public InputNeuron getNeuron(int index) {
        return neurons[index];
    }
}
