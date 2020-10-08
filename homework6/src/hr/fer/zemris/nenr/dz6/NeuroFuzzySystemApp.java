package hr.fer.zemris.nenr.dz6;

import java.io.IOException;

public class NeuroFuzzySystemApp {

    /**
     * 1.parametar - broj pravila
     * 2.parametar - broj iteracija
     *
     */
    public static void main(String[] args) {
        int rulesNumber = Integer.parseInt(args[0]);
        int iterations = Integer.parseInt(args[1]);

        TrainingSample[] trainingSamples = prepareTrainingSamples();
        AnfisNetwork net = new AnfisNetwork(trainingSamples, rulesNumber);
        net.train(iterations, 0.0002, AnfisNetwork.TrainingAlgorithm.BATCH_LEARNING);

        try {
            DataPrinter.printMembershipFunctions(net);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            DataPrinter.printNetworkOutputForSampleInputs(net);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            DataPrinter.printMeanSquareErrorsForSampleInputs(net);
        } catch (IOException e) {
            e.printStackTrace();
        }
        DataPrinter.close();
    }

    private static TrainingSample[] prepareTrainingSamples() {
        TrainingSample[] samples = new TrainingSample[81];
        int i = 0;
        for(int x = -4; x <= 4; x++) {
            for(int y = -4; y <=4 ; y++) {
                double result = calculateTargetFunctionValue(x,y);
                samples[i] = new TrainingSample(x,y,result);
                i++;
            }
        }
        return samples;
    }

    public static double calculateTargetFunctionValue(double x, double y) {
        return (Math.pow(x-1,2) + Math.pow(y+2,2)-5*x*y+3) * Math.pow(Math.cos(x/5),2);
    }
}
