package com.melkamar.sat.evo;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Martin Melka (martin.melka@gmail.com) on 20.12.2016 17:45.
 */
public class ClauseTest {
    @Test
    public void isClauseCorrect() throws Exception {
        Problem.Clause clause = new Problem.Clause(new int[]{1, -2, -3});
        assertTrue(clause.isCorrect(new boolean[]{true, false, false}));

        clause = new Problem.Clause(new int[]{1, -2, -3});
        assertTrue(clause.isCorrect(new boolean[]{false, false, true}));

        clause = new Problem.Clause(new int[]{1, -2, -3});
        assertTrue(clause.isCorrect(new boolean[]{false, true, false}));

        clause = new Problem.Clause(new int[]{1, -2, -3});
        assertTrue(clause.isCorrect(new boolean[]{false, false, false}));

        clause = new Problem.Clause(new int[]{1, -2, -3});
        assertTrue(clause.isCorrect(new boolean[]{true, false, false, true, true, true, true, true}));

        clause = new Problem.Clause(new int[]{5, -2, -3});
        assertTrue(clause.isCorrect(new boolean[]{true, true, true, true, true, false, false, false}));
    }

    @Test
    public void isClauseNotCorrect() throws Exception {
        Problem.Clause clause = new Problem.Clause(new int[]{1, -2, -3});
        assertFalse(clause.isCorrect(new boolean[]{false, true, true}));

        clause = new Problem.Clause(new int[]{1, 2, 3});
        assertFalse(clause.isCorrect(new boolean[]{false, false, false}));

        clause = new Problem.Clause(new int[]{1, 2, 7});
        assertFalse(clause.isCorrect(new boolean[]{false, false, false, false, false, false, false}));
    }

}