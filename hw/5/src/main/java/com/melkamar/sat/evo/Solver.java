package com.melkamar.sat.evo;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Martin Melka on 19.12.2016 11:49.
 */
public class Solver {
    public static void main(String[] args) throws FileNotFoundException {
        for (int i=1; i<100; i++) {
            Problem problem = readProblemFile(
                    "d:\\cvut-checkouted\\mi-paa\\hw\\5\\resources\\problems\\set\\easy\\uf75-0" + i + ".cnf");
            new Evolution(problem, 0.25, 0.025, 3, 250, 5000, i).solve();
        }
    }

//    public static void main(String[] args) throws FileNotFoundException {
////        Problem problem = readProblemFile("d:\\cvut-checkouted\\mi-paa\\hw\\5\\resources\\problems\\uf20-01.cnf");
////        Problem problem = readProblemFile("d:\\cvut-checkouted\\mi-paa\\hw\\5\\resources\\problems\\uf200-01.cnf");
////        Problem problem = readProblemFile("d:\\cvut-checkouted\\mi-paa\\hw\\5\\resources\\problems\\uf75-01.cnf");
////        Problem problem = readProblemFile("d:\\cvut-checkouted\\mi-paa\\hw\\5\\resources\\problems\\uf75-01-easy.cnf");
//        Problem problem = readProblemFile("d:\\cvut-checkouted\\mi-paa\\hw\\5\\resources\\problems\\uf75-01-easier.cnf");
////        Problem problem = readProblemFile("d:\\cvut-checkouted\\mi-paa\\hw\\5\\resources\\problems\\uf20-01-easy.cnf");
////        Problem problem = readProblemFile("d:\\cvut-checkouted\\mi-paa\\hw\\5\\resources\\problems\\easyson.cnf");
//
////        analyzeIndividual(problem,
////                "1 1 1 1 0 1 0 1 0 1 0 0 0 1 1 0 1 0 0 0 0 0 0 0 0 1 1 0 1 0 1 0 0 1 0 1 0 1 0 0 1 1 1 0 0 0 1 0 1 0 0 0 0 0 0 1 1 0 0 0 1 1 1 1 1 0 0 1 1 1 0 1 1 0 0"); //324
//
////        private double PROBABILITY_CROSSOVER = 0.25;
////        private double PROBABILITY_MUTATION = 0.025;
////        private int TOURNAMENT_SIZE = 3;
////        private int POPULATION_SIZE = 250;
////        private int generations = 5000;
//
////        new Evolution(problem, 0.25, 0.025, 3, 50, 5000).solve();
////        new Evolution(problem, 0.25, 0.025, 3, 100, 5000).solve();
////        for (int i = 0; i < 10; i++)
//
////        new Evolution(problem, 0.25, 0.5, 0.01, 1000, 3, 150, 5000, 1).solve();
////        new Evolution(problem, 0.25, 0.5, 0.01, 2000, 3, 150, 5000, 2).solve();
////        new Evolution(problem, 0.25, 0.5, 0.01, 3000, 3, 150, 5000, 3).solve();
////        new Evolution(problem, 0.25, 0.5, 0.01, 4000, 3, 150, 5000, 4).solve();
////        new Evolution(problem, 0.25, 0.5, 0.01, 5000, 3, 150, 5000, 5).solve();
//
//        // TOP - anneal 0.5 - 0.025, end at 3000th gen
//
////        double[] values = new double[]{500, 1000, 2000, 3000, 4000, 5000, 6000, 8000, 10000};
//        double[] values = new double[]{10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
////        double[] values = new double[]{0.025};
//
//        for (int i = 0; i < values.length; i++) {
////            new Evolution(problem, 0.25, values[i], 3, 2500, 50000, i).solve();
////            new Evolution(problem, 0.25, 0.5, 0.025, values[i], 3, 250, 5000, i).solve();
////            new Evolution(problem, 0.25, 0.025, 3, 250, 5000, i).solve();
//            new Evolution(problem, 0.25, 0.025, 3, 250, 5000, (int)values[i]).setFitnessMultiplier(values[i]).solve();
//        }
//
////        new Evolution(problem, 0.25, 0.025, 3, 500, 5000).solve();
////        new Evolution(problem, 0.25, 0.025, 3, 1000, 5000).solve();
//    }

    public static void analyzeIndividual(Problem problem, String vector) {
        String[] vars = vector.split(" ");
        boolean[] boolVector = new boolean[vars.length];
        for (int i = 0; i < problem.varCount; i++) boolVector[i] = (!vars[i].equals("0"));
        for (int i = 0; i < problem.clauses.length; i++) {
            Problem.Clause clause = problem.clauses[i];

            System.out.print(clause);
            if (clause.isCorrect(boolVector)) {
                System.out.println("  OK");
            } else {
                System.out.println("  FAIL");
            }
        }

        Individual individual = Individual.createEmpty(problem);
        individual.setSolutionVect(boolVector);
        System.out.println(individual);
    }

    public static Problem readProblemFile(String filename) throws FileNotFoundException {
        Scanner in = new Scanner(new FileReader(filename));

        String line = nextLine(in);

        // Get basic info
        String[] tokens = line.split("\\s+");
        int variables = Integer.parseInt(tokens[2]);
        int clausesCount = Integer.parseInt(tokens[3]);

        Problem.Clause[] clauses = new Problem.Clause[clausesCount];
        for (int i = 0; i < clausesCount; i++) {
            // Parse line of clause
            String[] clauseVarStrs = nextLine(in).split("\\s+");
            int[] clauseVars = new int[3];
            for (int j = 0; j < 3; j++)
                clauseVars[j] = Integer.parseInt(clauseVarStrs[j]);

            clauses[i] = new Problem.Clause(clauseVars);

            assert Integer.parseInt(clauseVarStrs[3]) == 0;
        }

        // Parse weights
        String[] weightStrs = nextLine(in).split("\\s+");
        int[] weights = new int[variables];
        for (int i = 0; i < variables; i++)
            weights[i] = Integer.parseInt(weightStrs[i]);

        return new Problem(variables,
                           weights,
                           clauses);
    }

    private static String nextLine(Scanner scanner) {
        String line = scanner.nextLine();
        while (line.startsWith("c")) {
            line = scanner.nextLine();
        }
        return line.trim();
    }
}
