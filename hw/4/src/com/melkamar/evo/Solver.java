package com.melkamar.evo;

import jdk.nashorn.internal.runtime.PrototypeObject;

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
        Problem[] problems = readProblemFile("D:\\cvut-checkouted\\mi-paa\\hw\\4\\resources\\problems\\knap_10.inst.dat");

        for (Problem problem: problems){
            System.out.println("Problem: "+problem);
            Evolution evolution = new Evolution(problem);
            int result = evolution.solve();
            System.out.println("    --> "+result);
        }
    }

    public static Problem[] readProblemFile(String filename) throws FileNotFoundException {
        Scanner in = new Scanner(new FileReader(filename));
        List<Problem> problems = new ArrayList<>();

        while (in.hasNextLine()) {
            String line = in.nextLine();
            Problem problem = parseProblem(line);
            problems.add(problem);
        }

        Problem [] retArr = new Problem[problems.size()];
        return problems.toArray(retArr);
    }

    public static Problem parseProblem(String problemLine) {
        String[] tokens = problemLine.split(" ");
        int id = Integer.parseInt(tokens[0]);
        int itemsCount = Integer.parseInt(tokens[1]);
        int maxWeight = Integer.parseInt(tokens[2]);

        Problem.Item[] items = new Problem.Item[itemsCount];
        for (int i = 0; i < itemsCount; i++) {
            items[i] = new Problem.Item(
                    Integer.parseInt(tokens[3 + (i * 2)]),
                    Integer.parseInt(tokens[3 + (i * 2) + 1]));
        }

        return new Problem(id, items, maxWeight);
    }

}
