package com.melkamar.sat.evo;

import java.util.Arrays;

/**
 * Created by Martin Melka (martin.melka@gmail.com) on 19.12.2016 11:55.
 */
public class Problem {
    int varCount;
    int[] weights;
    Clause[] clauses;

    public Problem(int varCount, int[] weights, Clause[] clauses) {
        this.varCount = varCount;
        this.weights = weights;
        this.clauses = clauses;
    }

    public static class Clause {
        int[] variables;

        public Clause(int[] variables) {
            this.variables = variables;
        }

        public boolean isCorrect(boolean[] variables) {
            for (int i = 0; i < 3; i++) {
                int a = this.variables[i];
                boolean expected = a > 0; // if a>0, then expect it to be true
                int idx = Math.abs(a) - 1;

                if (variables[idx] == expected)
                    return true;
            }

            return false;
        }

        @Override
        public String toString() {
            return "{" +
                    Arrays.toString(variables) +
                    '}';
        }
    }
}
