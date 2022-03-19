package com.netmillennium.gagrid.parameter;

import java.util.List;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteException;
import org.apache.ignite.IgniteLogger;
import org.apache.ignite.compute.ComputeJobAdapter;
import org.apache.ignite.resources.IgniteInstanceResource;
import org.apache.ignite.resources.LoggerResource;
import org.apache.ignite.transactions.Transaction;

import com.netmillennium.gagrid.model.Chromosome;

/**
 * Responsible for applying mutation on respective Chromosome based on mutation Rate
 */
public class MutateJob extends ComputeJobAdapter {
    /** primary key of Chromosome to mutate **/
    private Long key;

    /** primary keys of genes to be used in mutation **/
    private List<Long> mutatedGeneKeys;

    /** Ignite instance */
    @IgniteInstanceResource
    private Ignite ignite = null;

    /** Ignite logger */
    @LoggerResource
    private IgniteLogger log = null;
    
    /** Mutation Rate **/
    private double mutationRate;

    /**
     * @param key Primary key of chromosome
     * @param mutatedGeneKeys Primary keys of genes to be used in mutation
     * @param mutationRate Mutation rate
     */
    public MutateJob(Long key, List<Long> mutatedGeneKeys, double mutationRate) {
        this.key = key;
        this.mutationRate = mutationRate;
        this.mutatedGeneKeys = mutatedGeneKeys;
    }

    /**
     * Perform mutation
     *
     * @return Boolean value
     */
    public Boolean execute() throws IgniteException {

        IgniteCache<Long, Chromosome> populationCache = ignite.cache(GAGridConstants.POPULATION_CACHE);

        Chromosome chromosome = populationCache.localPeek(key);

        long[] geneKeys = chromosome.getGenes();

        for (int k = 0; k < this.mutatedGeneKeys.size(); k++) {
            // Mutate gene based on MutatonRate
            if (this.mutationRate > Math.random())
                geneKeys[k] = this.mutatedGeneKeys.get(k);
        }

        chromosome.setGenes(geneKeys);

        Transaction tx = ignite.transactions().txStart();

        populationCache.put(chromosome.id(), chromosome);

        tx.commit();

        if(!log.isQuiet())log.info(new StringBuffer("event=mutationEvaluation|result=success").toString());

        return Boolean.TRUE;
    }

}
