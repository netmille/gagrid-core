package com.netmillennium.gagrid.parameter;

import java.util.List;

import com.netmillennium.gagrid.model.Gene;

/**
 * Fitness function are used to determine how optimal a particular solution is relative to other solutions.
 *
 * <p> The evaluate() method should be implemented for this interface. The fitness function is provided list of Genes.
 *
 * The evaluate method should return a positive double value that reflects fitness score.
 *
 * </p>
 */
@FunctionalInterface
public interface IFitnessFunction {
    /**
     * @param genes Genes within an individual Chromosome
     * @return Fitness score
     */
    public double evaluate(List<Gene> genes);
}
