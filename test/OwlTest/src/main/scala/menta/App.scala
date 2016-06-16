package menta

import java.io.File
import org.openrdf.repository.sail.SailRepository
import org.openrdf.sail.nativerdf.NativeStore
//import com.ontotext.trree.owlim_ext.SailImpl
import org.openrdf.sail.Sail
import org.openrdf.repository.{RepositoryException, Repository, RepositoryConnection}
import org.openrdf.rio.{RDFParseException, RDFFormat}
import java.io.IOException
import org.openrdf.query.{TupleQueryResult, QueryLanguage, TupleQuery}
import java.net.URL;


//import org.openrdf.repository.http.HTTPRepository;

/**
 * @author ${user.name}
 */
object App {

  var rep : Repository = null;
  var repConn : RepositoryConnection = null;

  def main(args : Array[String]) {
    val rdfPath : File = new File("C:/Temp/storage.rdf");

    val url : URL = new URL("http://menta.googlecode.com/hg/design-specification/data-model/owl/model_v.0.2.owl?r=8c41f462bbf6f41dbdf417a0ff9dfdc8eb753d6d");
    val baseURI : String = url.toString();

    try {
      rep = new SailRepository(new NativeStore(rdfPath));
      rep.initialize();

      repConn = rep.getConnection();
      repConn.add(url, baseURI, RDFFormat.RDFXML);
      repConn.commit();

      val queryText : String =
        "prefix super: <" + "http://www.semanticweb.org/ontologies/2010/6/Ontology1280399737080.owl#" + ">\n" +
        "prefix owl: <http://www.w3.org/2002/07/owl#>\n" +
        "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
        "SELECT * \n" +
          "{ ?x ?y ?z }";
      val tupleQuery : TupleQuery = repConn.prepareTupleQuery(QueryLanguage.SPARQL, queryText)

      tupleQuery.setIncludeInferred(false)
      val result: TupleQueryResult = tupleQuery.evaluate

      while (result.hasNext) {
        println(result.next)
      }

      println()
    } catch {
      case repEx : RepositoryException => println(repEx.getMessage)
      case ex : Exception => println(ex.getMessage)
    } finally {
      repConn.close()
    }
  }
}