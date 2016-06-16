package menta.model

import org.hypergraphdb.{HGValueLink, HGHandle, HyperGraph}
import org.hypergraphdb.HGQuery.hg
import java.net.URI

/**
 * Created with IntelliJ IDEA.
 * User: ChepkunovA
 * Date: 19.06.12
 * Time: 19:35
 * To change this template use File | Settings | File Templates.
 */

class Tag (val tagName:String) extends Knowledge{

  var childs:List[Knowledge] = Nil

  //def this(aValue: Knowledge) = this (aValue, null)

  def addLink(v:Knowledge){childs = v :: childs}

  override def loadChildren(graph: HyperGraph, handle: HGHandle) = {
    super.loadChildren(graph, handle);
    val links = hg.getAll[HGValueLink](graph, hg.and(hg.incident(handle), hg.eq("value", "theVariables")))

    if (links.length > 0) {
      childs =  hg.getAll[Knowledge](graph, hg.target(graph.getHandle(links.head)))
    }

    null

  }


  /**
   * save all children connected to this object
   * @param graph current instance of HGDB (suggest to use local)
   * @param handle handle of parent instance
   */
  override def saveChildren(graph: HyperGraph, handle: HGHandle) = {
    val links = hg.getAll[HGValueLink](graph, hg.and(hg.incident(handle), hg.eq("value", "theVariables")))

    if (links.length > 0) {
      graph.remove(graph.getHandle(links.head))
    }

    //format array
    var lst = List(handle)
    //suppose all are stored in HGDB
    childs.foreach(vr => {
      lst ++= List(graph.getHandle(vr._2))
    })
    lnk = new HGValueLink("theVariables", lst: _*)
    graph.add(lnk)
  }

}
