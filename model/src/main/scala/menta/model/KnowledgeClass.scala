package menta.model

import java.net.URI
import reflect.BeanInfo
import org.hypergraphdb.HGQuery.hg
import org.hypergraphdb.{HGValueLink, HGHandle, HyperGraph}
import scala.collection.JavaConversions._

/**
 * Stores the Class(template) information.
 * @author: talanovm
 * Date: 07.01.11
 * Time: 14:07
 */

class KnowledgeClass extends Knowledge {
  var individuals: Set[KnowledgeIndividual] = Set[KnowledgeIndividual]()
  var superClasses: Set[KnowledgeClass] = Set[KnowledgeClass]()
  var subClasses: Set[KnowledgeClass] = Set[KnowledgeClass]()
  var properties: propertyType = Map[URI, KnowledgeClass]()

  val propLink="properties"

  override def loadChildren(graph: HyperGraph, handle: HGHandle) = {
    super.loadChildren(graph,handle)
    var lnks = hg.getAll[HGValueLink](graph, hg.and(hg.incident(handle), hg.eq("value", propLink)))

    var lnk: HGValueLink = null

    if (lnks.length > 0) {
      lnk = lnks.head
    }

    if (lnk != null) {
      var childs = hg.getAll[Knowledge](graph, hg.target(graph.getHandle(lnk)))
      childs.foreach(g => {
        properties(g.uri) = g
      })
    }

    null

  }
 /**
   * save all children connected to this object
   * @param graph current instance of HGDB (suggest to use local)
   * @param handle handle of parent instance
   */
  override def saveChildren(graph: HyperGraph, handle: HGHandle) = {
    super.saveChildren(graph,handle)

    var lnks = hg.getAll[HGValueLink](graph, hg.and(hg.incident(handle), hg.eq("value", propLink)))

    var lnk: HGValueLink = null

    if (lnks.length > 0) {
      lnk = lnks.head
    }

    if (lnk != null)
      graph.remove(graph.getHandle(lnk))

    //format array
    var lst = List(handle)
    //suppose all are stored in HGDB
    properties.foreach(vr => {
      lst ++= List(graph.getHandle(vr._2))
    })
    lnk = new HGValueLink(propLink, lst: _*)
    graph.add(lnk)
  }
}