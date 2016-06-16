package menta.model.howto

import org.hypergraphdb.HGQuery.hg
import org.hypergraphdb.{HGValueLink, HGHandle, HyperGraph}
import menta.model.Knowledge
import scala.collection.JavaConversions._

/**
 * Parent for all the HowTo Classes(templates)
 * @author: talanovm
 * Date: 07.01.11
 * Time: 16:51
 */

abstract class ActionClass(var _superClass: ActionClass, var _parameters: List[HowTo]) extends HowTo() {
  def this() = this (null, List[HowTo]())

  def parameters = _parameters

  def parameters_=(aParameters: List[HowTo]) {
    _parameters = aParameters
  }

  override def mClone(aVal: Knowledge) = {
    super.mClone(aVal)
    //clone parameters
    val casted = aVal.asInstanceOf[ActionClass]
    casted.parameters = parameters.map(b => b.clone().asInstanceOf[HowTo]);
    casted.superClass=superClass.clone().asInstanceOf[ActionClass]
  }


  def superClass = _superClass


  def superClass_=(aSuperClass: ActionClass) {
    _superClass = aSuperClass
  }

  val prmsTag = "childParameters"

  val superClassTag = "superClass"
  /**
   * load all children connected to this object
   * @param graph current instance of HGDB (suggest to use local)
   * @param handle handle of parent instance
   */
  override def loadChildren(graph: HyperGraph, handle: HGHandle) = {

    super.loadChildren(graph, handle)

    var lnks = hg.getAll[HGValueLink](graph, hg.and(hg.incident(handle), hg.eq("value", prmsTag)))

    var lnk: HGValueLink = null

    if (lnks.length > 0) {
      lnk = lnks.head
    }

    if (lnk != null) {
      var children = hg.getAll[HowTo](graph, hg.target(graph.getHandle(lnk)))
      parameters = List[HowTo](children: _*)
    }

    lnks = hg.getAll[HGValueLink](graph, hg.and(hg.incident(handle), hg.eq("value", superClassTag)))


    if (lnks.length > 0) {
      lnk = lnks.head
    }

    if (lnk != null) {
      var children = hg.getAll[ActionClass](graph, hg.target(graph.getHandle(lnk)))
      if (children.length > 0) {
        superClass = children.head;
      }
    }

    null

  }

  override def getAllChildren(): List[Knowledge] = {
    var lst = super.getAllChildren()
    if (lst == Nil) {
      lst = parameters
    }
    else {
      lst = lst ::: parameters
    }

    if (lst == Nil || lst==null) {
      if (superClass != null) {
        lst = List(superClass)
      }
    }
    else {
      if (superClass != null) {
        lst = lst ::: List(superClass)
      }
    }


    lst
  }


  /**
   * save all childs connected to this object
   * @param graph current instance of HGDB (suggest to use local)
   * @param handle handle of parent instance
   */
  override def saveChildren(graph: HyperGraph, handle: HGHandle) = {

    var lnks = hg.getAll[HGValueLink](graph, hg.and(hg.incident(handle), hg.eq("value", prmsTag)))

    var lnk: HGValueLink = null

    if (lnks.length > 0) {
      lnk = lnks.head
    }

    if (lnk != null)
      graph.remove(graph.getHandle(lnk))

    if (parameters != null) {
      //format array
      var lst = List(handle)
      //suppose all are stored in HGDB
      parameters.foreach(vr => {
        var hdl = graph.getHandleFactory.makeHandle(vr.extractHandle())
        lst ++= List(hdl)
      })

      lnk = new HGValueLink(prmsTag, lst: _*)
      graph.add(lnk)
    }

    if (superClass != null) {
      lnks = hg.getAll[HGValueLink](graph, hg.and(hg.incident(handle), hg.eq("value", superClassTag)))
      if (lnks.length > 0) {
        lnk = lnks.head
      }

      if (lnk != null)
        graph.remove(graph.getHandle(lnk))
      var lst = List(handle)
      //suppose all are stored in HGDB

      var hdl = graph.getHandleFactory.makeHandle(superClass.extractHandle())
      lst ++= List(hdl)


      lnk = new HGValueLink(superClassTag, lst: _*)
      graph.add(lnk)

    }

    null
  }
}
