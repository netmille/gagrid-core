package com.netmillennium.gagrid.parameter;

import com.netmillennium.gagrid.model.Chromosome;

/**
 * Represents the terminate condition for a genetic algorithm.
 *
 * <p>
 *
 * Implement this interface for your respective use case.
 *
 * </p>
 */
@FunctionalInterface
public interface ITerminateCriteria {
    /**
     * @param fittestChromosome Fittest chromosome as of the nth generation
     * @param averageFitnessScore Average fitness score
     * @param generation Current number of generations
     * @return Boolean value to determine when to stop evolution
     */
    public boolean isTerminationConditionMet(Chromosome fittestChromosome, double averageFitnessScore, int generation);
}
