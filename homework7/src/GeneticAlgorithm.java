import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class GeneticAlgorithm {
    private static final double TARAGET_MSE = 1E-7;

    protected int populationSize;
    private Dataset evaluationSamples;
    protected Chromosome bestChromosome;
    private double mutationProbability;
    private NeuralNetwork network;
    private double initialParametersDeviation;
    private double incrementingMutationLowerDeviation;
    private double incrementingMutationHigherDeviation;
    private double replacingMutationDeviation;

    private double incrementingLowerDevPreference;
    private double incrementingHigherDevPreference;
    private double replacingMutationPreference;

    public GeneticAlgorithm(int populationSize, Dataset evaluationSamples, double mutationProbability,
                            NeuralNetwork neuralNetwork, double initialParametersDeviation,
                            double incrementingMutationLowerDeviation, double incrementingMutationHigherDeviation,
                            double replacingMutationDeviation, double incrementingLowerDevPreference,
                            double incrementingHigherDevPreference, double replacingMutationPreference) {
        this.populationSize = populationSize;
        this.evaluationSamples = evaluationSamples;
        this.mutationProbability = mutationProbability;
        this.network = neuralNetwork;
        this.initialParametersDeviation = initialParametersDeviation;
        this.incrementingMutationLowerDeviation = incrementingMutationLowerDeviation;
        this.incrementingMutationHigherDeviation = incrementingMutationHigherDeviation;
        this.replacingMutationDeviation = replacingMutationDeviation;
        this.incrementingLowerDevPreference = incrementingLowerDevPreference;
        this.incrementingHigherDevPreference = incrementingHigherDevPreference;
        this.replacingMutationPreference = replacingMutationPreference;
    }

    public void run(int numberOfGenerations) {
        List<Chromosome> population = generatePopulation(populationSize);
        Chromosome bestInCurrentPopulation = null;
        long startTime = System.currentTimeMillis();
        for(int iteration = 0; iteration < numberOfGenerations; iteration++) {
            List<Chromosome> nextPopulation = new ArrayList<>();


            if(bestInCurrentPopulation==null) {
                nextPopulation.add(getBestInPopulation(population));
            } else {
                nextPopulation.add(bestInCurrentPopulation);
            }

            for(int i = 1; i < populationSize; i++) {
                ChromosomeParents parentChromosomes = selectParentsFromPopulation(population);
                Chromosome childChromosome = crossoverChromosomeParents(parentChromosomes);
                mutateChromosome(childChromosome);
                childChromosome.setFitness(evaluateChromosomeFitness(childChromosome));
                nextPopulation.add(childChromosome);
            }

            population = nextPopulation;

            bestInCurrentPopulation = getBestInPopulation(population);
            if(bestChromosome==null || bestInCurrentPopulation.getFitness() > bestChromosome.getFitness()) {
                bestChromosome = bestInCurrentPopulation;
                printNewBestChromosome(bestChromosome,iteration);
                System.out.println("Proteklo vrijeme: "+(System.currentTimeMillis()-startTime)/1000f);
                if(1/bestChromosome.getFitness() < TARAGET_MSE) {
                    return;
                }
            }
        }
    }

    public Chromosome getBestChromosome() {
        return bestChromosome;
    }

    protected List<Chromosome> generatePopulation(int populationSize) {
        List<Chromosome> population = new ArrayList<>(populationSize);
        int requiredParams = network.getNumberOfRequiredParameters();
        for(int i = 0; i < populationSize; i++) {
            Chromosome chromosome = Chromosome.newRandomChromosome(requiredParams,initialParametersDeviation);
            chromosome.setFitness(evaluateChromosomeFitness(chromosome));
            population.add(chromosome);
        }
        return population;
    }

    protected void printNewBestChromosome(Chromosome bestChromosome,int iteration) {
        System.out.printf("Novo najbolje rješenje: %s\nSrednja kvadratna pogreska: %.15f\nBroj generacije: %d\n\n",
                bestChromosome.toString(),1/bestChromosome.getFitness(),iteration);
    }

    protected Chromosome getBestInPopulation(List<Chromosome> population) {
        return population.stream().max(Comparator.comparingDouble(Chromosome::getFitness)).get();
    }

    protected double evaluateChromosomeFitness(Chromosome chromosome) {
        double mse = network.calcError(evaluationSamples,chromosome.getParameters());
        return 1/mse;
    }

    private void mutateChromosomeParameterUsingIncrementingMutation(Chromosome chromosome, int parameterIndex, double deviation) {
        double[] chromosomeParams = chromosome.getParameters();
        double oldParamValue = chromosomeParams[parameterIndex];

        double newParamValueDiff = RandomGenerator.generateGaussianDouble(deviation);
        double newParamValue = oldParamValue + newParamValueDiff;

        chromosomeParams[parameterIndex] = newParamValue;
    }

    private void mutateChromosomeParameterUsingReplacingMutation(Chromosome chromosome, int parameterIndex, double deviation) {
        double[] chromosomeParams = chromosome.getParameters();
        chromosomeParams[parameterIndex] = RandomGenerator.generateGaussianDouble(deviation);
    }

    private void mutateChromosome(Chromosome chromosome) {
        int parametersNum = chromosome.getNumberOfParameters();
        MutationAlgorithm selectedMutationAlg = selectMutationAlgorithm();
        for(int i = 0; i < parametersNum; i++) {
            if(RandomGenerator.generateDouble(0,1) <= mutationProbability) {
                 if(selectedMutationAlg == MutationAlgorithm.INCREMENTING_MUTATION_LOWER_DEV) {
                     mutateChromosomeParameterUsingIncrementingMutation(chromosome,i,incrementingMutationLowerDeviation);
                 } else if(selectedMutationAlg == MutationAlgorithm.INCREMENTING_MUTATION_HIGHER_DEV) {
                     mutateChromosomeParameterUsingIncrementingMutation(chromosome,i,incrementingMutationHigherDeviation);
                 } else {
                     mutateChromosomeParameterUsingReplacingMutation(chromosome,i,replacingMutationDeviation);
                 }
            }
        }
    }

    private enum MutationAlgorithm {
        INCREMENTING_MUTATION_LOWER_DEV,INCREMENTING_MUTATION_HIGHER_DEV,REPLACING_MUTATION
    }

    private MutationAlgorithm selectMutationAlgorithm() {
        double preferencesSum = replacingMutationPreference+incrementingHigherDevPreference+incrementingLowerDevPreference;
        double randomValue = RandomGenerator.generateDouble(0,preferencesSum);

        if(randomValue < incrementingLowerDevPreference) {
            return MutationAlgorithm.INCREMENTING_MUTATION_LOWER_DEV;
        } else if(randomValue > incrementingLowerDevPreference + incrementingHigherDevPreference) {
            return MutationAlgorithm.REPLACING_MUTATION;
        } else {
            return MutationAlgorithm.INCREMENTING_MUTATION_HIGHER_DEV;
        }
    }


    private Chromosome discreteUniformRecombinationCrossover(ChromosomeParents parents) {
        int paramsNum = parents.parent1.getNumberOfParameters();
        double[] params = new double[paramsNum];

        for(int i = 0; i < paramsNum; i++) {
            int selectedParent = RandomGenerator.generateInt(1,2);
            if(selectedParent == 1) {
                params[i] = parents.parent1.getParameter(i);
            } else {
                params[i] = parents.parent2.getParameter(i);
            }

        }

        return new Chromosome(params);
    }

    private Chromosome simpleArithmeticRecombinationCrossover(ChromosomeParents parents) {
        int paramsNum = parents.parent1.getNumberOfParameters();
        int crossoverPoint = RandomGenerator.generateInt(1,paramsNum-1);

        double[] params = new double[paramsNum];
        for(int i = 0; i < crossoverPoint; i++) {
            params[i] = parents.parent1.getParameter(i);
        }
        for(int i = crossoverPoint; i < paramsNum; i++) {
            params[i] = (parents.parent1.getParameter(i)+parents.parent2.getParameter(i))/2;
        }

        return new Chromosome(params);
    }

    private Chromosome singleArithmeticRecombinationCrossover(ChromosomeParents parents) {
        int paramsNum = parents.parent1.getNumberOfParameters();

        double[] params = Arrays.copyOf(parents.parent1.getParameters(),paramsNum);
        int selectedIndex = RandomGenerator.generateInt(0,paramsNum-1);
        params[selectedIndex] = (parents.parent1.getParameter(selectedIndex)+parents.parent2.getParameter(selectedIndex))/2;

        return new Chromosome(params);
    }

    private Chromosome crossoverChromosomeParents(ChromosomeParents parents) {
        int selectedCrossoverAlgorithm = RandomGenerator.generateInt(1,3);
        if(selectedCrossoverAlgorithm == 1) {
            return discreteUniformRecombinationCrossover(parents);
        } else if(selectedCrossoverAlgorithm == 2) {
            return simpleArithmeticRecombinationCrossover(parents);
        } else {
            return singleArithmeticRecombinationCrossover(parents);
        }
    }



    private ChromosomeParents selectParentsFromPopulation(List<Chromosome> population) {
        return new ChromosomeParents(selectParentUsingRouletteWheelSelection(population),
                selectParentUsingRouletteWheelSelection(population));
    }

    private Chromosome selectParentUsingRouletteWheelSelection(List<Chromosome> population) {
        double fitnessValuesSum = population.stream().mapToDouble(Chromosome::getFitness).sum();
        double randomValue = RandomGenerator.generateDouble(0,fitnessValuesSum);
        double value = 0;
        for(Chromosome c : population) {
            value += c.getFitness();
            if(value > randomValue) {
                return c;
            }
        }
        return population.get(populationSize-1);
    }
}
