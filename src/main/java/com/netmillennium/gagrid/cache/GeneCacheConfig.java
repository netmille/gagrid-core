package com.netmillennium.gagrid.cache;

import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.CacheRebalanceMode;
import org.apache.ignite.configuration.CacheConfiguration;

import com.netmillennium.gagrid.functions.GAGridFunction;
import com.netmillennium.gagrid.model.Gene;
import com.netmillennium.gagrid.parameter.GAGridConstants;

/**
 * 
 * Cache configuration for GAGridConstants.GENE_CACHE
 * 
 * cache maintains full population of genes.
 * 
 * @author turik.campbell
 *
 */

public class GeneCacheConfig {

    /**
     * 
     * @return CacheConfiguration<Long, Gene>
     */
    public static CacheConfiguration<Long, Gene> geneCache() {

        CacheConfiguration<Long, Gene> cfg = new CacheConfiguration<>(GAGridConstants.GENE_CACHE);
        cfg.setIndexedTypes(Long.class, Gene.class);
        cfg.setCacheMode(CacheMode.REPLICATED);
        cfg.setRebalanceMode(CacheRebalanceMode.SYNC);
        cfg.setStatisticsEnabled(true);
        cfg.setBackups(1);
        cfg.setSqlFunctionClasses(GAGridFunction.class);
        return cfg;

    }

}
