package menta

import java.net.URL
import org.openrdf.repository.{RepositoryResult, RepositoryException, Repository, RepositoryConnection}
import org.openrdf.rio.RDFFormat
import java.io.File
import org.openrdf.model.{Statement, URI}
import org.openrdf.repository.sail.SailRepository
import org.openrdf.sail.nativerdf.NativeStore
import org.openrdf.query.{QueryLanguage, TupleQuery}

/**
 * @author ${user.name}
 */
object App {

  var rep : Repository = null;
  var repConn : RepositoryConnection = null;

  def main(args : Array[String]) {
    val rdfPath : File = new File("./storage.rdf");

    val url : URL = new URL("http://menta.googlecode.com/hg/design-specification/data-model/owl/model_v.0.2.owl?r=8c41f462bbf6f41dbdf417a0ff9dfdc8eb753d6d");
    val baseURI : String = url.toString();
    val owlTestFile: File = new File("./src/main/resources/preload/storage.test.0.2.owl")


    try {
      rep = new SailRepository(new NativeStore(rdfPath));
      rep.initialize();

      repConn = rep.getConnection();
      var before = System.nanoTime
      repConn.add(owlTestFile, baseURI, RDFFormat.RDFXML);
      var after = System.nanoTime
      println("Load ontology " + (after - before))
      repConn.commit();

      val queryText : String =
        "prefix super: <" + "http://www.semanticweb.org/ontologies/2010/6/Ontology1280399737080.owl#" + ">\n" +
        "prefix owl: <http://www.w3.org/2002/07/owl#>\n" +
        "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
        "SELECT * \n" +
          "{ ?x ?y ?z }";

      val tupleQuery : TupleQuery = repConn.prepareTupleQuery(QueryLanguage.SPARQL, queryText)
      tupleQuery.setIncludeInferred(false)
      before = System.nanoTime
      tupleQuery.evaluate
      after = System.nanoTime
      println("selected all entities in {} ns " + (after - before))

      var subjList = List[String]("http://menta.org/ontologies/v.0.2#HowTo.Action.Add.Module", "http://menta.org/ontologies/v.0.2#HowTo.Action.Add.Scope",
        "http://menta.org/ontologies/v.0.2#HowTo.Action.Add.Project",
        "http://menta.org/ontologies/v.0.2#HowTo.Action.Add.Method", "http://menta.org/ontologies/v.0.2#HowTo.Action.Add.MethodAction.Operator.Negation",
        "http://menta.org/ontologies/v.0.2#SolutionGeneratorGroup", "http://menta.org/ontologies/v.0.2#SolutonCheckerGroup", "http://menta.org/ontologies/v.0.2#Rule",
        "http://menta.org/ontologies/v.0.2#Sequence", "http://menta.org/ontologies/v.0.2#SequenceElement")

      before = System.nanoTime
      for (val subj <- subjList) {
        var subjURI: URI = rep.getValueFactory.createURI(subj)
        var iter: RepositoryResult[Statement] = repConn.getStatements(subjURI, null, null, true)
      }
      after = System.nanoTime

      println("selected 10 entities in {} ns " + (after - before))
    } catch {
      case repEx : RepositoryException => println(repEx.getMessage)
      case ex : Exception => println(ex.getMessage)
    } finally {
      repConn.close()
    }
  }
}