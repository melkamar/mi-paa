package com.melkamar.sat.evo;

import java.io.BufferedWriter;
import java.io.File;
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
    private double PROBABILITY_MUTATION = 0.025;
    private int TOURNAMENT_SIZE = 3;
    private int POPULATION_SIZE = 250;
    private int generations = 5000;

    private double MUT_START = 0.1;
    private double MUT_BOTTOM = 0.01;
    private double MUT_STEPS = generations / 2;
    private double MUT_DELTA_START = (MUT_START - MUT_BOTTOM) / MUT_STEPS;
    private double MUT_DELTA = MUT_DELTA_START;

    private int idx = 1;

    private boolean annealMutation = false; // If true, mutation probability will be annealed

    public static double FITNESS_CORRECT_MULTIPLIER = 10;

    public Evolution(Problem problem,
                     double PROBABILITY_CROSSOVER,
                     double PROBABILITY_MUTATION,
                     int TOURNAMENT_SIZE,
                     int POPULATION_SIZE,
                     int generations,
                     int idx
    ) {
        this.problem = problem;
        this.PROBABILITY_CROSSOVER = PROBABILITY_CROSSOVER;
        this.PROBABILITY_MUTATION = PROBABILITY_MUTATION;
        this.TOURNAMENT_SIZE = TOURNAMENT_SIZE;
        this.POPULATION_SIZE = POPULATION_SIZE;
        this.generations = generations;

        this.idx = idx;

        population = new Individual[POPULATION_SIZE];
    }

    public Evolution setFitnessMultiplier(double multiplier) {
        Evolution.FITNESS_CORRECT_MULTIPLIER = multiplier;
        return this;
    }

    public Evolution(Problem problem,
                     double PROBABILITY_CROSSOVER,
                     double MUT_START,
                     double MUT_BOTTOM,
                     double MUT_STEPS,
                     int TOURNAMENT_SIZE,
                     int POPULATION_SIZE,
                     int generations,
                     int idx) {
        annealMutation = true;

        this.problem = problem;
        this.PROBABILITY_CROSSOVER = PROBABILITY_CROSSOVER;

        this.PROBABILITY_MUTATION = MUT_START;
        this.MUT_START = MUT_START;
        this.MUT_BOTTOM = MUT_BOTTOM;
        this.MUT_STEPS = MUT_STEPS;
        this.MUT_DELTA_START = (MUT_START - MUT_BOTTOM) / MUT_STEPS;
        this.MUT_DELTA = MUT_DELTA_START;

        this.TOURNAMENT_SIZE = TOURNAMENT_SIZE;
        this.POPULATION_SIZE = POPULATION_SIZE;
        this.generations = generations;

        this.idx = idx;

        population = new Individual[POPULATION_SIZE];
    }

    public int solve() {
        String flag = "";
        String parameterVals = "id" + idx + "-cross" + PROBABILITY_CROSSOVER + "-mut" + PROBABILITY_MUTATION + "-tour" + TOURNAMENT_SIZE + "-pop" + POPULATION_SIZE + "-gen" + generations;
        String descrstr = "generic.dat";
        String outName = "D:\\cvut-checkouted\\mi-paa\\hw\\5\\resources\\graphs\\out\\" + descrstr;

        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(outName));

            initPopulation();

            Individual startBest = getBest();
            for (int generation = 1; generation < generations + 1; generation++) {
                List<Individual> currentIndividuals = new ArrayList<>(Arrays.asList(population));
                doCrossovers(currentIndividuals);

                doMutation(currentIndividuals);
                if (annealMutation) {
                    PROBABILITY_MUTATION -= MUT_DELTA;
                    PROBABILITY_MUTATION = Math.max(PROBABILITY_MUTATION, MUT_BOTTOM);
                }

                System.out.println("   Selection from " + currentIndividuals.size() + " individuals.");
                doSelection(currentIndividuals);

                System.out.println(stats(generation));
                if (annealMutation) {
                    System.out.println("    mutation %: " + PROBABILITY_MUTATION);
                    System.out.println("      delta: " + MUT_DELTA);
                    System.out.println();
                }

                out.write(gnuplotLine(generation));
                out.newLine();

                if (generation > 200 && !getBest().correct) {
                    // restart evolution if still no good thing found
                    generation = 1;
                    initPopulation();
                    if (annealMutation)
                        PROBABILITY_MUTATION = MUT_START;
                }
            }
            Individual endBest = getBest();

            out.close();

            System.out.println("Improvement:");
            System.out.println("   " + startBest);
            System.out.println("   " + endBest);


//            return endBest.value;
            makeGraph(parameterVals);
            return 0;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private void makeGraph(String filename) throws IOException {

        Process process = new ProcessBuilder("c:\\Program Files (x86)\\gnuplot\\bin\\gnuplot.exe",
//                                             "-e \"filename='"+filename+"'\"",
                                             "-e", "\"\"filename='" + filename + "'\"\"",
                                             "d:\\cvut-checkouted\\mi-paa\\hw\\5\\resources\\gnupl.file.script")
                .inheritIO()
                .directory(new File("d:\\cvut-checkouted\\mi-paa\\hw\\5\\resources\\graphs\\pics\\"))
                .start();
        try {
            Thread.currentThread().sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        process = new ProcessBuilder("c:\\Program Files (x86)\\gnuplot\\bin\\gnuplot.exe",
//                                     "d:\\cvut-checkouted\\mi-paa\\hw\\5\\resources\\gnupl.script",
//                                     "-p").start();
    }

    private void initPopulation() {
        for (int i = 0; i < population.length; i++) {
            population[i] = Individual.createRandom(problem);
        }
    }

    private void initPopulation2() {
        int typeIdx = population.length / 4;
        for (int i = 0; i < typeIdx; i++) {
            population[i] = Individual.createEmpty(problem);
        }

        for (int i = typeIdx; i < typeIdx * 2; i++) {
            population[i] = Individual.createFull(problem);
        }

        for (int i = typeIdx * 2; i < population.length; i++) {
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
                .append(getBest().getFitness())
//                .append(getBest().getFitnessOrig())
                .append(",")
//                .append(getAverageOrig())
                .append(getAverage())
                .append(",")
                .append(getWorst().getFitness())
                .toString();
    }

    private Individual getBest() {
        Individual best = null;
        for (int indIdx = 0; indIdx < population.length; indIdx++) {
            if (best == null || population[indIdx].compareTo(best) > 0 && population[indIdx].correct) {
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
            totalFitness += population[indIdx].getFitness();
        }

        return ((double) totalFitness) / population.length;
    }

//    private double getAverageOrig() {
//        int totalFitness = 0;
//        for (int indIdx = 0; indIdx < population.length; indIdx++) {
//            totalFitness += population[indIdx].getFitnessOrig();
//        }
//
//        return ((double) totalFitness) / population.length;
//    }

}
