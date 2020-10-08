import java.util.Arrays;

public class NeuralNetwork {
    private int[] layersSizes;
    private int neuronsNumber;

    public NeuralNetwork(int[] layersSizes) {
        this.layersSizes = layersSizes;
        this.neuronsNumber = calculateNumberOfNeurons(layersSizes);
    }

    private int calculateNumberOfNeurons(int[] layersSizes) {
        return Arrays.stream(layersSizes).sum();
    }

    public int getNumberOfRequiredParameters() {
        int params = layersSizes[1]*4;
        for(int i = 2; i < layersSizes.length; i++) {
            params += layersSizes[i]*(layersSizes[i-1]+1);
        }
        return params;
    }

    public double[] calcOutput(double[] networkParams,double[] inputValues) {
        if(inputValues.length != layersSizes[0]) {
            throw new RuntimeException("Broj ulaznih vrijednosti ne odgovara veličini ulaznog sloja.");
        }

        double[] neuronOutputs = new double[neuronsNumber - inputValues.length];
        int paramIndex = 0;
        for(int i = 0; i < layersSizes[1]; i++) {
            double sum = 0;
            for (double inputValue : inputValues) {
                double wParam = networkParams[paramIndex];
                double sParam = networkParams[paramIndex + 1];
                sum += Math.abs(inputValue - wParam) / Math.abs(sParam);
                paramIndex+=2;
            }
            neuronOutputs[i] = 1/(1+sum);
        }

        int previousLayerFirstOutputIndex;
        int currentLayerFirstOutputIndex = 0;
        for(int layerIndex = 2; layerIndex < layersSizes.length; layerIndex++) {
            previousLayerFirstOutputIndex = currentLayerFirstOutputIndex;
            currentLayerFirstOutputIndex += layersSizes[layerIndex-1];

            for(int i = 0; i < layersSizes[layerIndex]; i++) {
                double sum = 0;
                for(int j = 0; j < layersSizes[layerIndex-1]; j++) {
                    sum += networkParams[paramIndex] * neuronOutputs[previousLayerFirstOutputIndex+j];
                    paramIndex++;
                }
                sum += networkParams[paramIndex];
                paramIndex++;

                neuronOutputs[currentLayerFirstOutputIndex+i] = calculateSigmoid(sum);
            }

        }

        int outputLayerSize = layersSizes[layersSizes.length-1];
        double[] outputValues = new double[outputLayerSize];
        for(int i = 0; i < outputLayerSize; i++) {
            outputValues[i] = neuronOutputs[neuronOutputs.length-outputLayerSize+i];
        }
        return outputValues;
    }

    public double calcError(Dataset dataset, double[] networkParams) {
        double sum = 0;
        for(DatasetSample sample : dataset) {
            double[] sampleOutput = sample.getOutputValues();
            double[] calculatedOutput = calcOutput(networkParams,sample.getInputValues());

            for(int o = 0; o < sampleOutput.length; o++) {
                sum+=Math.pow(sampleOutput[o]-calculatedOutput[o],2);
            }
        }
        return sum/dataset.getNumberOfSamples();
    }

    public double calculateSigmoid(double x) {
        return 1/(1+Math.exp(-x));
    }
}
