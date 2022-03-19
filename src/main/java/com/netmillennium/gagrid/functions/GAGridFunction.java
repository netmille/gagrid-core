package com.netmillennium.gagrid.functions;

import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.query.annotations.QuerySqlFunction;
import org.h2.tools.SimpleResultSet;

import com.netmillennium.gagrid.model.Chromosome;
import com.netmillennium.gagrid.model.Gene;
import com.netmillennium.gagrid.utils.GAGridUtils;

/**
 * Responsible for providing custom SQL functions to retrieve optimization results.
 */
public class GAGridFunction {
     /** */
    public GAGridFunction() {
    }

    /**
     * Retrieve solutions in descending order based on fitness score.
     *
     * @return Result set.
     * @throws SQLException If failed.
     */
    @QuerySqlFunction
    public static SimpleResultSet getSolutionsDesc() {
        return (getChromosomes("order by fitnessScore desc"));
    }

    /**
     * Retrieve solutions in ascending order based on fitness score.
     *
     * @return Result set
     * @throws SQLException If failed.
     */
    @QuerySqlFunction
    public static SimpleResultSet getSolutionsAsc() throws SQLException {
        return (getChromosomes("order by fitnessScore asc"));
    }

    /**
     * Retrieve and individual solution by Chromosome key.
     *
     * @param key Primary key of Chromosome.
     * @return SimpleResultSet.
     * @throws SQLException If failed.
     */
    @QuerySqlFunction
    public static SimpleResultSet getSolutionById(int key) throws SQLException {
        StringBuffer sbSqlClause = new StringBuffer();
        sbSqlClause.append("_key IN");
        sbSqlClause.append("(");
        sbSqlClause.append(key);
        sbSqlClause.append(")");
        return (getChromosomes(sbSqlClause.toString()));
    }

    /**
     * Helper routine to return 'pivoted' results using the provided query param.
     *
     * @param qry Sql
     * @return Result set
     */
    private static SimpleResultSet getChromosomes(String qry) {
        Ignite ignite = Ignition.localIgnite();

        List<Chromosome> chromosomes = GAGridUtils.getChromosomes(ignite, qry);

        SimpleResultSet rs2 = new SimpleResultSet();

        Chromosome aChrom = chromosomes.get(0);
        int genesCnt = aChrom.getGenes().length;

        rs2.addColumn("Chromosome Id", Types.INTEGER, 0, 0);
        rs2.addColumn("Fitness Score", Types.DOUBLE, 0, 0);

        for (int i = 0; i < genesCnt; i++) {
            int colIdx = i + 1;
            rs2.addColumn("Gene " + colIdx, Types.VARCHAR, 0, 0);
        }

        for (Chromosome rowChrom : chromosomes) {

            Object[] row = new Object[genesCnt + 2];
            row[0] = rowChrom.id();
            row[1] = rowChrom.getFitnessScore();

            List<Gene> genes = GAGridUtils.getGenesInOrderForChromosome(ignite, rowChrom);
            int i = 2;

            for (Gene gene : genes) {
                row[i] = gene.getVal().toString();
                i = i + 1;
            }
            //Add a row for an individual Chromosome
            rs2.addRow(row);
        }

        return rs2;
    }

}
