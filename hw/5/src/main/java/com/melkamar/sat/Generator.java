package com.melkamar.sat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Random;

/**
 * Created by Martin Melka (martin.melka@gmail.com) on 20.12.2016 10:07.
 */
public class Generator {
    public static void main(String[] args) throws IOException, URISyntaxException {
//        String problem = generate(10, 2);
//
//        BufferedWriter bw = new BufferedWriter(new FileWriter("hw\\5\\resources\\problem"));
//        bw.write(problem);
//        bw.close();

        System.out.println(generateWeights(75));
    }

    public static String generateWeights(int variables){
        Random rnd = new Random();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < variables; i++) {
            result.append(rnd.nextInt(1) + 1).append(" ");
        }
        result.append("0\n");
        return result.toString();
    }


    /**
     * Generate SAT3 problem in "extended" DIMACS CNF format:
     * <p>
     * (x1 + x3' + x4) * (x2 + x3 + x5') * ...
     * <p>
     * p cnf 5 2      # p cnf [variables] [clauses]
     * 1 -3 4 0
     * 2 3 -5 0
     * ...
     * 10 5 12 16 13 0    # <- weights for x1, x2 ... x5
     *
     * @param variables Number of variables x1, x2,...
     * @param ratio     Number of clauses vs number of parameters. Hardest 3SAT problems arise from ratio = 4.3.
     * @return 3SAT problem in extended DIMACS CNF format with weights as the last line.
     */
    public static String generate(int variables, double ratio) {
        int clauses = (int) Math.floor(variables * ratio);
        StringBuilder result = new StringBuilder();
        Random rnd = new Random();

        result.append("p cnf ").append(variables).append(" ").append(clauses).append("\n");
        for (int clause = 0; clause < clauses; clause++) {
            for (int var = 0; var < 3; var++) {
                int variable = rnd.nextInt(variables) + 1; // Generate a variable
                if (rnd.nextBoolean()) variable *= -1; // Randomly negate
                result.append(variable).append(" ");
            }
            result.append("0\n");
        }

        // Generate weights
        for (int i = 0; i < variables; i++) {
            result.append(rnd.nextInt(100) + 1).append(" ");
        }
        result.append("0\n");

        return result.toString();
    }
}
