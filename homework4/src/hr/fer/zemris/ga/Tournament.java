package hr.fer.zemris.ga;

import java.util.*;

public class Tournament {
    private Chromosome[] tournamentChromosomes;
    private Chromosome worstChromosome;
    private List<Chromosome> chromosomesForReproduction;

    public Tournament(List<Chromosome> population) {
        this.tournamentChromosomes = selectChromosomesForTournament(population);

        this.worstChromosome = Arrays.stream(tournamentChromosomes).max(Comparator.comparingDouble(Chromosome::getPenalty)).get();
        chromosomesForReproduction = new ArrayList<>();
        for(Chromosome chromosome : tournamentChromosomes) {
            if(chromosome!=worstChromosome) {
                chromosomesForReproduction.add(chromosome);
            }
        }
    }

    public Chromosome[] getTournamentChromosomes() {
        return tournamentChromosomes;
    }

    public Chromosome getWorstChromosome() {
        return worstChromosome;
    }

    public ChromosomeParents getChromosomesForReproduction() {
        return new ChromosomeParents(chromosomesForReproduction.get(0),
                chromosomesForReproduction.get(1));
    }

    private Chromosome[] selectChromosomesForTournament(List<Chromosome> population) {
        Set<Chromosome> tournamentChromosomes = new HashSet<>();
        while (tournamentChromosomes.size() < 3) {
            Chromosome selectedChromosome = population.get(RandomGenerator.generateInt(0,population.size()-1));
            tournamentChromosomes.add(selectedChromosome);
        }
        return tournamentChromosomes.toArray(new Chromosome[0]);
    }
}
