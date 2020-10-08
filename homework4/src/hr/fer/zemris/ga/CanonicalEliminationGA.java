package hr.fer.zemris.ga;

import java.util.*;

public class CanonicalEliminationGA extends GeneticAlgorithm {

    public CanonicalEliminationGA(int populationSize, List<DatasetSample> evaluationSamples, double maxMutationDifference,double mutationProbability) {
        super(maxMutationDifference,populationSize,evaluationSamples,mutationProbability);
    }

    @Override
    public void run(int numberOfGenerations) {
        List<Chromosome> population = generatePopulation(populationSize);

        for(int iteration = 0; iteration < numberOfGenerations; iteration++) {

            for(int i = 0; i < populationSize; i++) {
                Tournament tournament = new Tournament(population);
                ChromosomeParents parentChromosomes = tournament.getChromosomesForReproduction();
                Chromosome worstChromosome = tournament.getWorstChromosome();

                Chromosome childChromosome = crossoverChromosomeParents(parentChromosomes);
                mutateChromosome(childChromosome);
                childChromosome.setPenalty(evaluateChromosomePenalty(childChromosome));
                population.set(population.indexOf(worstChromosome),childChromosome);
            }

            Chromosome bestInCurrentPopulation = getBestInPopulation(population);
            if(bestChromosome==null || bestInCurrentPopulation.getPenalty() < bestChromosome.getPenalty()) {
                bestChromosome = bestInCurrentPopulation;
                printNewBestChromosome(bestChromosome,iteration);
            }
        }
    }

}
