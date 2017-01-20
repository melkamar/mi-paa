package com.melkamar.sat.evo;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by Martin Melka (martin.melka@gmail.com) on 19.12.2016 11:49 11:54.
 */
@SuppressWarnings("Duplicates")
public class Individual implements Comparable {
    private boolean[] solutionVect;
    Problem problem;
    public boolean correct;
    private int fitness;

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
        individual.recalculateStats();

        return individual;
    }

    public static Individual createFull(Problem problem) {
        Individual individual = new Individual(problem);
        for (int i = 0; i < problem.varCount; i++)
            individual.solutionVect[i] = true;

        individual.recalculateStats();

        return individual;
    }

    private void recalculateStats() {
//        fitness = countStats();
        countStats();
    }

    private Individual(Problem problem) {
        solutionVect = new boolean[problem.varCount];
        this.problem = problem;
    }

    public Individual(Individual individual) {
        solutionVect = Arrays.copyOf(individual.solutionVect, individual.solutionVect.length);
        this.problem = individual.problem;
        recalculateStats();
    }

    public void randomize() {
        for (int i = 0; i < solutionVect.length; i++)
            solutionVect[i] = rnd.nextBoolean();
    }

    /**
     * Mutate some random bits.
     *
     * @return
     */
    public Individual mutate() {
        Individual newIndividual = new Individual(this);
        for (int i = 0; i < rnd.nextInt(solutionVect.length / 2); i++) {
            int idx = rnd.nextInt(newIndividual.solutionVect.length);
            newIndividual.solutionVect[idx] = !newIndividual.solutionVect[idx];
        }

        return newIndividual;
    }

    /**
     * For each bit get probability and invert if proc.
     *
     * @param probability
     * @return
     */
    public Individual mutateAlternative(double probability) {
        Individual newIndividual = new Individual(this);
        for (int i = 0; i < newIndividual.solutionVect.length; i++) {
            if (rnd.nextDouble() > probability) continue;
            newIndividual.solutionVect[i] = !newIndividual.solutionVect[i];
        }

        newIndividual.recalculateStats();
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
        individual.recalculateStats();

        return individual;
    }

    protected int countStats() {
        correct = true;
        int correctClauses = 0;
        for (int i = 0; i < problem.clauses.length; i++) {
            Problem.Clause clause = problem.clauses[i];

            // Check if all variables are fit
            if (clause.isCorrect(this.solutionVect)) {
                correctClauses++;
            } else {
                correct = false;
            }
        }

        if (correct) {
            fitness = 0;
            for (int i = 0; i < solutionVect.length; i++) {
                // if variable [i] is 1, add its weight to fitness
                fitness += (solutionVect[i] ? 1 : 0) * problem.weights[i];
            }
            fitness *= Evolution.FITNESS_CORRECT_MULTIPLIER;
        } else {
//            // If not correct, just make fitness be portion of the original
//            fitness = 0;
//            for (int i = 0; i < solutionVect.length; i++) {
//                // if variable [i] is 1, add its weight to fitness
//                fitness += (solutionVect[i] ? 1 : 0) * problem.weights[i];
//            }
//
//            fitness /= 4;
            fitness = correctClauses;
        }
        return fitness;
    }

    public int getFitnessOrig(){
        correct = true;
        int correctClauses = 0;
        for (int i = 0; i < problem.clauses.length; i++) {
            Problem.Clause clause = problem.clauses[i];

            // Check if all variables are fit
            if (clause.isCorrect(this.solutionVect)) {
                correctClauses++;
            } else {
                correct = false;
            }
        }

        if (correct) {
            fitness = 0;
            for (int i = 0; i < solutionVect.length; i++) {
                // if variable [i] is 1, add its weight to fitness
                fitness += (solutionVect[i] ? 1 : 0) * problem.weights[i];
            }
            fitness *= 10;
        } else {
//            // If not correct, just make fitness be portion of the original
//            fitness = 0;
//            for (int i = 0; i < solutionVect.length; i++) {
//                // if variable [i] is 1, add its weight to fitness
//                fitness += (solutionVect[i] ? 1 : 0) * problem.weights[i];
//            }
//
//            fitness /= 4;
            fitness = correctClauses;
        }
        return fitness;
    }

    @Override
    public String toString() {
        return "{" +
                fitness + " " +
                (correct ? " OK " : "FAIL") +
                " (" + vectToBinary() + ") " +
                '}';
    }

    private String vectToBinary() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < solutionVect.length; i++)
            builder.append((solutionVect[i] ? "1 " : "0 "));
        return builder.toString();
    }

    @Override
    public int compareTo(Object o) {
        if (!(o instanceof Individual)) return 1;
        return this.fitness - ((Individual) o).fitness;
    }

    public int getFitness() {
        return fitness;
    }

    public boolean[] getSolutionVect() {
        return solutionVect;
    }

    public void setSolutionVect(boolean[] solutionVect) {
        this.solutionVect = solutionVect;
        recalculateStats();
    }
}
