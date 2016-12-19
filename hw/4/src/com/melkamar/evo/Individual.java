package com.melkamar.evo;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by Martin Melka (martin.melka@gmail.com) on 19.12.2016 11:49 11:54.
 */
public class Individual implements Comparable {
    boolean[] solutionVect;
    Problem problem;
    public final int fitness;

    public Individual(Problem problem) {
        solutionVect = new boolean[problem.itemsCount];
        this.problem = problem;
        randomize();

        fitness = countFitness();
    }

    public Individual(Problem problem, boolean[] solutionVect) {
        this.problem = problem;
        this.solutionVect = Arrays.copyOf(solutionVect, solutionVect.length);

        fitness = countFitness();
    }

    public void randomize() {
        Random rnd = new Random();
        for (int i = 0; i < solutionVect.length; i++)
            solutionVect[i] = rnd.nextBoolean();
    }

    public Individual mutate() {
        throw new NotImplementedException();
    }

    public Individual cross(Individual otherIndividual) {
        throw new NotImplementedException();
    }

    private int countFitness() {
        int fitness = 0;
        int weight = 0;
        for (int i = 0; i < solutionVect.length; i++) {
            fitness += problem.items[i].value;
            weight += problem.items[i].weight;
        }

        // Recalculate fitness for broken individuals based on distance from the maximum weight
        if (weight > problem.maxWeight) {
            fitness = Math.max(
                    0,
                    fitness - (weight - problem.maxWeight) * 2);
        }

        return fitness;
    }

    @Override
    public int compareTo(Object o) {
        if (!(o instanceof Individual)) return 1;
        return this.fitness - ((Individual) o).fitness;
    }
}
