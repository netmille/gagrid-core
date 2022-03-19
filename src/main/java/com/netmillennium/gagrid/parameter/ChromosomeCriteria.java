package com.netmillennium.gagrid.parameter;

import java.util.ArrayList;

import java.util.List;

/**
 * Responsible for describing the characteristics of an individual Chromosome.
 */
public class ChromosomeCriteria {
    /** List of criteria for a Chromosome */
    private List<String> criteria = new ArrayList<String>();

    /**
     * Retrieve criteria
     *
     * @return List of strings
     */
    public List<String> getCriteria() {
        return criteria;
    }

    /**
     * Set criteria
     *
     * @param criteria List of criteria to be applied for a Chromosome ;Use format "name=value", ie: "coinType=QUARTER"
     */
    public void setCriteria(List<String> criteria) {
        this.criteria = criteria;
    }

}
