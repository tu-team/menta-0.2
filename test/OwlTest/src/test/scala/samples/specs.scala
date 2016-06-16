package samples

import org.junit.runner.RunWith
import org.specs._
import org.specs.runner.{JUnitSuiteRunner, JUnit}
import org.slf4j.LoggerFactory
import java.io.File
import owlimtest.GettingStarted
import java.net.URL
import org.openrdf.model.URI
import org.openrdf.repository.{RepositoryResult, Repository, RepositoryConnection}
import org.openrdf.model.Statement

//import org.scalacheck.Gen

/**
 * Sample specification.
 *
 * This specification can be executed with: scala -cp <your classpath=""> ${package}.SpecsTest
 * Or using maven: mvn test
 *
 * For more information on how to write or run specifications, please visit: http://code.google.com/p/specs.
 *
 */
@RunWith(classOf[JUnitSuiteRunner])
class MySpecTest extends Specification with JUnit {

  val REPOSITORY_DIR: String = "./repositories"
  val PARAM_CONFIG: String = "config"
  val PARAM_SHOWRESULTS: String = "showresults"
  val PARAM_UPDATES: String = "updates"
  val PARAM_PRELOAD: String = "preload"
  val PARAM_QUERY: String = "queryfile"

  val LOG = LoggerFactory.getLogger(this.getClass)
  // using getting started config
  val configFileAddress = "./src/main/resources/owlim.ttl"
  val owlTestFile: File = new File("/src/main/resources/storage.test.0.2.owl")
  val url: URL = new URL("http://menta.googlecode.com/hg/design-specification/data-model/owl/model_v.0.2.owl?r=8c41f462bbf6f41dbdf417a0ff9dfdc8eb753d6d")
  val storageDir: File = new File("./storage")

  var repository: Repository = null
  var repositoryConnection: RepositoryConnection = null


  "OWL test" should {
    "run" in {
      // Special handling for JAXP XML parser that limits entity expansion
      // see http://java.sun.com/j2se/1.5.0/docs/guide/xml/jaxp/JAXP-Compatibility_150.html#JAXP_security
      System.setProperty("entityExpansionLimit", "1000000")
      // Parse all the parameters
      var params: GettingStarted.Parameters = new GettingStarted.Parameters(args)
      // Set default values for missing parameters
      params.setDefaultValue(PARAM_CONFIG, "./src/main/resources/owlim.ttl")
      params.setDefaultValue(PARAM_SHOWRESULTS, "false")
      params.setDefaultValue(PARAM_UPDATES, "false")
      params.setDefaultValue(PARAM_PRELOAD, "./src/main/resources/preload")
      params.setDefaultValue(PARAM_QUERY, "./queries/sample.sparql")
      var gettingStartedApplication: GettingStarted = null
      try {
        var initializationStart: Long = System.currentTimeMillis
        gettingStartedApplication = new GettingStarted(params.getParameters)
        var before = System.nanoTime
        if (!(new File(REPOSITORY_DIR)).exists) {
          gettingStartedApplication.loadFiles
        }
        var after = System.nanoTime
        // gettingStartedApplication.showInitializationStatistics(System.currentTimeMillis - initializationStart)
        /* gettingStartedApplication.iterateNamespaces
        if ("true".equals(params.getValue(PARAM_UPDATES).asInstanceOf[String])) {
          gettingStartedApplication.uploadAndDeleteStatement
        } */
        LOG.info("Initialized")
        LOG.info("loaded files in: {} ns", after - before)

        // getting all entities from KB
        val baseURI: String = url.toString();
        val queryText: String =
          "prefix super: <" + "http://www.semanticweb.org/ontologies/2010/6/Ontology1280399737080.owl#" + ">\n" +
            "prefix owl: <http://www.w3.org/2002/07/owl#>\n" +
            "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
            "SELECT * \n" +
            "{ ?x ?y ?z }";
        before = System.nanoTime
        gettingStartedApplication.executeSingleQuery(queryText)
        after = System.nanoTime
        LOG.info("selected all entities in {} ns", after - before)

        repository = gettingStartedApplication.repository
        repositoryConnection = gettingStartedApplication.repositoryConnection
        // select 10 entity one by one.

        var subjList = List[String]("http://menta.org/ontologies/v.0.2#HowTo.Action.Add.Module", "http://menta.org/ontologies/v.0.2#HowTo.Action.Add.Scope",
          "http://menta.org/ontologies/v.0.2#HowTo.Action.Add.Project",
          "http://menta.org/ontologies/v.0.2#HowTo.Action.Add.Method", "http://menta.org/ontologies/v.0.2#HowTo.Action.Add.MethodAction.Operator.Negation",
          "http://menta.org/ontologies/v.0.2#SolutionGeneratorGroup", "http://menta.org/ontologies/v.0.2#SolutonCheckerGroup", "http://menta.org/ontologies/v.0.2#Rule",
          "http://menta.org/ontologies/v.0.2#Sequence", "http://menta.org/ontologies/v.0.2#SequenceElement")
        before = System.nanoTime
        for (val subj <- subjList) {
          var subjURI: URI = repository.getValueFactory.createURI(subj)

          var iter: RepositoryResult[Statement] = repositoryConnection.getStatements(subjURI, null, null, true)

        }
        after = System.nanoTime

        LOG.info("select 10 entities in {} ns", after - before)
      }
      catch {
        case ex: Throwable => {
          System.out.println("An exception occured at some point during execution:")
          ex.printStackTrace
        }
      }
      finally {
        if (gettingStartedApplication != null) gettingStartedApplication.shutdown
      }
    }
  }
}

object MySpecMain {
  def main(args: Array[String]) {
    new MySpecTest().main(args)
  }
}
