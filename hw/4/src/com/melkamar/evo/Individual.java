package com.melkamar.evo;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by Martin Melka (martin.melka@gmail.com) on 19.12.2016 11:49 11:54.
 */
public class Individual implements Comparable {
    boolean[] solutionVect;
    Problem problem;
    public int fitness;
    public int weight;
    public int value;

    //    public final double BIT_FLIP_PROBABILITY = 0.5;
    private Random rnd = new Random();

    public static Individual createRandom(Problem problem) {
        Individual individual = new Individual(problem);
        individual.randomize();
        individual.recalculateStats();

        return individual;
    }

    public static Individual createEmpty(Problem problem) {
        Individual individual = new Individual(problem);
//        individual.randomize();
        individual.recalculateStats();

        return individual;
    }

    private void recalculateStats() {
        int[] stats = countStats();
        value = stats[0];
        weight = stats[1];
        fitness = stats[2];
    }

    private Individual(Problem problem) {
        solutionVect = new boolean[problem.itemsCount];
        this.problem = problem;
    }

    public Individual(Individual individual) {
        solutionVect = Arrays.copyOf(individual.solutionVect, individual.solutionVect.length);
        this.problem = individual.problem;
        int[] stats = countStats();
        value = stats[0];
        weight = stats[1];
        fitness = stats[2];
    }

//    public Individual(Problem problem, boolean[] solutionVect) {
//        this.problem = problem;
//        this.solutionVect = Arrays.copyOf(solutionVect, solutionVect.length);
//
//        fitness = countStats();
//    }

    public void randomize() {
        Random rnd = new Random();
        for (int i = 0; i < solutionVect.length; i++)
            solutionVect[i] = rnd.nextBoolean();
    }

    public Individual mutate() {
        Individual newIndividual = new Individual(this);
//        for (int i = 0; i < solutionVect.length; i++) {
//            if (rnd.nextDouble() > BIT_FLIP_PROBABILITY) continue;
//
//            individual.solutionVect[i] = !individual.solutionVect[i];
//        }
        int idx = rnd.nextInt(newIndividual.solutionVect.length);
        newIndividual.solutionVect[idx] = !newIndividual.solutionVect[idx];

        return newIndividual;
    }

    public Individual cross(Individual otherIndividual) {
        Individual individual = new Individual(this);

        // uniform crossover
        for (int i = 0; i < solutionVect.length; i++) {
            if (rnd.nextBoolean()) {
                individual.solutionVect[i] = this.solutionVect[i];
            } else {
                individual.solutionVect[i] = otherIndividual.solutionVect[i];
            }
        }

        return individual;
    }

    private int[] countStats() {
        int fitness;
        int weight = 0;
        int value = 0;
        for (int i = 0; i < solutionVect.length; i++) {
            if (solutionVect[i]) {
                value += problem.items[i].value;
                weight += problem.items[i].weight;
            }
        }


        // Recalculate fitness for broken individuals based on distance from the maximum weight
        if (weight > problem.maxWeight) {
            fitness = Math.max(
                    0,
                    value / 4 - (weight - problem.maxWeight) * 2);
//            fitness = 0;
        } else {
            fitness = value;
        }

        return new int[]{
                value, weight, fitness
        };
    }

    @Override
    public String toString() {
        return "{" +
                "fitness=" + fitness +
                ", weight=" + weight +
                ", value=" + value +
//                ", solutionVect=" + Arrays.toString(solutionVect) +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        if (!(o instanceof Individual)) return 1;
        return this.fitness - ((Individual) o).fitness;
    }
}
