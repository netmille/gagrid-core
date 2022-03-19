package com.netmillennium.gagrid.parameter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteException;
import org.apache.ignite.cache.affinity.Affinity;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.compute.ComputeJob;
import org.apache.ignite.compute.ComputeJobResult;
import org.apache.ignite.compute.ComputeJobResultPolicy;
import org.apache.ignite.compute.ComputeTaskAdapter;
import org.apache.ignite.resources.IgniteInstanceResource;

/**
 * Responsible for fitness operation
 */
public class FitnessTask extends ComputeTaskAdapter<List<Long>, Boolean> {
    /** Ignite instance */
    @IgniteInstanceResource
    private Ignite ignite = null;

    /** GAConfiguration */
    private GAConfiguration cfg;

    /**
     * @param cfg GAConfiguration
     */
    public FitnessTask(GAConfiguration cfg) {
        this.cfg = cfg;
    }

    /**
     * @param nodes List of ClusterNode
     * @param chromosomeKeys List of chromosome keys
     * @return Map of jobs to nodes
     */
    public Map map(List<ClusterNode> nodes, List<Long> chromosomeKeys) throws IgniteException {

        Map<ComputeJob, ClusterNode> map = new HashMap<>();

        Affinity affinity = ignite.affinity(GAGridConstants.POPULATION_CACHE);

        for (Long key : chromosomeKeys) {

            FitnessJob ajob = new FitnessJob(key, this.cfg.getFitnessFunction());

            ClusterNode primary = affinity.mapKeyToNode(key);

            map.put(ajob, primary);
        }
        return map;
    }

    /**
     * @param list List of ComputeJobResult
     * @return Boolean value
     */
    public Boolean reduce(List<ComputeJobResult> list) throws IgniteException {

        return Boolean.TRUE;
    }

    /**
     * @param res ComputeJobResult
     * @param rcvd List of ComputeJobResult
     * @return ComputeJobResultPolicy
     */
    public ComputeJobResultPolicy result(ComputeJobResult res, List<ComputeJobResult> rcvd) {
        IgniteException err = res.getException();

        if (err != null)
            return ComputeJobResultPolicy.FAILOVER;

        // If there is no exception, wait for all job results.
        return ComputeJobResultPolicy.WAIT;

    }
}
