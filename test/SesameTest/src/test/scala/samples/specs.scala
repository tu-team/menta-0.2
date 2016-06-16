package samples

import org.junit.runner.RunWith
import org.specs._
import org.specs.runner.{JUnitSuiteRunner, JUnit}
import java.net.URL
import org.openrdf.repository.{RepositoryResult, RepositoryException, Repository, RepositoryConnection}
import org.openrdf.rio.RDFFormat
import java.io.File
import org.openrdf.model.{Statement, URI}
import org.openrdf.repository.sail.SailRepository
import org.openrdf.sail.nativerdf.NativeStore
import org.openrdf.query.{QueryLanguage, TupleQuery}
import org.slf4j.LoggerFactory

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

  var rep: Repository = null;
  var repConn: RepositoryConnection = null;
  val LOG = LoggerFactory.getLogger(this.getClass)

  val url: URL = new URL("http://menta.googlecode.com/hg/design-specification/data-model/owl/model_v.0.2.owl?r=8c41f462bbf6f41dbdf417a0ff9dfdc8eb753d6d");
  val baseURI: String = url.toString();
  val owlTestFile: File = new File("./src/main/resources/preload/storage.test.0.2.owl")

  "An application" should {
    "initilize" in {
      try {
        var rdfPath: File = new File("./storage.rdf");
        var flag: Boolean = rdfPath.exists;
        rep = new SailRepository(new NativeStore(rdfPath));
        rep.initialize();

        repConn = rep.getConnection();
        var before = System.nanoTime
        if (!flag) {
          repConn.add(owlTestFile, baseURI, RDFFormat.RDFXML);
        }
        var after = System.nanoTime
        LOG.info("Load ontology " + (after - before))
        repConn.commit();

        val queryText: String =
          "prefix super: <" + "http://www.semanticweb.org/ontologies/2010/6/Ontology1280399737080.owl#" + ">\n" +
            "prefix owl: <http://www.w3.org/2002/07/owl#>\n" +
            "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
            "SELECT * \n" +
            "{ ?x ?y ?z }";

        val tupleQuery: TupleQuery = repConn.prepareTupleQuery(QueryLanguage.SPARQL, queryText)
        tupleQuery.setIncludeInferred(false)
        before = System.nanoTime
        tupleQuery.evaluate
        after = System.nanoTime
        LOG.info("selected all entities in {} ns ", (after - before))

        var subjList = List[String]("HowTo.Action.Add.Module",
          "HowTo.Action.Add.Scope",
          "HowTo.Action.Add.Project",
          "HowTo.Action.Add.Method",
          "HowTo.Action.Add.MethodAction.Operator.Negation",
          "SolutionGeneratorGroup",
          "SolutonCheckerGroup",
          "Rule",
          "Sequence",
          "SequenceElement")

        // using SPARQL request.
        val q1 = "prefix super: <" + "http://www.semanticweb.org/ontologies/2010/6/Ontology1280399737080.owl#" + ">\n" +
          "prefix owl: <http://www.w3.org/2002/07/owl#>\n" +
          "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
          "prefix menta: <http://menta.org/ontologies/v.0.2#>\n" +
          "SELECT * \n" +
          "{ menta:"
        val q2 = " ?y ?z }"
        before = System.nanoTime
        for (val subj <- subjList) {
          // LOG.debug(q1 + subj + q2)
          val tupleQuery: TupleQuery = repConn.prepareTupleQuery(QueryLanguage.SPARQL, q1 + subj + q2)
          tupleQuery.setIncludeInferred(false)
          tupleQuery.evaluate
        }
        after = System.nanoTime

        LOG.info("selected 10 entities in {} ns ", (after - before))
      } catch {
        case repEx: RepositoryException => println(repEx.getMessage)
        case ex: Exception => println(ex.getMessage)
      } finally {
        repConn.close()
      }
    }
  }

}

object MySpecMain {
  def main(args: Array[String]) {
    new MySpecTest().main(args)
  }
}
