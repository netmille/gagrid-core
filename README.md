

<div style="width:100%;height:100%;background-color:black">
<img src="javadoc/images/GAGridbanner.jpg">
</div>

* <a href="#summary">Summary</a>  <br/>
* <a href="#gettingstarted">Getting Started</a><br/>
    - <a href="#startgagrid">Install using JAR archive
 </a>  <br/>
   - <a href="#startgagriddocker">Install using Docker
 </a>  <br/>
* <a href="#usageguide">Usage Guide</a>  <br/>
    - <a href="#startgagrid">Start GA Grid</a>  <br/>
* <a href="#examples">Examples</a> 
* <a href="#glossary">Glossary</a>  <br/>



### <a name="summary">Summary</a> <br/>

**GA Grid** is distributive in memory **Genetic Algorithm (GA)** component for [Apache Ignite](https://ignite.apache.org/). A **GA** is a method of solving optimization problems by simulating the process of biological evolution.
**GA Grid** provides a distributive **GA**  library built on top of a mature and scalable [Apache Ignite](https://ignite.apache.org/) platform. **GAs** are excellent for searching through large and complex data sets for an optimal solution.  Real world applications of **GAs** include: automotive design, computer gaming, robotics, investments, traffic/shipment routing and more.


The following diagram depicts the steps performed by **GA Grid**.


 > NOTE: If you are unfamiliar with **GA** terminology please refer to the <a href="#glossary">Glossary</a> .


**Figure 1: Genetic Algorithm Process**

<br/>

![GA Process Diagram](javadoc/images/GAProcessDiagram.png)

<br/>


**GA Grid** is a viable solution for developing scalable **GAs** as it leverages **Apache Ignite's** core components :

  - **Advanced Clustering**
  - **Compute Grid**
  - **Data Grid**
  - **SQL Grid**

<br/>

The following diagram depicts **GA Grid's** architecture:
<br/>
<br/>

**Figure 2: GA Grid Architecture**

![GA Process Diagram](javadoc/images/GAGrid_Overview.png)


The diagram above depicts the major elements of **GA Grid**.<br/>
 **(F)itness Calculation**, **(C)rossover**, and **(M)utation** operations are modeled as a ComputeTask for distributive behavior.   The ComputeTask is split into multiple ComputeJobs, (ie: **F~n~,C~n~,M~n~**) assigned to respective nodes, and executed in parallel.

All of these ComputeTasks leverage  <a href="https://apacheignite.readme.io/docs/affinity-collocation#collocating-compute-with-data" target="_blank">Apache Ignite's Affinity Colocation</a> to route ComputeJobs to respective nodes where Chromosomes are stored in cache.


### <a name="gettingstarted">Getting Started</a> <br/>

<a name="installviaJAR">***Install Using JAR archive***</a>

>NOTE: **GAGRID_HOME**  refers to GA Grid installation folder.

 **Prerequisites:** <br/>

| Name | Value |
| ------ | ------|
| JDK | Oracle JDK 8 and above|
| Ignite | 2.15.x and above|


**Steps:**

Here is the quick summary on installation of **GA Grid** on Apache Ignite:

 - Copy the <a href="https://github.com/netmille/gagrid-core/packages/1300802" target="_blank">gagrid-core.jar</a> to **IGNITE_HOME\libs**
 
 <a name="installviaDocker">***Install Using Docker***</a> <br/>
 Assuming that you already have Docker installed on your machine, you can pull and run the **GA Grid**  enabled Ignite Docker image using the following commands. <br/>
 
 > NOTE: **GA Grid** enabled Ignite Docker image is bundled with example services library.
 
 **Run the latest version** <br/>
 ```
$ docker run -d netmille/ignite-gagrid

```
### <a name="usageguide">Usage Guide</a> <br/>

In order to begin using **GA Grid**, you will follow these steps:

 1. <a href="#step1">Create a GAConfiguration object</a><br/>
 2. <a href="#step2">Define the Gene and Chromosome </a><br/>
 4. <a href="#step3">Implement a fitness function</a><br/>
 5. <a href="#step4">Define terminate condition</a> <br/>
 6. <a href="#step5">Evolve the population</a><br/>
 
 We will use the **gagrid-app-helloworld** microservice to demonstrate.

 In the **gagrid-app-helloworld** example, our goal is to derive the phrase: <br/>
 
  ***"HELLO WORLD"***
 
 
<a name="step1">***Create a GAConfiguration***</a> <br/><br/>
To begin, we create a [GAConfiguration](javadoc/com/netmillennium/gagrid/parameter/GAConfiguration.html) object.  This class is utilized to customize the behaviour of **GA Grid**. 

```
           // Create GAConfiguration
            gaConfig = new GAConfiguration();
```          
 
<a name="step2">***Define the Gene and Chromosome***</a> <br/><br/>

 Next, we define our [Gene](javadoc/com/netmillennium/gagrid/model/Gene.html).  For our problem domain, an optimal solution is the phrase ***"HELLO WORLD"***.  Since the discrete parts are letters, we use a Character to model our [Gene](javadoc/com/netmillennium/gagrid/model/Gene.html). Next, we need to initialize a Gene pool of ***27*** [Gene](javadoc/com/netmillennium/gagrid/model/Gene.html) objects utilizing Characters. The code snippet below depicts this process.
 

 > NOTE: **GA Grid** utilizes the Gene pool to initialize a replicated Gene cache.

 ```
     
        List<Gene> list = new ArrayList();

        char[] chars = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
            'T', 'U', 'V', 'W', 'X', 'Y', 'Z', ' ' };

        for (int i = 0; i < chars.length; i++) {
            Gene gene = new Gene(new Character(chars[i]));
            list.add(gene);
        };
       
    
```
 Next, we define the [Chromosome](javadoc/com/netmillennium/gagrid/model/Chromosome.html) as is central to a **GA**, because it models an optimal solution.  The [Chromosome](javadoc/com/netmillennium/gagrid/model/Chromosome.html) is made of  **Genes** which represent discrete parts of a particular solution.
 
 For our **GA**, since the goal is a solution containing the phrase ***"HELLO WORLD"***, our Chromosomes should have ***11*** Genes (ie:Characters). As a result, we use the GAConfiguration to set to our Chromosome length to **11**.
 
 ```
 //set the Chromosome Length to '11' since 'HELLO WORLD' contains 11 characters.
 gaConfig.setChromosomeLength(11);
 
 ```
 
During **GA Grid** execution, Chromosomes evolve into optimal solutions through the process of crossover and mutation.  Next, only the best **Chromosomes** (ie: solutions) are chosen based on a fitness score.

 > NOTE: **GA Grid** internally stores potential (Chromosomes) in a Chromosome cache.
  
 

<br/>

**Optimal Solution:** <br/>

![HelloWorld](javadoc/images/helloworld_genes.png)

<br/>

<a name="step3">***Implement Fitness Function***</a> <br/><br/>

 **GA Grid** is intelligent enough to perform a majority of the process of natural selection. However, the **GA Grid** has no knowledge of the problem domain. For this reason, we need to define a fitness function. We will need to extend **GA Grid’s** [IFitnessFunction](javadoc/com/netmillennium/gagrid/parameter/IFitnessFunction.html) class to calculate a 'fitness score' for a potential [Chromosome](javadoc/com/netmillennium/gagrid/model/Chromosome.html).  A ***‘fitness score’*** is used to determine how optimal the solution is relative to other potential solutions in the population.  The code below demonstrates our fitness function.


<br/>

```
   public class HelloWorldFitnessFunction implements IFitnessFunction {

    private String targetString = "HELLO WORLD";

    @Override
    public double evaluate(List<Gene> genes) {

        double matches = 0;

        for (int i = 0; i < genes.size(); i++) {
            if (((Character) (genes.get(i).getVal())).equals(targetString.charAt(i))) {
                matches = matches + 1;
            }
        }
 
        return matches;
    }
}    

```  
Next, we configure [GAConfiguration](javadoc/com/netmillennium/gagrid/parameter/GAConfiguration.html) with our **HelloWorldFitnessFunction**

```
  // create and set Fitness function
            HelloWorldFitnessFunction function = new HelloWorldFitnessFunction();
            gaConfig.setFitnessFunction(function);

```

<br/>

<a name="step4">***Define terminate condition***</a> <br/><br/>

The next step is to specify a suitable terminate condition for **GA Grid**.  The terminate condition will vary 
depending the problem domain.  For our use case, we want **GA Grid** to terminate when
a Chromosome's fitness score equals 11.  We specify a terminate condition by implementing the [ITerminateCriteria](javadoc/com/netmillennium/gagrid/parameter/ITerminateCriteria.html) interface which has a single method **isTerminateConditionMet()**. 

```
public class HelloWorldTerminateCriteria implements ITerminateCriteria {

    private IgniteLogger igniteLogger = null;
    private Ignite ignite = null;
    
    public HelloWorldTerminateCriteria(Ignite ignite) {
        this.ignite = ignite;
        this.igniteLogger = ignite.log();

    }

    public boolean isTerminationConditionMet(Chromosome fittestChromosome, double averageFitnessScore, int currentGeneration) {
        boolean isTerminate = true;

        igniteLogger.info("##########################################################################################");
        igniteLogger.info("Generation: " + currentGeneration);
        igniteLogger.info("Fittest is Chromosome Key: " + fittestChromosome);
        igniteLogger.info("Chromsome: " + fittestChromosome);
        printPhrase(GAGridUtils.getGenesInOrderForChromosome(ignite, fittestChromosome));
        igniteLogger.info("Avg Chromsome Fitness: " + averageFitnessScore);
        igniteLogger.info("##########################################################################################");

        if (!(fittestChromosome.getFitnessScore() > 10)) {
            isTerminate = false;
        }
        
        return isTerminate;
    }
    
    
    /**
     * Helper to print Phrase
     * 
     * @param genes
     */
    private void printPhrase(List<Gene> genes) {
        
        StringBuffer sbPhrase = new StringBuffer();
        
        for (Gene gene : genes) {
            sbPhrase.append(((Character) gene.getValue()).toString());
        }
        igniteLogger.info(sbPhrase.toString());
    }
```


Next, we configure [GAConfiguration](javadoc/com/netmillennium/gagrid/parameter/GAConfiguration.html) with our **HelloWorldTerminateCriteria**.

```
//create and set TerminateCriteria
HelloWorldTerminateCriteria termCriteria = new HelloWorldTerminateCriteria(ignite);
gaConfig.setTerminateCriteria(termCriteria);

```

<br/>

<a name="step5">***Evolve the population***</a> <br/><br/>

The final step is to initialize a [GAGrid](javadoc/com/netmillennium/gagrid/parameter/GAGrid.html) instance using our [GAConfiguration](javadoc/com/netmillennium/gagrid/parameter/GAConfiguration.html) and Ignite instances. Then we evolve the population by invoking
[GAGrid.evolve()](javadoc/com/netmillennium/gagrid/parameter/GAGrid.html).

```
// initialize GAGrid
gaGrid = new GAGrid((GAConfiguration)applicationContext.getBean("gaConfiguration"), ignite);
		
// evolve the population
Chromosome fittestChromosome = gaGrid.evolve();
```
<br/>

<a name="startgagrid">***How to run Helloworld GA***</a> <br/><br/>
 


- Copy the **gagrid-services-helloworld-<version>.jar*** to **IGNITE_HOME\libs**


To run this example, begin by starting a few Apache Ignite nodes. 


Open the command shell and type the following: 


***Run using command shell:***
```
$ bin/ignite.sh -v

```
***Run using Docker:***
```
$ docker run -e IGNITE_QUIET=false netmille/ignite-gagrid:2.15.0_0.1.0-beta

```

 > NOTE: Ignite is started in verbose mode to provide insight of **GA Grid's** distributive internal operations. In verbose (ie: -v) mode, Ignite writes all the logging information, both on the console and into the files.  This mode is not recommended to use in a production environment.
 

Repeat this step for the number nodes you desire in your cluster.


Next, go to **gagrid-app-helloworld** folder 

**To Build:** <br/>
```
mvn clean package

```

**Run Using Java:**<br/>
```
java -DIGNITE_QUIET=false -jar target/gagrid-app-helloworld-<version>.jar

```

**Run Using Docker:**<br/>
```
docker run -e IGNITE_QUIET=false netmille/gagrid-app-helloworld 

```


Upon startup, you should see the following similar output on all nodes in the topology:

```
[21:46:01,750][INFO][sys-#64][GridCacheProcessor] Started cache [name=populationCache, id=-155200459, dataRegionName=default, mode=PARTITIONED, atomicity=TRANSACTIONAL, backups=1, mvcc=false]
[21:46:01,750][INFO][exchange-worker-#57][GridCacheProcessor] Started cache [name=geneCache, id=243534189, dataRegionName=default, mode=REPLICATED, atomicity=ATOMIC, backups=2147483647, mvcc=false]
```

Next, You will see the following output on all nodes in your cluster:

```
[08:47:50,496][INFO][pub-#21][FitnessJob] event=fitnessEvaluation|fitnessScore=5.0
[08:47:50,496][INFO][pub-#76][FitnessJob] event=fitnessEvaluation|fitnessScore=7.0
[19:21:53,416][INFO][pub-#101][CrossOverJob] event=crossoverEvaluation|result=success
[19:21:53,417][INFO][pub-#96][CrossOverJob] event=crossoverEvaluation|result=success
[19:21:53,423][INFO][pub-#101][MutateJob] event=mutationEvaluation|result=success
[19:21:53,424][INFO][pub-#96][MutateJob] event=mutationEvaluation|result=success

```

> NOTE: By setting -DIGNITE_QUIET=false, you will see distributive internal operations (ie: Fitness, Crossover, and Mutation) performed by GA Grid on cluster.


Next, You will see the following output after some number of generations:


```
2022-03-04 21:33:02.785  INFO 18356 --- [           main]                                          : ##########################################################################################
2022-03-04 21:33:02.785  INFO 18356 --- [           main]                                          : Generation: 655
2022-03-04 21:33:02.785  INFO 18356 --- [           main]                                          : Fittest is Chromosome Key: Chromosome [fitnessScore=11.0, id=459, genes=[8, 5, 12, 12, 15, 27, 23, 15, 18, 12, 4]]
2022-03-04 21:33:02.785  INFO 18356 --- [           main]                                          : Chromosome: Chromosome [fitnessScore=11.0, id=459, genes=[8, 5, 12, 12, 15, 27, 23, 15, 18, 12, 4]]
2022-03-04 21:33:02.785  INFO 18356 --- [           main]                                          : HELLO WORLD
2022-03-04 21:33:02.785  INFO 18356 --- [           main]                                          : Avg Chromosome Fitness: 5.76
2022-03-04 21:33:02.785  INFO 18356 --- [           main]                                          : #############################################################################################
```
<br/>

<a name="examples">***Examples***</a> <br/><br/>
 
All examples are Spring Boot applications that can be accessed on Github and Docker:


| Name | Description | Github | Docker|
| ------ | ------ | ------| ------| 
| Helloworld | GA produces the phrase 'Hello World'   | <a href="https://github.com/netmille/gagrid-app-helloworld" target="_blank">gagrid-app-helloworld</a>, <a href="https://github.com/netmille/gagrid-services-helloworld" target="_blank">gagrid-services-helloworld</a>| docker run -e IGNITE_QUIET=false netmille/gagrid-app-helloworld:0.1.0-beta <br/><br/>Or<br/> <br/> docker compose -f helloworld.yml up   |
| Optimize Change | GA calculates minimun number of coins to produce exact change less than $1.00   |  <a href="https://github.com/netmille/gagrid-app-optchange" target="_blank">gagrid-app-optchange</a>, <a href="https://github.com/netmille/gagrid-services-optchange" target="_blank">gagrid-services-optchange</a>|  docker run -e IGNITE_QUIET=false netmille/gagrid-app-optchange:0.1.0-beta  <br/><br/>Or<br/> <br/>  docker compose -f optchange.yml up |
| Movie Recommendation  | GA recommends a set of movies based on genre       |   <a href="https://github.com/netmille/gagrid-app-movie" target="_blank">gagrid-app-movie</a>, <a href="https://github.com/netmille/gagrid-services-movie" target="_blank">gagrid-services-movie</a>     | docker compose -f gagrid-app-movie.yml up     |
> NOTE: In in the above examples using Docker, you my decide to scale the number of 'n' worker nodes by adding --scale option: <br/> 
	 docker compose -f fileName up --scale ignite-gagrid-service=n

Please see examples for additional help on using **GA Grid**. 


### <a name="glossary">Glossary</a> <br/>

**Chromosome** is a sequence of Genes. A **Chromosome** represents a potential solution.

**Crossover** is the process in which the genes within chromosomes are combined to derive new chromosomes.

**Fitness Score** is a numerical score that measures the value of a particular Chromosome (ie: solution) relative to other Chromosome in the population.

**Gene** is the discrete building blocks that make up the Chromosome.

**Genetic Algorithm (GA)** is a method of solving optimization problems by simulating the process of biological evolution. A **GA** continuously enhances a population of potential solutions.  With each iteration, a **GA** selects the *'best fit'* individuals from the current population  to create offspring for the next generation. After subsequent generations, a **GA** will "evolve" the population toward an optimal solution.

**Mutation** is the process where genes within a chromosomes are randomly updated to produce new characteristics.

**Population** is the collection of potential solutions or Chromosomes.

**Selection** is the process of choosing candidate solutions (Chromosomes) for the next generation.
