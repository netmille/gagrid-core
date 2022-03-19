package com.netmillennium.gagrid.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.cache.Cache.Entry;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.SqlQuery;

import com.netmillennium.gagrid.cache.PopulationCacheConfig;
import com.netmillennium.gagrid.model.Chromosome;
import com.netmillennium.gagrid.model.Gene;
import com.netmillennium.gagrid.parameter.GAGridConstants;

/**
 * 
 *  GA Grid Helper routines
 *  
 * @author turik.campbell
 *
 */
public class GAGridUtils {

	 /**
	  *  Retrieve chromosomes
	  *  
	  * @param ignite
	  * @param query
	  * @return
	  */
	 public static List<Chromosome> getChromosomes(Ignite ignite, String query)
	 {
		 List<Chromosome> chromosomes = new ArrayList();
		 
         IgniteCache<Long, Chromosome> populationCache = ignite.getOrCreateCache(PopulationCacheConfig.populationCache());
        
         SqlQuery sql = new SqlQuery(Chromosome.class, query);

         try (QueryCursor<Entry<Long, Chromosome>> cursor = populationCache.query(sql)) {
             for (Entry<Long, Chromosome> e : cursor)
           	  chromosomes.add(e.getValue());
             }
		 
		 return chromosomes;
	 }
	 
	 
    /**
     *  
     * @param ignite
     * @param chromosome
     * @return List<Gene> 
     */
    public static List<Gene> getGenesForChromosome(Ignite ignite, Chromosome chromosome) {
        List<Gene> genes = new ArrayList();
        IgniteCache<Long, Gene> cache = ignite.cache(GAGridConstants.GENE_CACHE);
        StringBuffer sbSqlClause = new StringBuffer();
        sbSqlClause.append("_key IN ");
        String sqlInClause = Arrays.toString(chromosome.getGenes());

        sqlInClause = sqlInClause.replace("[", "(");
        sqlInClause = sqlInClause.replace("]", ")");

        sbSqlClause.append(sqlInClause);

        SqlQuery sql = new SqlQuery(Gene.class, sbSqlClause.toString());

        try (QueryCursor<Entry<Long, Gene>> cursor = cache.query(sql)) {
            for (Entry<Long, Gene> e : cursor)
                genes.add(e.getValue());
        }

        return genes;
    }
    
    /**
     * 
     * Retrieve genes in order
     * 
     * @param ignite
     * @param chromosome
     * @return List<Gene>
     */
    
    public static List<Gene> getGenesInOrderForChromosome(Ignite ignite, Chromosome chromosome) {
        List<Gene> genes = new ArrayList();
        IgniteCache<Long, Gene> cache = ignite.cache(GAGridConstants.GENE_CACHE);
      
        long[] primaryKeys = chromosome.getGenes();
        
        for (int k =0; k< primaryKeys.length; k++)
        {
        
          StringBuffer sbSqlClause = new StringBuffer();
          sbSqlClause.append("_key IN ");
          sbSqlClause.append("(");
          sbSqlClause.append(primaryKeys[k]);
          sbSqlClause.append(")");
          
          SqlQuery sql = new SqlQuery(Gene.class, sbSqlClause.toString());

           try (QueryCursor<Entry<Long, Gene>> cursor = cache.query(sql)) {
            for (Entry<Long, Gene> e : cursor)
                genes.add(e.getValue());
            }
        }
       
        return genes;
    }
}
