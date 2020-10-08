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
        String datasetPath = "./dataset.txt";
        if(args.length>=1) {
            datasetPath = args[0];
        }
        String architecture = "2.8.3";
        if(args.length>=2) {
            architecture = args[1];
        }

        Dataset dataset = new Dataset(loadDataset(datasetPath));
        int populationSize = 10;
        int numberOfGenerations = 300000*populationSize;
        double mutationProbability = 0.01;
        double initialParametersDeviation = 10;
        double incrementingMutationLowerDeviation = 0.5;
        double incrementingMutationHigherDeviation = 1;
        double replacingMutationDeviation = 1;
        double incrementingLowerDevPreference = 1;
        double incrementingHigherDevPreference = 1;
        double replacingMutationPreference = 1;

        if(incrementingLowerDevPreference + incrementingHigherDevPreference + replacingMutationPreference == 0) {
            System.out.println("Nije dozvoljeno da sve preferencije za mutaciju budu jednake 0.");
            return;
        }

        NeuralNetwork net = new NeuralNetwork(parseArchitecture(architecture));

        GeneticAlgorithm generationGa = new GeneticAlgorithm(populationSize,dataset,mutationProbability,net,
                initialParametersDeviation,incrementingMutationLowerDeviation, incrementingMutationHigherDeviation,
                replacingMutationDeviation, incrementingLowerDevPreference, incrementingHigherDevPreference,
                replacingMutationPreference
        );
        generationGa.run(numberOfGenerations);

        int correct = 0;
        double[] networkParams = generationGa.getBestChromosome().getParameters();

        for(DatasetSample sample : dataset) {
            double[] sampleOutput = sample.getOutputValues();
            double[] calculatedOutput = net.calcOutput(networkParams,sample.getInputValues());

            roundValues(calculatedOutput);
            System.out.printf("%.0f %.0f %.0f | %.0f %.0f %.0f",sampleOutput[0],sampleOutput[1],sampleOutput[2],
                    calculatedOutput[0],calculatedOutput[1],calculatedOutput[2]);

            if(Arrays.equals(sampleOutput,calculatedOutput)) {
                correct++;
                System.out.println(" | TOČNO");
            } else {
                System.out.println(" | NETOČNO");
            }

        }
        System.out.printf("Ispravno: %d/%d\n",correct, dataset.getNumberOfSamples());
        System.out.printf("Neispravno: %d/%d\n",(dataset.getNumberOfSamples()-correct), dataset.getNumberOfSamples());

    }

    private static int[] parseArchitecture(String architecture) {
        return Arrays.stream(architecture.split("\\.")).mapToInt(Integer::parseInt).toArray();
    }

    private static void roundValues(double[] values) {
        for(int i = 0; i < values.length; i++) {
            values[i] = values[i]<0.5?0:1;
        }
    }

    public static List<DatasetSample> loadDataset(String path) throws IOException {
        BufferedReader reader = Files.newBufferedReader(Paths.get(path));
        List<DatasetSample> datasetSamples = new ArrayList<>();

        while(true) {
            String line = reader.readLine();
            if(line == null) {
                break;
            }

            List<Double> values = Arrays.stream(line.split("\t")).map(Double::parseDouble).collect(Collectors.toList());
            datasetSamples.add(new DatasetSample(
                    new double[] {values.get(0),values.get(1)},
                    new double[] {values.get(2),values.get(3),values.get(4)}
                    )
            );
        }
        reader.close();

        return datasetSamples;
    }
}
