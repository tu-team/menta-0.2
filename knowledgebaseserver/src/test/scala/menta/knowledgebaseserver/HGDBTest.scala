package menta.knowledgebaseserver

import org.junit._
import java.net.URI
import org.slf4j.LoggerFactory
import org.hypergraphdb.HGQuery.hg
import org.hypergraphdb.handle.UUIDPersistentHandle
import org.hypergraphdb.query.{AtomPartCondition, And, HGQueryCondition, AtomTypeCondition}
import scala.collection.JavaConversions._
import java.lang.reflect._
import org.hypergraphdb._
import menta.model.howto.{AddClass, ActionClass, Terminal, HowTo}
import menta.model.Knowledge
;

/**
 * @author toscheva
 * Date: 24.08.11
 * Time: 18:47
 */
@Test
class HGDBTest
{
  val databaseLocation = "D:/hyper";
  val LOG = LoggerFactory.getLogger(this.getClass)

  var graph:HyperGraph= null

  @Test
  def storeScalaClassesWithChilds() = {
    try {
      graph = HGEnvironment.get(databaseLocation);

      val trgt = new Terminal
      val act = new AddClass()

      //setup handlers
      val custHndl = graph.getHandleFactory.makeHandle
      graph.getTypeSystem.addPredefinedType(custHndl, trgt, trgt.getClass)

      val actionClass = graph.getHandleFactory.makeHandle()
      graph.getTypeSystem.addPredefinedType(actionClass,act,act.getClass)

      //create values
      trgt.uri = new URI("menta.org/testHGDB")
      trgt.name = "testTerminal"


      act.uri=  new URI("menta.org/testHGDBComposite")
      act.variables_( Map(new URI("menta.org/testHGDB")->trgt))

      var hdl = graph.add(trgt)
      var prt = graph.add(act)

      //save children
      act.saveChildren(graph,prt)

      var actFromDB = graph.get[AddClass](prt)
      actFromDB.loadChildren(graph,prt)

      assert(actFromDB.variables.count(p=>p._1== new URI("menta.org/testHGDB"))>0)

      LOG.info("childs load successfully")
    }
    catch {
      case t: Throwable => t.printStackTrace();

    }
    finally {
      graph.close();
    }
  }
}