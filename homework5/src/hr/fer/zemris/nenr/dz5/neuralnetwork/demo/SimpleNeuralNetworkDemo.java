package hr.fer.zemris.nenr.dz5.neuralnetwork.demo;

import hr.fer.zemris.nenr.dz5.neuralnetwork.NeuralNetwork;
import hr.fer.zemris.nenr.dz5.neuralnetwork.training.TrainingSample;
import hr.fer.zemris.nenr.dz5.neuralnetwork.training.TrainingSampleGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimpleNeuralNetworkDemo {

    enum TrainingAlgorithm {
        BACKPROPAGATION, STOHASTIC_BACKPROPAGATION, MINI_BATCH_BACKPROPAGATION
    }

    public static void main(String[] args) {
        NeuralNetwork net = new NeuralNetwork(Arrays.asList(1,6,1));

        TrainingSample[] trainingSamples = new TrainingSample[] {
                new TrainingSample(new double[] {-1}, new double[] {1}),
                new TrainingSample(new double[] {-0.8}, new double[] {0.64}),
                new TrainingSample(new double[] {-0.6}, new double[] {0.36}),
                new TrainingSample(new double[] {-0.4}, new double[] {0.16}),
                new TrainingSample(new double[] {-0.2}, new double[] {0.04}),
                new TrainingSample(new double[] {0}, new double[] {0}),
                new TrainingSample(new double[] {0.2}, new double[] {0.04}),
                new TrainingSample(new double[] {0.4}, new double[] {0.16}),
                new TrainingSample(new double[] {0.6}, new double[] {0.36}),
                new TrainingSample(new double[] {0.8}, new double[] {0.64}),
                new TrainingSample(new double[] {1}, new double[] {1})
        };

        TrainingAlgorithm trainingAlgorithm = TrainingAlgorithm.MINI_BATCH_BACKPROPAGATION;
        int iterations = 5_000_000;
        double learningRate = 0.02;
        int iterationsBetweenProgressInfo = 500_000;
        double targetMse = 0.0003;
        if(trainingAlgorithm == TrainingAlgorithm.BACKPROPAGATION) {
            net.trainUsingBackpropagationAlgorithm(trainingSamples, iterations, learningRate, iterationsBetweenProgressInfo,targetMse);
        } else if(trainingAlgorithm == TrainingAlgorithm.STOHASTIC_BACKPROPAGATION) {
            net.trainUsingStohasticBackpropagationAlgorithm(trainingSamples, iterations, learningRate, iterationsBetweenProgressInfo,targetMse);
        } else {
            TrainingSampleGroup[] groups = splitSamplesIntoGroups(trainingSamples,3);
            net.trainUsingMiniBatchBackpropagationAlgorithm(groups,iterations,learningRate,iterationsBetweenProgressInfo,targetMse);
        }

        double[] out;
        System.out.println("\n\nRezultati:\n x -> x^2 (tocno rjesenje)");
        for(double d : new double[] {-1,-0.9,-0.8,-0.7,-0.6,-0.5,-0.4,-0.3,-0.2,-0.1,0,0.2,0.4,0.6,0.8,1}) {
            out = net.calculateOutput(new double[]{d});
            System.out.printf("%f -> %f (%f)\n",d,out[0],d*d);
        }

    }

    private static TrainingSampleGroup[] splitSamplesIntoGroups(TrainingSample[] samples, int groupSize) {
        TrainingSampleGroup[] groups = new TrainingSampleGroup[(int)Math.ceil(samples.length/(double)groupSize)];
        for(int i = 0 ; i < groups.length; i++) {
            List<TrainingSample> groupSamples = new ArrayList<>();
            for(int j=i*groupSize ; j < samples.length && j < (i+1)*groupSize; j++) {
                groupSamples.add(samples[j]);
            }
            groups[i] = new TrainingSampleGroup(groupSamples);
        }
        return groups;
    }

}
