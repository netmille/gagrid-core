package com.netmillennium.gagrid.model;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

/**
 * Represents a potential solution consisting of a fixed-length collection of genes. <br/>
 *
 * <p>
 *
 * NOTE: Chromosome resides in cache: GAGridConstants.POPULATION_CACHE. This cached is partitioned.
 *
 * </p>
 */
public class Chromosome {
    /** primary key of Chromosome */
    private static final AtomicLong ID_GEN = new AtomicLong();

    /** fitness score */
    @QuerySqlField(index = true)
    private double fitnessScore = -1;

    /** Id (indexed). */
    @QuerySqlField(index = true)
    private Long id;

    /** array of gene keys. */
    private long[] genes;

    /**
     * @param genes Primary keys of Genes
     */
    public Chromosome(long[] genes) {
        id = ID_GEN.incrementAndGet();
        this.genes = genes;
    }

    /**
     * Gets the fitnessScore
     *
     * @return This chromosome's fitness score
     */
    public double getFitnessScore() {
        return fitnessScore;
    }

    /**
     * Set the fitnessScore for this chromosome
     *
     * @param fitnessScore This chromosome's new fitness score
     */
    public void setFitnessScore(double fitnessScore) {
        this.fitnessScore = fitnessScore;
    }

    /**
     * Gets the gene keys (ie: primary keys) for this chromosome
     *
     * @return This chromosome's genes
     */
    public long[] getGenes() {
        return genes;
    }

    /**
     * Set the gene keys (ie: primary keys)
     *
     * @param genes This chromosome's new genes
     */
    public void setGenes(long[] genes) {
        this.genes = genes;
    }

    /**
     * Get the id (primary key) for this chromosome
     *
     * @return This chromosome's primary key
     */
    public Long id() {
        return id;
    }

    /** {@inheritDoc} */
    @Override public String toString() {
        return "Chromosome [fitnessScore=" + fitnessScore + ", id=" + id + ", genes=" + Arrays.toString(genes) + "]";
    }

}
