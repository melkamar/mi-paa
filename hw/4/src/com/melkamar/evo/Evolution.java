package com.melkamar.evo;

/**
 * Created by Martin Melka (martin.melka@gmail.com) on 19.12.2016 12:20.
 */
public class Evolution {
    private Problem problem;
    private Individual[] population;

    private final int generations = 1000;

    public Evolution(Problem problem) {
        this.problem = problem;
    }

    public int solve() {
        initPopulation();

        for (int generation = 1; generation < generations + 1; generation++) {
            // Select some individuals


            // Cross some over
            // Mutate
        }
    }

    private void initPopulation() {
        for (int i = 0; i < problem.itemsCount; i++) {
            population[i] = new Individual(problem);
            population[i].randomize();
        }
    }
}
