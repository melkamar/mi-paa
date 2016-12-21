package com.melkamar.sat.evo;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Martin Melka (martin.melka@gmail.com) on 20.12.2016 19:36.
 */
public class IndividualTest {
    @Test
    public void countStats() throws Exception {
        Problem problem = new Problem(3,
                                      new int[]{1, 2, 3},
                                      new Problem.Clause[]{
                                              new Problem.Clause(new int[]{1, 2, -3}),
                                              new Problem.Clause(new int[]{-1, -2, -3})
                                      });
        Individual individual = Individual.createEmpty(problem);
        individual.countStats();
        assertTrue(individual.correct);
    }

}