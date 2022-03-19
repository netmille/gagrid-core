package com.netmillennium.gagrid.parameter;

import java.util.List;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteException;
import org.apache.ignite.compute.ComputeJobAdapter;
import org.apache.ignite.resources.IgniteInstanceResource;
import org.apache.ignite.transactions.Transaction;

import com.netmillennium.gagrid.model.Chromosome;

/**
 * Responsible for performing truncate selection
 */
public class TruncateSelectionJob extends ComputeJobAdapter {
    /** primary key of Chromosome to mutate */
    private Long key;

    /** primary keys of genes to be used in mutation */
    private List<Long> mutatedGeneKeys;

    /** Ignite instance */
    @IgniteInstanceResource
    private Ignite ignite = null;

    /**
     * @param key Primary key of Chromosome to mutate
     * @param mutatedGeneKeys Primary keys of genes to be used in mutation
     */
    public TruncateSelectionJob(Long key, List<Long> mutatedGeneKeys) {
        this.key = key;
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

        for (int k = 0; k < this.mutatedGeneKeys.size(); k++)
            geneKeys[k] = this.mutatedGeneKeys.get(k);

        chromosome.setGenes(geneKeys);

        Transaction tx = ignite.transactions().txStart();

        populationCache.put(chromosome.id(), chromosome);

        tx.commit();

        return Boolean.TRUE;
    }

}
