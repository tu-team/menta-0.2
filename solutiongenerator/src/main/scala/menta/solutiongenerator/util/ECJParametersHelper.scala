package menta.solutiongenerator.util

import org.slf4j.{LoggerFactory, Logger}

/**
 * Created by IntelliJ IDEA.
 * User: toscheva
 * Date: 26.10.11
 * Time: 18:25
 * To change this template use File | Settings | File Templates.
 */

object ECJParametersHelper {

  val log: Logger = LoggerFactory.getLogger(this.getClass);

  /*
   populate
  */
  def loadDefaultParameters(): collection.mutable.Map[String, String] = {
    var parameters = collection.mutable.Map[String, String]();

    //ecj.default.params
    parameters("verbosity") = "0";
    generateThreads(parameters);
    //check point setup block
    parameters("checkpoint") = "false"; //disable writing checkpoints
    parameters("checkpoint-modulo") = "1";
    parameters("prefix") = "menta.solutiongenerator.ecj.";


    //ecj.basic.params
    parameters("state") = "ec.simple.SimpleEvolutionState";
    parameters("init") = "ec.simple.SimpleInitializer";
    parameters("finish") = "ec.simple.SimpleFinisher";
    parameters("exch") = "ec.simple.SimpleExchanger";
    parameters("breed") = "ec.simple.SimpleBreeder";
    parameters("eval") = "ec.simple.SimpleEvaluator";
    parameters("stat") = "ec.gp.koza.KozaStatistics";
    parameters("generations") = "500";
    parameters("quit-on-run-complete") = "true";
    parameters("pop") = "ec.Population";
    parameters("pop.subpops") = "1";
    parameters("pop.subpop.0") = "ec.Subpopulation";
    parameters("pop.subpop.0.size") = "200";
    parameters("pop.subpop.0.duplicate-retries") = "0";
    parameters("stat.file") = "$out.stat";

    //ecj.koza.params

    /*
      #
      # We define the fitness of an individual to use the traditional
      # Koza-style fitness metrics, just to make everyone happy :-)
      #
     */
    //parameters("pop.subpop.0.species.fitness") = "ec.gp.koza.KozaFitness";
    parameters("pop.subpop.0.species.fitness") = "ec.simple.SimpleFitness";
    //ec.simple
    parameters("init") = "ec.gp.GPInitializer";
    parameters("stat") = "menta.solutiongenerator.util.MentaStatistics";
    parameters("pop.subpop.0.species") = "ec.gp.GPSpecies";
    parameters("pop.subpop.0.species.ind") = "ec.gp.GPIndividual";
    /*
      # We retry 100 times for duplicates (this is the lil-gp default)
      # in our subpopulation 0
     */
    parameters("pop.subpop.0.duplicate-retries") = "100";
    parameters("pop.subpop.0.species.ind.numtrees") = "1";
    parameters("pop.subpop.0.species.ind.tree.0") = "ec.gp.GPTree";
    parameters("pop.subpop.0.species.ind.tree.0.tc") = "tc0";
    parameters("pop.subpop.0.species.mutation-prob") = "0.005";
    /*
      # The GPSpecies has 2 pipelines, Crossover and Reproduction,
      # chosen with 0.9 and 0.1 likelihood respectively.
     */
    parameters("pop.subpop.0.species.pipe") = "ec.breed.MultiBreedingPipeline";
    parameters("pop.subpop.0.species.pipe.generate-max") = "false";
    parameters("pop.subpop.0.species.pipe.num-sources") = "2";
    parameters("pop.subpop.0.species.pipe.source.0") = "ec.gp.koza.CrossoverPipeline";
    parameters("pop.subpop.0.species.pipe.source.0.prob") = "0.8";
    parameters("pop.subpop.0.species.pipe.source.1") = "ec.breed.ReproductionPipeline";
    parameters("pop.subpop.0.species.pipe.source.1.prob") = "0.2";


    /*
      # Here we define the default values for Crossover,
      # Reproduction, Mutation, as well as our selection
      # approaches (Koza I).  These can be overridden on a per-species
      # level of course.
     */
    parameters("breed.reproduce.source.0") = "ec.select.TournamentSelection";
    parameters("gp.koza.xover.source.0") = "ec.select.TournamentSelection";
    parameters("gp.koza.xover.source.1") = "same";
    parameters("gp.koza.xover.ns.0") = "ec.gp.koza.KozaNodeSelector";
    parameters("gp.koza.xover.ns.1") = "same";
    parameters("gp.koza.xover.maxdepth") = "17";
    parameters("gp.koza.xover.tries") = "1";

    /*
      # Point Mutation will use Tournament Selection, try only 1
      # time, have a max depth of 17, and use KozaNodeSelector
      # and GROW for building.  Also, Point Mutation uses a GrowBuilder
      # by default, with a default of min-depth=max-depth=5
      # as shown a ways below
     */
    parameters("gp.koza.mutate.source.0") = "ec.select.TournamentSelection";
    parameters("gp.koza.mutate.ns.0") = "ec.gp.koza.KozaNodeSelector";
    parameters("gp.koza.mutate.build.0") = "ec.gp.koza.GrowBuilder";
    parameters("gp.koza.mutate.maxdepth") = "17";
    parameters("gp.koza.mutate.tries") = "1";
    parameters("select.tournament.size") = "7";

    /*
      # Since GROW is only used for subtree mutation, ECJ uses
      # the Koza-standard subtree mutation GROW values for the
      # default for GROW as a whole.  This default is
      # min-depth=max-depth=5, which I don't like very much,
      # but hey, that's the standard.
      # This means that if someone decided to use GROW to generate
      # new individual trees, it's also use the defaults below
      # unless he overrided them locally.
     */
    parameters("gp.koza.grow.min-depth") = "5";
    parameters("gp.koza.grow.max-depth") = "5";
    parameters("eval.problem.stack") = "ec.gp.ADFStack";
    parameters("eval.problem.stack.context") = "ec.gp.ADFContext";


    /*
        #
        # Here we define the default values for KozaNodeSelection;
        # as always, these can be overridden by values hanging off
        # of the Crossover/Reproduction/Mutation/whatever pipelines,
        # like we did for node-building, but hey, whatever.
        # The default is 10% terminals, 90% nonterminals when possible,
        # 0% "always pick the root", 0% "pick any node"
     */
    parameters("gp.koza.ns.terminals") = "0.5";
    parameters("gp.koza.ns.nonterminals") = "0.5";
    parameters("gp.koza.ns.root") = "0.0";

    /*
        # You need to create at least one function set,
        # called "f0", which your first tree will use.
        # You don't need to include the class declaration here,
        # but it quiets warnings.
     */
    parameters("gp.fs.size") = "1";
    parameters("gp.fs.0") = "ec.gp.GPFunctionSet";
    parameters("gp.fs.0.name") = "f0";
    /*
        # Here we define a single atomic type, "nil", which everyone will use.
        # There are no set types defined.
     */
    parameters("gp.type.a.size") = "1";
    parameters("gp.type.a.0.name") = "nil";
    parameters("gp.type.s.size") = "0";


    /*
        # Here we define one GPTreeConstraints object, "tc0",
        # which uses ec.gp.koza.HalfBuilder to create nodes,
        # only allows nodes from the GPFunctionSet "fset",
        # and has the single type "nil" as its tree type.
        # You don't need to include the class declaration here,
        # but it quiets warnings.
     */
    parameters("gp.tc.size") = "1";
    parameters("gp.tc.0") = "ec.gp.GPTreeConstraints";
    parameters("gp.tc.0.name") = "tc0";
    parameters("gp.tc.0.fset") = "f0";
    parameters("gp.tc.0.returns") = "nil";

    /*
        # The tree uses an ec.gp.koza.HalfBuilder to create
        # itself initially.
        # HalfBuilder will pick GROW half the time and FULL
        # the other half, with a ramp from 2 to 6 inclusive.
        # By ramp we mean that it first picks a random number between
        # 2 and 6 inclusive.  This then becomes the *maximum* tree size
        # (for the FULL approach, it's the tree size of the tree, for
        # GROW, the tree can get no bigger than this)
     */
    parameters("gp.tc.0.init") = "ec.gp.koza.HalfBuilder";
    parameters("gp.koza.half.min-depth") = "2";
    parameters("gp.koza.half.max-depth") = "6";
    parameters("gp.koza.half.growp") = "0.5";

    /*
        # Here we define 7 GPNodeConstraints, nc0...nc6, which
        # describe nodes with 0...6 children respectively, which only
        # use a single type, "nil", for their argument and return types
        # You don't need to include the class declarations with everything
        # else below, but it quiets warnings
     */
    val constCount = 7;
    var i: java.lang.Integer = 0;
    parameters("gp.nc.size") = constCount.toString;
    for (i <- 0 to constCount - 1) {
      parameters("gp.nc." + i.toString) = "ec.gp.GPNodeConstraints";
      parameters("gp.nc." + i.toString + ".name") = "nc" + i.toString;
      parameters("gp.nc." + i.toString + ".returns") = "nil";
      parameters("gp.nc." + i.toString + ".size") = i.toString;
      if (i > 0) {
        //initialize children
        var childIndex: java.lang.Integer = 0;
        for (childIndex <- 0 to i - 1) {
          log.info("gp.nc." + i.toString + ".child." + childIndex.toString);
          parameters("gp.nc." + i.toString + ".child." + childIndex.toString) = "nil";
        }
      }


    }

    parameters
  }


  def generateThreads(map: collection.mutable.Map[String, String]) {
    var availibleThreads=systemProcessorsAvailible();

    map("evalthreads") = availibleThreads.toString; //TODO: call to systemProcessorsAvailible when NARS will be not static
    map("breedthreads") = availibleThreads.toString;//"1"; //TODO: call to systemProcessorsAvailible when NARS will be not static
    for (val i <-Range(0,availibleThreads))
    {
      map("seed."+i)=   "time";
    }

  }

  def systemProcessorsAvailible(): Int = {
    Runtime.getRuntime().availableProcessors()
  }
}