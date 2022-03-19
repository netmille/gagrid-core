package com.netmillennium.gagrid.parameter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CachePeekMode;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import com.netmillennium.gagrid.model.Gene;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GAGridTest {
	
	private GAGrid gaGrid;

	@BeforeAll
	public void initialize()
	{
		// Start the node in client mode.
		Ignite ignite = Ignition.start();
		
        // Create GAConfiguration
        GAConfiguration gaConfig = new GAConfiguration();

        // set Gene Pool
        List<Gene> genes = getGenePool();

        // set the Chromosome Length to '11' since 'HELLO WORLD' contains 11 characters.
        gaConfig.setChromosomeLen(11);

        // initialize gene pool
        gaConfig.setGenePool(genes);
        
    	gaGrid = new GAGrid(gaConfig, ignite);	
	}
	
	 /**
     * Helper routine to initialize Gene pool
     * 
     * In typical usecase genes may be stored in database.
     * 
     * @return List<Gene>
     */
    private List<Gene> getGenePool() {
        List<Gene> list = new ArrayList();

        char[] chars = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
            'T', 'U', 'V', 'W', 'X', 'Y', 'Z', ' ' };

        for (int i = 0; i < chars.length; i++) {
            Gene gene = new Gene(new Character(chars[i]));
            list.add(gene);
        }
        return list;
    }
    
	/**
	 * Test initialization of Gene Population
	 */
	@Test
	public void testInitializeGenePopulation()
	{
		gaGrid.initializeGenePopulation();
		
		//Test gene population was successfully created
		assertEquals(gaGrid.getGAGridConfiguration().getGenePool().size(),gaGrid.getGeneCache().size(CachePeekMode.PRIMARY));
	}
	
	/**
	 * Test initialization of Gene Population
	 */
	@Test
	public void testInitializeChromosomePopulation()
	{
		gaGrid.initializePopulation();
		//Test initialize Chromosome population was successfully created
		assertEquals(gaGrid.getGAGridConfiguration().getPopulationSize(),gaGrid.getPopulationCache().size(CachePeekMode.PRIMARY));
	}
	
	@AfterAll
	public void tearDown()
	{
	  gaGrid=null;
	}
	
}
