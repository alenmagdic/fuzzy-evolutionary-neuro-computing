package hr.fer.zemris.ga;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {
        List<DatasetSample> datasetSamples = loadDataset("./dataset1.txt");
        int populationSize = 10;
        boolean useElitism = true;
        int numberOfGenerations = 5000;
        double maxMutationDiff = 2;
        double mutationProbability = 0.2;

        GeneticAlgorithm generationGa = new CanonicalGenerationGA(populationSize,datasetSamples,maxMutationDiff,mutationProbability,useElitism);
        GeneticAlgorithm eliminationGa = new CanonicalEliminationGA(populationSize,datasetSamples,maxMutationDiff,mutationProbability);

        GeneticAlgorithm ga = eliminationGa;
        ga.run(numberOfGenerations);
    }

    private static List<DatasetSample> loadDataset(String path) throws IOException {
        BufferedReader reader = Files.newBufferedReader(Paths.get(path));
        List<DatasetSample> datasetSamples = new ArrayList<>();

        while(true) {
            String line = reader.readLine();
            if(line == null) {
                break;
            }

            List<Double> values = Arrays.stream(line.split("\t")).map(Double::parseDouble).collect(Collectors.toList());
            datasetSamples.add(new DatasetSample(values.get(0),values.get(1),values.get(2)));
        }
        reader.close();

        return datasetSamples;
    }
}
