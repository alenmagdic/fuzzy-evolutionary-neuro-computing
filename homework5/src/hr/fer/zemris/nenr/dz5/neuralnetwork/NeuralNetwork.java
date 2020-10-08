package hr.fer.zemris.nenr.dz5.neuralnetwork;

import hr.fer.zemris.nenr.dz5.neuralnetwork.networklayer.INeuralNetworkLayer;
import hr.fer.zemris.nenr.dz5.neuralnetwork.networklayer.NeuralNetworkInputLayer;
import hr.fer.zemris.nenr.dz5.neuralnetwork.networklayer.NeuralNetworkLayer;
import hr.fer.zemris.nenr.dz5.neuralnetwork.neuron.INeuron;
import hr.fer.zemris.nenr.dz5.neuralnetwork.neuron.Neuron;
import hr.fer.zemris.nenr.dz5.neuralnetwork.training.TrainingSample;
import hr.fer.zemris.nenr.dz5.neuralnetwork.training.TrainingSampleGroup;
import hr.fer.zemris.nenr.dz5.neuralnetwork.transferfunction.SigmoidTransferFunction;
import hr.fer.zemris.nenr.dz5.neuralnetwork.transferfunction.TransferFunction;

import java.util.*;
import java.util.function.BiFunction;

public class NeuralNetwork {
    private static final TransferFunction TRANSFER_FUNCTION = new SigmoidTransferFunction();
    private NeuralNetworkInputLayer inputLayer;
    private NeuralNetworkLayer[] internalLayers;
    private NeuralNetworkLayer outputLayer;
    private double learningRate = 0.02;

    private double[] calculatedOutput;

    public NeuralNetwork(List<Integer> layersSizes) {
        inputLayer = new NeuralNetworkInputLayer(layersSizes.get(0));
        internalLayers = new NeuralNetworkLayer[layersSizes.size() - 2];

        int layersNum = layersSizes.size();
        for(int i = 1; i < layersNum - 1; i++) {
            internalLayers[i-1] = new NeuralNetworkLayer(layersSizes.get(i),layersSizes.get(i-1),TRANSFER_FUNCTION);
        }

        outputLayer = new NeuralNetworkLayer(layersSizes.get(layersNum-1),layersSizes.get(layersNum-2),TRANSFER_FUNCTION);

        setNextLayerForEachNeuron();

        calculatedOutput = new double[outputLayer.size()];

    }

    private void setNextLayerForEachNeuron() {
        setNextLayerForNeuronsFromLayer(inputLayer,internalLayers[0]);
        for (int i = 0; i < internalLayers.length; i++) {
            setNextLayerForNeuronsFromLayer(internalLayers[i], i==internalLayers.length-1 ? outputLayer : internalLayers[i+1]);
        }
    }

    private void setNextLayerForNeuronsFromLayer(INeuralNetworkLayer layer, INeuralNetworkLayer nextLayer) {
        for(int i = 0, n = layer.size(); i < n; i++) {
            layer.getNeuron(i).setNextLayer(nextLayer);
        }
    }

    static class NeuronTrainingParams {
        OneSampleTrainingParams[] sampleTrainingParams;

        NeuronTrainingParams(int numberOfSamples) {
            sampleTrainingParams = new OneSampleTrainingParams[numberOfSamples];
            for(int i = 0; i < sampleTrainingParams.length; i++) {
                sampleTrainingParams[i] = new OneSampleTrainingParams();
            }
        }
    }

    static class OneSampleTrainingParams {
        double deltaParam; //vrijednost funkcije delta za pripadni neuron i pripadni uzorak
        double outputParam; //vrijednost outputa kojeg daje pripadni neuron za pripadni uzorak
    }

    public void trainUsingBackpropagationAlgorithm(TrainingSample[] trainingSet, int iterations,
                                                   double learningRate, int iterationsBetweenProgressInfo,
                                                   double targetMeanSquareError) {

        TrainingSampleGroup[] trainingGroups = new TrainingSampleGroup[] {new TrainingSampleGroup(Arrays.asList(trainingSet))};
        train(trainingGroups,iterations,learningRate,iterationsBetweenProgressInfo,targetMeanSquareError);
    }

    public void trainUsingStohasticBackpropagationAlgorithm(TrainingSample[] trainingSet, int iterations,
                                                            double learningRate, int iterationsBetweenProgressInfo,
                                                            double targetMeanSquareError) {

        TrainingSampleGroup[] trainingGroups = new TrainingSampleGroup[trainingSet.length];
        for(int i = 0 ; i < trainingSet.length; i++) {
            trainingGroups[i] = new TrainingSampleGroup(Collections.singletonList(trainingSet[i]));
        }

        train(trainingGroups,iterations,learningRate,iterationsBetweenProgressInfo,targetMeanSquareError);
    }

    public void trainUsingMiniBatchBackpropagationAlgorithm(TrainingSampleGroup[] trainingGroups, int iterations,
                                                            double learningRate, int iterationsBetweenProgressInfo,
                                                            double targetMeanSquareError) {

        train(trainingGroups,iterations,learningRate,iterationsBetweenProgressInfo,targetMeanSquareError);
    }

    private TrainingSample[] collectTrainingSamplesFromGroups(TrainingSampleGroup[] groups) {
        List<TrainingSample> trainingSamples = new ArrayList<>();
        for(TrainingSampleGroup group : groups) {
            group.forEach(trainingSamples::add);
        }

        return trainingSamples.toArray(new TrainingSample[0]);
    }

    private void train(TrainingSampleGroup[] trainingGroups, int iterations, double learningRate, int iterationsBetweenProgressInfo,
                      double targetMeanSquareError) {
        long startTime = System.currentTimeMillis();

        this.learningRate = learningRate;

        TrainingSample[] trainingSet = collectTrainingSamplesFromGroups(trainingGroups);

        Map<INeuron,NeuronTrainingParams> trainingParamsMap = new HashMap<>();
        List<INeuron> neurons = getAllNeurons();
        neurons.forEach(neuron -> trainingParamsMap.put(neuron,new NeuronTrainingParams(trainingSet.length)));

        for(int iteration = 1; iteration <= iterations; iteration++) {
            int s = 0;
            for(TrainingSampleGroup group : trainingGroups) {
                int firstSampleInGroup = s;
                for(; s < firstSampleInGroup+group.getGroupSize(); s++) {
                    calculateOutput(trainingSet[s].getInputValues());

                    int sampleIndex = s;
                    calculateTrainingParamsForLayer(outputLayer,trainingParamsMap,s,
                            (y,j) -> y*(1-y)*(trainingSet[sampleIndex].getOutputValues()[j] - y));

                    for(int k = internalLayers.length - 1; k >= 0; k--) {
                        final int neuronIndex = k;
                        calculateTrainingParamsForLayer(internalLayers[k],trainingParamsMap,s,
                                (y,j) -> y*(1-y)*
                                        calculateNextLayerDeltaWeightSum(internalLayers[neuronIndex].getNeuron(j),j,sampleIndex,trainingParamsMap));
                    }

                    calculateTrainingParamsForLayer(inputLayer,trainingParamsMap,s,(y,j) -> 0.0);
                }

                updateWeights(trainingParamsMap,firstSampleInGroup,firstSampleInGroup+group.getGroupSize()-1);
            }

            if(iteration%iterationsBetweenProgressInfo == 0 || iteration==iterations || iteration ==1) {
                double mse = calculateMeanSquareError(trainingSet);
                printProgressInfo(mse,iteration,startTime,iterations);
                if(mse <= targetMeanSquareError) {
                    break;
                }
            }
        }
    }

    private void printProgressInfo(double meanSquareError,int iteration,long startTime,int iterations) {
        double elapsedTime = (System.currentTimeMillis()-startTime)/1000.0;
        System.out.printf("\n\n\niteracija: %d \n proteklo vremena: %f sekundi",iteration,elapsedTime);

        if(iteration>1 && iteration < iterations) {
            double estimatedTimeRemaining = elapsedTime / iteration * (iterations - iteration);
            System.out.printf("\nprocjena preostalog vremena za preostale iteracije: %f sekundi", estimatedTimeRemaining);
        }

        System.out.printf("\nsrednja kvadratna pogreška: %f",meanSquareError);
    }

    private void calculateTrainingParamsForLayer(INeuralNetworkLayer layer, Map<INeuron,NeuronTrainingParams> trainingParamsMap, int sampleIndex, BiFunction<Double,Integer,Double> deltaParamFunction) {
        for(int j = 0, n = layer.size(); j < n; j++) {
            INeuron neuron = layer.getNeuron(j);
            OneSampleTrainingParams currentSampleParams = trainingParamsMap.get(neuron).sampleTrainingParams[sampleIndex];

            double output = neuron.getOutput();
            currentSampleParams.outputParam = output;
            currentSampleParams.deltaParam = deltaParamFunction.apply(output,j);
        }
    }

    private double calculateNextLayerDeltaWeightSum(INeuron currentLayerNeuron,int neuronIndexInCurrentLayer, int sampleIndex,Map<INeuron,NeuronTrainingParams> trainingParamsMap) {
        double nextLayerDeltaWeightSum = 0;
        INeuralNetworkLayer nextLayer = currentLayerNeuron.getNextLayer();
        for(int o = 0, m = nextLayer.size() ; o < m; o++) {
            Neuron nextLayerNeuron = (Neuron)nextLayer.getNeuron(o);
            nextLayerDeltaWeightSum += nextLayerNeuron.getInputWeight(neuronIndexInCurrentLayer) *
                    trainingParamsMap.get(nextLayerNeuron).sampleTrainingParams[sampleIndex].deltaParam;
        }
        return nextLayerDeltaWeightSum;
    }

    private void updateWeights(Map<INeuron, NeuronTrainingParams> trainingParamsMap,int samplesStartIndex,int samplesEndIndex) {
        updateWeightsInLayer(outputLayer,internalLayers[internalLayers.length-1],trainingParamsMap,samplesStartIndex,samplesEndIndex);
        updateWeightsInLayer(internalLayers[0],inputLayer,trainingParamsMap,samplesStartIndex,samplesEndIndex);
        for(int i = 1 ; i < internalLayers.length; i++) {
            updateWeightsInLayer(internalLayers[i],internalLayers[i-1],trainingParamsMap,samplesStartIndex,samplesEndIndex);
        }
    }

    private void updateWeightsInLayer(INeuralNetworkLayer layer, INeuralNetworkLayer previousLayer,Map<INeuron, NeuronTrainingParams> trainingParamsMap,int samplesStartIndex,int samplesEndIndex) {
        for(int j = 0, n = layer.size(); j < n; j++) {
            Neuron neuron = (Neuron) layer.getNeuron(j);
            for(int i = 0, m = previousLayer.size(); i <= m; i++) {
                INeuron previousLayerNeuron = i>0 ? previousLayer.getNeuron(i-1) : null; //index 0 je rezerviran za pomocnu tezinu, a ne neku koja dolazi od neurona iz prethodnog sloja

                double oldWeight = neuron.getInputWeight(i);

                double deltaAndOutputSum = 0;
                for(int s = samplesStartIndex; s <= samplesEndIndex; s++) {
                    double delta = trainingParamsMap.get(neuron).sampleTrainingParams[s].deltaParam;
                    double output;
                    if(previousLayerNeuron != null) {
                        output = trainingParamsMap.get(previousLayerNeuron).sampleTrainingParams[s].outputParam;
                    } else {
                        output = 1;
                    }
                    deltaAndOutputSum += delta*output;
                }

                double newWeight = oldWeight + learningRate*deltaAndOutputSum;
                neuron.setInputWeight(i,newWeight);
            }
        }
    }


    private List<INeuron> getAllNeurons() {
        List<INeuron> neurons = new ArrayList<>();
        addNeuronsFromLayerToList(inputLayer,neurons);
        addNeuronsFromLayerToList(outputLayer,neurons);
        for (NeuralNetworkLayer internalLayer : internalLayers) {
            addNeuronsFromLayerToList(internalLayer, neurons);
        }
        return neurons;
    }

    private void addNeuronsFromLayerToList(INeuralNetworkLayer layer, List<INeuron> neuronList) {
        for(int i = 0, n = layer.size(); i < n; i++) {
            neuronList.add(layer.getNeuron(i));
        }
    }

    private double calculateMeanSquareError(TrainingSample[] samples) {
        double sumSquare = 0;
        for(TrainingSample sample : samples) {
            sumSquare += calculateErrorSumSquareForCurrentSample(sample);
        }

        return sumSquare/samples.length;
    }

    private double calculateErrorSumSquareForCurrentSample(TrainingSample sample) {
        double[] output = calculateOutput(sample.getInputValues());
        double sumSquare = 0;
        double[] sampleOutput = sample.getOutputValues();
        for(int i = 0; i < output.length; i++) {
            sumSquare += Math.pow(sampleOutput[i] - output[i],2);
        }
        return sumSquare;
    }

    public double[] calculateOutput(double[] inputValues) {
        inputLayer.setOutputs(inputValues);
        for(int i = 0; i < internalLayers.length; i++) {
            internalLayers[i].calculateOutputs(i==0?inputLayer:internalLayers[i-1]);
        }
        outputLayer.calculateOutputs(internalLayers[internalLayers.length-1]);

        for(int i = 0, n = outputLayer.size() ; i < n; i++) {
            calculatedOutput[i] = outputLayer.getNeuron(i).getOutput();
        }

        return calculatedOutput;
    }
}
