package com.melkamar.evo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by Martin Melka (martin.melka@gmail.com) on 19.12.2016 12:20.
 */
public class Evolution {
    private Problem problem;
    private Individual[] population;
    private Random rnd = new Random();

    private double PROBABILITY_CROSSOVER = 0.25;
    private double PROBABILITY_MUTATION = 0.01;
    private int TOURNAMENT_SIZE = 3;
    private int POPULATION_SIZE = 200;
    private int generations = 2500;

    public Evolution(Problem problem) {
        this.problem = problem;
        population = new Individual[POPULATION_SIZE];
    }

    public int solve() {
        String flag = "";
        String descrstr = flag + "-cross" + PROBABILITY_CROSSOVER + "-mut" + PROBABILITY_MUTATION + "-tour" + TOURNAMENT_SIZE + "-pop" + POPULATION_SIZE + "-gen" + generations + ".dat";

        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(
                    "D:\\cvut-checkouted\\mi-paa\\hw\\4\\resources\\graphs\\" + descrstr));

            initPopulation();

            Individual startBest = getBest();
            for (int generation = 1; generation < generations + 1; generation++) {
                List<Individual> currentIndividuals = new ArrayList<>(Arrays.asList(population));
                doCrossovers(currentIndividuals);
                doMutation(currentIndividuals);

                doSelection(currentIndividuals);

//                System.out.println(stats(generation));
                out.write(gnuplotLine(generation));
                out.newLine();

            }
            Individual endBest = getBest();

            out.close();

            System.out.println("Improvement:");
            System.out.println("   " + startBest);
            System.out.println("   " + endBest);

            return endBest.value;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private void initPopulation() {
        int typeIdx = population.length / 4;
        for (int i = 0; i < typeIdx; i++) {
            population[i] = Individual.createEmpty(problem);
        }

        for (int i = typeIdx; i < population.length; i++) {
            population[i] = Individual.createRandom(problem);
        }
    }

    /**
     * Take a List of individuals and select only some of them so that the population is filled.
     * There has to be equal or greater number of individuals in the currentIndividuals List than the population.
     *
     * @param currentIndividuals
     */
    private void doSelection(List<Individual> currentIndividuals) {
        // Elitism - get best and add to next generation
        Individual elite = null;
        for (int i = 0; i < currentIndividuals.size(); i++) {
            if (elite == null || currentIndividuals.get(i).compareTo(elite) > 0) {
                elite = currentIndividuals.get(i);
            }
        }
        population[0] = elite;


        List<Integer> randomNums = new ArrayList<>(currentIndividuals.size());
        for (int i = 0; i < currentIndividuals.size(); i++) randomNums.add(i);

        for (int individualIdx = 1; individualIdx < population.length; individualIdx++) {
            // Use randomNums as list of random indices, from those take the best individual and remove from pool
            // If there are not enough individuals, reinitialize
            if (randomNums.size() < TOURNAMENT_SIZE) {
                randomNums.clear();
                for (int i = 0; i < currentIndividuals.size(); i++) randomNums.add(i);
            }

            Collections.shuffle(randomNums);

            Individual best = null;
            int bestIdx = 0;
            for (int tournamentIdx = 0; tournamentIdx < TOURNAMENT_SIZE; tournamentIdx++) {
                int indIdx = randomNums.get(tournamentIdx);
                if (best == null || currentIndividuals.get(indIdx).compareTo(best) > 0) {
                    best = currentIndividuals.get(indIdx);
                    bestIdx = tournamentIdx;
                }
            }
            population[individualIdx] = best;
            randomNums.remove(bestIdx);
        }
    }

    private void doCrossovers(List<Individual> currentIndividuals) {
        for (int i = 0; i < population.length; i++) {
            if (rnd.nextDouble() > PROBABILITY_CROSSOVER) continue;
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
//            if (rnd.nextDouble() > PROBABILITY_MUTATION) continue;

//            currentIndividuals.add(population[i].mutate());
            currentIndividuals.add(population[i].mutateAlternative(PROBABILITY_MUTATION));
        }
    }


    private String stats(int idx) {
        return new StringBuilder()
                .append("Generation " + idx + " completed.\n")
                .append("  best: " + getBest() + "\n")
                .append("  average fitness: " + getAverage() + "\n")
                .append("  worst: " + getWorst() + "\n")
                .toString();
    }

    private String gnuplotLine(int idx) {
        return new StringBuilder()
                .append(getBest().fitness)
                .append(",")
                .append(getAverage())
                .append(",")
                .append(getWorst().fitness)
                .toString();
    }

    private Individual getBest() {
        Individual best = null;
        for (int indIdx = 0; indIdx < population.length; indIdx++) {
            if (best == null || population[indIdx].compareTo(best) > 0) {
                best = population[indIdx];
            }
        }

        return best;
    }

    private Individual getWorst() {
        Individual worst = null;
        for (int indIdx = 0; indIdx < population.length; indIdx++) {
            if (worst == null || population[indIdx].compareTo(worst) < 0) {
//                if (population[indIdx].weight > problem.maxWeight) continue;
                worst = population[indIdx];
            }
        }

        return worst;
    }

    private double getAverage() {
        int totalFitness = 0;
        for (int indIdx = 0; indIdx < population.length; indIdx++) {
            totalFitness += population[indIdx].fitness;
        }

        return ((double) totalFitness) / population.length;
    }

}
