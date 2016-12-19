package com.melkamar.evo;

import java.util.*;

/**
 * Created by Martin Melka (martin.melka@gmail.com) on 19.12.2016 12:20.
 */
public class Evolution {
    private Problem problem;
    private Individual[] population;
    private Random rnd = new Random();

    private double PROBABILITY_CROSSOVER = 0.05;
    private double PROBABILITY_MUTATION = 0.05;
    private int TOURNAMENT_SIZE = 10;

    private final int generations = 1000;

    public Evolution(Problem problem) {
        this.problem = problem;
    }

    public int solve() {
        initPopulation();

        for (int generation = 1; generation < generations + 1; generation++) {
            List<Individual> currentIndividuals = new ArrayList<>(Arrays.asList(population));
            doCrossovers(currentIndividuals);
            doMutation(currentIndividuals);

            doSelection(currentIndividuals);

            System.out.println("\n================================================");
            System.out.println("Generation "+generation+" completed. Stats:");
            System.out.println("  best individual: "+getBest());
            System.out.println("================================================\n");
        }

        return 0;
    }

    private Individual getBest(){
        Individual best = null;
        for (int indIdx= 0; indIdx < population.length; indIdx++) {
            if (best == null || population[indIdx].compareTo(best) > 0) {
                best = population[indIdx];
            }
        }

        return best;
    }

    private void initPopulation() {
        for (int i = 0; i < problem.itemsCount; i++) {
            population[i] = new Individual(problem);
            population[i].randomize();
        }
    }

    /**
     * Take a List of individuals and select only some of them so that the population is filled.
     * There has to be equal or greater number of individuals in the currentIndividuals List than the population.
     *
     * @param currentIndividuals
     */
    private void doSelection(List<Individual> currentIndividuals) {
        List<Integer> randomNums = new ArrayList<>(currentIndividuals.size());
        for (int i = 0; i < currentIndividuals.size(); i++) randomNums.add(i);

        for (int individualIdx = 0; individualIdx < population.length; individualIdx++) {
            // Use randomNums as list of random indices, from those take the best individual
            Collections.shuffle(randomNums);

            Individual best = null;
            for (int tournamentIdx = 0; tournamentIdx < TOURNAMENT_SIZE; tournamentIdx++) {
                int indIdx = randomNums.get(tournamentIdx);
                if (best == null || currentIndividuals.get(indIdx).compareTo(best) > 0) {
                    best = currentIndividuals.get(indIdx);
                }

                population[individualIdx] = best;
            }
        }
    }

    private void doCrossovers(List<Individual> currentIndividuals) {
        for (int i = 0; i < population.length; i++) {
            if (rnd.nextDouble() > PROBABILITY_CROSSOVER) return;

            int anotherIndex = rnd.nextInt(population.length);
            if (anotherIndex == i) {
                // dont want to crossover with the same individual - get next one
                anotherIndex += 1;
                anotherIndex %= population.length;
            }

            Individual another = population[anotherIndex];
            currentIndividuals.add(population[i].cross(another));
        }
    }

    private void doMutation(List<Individual> currentIndividuals) {
        for (int i = 0; i < population.length; i++) {
            if (rnd.nextDouble() > PROBABILITY_MUTATION) return;

            currentIndividuals.add(population[i].mutate());
        }
    }


}
