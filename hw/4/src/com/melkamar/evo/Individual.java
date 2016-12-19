package com.melkamar.evo;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Random;

/**
 * Created by Martin Melka (martin.melka@gmail.com) on 19.12.2016 11:49 11:54.
 */
public class Individual {
    boolean [] solutionVect;
    Problem problem;

    public Individual(int solutionSize, Problem problem) {
        solutionVect = new boolean[solutionSize];
        this.problem = problem;
    }

    public void randomize(){
        Random rnd = new Random();
        for (int i=0; i<solutionVect.length; i++)
            solutionVect[i] = rnd.nextBoolean();
    }

    public Individual mutate(){
        throw new NotImplementedException();
    }

    public void cross(Individual otherIndividual){
        throw new NotImplementedException();
    }
}
