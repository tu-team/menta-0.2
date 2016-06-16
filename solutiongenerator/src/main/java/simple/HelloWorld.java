/**
 * HelloWorld.java
 *
 * This is an example only! Use it for anything else at your own risk!
 * You have been warned! Coder/user beware!
 */
package simple;

import ec.EvolutionState;
import ec.Individual;
import ec.Problem;
import ec.simple.SimpleFitness;
import ec.simple.SimpleProblemForm;
import ec.vector.GeneVectorIndividual;

public class HelloWorld extends Problem implements SimpleProblemForm {
    private char[] _expected = "Hello, world!".toCharArray();

    public void evaluate(final EvolutionState evolutionState,
                                    final Individual individual,
                                    final int subPopulation,
                                    final int threadNum) {
        if (individual.evaluated)
            return;

        int fitnessValue = 0;

        GeneVectorIndividual charVectorIndividual = (GeneVectorIndividual) individual;
        long length = charVectorIndividual.size();
        for (int i = 0; i < length; i++) {
            CharVectorGene charVectorGene
                    = (CharVectorGene) charVectorIndividual.genome[i];
            char actual = charVectorGene.getAllele();
            if (actual == _expected[i]) {
                fitnessValue += 1;
            }
        }

        SimpleFitness fitness
                         = (SimpleFitness) charVectorIndividual.fitness;
        fitness.setFitness(evolutionState, fitnessValue,
                fitnessValue == charVectorIndividual.genomeLength());

        charVectorIndividual.evaluated = true;
    }

    public void describe(final Individual individual,
                         final EvolutionState state,
                         final int subPopulation,
                         final int threadNum,
                         final int log, final int verbosity) {
        // Do Nothing
    }
}