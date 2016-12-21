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
//        Problem problem = readProblemFile("d:\\cvut-checkouted\\mi-paa\\hw\\5\\resources\\problems\\uf20-01.cnf");
//        Problem problem = readProblemFile("d:\\cvut-checkouted\\mi-paa\\hw\\5\\resources\\problems\\uf200-01.cnf");
//        Problem problem = readProblemFile("d:\\cvut-checkouted\\mi-paa\\hw\\5\\resources\\problems\\uf75-01.cnf");
        Problem problem = readProblemFile("d:\\cvut-checkouted\\mi-paa\\hw\\5\\resources\\problems\\uf75-01-easy.cnf");
//        Problem problem = readProblemFile("d:\\cvut-checkouted\\mi-paa\\hw\\5\\resources\\problems\\uf20-01-easy.cnf");
//        Problem problem = readProblemFile("d:\\cvut-checkouted\\mi-paa\\hw\\5\\resources\\problems\\easyson.cnf");

        Evolution evolution = new Evolution(problem);
        analyzeIndividual(problem,
                "1 1 1 1 0 1 0 1 0 1 0 0 0 1 1 0 1 0 0 0 0 0 0 0 0 1 1 0 1 0 1 0 0 1 0 1 0 1 0 0 1 1 1 0 0 0 1 0 1 0 0 0 0 0 0 1 1 0 0 0 1 1 1 1 1 0 0 1 1 1 0 1 1 0 0"); //324
        int result = evolution.solve();
    }

    public static void analyzeIndividual(Problem problem, String vector) {
        String[] vars = vector.split(" ");
        boolean[] boolVector = new boolean[vars.length];
        for (int i = 0; i < problem.varCount; i++) boolVector[i] = (!vars[i].equals("0"));
        for (int i = 0; i < problem.clauses.length; i++) {
            Problem.Clause clause = problem.clauses[i];

            System.out.print(clause);
            if (clause.isCorrect(boolVector)){
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
