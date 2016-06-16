package menta.knowledgebaseserver.dao

import org.junit.Test
import org.slf4j.LoggerFactory
import org.hypergraphdb.HyperGraph
import java.net.URI
import menta.model.howto.{Terminal, HowTo}

/**
 * @author toscheva
 * Date: 08.08.11
 * Time: 19:21
 */

class HyperGraphTest {

  val log = LoggerFactory.getLogger(this.getClass)

  @Test
  def storeAndLoadNewModel() {
    val databaseLocation = "/hyper";
    var graph: HyperGraph = null;
    // ...
    try {
      val trgt = new Terminal("terminal", true)
      trgt.uri = new URI("menta.org/testHGDB")
      graph = new HyperGraph(databaseLocation);
      //load ontology
      val start: Long = System.nanoTime
      var hdl = graph.add(trgt)

      var obj = graph.get[Terminal](hdl)


      //hypergrpahDB.HGDBHelper.createObjects(graph)
      //hypergrpahDB.HGDBHelper.queryAll(graph)
      //select ontology
      log.info("createObjects and queryAll is done in {}", System.nanoTime - start)
    }
    catch {
      case t: Throwable => t.printStackTrace();
      log.error("Please check that you are put all dlls into system32")
    }
    finally {
      graph.close();
    }
  }
}