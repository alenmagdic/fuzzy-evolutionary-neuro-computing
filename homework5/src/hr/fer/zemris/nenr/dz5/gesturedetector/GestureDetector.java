package hr.fer.zemris.nenr.dz5.gesturedetector;

import hr.fer.zemris.nenr.dz5.neuralnetwork.NeuralNetwork;
import hr.fer.zemris.nenr.dz5.util.ListToPrimitivesArray;
import hr.fer.zemris.nenr.dz5.util.Point;
import hr.fer.zemris.nenr.dz5.util.PointDouble;

import java.util.List;
import java.util.Map;

public class GestureDetector {
    private NeuralNetwork neuralNetwork;
    private int representativePointsNum;
    private Map<Integer, List<List<Point>>> classToRepresentativeSamplesMap;
    private GesturePointsConverter pointsConverter;

    public GestureDetector(NeuralNetwork network, int representativePointsNum, Map<Integer, List<List<Point>>> classToRepresentativeSampleMap) {
        this.pointsConverter = new GesturePointsConverter();
        this.neuralNetwork = network;
        this.representativePointsNum = representativePointsNum;
        this.classToRepresentativeSamplesMap = classToRepresentativeSampleMap;
    }

    public List<List<Point>> getDisplayableOutputsForInput(List<Point> inputPoints) {
        List<PointDouble> representativePoints = pointsConverter.convertSamplePointsToRepresentativePoints(inputPoints,representativePointsNum);
        List<Double> representativePointsValues = pointsConverter.getRepresentativePointsValues(representativePoints);
        double[] networkInput = ListToPrimitivesArray.convertToArrayOfDoubles(representativePointsValues);
        double[] networkOutput = neuralNetwork.calculateOutput(networkInput);

        System.out.println("\n\nIzlaz mreže:");
        for(int i = 0; i < networkOutput.length; i++) {
            System.out.printf("Razred %d : %f\n",i+1,networkOutput[i]);
        }

        int classNumber = getClassNumberFromOutputArray(networkOutput);
        return classToRepresentativeSamplesMap.get(classNumber);
    }

    public static int getClassNumberFromOutputArray(double[] outputArray) {
        int maxIndex = 0;
        for(int i = 1; i < outputArray.length; i++) {
            if(outputArray[i] > outputArray[maxIndex]) {
                maxIndex = i;
            }
        }
        return maxIndex+1;
    }
}
