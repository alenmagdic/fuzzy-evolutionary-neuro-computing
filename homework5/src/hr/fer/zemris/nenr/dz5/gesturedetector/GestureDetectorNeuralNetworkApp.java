package hr.fer.zemris.nenr.dz5.gesturedetector;

import hr.fer.zemris.nenr.dz5.neuralnetwork.NeuralNetwork;
import hr.fer.zemris.nenr.dz5.neuralnetwork.training.TrainingSample;
import hr.fer.zemris.nenr.dz5.neuralnetwork.training.TrainingSampleGroup;
import hr.fer.zemris.nenr.dz5.util.ListToPrimitivesArray;
import hr.fer.zemris.nenr.dz5.util.Point;
import hr.fer.zemris.nenr.dz5.util.PointDouble;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

public class GestureDetectorNeuralNetworkApp {
    private static final int BACKPROPAGATION_ALGORITHM = 1;
    private static final int STOHASTIC_BACKPROPAGATION_ALGORITHM = 2;
    private static final int MINI_BATCH_BACKPROPAGATION_ALGORITHM = 3;

    private static final double LEARNING_RATE = 0.02;
    private static final int ITERATIONS_BETWEEN_PROGRESS_INFO = 5000;

    private static final double SAMPLES_FOR_EACH_CLASS_IN_GROUP = 2; //određuje broj uzoraka od svakog razreda u trening grupi

    /**
     * 1.argument: putanja datoteke s pohranjenim uzorcima
     * 2.argument: broj reprezentativnih točaka uzorka
     * 3.argument: arhitektura mreže
     * 4.argument: oznaka algoritma učenja (1 = backpropagation, 2 = stohastic backpropagation, 3 = mini-batch backpropagation)
     * 5.argument: maksimalni broj iteracija
     * 6.argument: ciljana srednja kvadratna pogreška
     */
    public static void main(String[] args) {
        if(args.length != 6) {
            System.out.println("Neočekivani broj argumenata. Očekivano 6 argumenata, a zadano: "+args.length);
        }

        Path samplesPath;
        try {
            samplesPath = Paths.get(args[0]);
        } catch (InvalidPathException ex) {
            System.out.println("Neispravna putanja datoteke: "+args[0]);
            return;
        }

        int representativeDotsNum = 0;
        try {
            representativeDotsNum = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("Neispravan broj reprezentativnih točaka: "+args[1]);
            return;
        }

        List<Integer> netLayerSizes = null;
        try {
            netLayerSizes = parseNetworkArchitecture(args[2]);
        } catch (Exception e) {
            System.out.println("Neispravna arhitektura mreže: "+args[2]);
            return;
        }

        if(netLayerSizes.get(0) != 2*representativeDotsNum) {
            System.out.println("Neispravna arhitektura. Prvi sloj mora imati točno duplo više neurona od broja reprezentativnih točaka.");
            return;
        }

        int trainingAlgorithm;
        try {
            trainingAlgorithm = Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.out.println("Neispravan algoritam učenja, očekivan broj 1-3, a zadana vrijednost: "+args[3]);
            return;
        }

        int iterationsNumber = 0;
        try {
            iterationsNumber = Integer.parseInt(args[4]);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.out.println("Neispravan broj iteracija, očekivan pozitivan broj, a zadana vrijednost: "+args[4]);
            return;
        }

        double targetMeanSquareError = 0;
        try {
            targetMeanSquareError = Double.parseDouble(args[5]);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.out.println("Neispravna ciljana srednja kvadratna pogreška, očekivan pozitivan decimalni broj, a zadana vrijednost: "+args[5]);
            return;
        }


        List<TrainingSample> samples;
        try {
            samples = loadSamplesFromFile(samplesPath);
        } catch (IOException | ParseException | NumberFormatException e) {
            e.printStackTrace();
            System.out.println("Došlo je do pogreške u učitavanju datoteke s uzorcima.");
            return;
        }
        if(samples.stream().map(s -> s.getOutputValues().length).collect(Collectors.toSet()).size() != 1) {
            System.out.println("Postoji nekonzistentnost u duljini izlaza, svi izlazi se moraju sastojati od jedankog broja vrijednosti.");
            return;
        }
        if(samples.size() == 0) {
            System.out.println("Datoteka s uzorcima je prazna.");
            return;
        }
        if(samples.get(0).getOutputValues().length != netLayerSizes.get(netLayerSizes.size()-1)) {
            System.out.println("Veličina izlaznog sloja u mreži ne odgovara duljini izlaza u uzorcima.");
            return;
        }

        NeuralNetwork net = new NeuralNetwork(netLayerSizes);
        trainNetwork(net,trainingAlgorithm,samples,iterationsNumber,targetMeanSquareError);

        Map<Integer, List<List<Point>>> classToRepresentativeSamples = createMapOfRepresentativeSamples(samples);

        openGestureDetectionWindow(net,representativeDotsNum,classToRepresentativeSamples);

    }

    private static Map<Integer,List<List<Point>>> createMapOfRepresentativeSamples(List<TrainingSample> samples) {
        Map<Integer, List<List<Point>>> classToRepresentativeSamples = new HashMap<>();
        GesturePointsConverter pointsConverter = new GesturePointsConverter();
        for(TrainingSample sample : samples) {
            int classNumber = GestureDetector.getClassNumberFromOutputArray(sample.getOutputValues());
            List<PointDouble> points = pointsConverter.getRepresentativePointsFromValues(sample.getInputValues());
            double resizeFactor = GestureDetectionWindow.MAX_OUTPUT_DISPLAY_SIZE_IN_PIXELS/2.0;
            List<Point> displayablePoints = pointsConverter
                    .convertRepresentativePointsToDisplayablePoints(points,resizeFactor);

            List<List<Point>> samplesForClass = classToRepresentativeSamples.computeIfAbsent(classNumber, k -> new ArrayList<>());
            samplesForClass.add(displayablePoints);
        }

        return classToRepresentativeSamples;
    }

    private static void openGestureDetectionWindow(NeuralNetwork net, int representativeDotsNum,Map<Integer,
            List<List<Point>>> classToRepresentativeSamplesMap ) {
        GestureDetector gestureDetector = new GestureDetector(net,representativeDotsNum,classToRepresentativeSamplesMap);
        SwingUtilities.invokeLater(
                () -> new GestureDetectionWindow(gestureDetector).setVisible(true));
    }

    private static void trainNetwork(NeuralNetwork net, int trainingAlgorithm, List<TrainingSample> trainingSamples, int iterations,
                              double targetMeanSquareError) {
        TrainingSample[] trainingSet = trainingSamples.toArray(new TrainingSample[0]);
        if(trainingAlgorithm == BACKPROPAGATION_ALGORITHM) {
            net.trainUsingBackpropagationAlgorithm(trainingSet,iterations,LEARNING_RATE,ITERATIONS_BETWEEN_PROGRESS_INFO,targetMeanSquareError);
        } else if(trainingAlgorithm == STOHASTIC_BACKPROPAGATION_ALGORITHM) {
            net.trainUsingStohasticBackpropagationAlgorithm(trainingSet,iterations,LEARNING_RATE,ITERATIONS_BETWEEN_PROGRESS_INFO,targetMeanSquareError);
        } else if(trainingAlgorithm == MINI_BATCH_BACKPROPAGATION_ALGORITHM) {
            TrainingSampleGroup[] groups = splitIntoGroups(trainingSet);
            net.trainUsingMiniBatchBackpropagationAlgorithm(groups,iterations,LEARNING_RATE,ITERATIONS_BETWEEN_PROGRESS_INFO,targetMeanSquareError);
        } else {
            throw new IllegalArgumentException("Neočekivani izbor algoritma učenja: "+trainingAlgorithm);
        }
    }

    private static TrainingSampleGroup[] splitIntoGroups(TrainingSample[] trainingSamples) {
        List<TrainingSampleGroup> groups = new ArrayList<>();
        List<TrainingSample> samples = new ArrayList<>(Arrays.asList(trainingSamples));
        int maxClassNumber = samples.get(0).getOutputValues().length;
        while(samples.size()>0) {
            List<TrainingSample> groupSamples = new ArrayList<>();
            int[] classSamplesInGroup = new int[maxClassNumber]; //indeks je broj razreda, vrijednost broj uzoraka tog razreda u grupi
            for(int i = 0; i < samples.size(); i++) {
                int classNumber = GestureDetector.getClassNumberFromOutputArray(samples.get(i).getOutputValues());
                if(classSamplesInGroup[classNumber-1] < SAMPLES_FOR_EACH_CLASS_IN_GROUP) {
                    groupSamples.add(samples.get(i));
                    samples.remove(i);
                    i--;
                    classSamplesInGroup[classNumber-1]++;
                }
            }

            TrainingSampleGroup group = new TrainingSampleGroup(groupSamples);
            groups.add(group);
        }

        return groups.toArray(new TrainingSampleGroup[0]);

    }

    private static List<TrainingSample> loadSamplesFromFile(Path file) throws IOException, ParseException {
        BufferedReader reader = Files.newBufferedReader(file);
        List<TrainingSample> samples = new ArrayList<>();
        while(true) {
            String line = reader.readLine();
            if(line == null) {
                reader.close();
                break;
            }

            samples.add(parseTrainingSample(line));
        }

        return samples;
    }

    private static TrainingSample parseTrainingSample(String line) throws ParseException {
        line = line.trim();
        List<String> lineParts = Arrays.asList(line.split(" "));
        List<Double> sampleInputValues = lineParts.subList(0,lineParts.size()-1)
                .stream().map(Double::parseDouble).collect(Collectors.toList());

        List<Double> sampleOutputValues = parseSampleOutput(lineParts.get(lineParts.size()-1));

        return new TrainingSample(ListToPrimitivesArray.convertToArrayOfDoubles(sampleInputValues),
                ListToPrimitivesArray.convertToArrayOfDoubles(sampleOutputValues));
    }

    private static List<Double> parseSampleOutput(String sampleOutput) throws ParseException {
        List<Double> outputValues = new ArrayList<>();
        for(char c : sampleOutput.trim().toCharArray()) {
            if(c=='1') {
                outputValues.add(1.0);
            } else if(c=='0') {
                outputValues.add(0.0);
            } else {
                throw new ParseException("Neispravan format izlaza uzorka: "+sampleOutput,0);
            }
        }
        return outputValues;
    }

    private static List<Integer> parseNetworkArchitecture(String networkArchString) {
        String[] parts = networkArchString.split("\\.");
        List<Integer> layersSizes = new ArrayList<>();
        for(String part: parts) {
            layersSizes.add(Integer.parseInt(part));
        }
        return layersSizes;
    }
}
