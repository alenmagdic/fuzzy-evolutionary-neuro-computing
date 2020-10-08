package hr.fer.zemris.ga;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public abstract class GeneticAlgorithm {
    private double maxMutationDifference;
    protected int populationSize;
    private List<DatasetSample> evaluationSamples;
    private ObjectiveFunction objectiveFunction;
    private Function<Chromosome,Double> penaltyFunction;
    protected Chromosome bestChromosome;
    private double mutationProbability;

    public GeneticAlgorithm(double maxMutationDifference, int populationSize, List<DatasetSample> evaluationSamples, double mutationProbability) {
        this.maxMutationDifference = maxMutationDifference;
        this.populationSize = populationSize;
        this.evaluationSamples = evaluationSamples;
        this.mutationProbability = mutationProbability;
        this.objectiveFunction = new ObjectiveFunction();
    }

    public abstract void run(int numberOfGenerations);

    public Chromosome getBestChromosome() {
        return bestChromosome;
    }

    protected List<Chromosome> generatePopulation(int populationSize) {
        List<Chromosome> population = new ArrayList<>(populationSize);
        for(int i = 0; i < populationSize; i++) {
            Chromosome chromosome = Chromosome.newRandomChromosome();
            chromosome.setPenalty(evaluateChromosomePenalty(chromosome));
            population.add(chromosome);
        }
        return population;
    }

    protected void printNewBestChromosome(Chromosome bestChromosome,int iteration) {
        System.out.printf("Novo najbolje rješenje: %s\nSrednja kvadratna pogreska: %.15f\nBroj generacije: %d\n\n",bestChromosome.toString(),bestChromosome.getPenalty(),iteration);
    }

    protected Chromosome getBestInPopulation(List<Chromosome> population) {
        return population.stream().min(Comparator.comparingDouble(Chromosome::getPenalty)).get();
    }

    protected double evaluateChromosomePenalty(Chromosome chromosome) {
        double squareErrorSum = 0;
        for(DatasetSample sample : evaluationSamples) {
            double sampleResult = sample.getFunctionValue();
            double chromosomeResult = objectiveFunction.calculateValueFor(sample.getX(),sample.getY(), chromosome.getParameters());
            squareErrorSum += Math.pow(chromosomeResult-sampleResult,2);
        }

        return squareErrorSum/evaluationSamples.size();
    }

    private void mutateChromosomeParameter(Chromosome chromosome, int parameterIndex) {
        double[] chromosomeParams = chromosome.getParameters();
        double oldParamValue = chromosomeParams[parameterIndex];

        double newParamValueDiff = RandomGenerator.generateDouble(-maxMutationDifference,maxMutationDifference);
        double newParamValue = oldParamValue + newParamValueDiff;
        if(newParamValue < Chromosome.MIN_PARAM_VALUE) {
            newParamValue = Chromosome.MIN_PARAM_VALUE;
        }
        if(newParamValue > Chromosome.MAX_PARAM_VALUE) {
            newParamValue = Chromosome.MAX_PARAM_VALUE;
        }

        chromosomeParams[parameterIndex] = newParamValue;
    }

    protected void mutateChromosome(Chromosome chromosome) {
        for(int i = 0; i < Chromosome.PARAMETERS_NUMBER; i++) {
            if(RandomGenerator.generateDouble(0,1) <= mutationProbability) {
                mutateChromosomeParameter(chromosome,i);
            }
        }
    }

    protected Chromosome crossoverChromosomeParents(ChromosomeParents parents) {
        int crossoverPoint = RandomGenerator.generateInt(1,Chromosome.PARAMETERS_NUMBER-1);

        double[] params = new double[Chromosome.PARAMETERS_NUMBER];
        for(int i = 0; i < crossoverPoint; i++) {
            params[i] = parents.parent1.getParameter(i);
        }
        for(int i = crossoverPoint; i < Chromosome.PARAMETERS_NUMBER; i++) {
            params[i] = parents.parent2.getParameter(i);
        }

        return new Chromosome(params);
    }
}
