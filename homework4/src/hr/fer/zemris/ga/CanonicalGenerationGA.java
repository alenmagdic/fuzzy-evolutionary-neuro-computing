package hr.fer.zemris.ga;

import java.util.ArrayList;
import java.util.List;

public class CanonicalGenerationGA extends GeneticAlgorithm {
    private boolean useElitism;

    public CanonicalGenerationGA(int populationSize, List<DatasetSample> evaluationSamples, double maxMutationDifference,double mutationProbability, boolean useElitism) {
        super(maxMutationDifference,populationSize,evaluationSamples,mutationProbability);
        this.useElitism = useElitism;
    }

    @Override
    public void run(int numberOfGenerations) {
        List<Chromosome> population = generatePopulation(populationSize);
        Chromosome bestInCurrentPopulation = null;
        for(int iteration = 0; iteration < numberOfGenerations; iteration++) {
            List<Chromosome> nextPopulation = new ArrayList<>();

            if(useElitism) {
                if(bestInCurrentPopulation==null) {
                    nextPopulation.add(getBestInPopulation(population));
                } else {
                    nextPopulation.add(bestInCurrentPopulation);
                }
            }

            for(int i = useElitism?1:0; i < populationSize; i++) { //ako se koristi elitizam, preostaje jedan kromosom manje za dodat u sljedecu populaciju
                ChromosomeParents parentChromosomes = selectParentsFromPopulation(population);
                Chromosome childChromosome = crossoverChromosomeParents(parentChromosomes);
                mutateChromosome(childChromosome);
                childChromosome.setPenalty(evaluateChromosomePenalty(childChromosome));
                nextPopulation.add(childChromosome);
            }

            population = nextPopulation;

            bestInCurrentPopulation = getBestInPopulation(population);
            if(bestChromosome==null || bestInCurrentPopulation.getPenalty() < bestChromosome.getPenalty()) {
                bestChromosome = bestInCurrentPopulation;
                printNewBestChromosome(bestChromosome,iteration);
            }
        }
    }

    private ChromosomeParents selectParentsFromPopulation(List<Chromosome> population) {
        return new ChromosomeParents(selectParentUsingRouletteWheelSelection(population),
                selectParentUsingRouletteWheelSelection(population));
    }

    private Chromosome selectParentUsingRouletteWheelSelection(List<Chromosome> population) {
        double invertedPenaltyValuesSum = population.stream().mapToDouble(c -> 1/c.getPenalty()).sum();
        double randomValue = RandomGenerator.generateDouble(0,invertedPenaltyValuesSum);
        double value = 0;
        for(Chromosome c : population) {
            value += 1/c.getPenalty();
            if(value > randomValue) {
                return c;
            }
        }
        return population.get(populationSize-1);
    }


}
