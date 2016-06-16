package menta.model.howto

import org.hypergraphdb.HGQuery.hg
import org.hypergraphdb.{HGValueLink, HGHandle, HyperGraph}
import scala.collection.JavaConversions._
import collection.mutable.Buffer
import menta.model.Knowledge

/**
 * Parent for all HowTo instances/individuals of the ActionClass
 * @author talanovm
 * @see menta.model.howto.ActionClass
 * Date: 07.01.11
 * Time: 17:44
 */

class ActionIndividual(private var _actionClass: ActionClass, private var _parameters: List[HowTo]) extends HowTo {

  def this() = this (new AddClass(), List[HowTo]())

  def getActionClass = this._actionClass

  def actionClass = this._actionClass

  def actionClass_=(aActionClass: ActionClass) {
    this._actionClass = aActionClass
  }

  override def mClone(aVal: Knowledge) = {
    val newObject = aVal.asInstanceOf[ActionIndividual];
    super.mClone(aVal);
    //copy action class
    newObject.actionClass = this.actionClass;
    //copy parameters
    if (parameters != null)
      newObject.parameters = parameters.map(b => if (b != null) b.clone().asInstanceOf[HowTo] else b)

    newObject;
  }

  def parameters = this._parameters

  def parameters_=(aParameters: List[HowTo]) {
    this._parameters = aParameters
  }

  override def getAllChildren(): List[Knowledge] = {
    var lst = super.getAllChildren()
    if (lst == Nil) {
      if (parameters != null)
        lst = parameters
      if (lst != null) {
        lst = lst ::: List(actionClass)

      }
      else {
        lst = List(actionClass);
      }

    }
    else {
      if (parameters != null)
        lst = lst ::: parameters
      if (actionClass != null)
        lst = lst ::: List(actionClass)
    }

    lst
  }

  val prmsTag = "childParameters"

  val actionClassTag = "actionClass"
  /**
   * load all children connected to this object
   * @param graph current instance of HGDB (suggest to use local)
   * @param handle handle of parent instance
   */
  override def loadChildren(graph: HyperGraph, handle: HGHandle) = {

    super.loadChildren(graph, handle)

    val lnks = hg.getAll[HGValueLink](graph, hg.and(hg.incident(handle), hg.eq("value", prmsTag)))

    var lnk: HGValueLink = null

    if (lnks.length > 0) {
      lnk = lnks.head
    }

    if (lnk != null) {
      var children: Buffer[HowTo] = hg.getAll[HowTo](graph, hg.target(graph.getHandle(lnk)))
      _parameters = List[HowTo](children: _*)
    }

    //load action class
    val ac = hg.getAll[HGValueLink](graph, hg.and(hg.incident(handle), hg.eq("value", actionClassTag)))
    if (ac.length > 0) {
      var lnkAC = ac.head;
      var child = hg.getAll[ActionClass](graph, hg.target(graph.getHandle(lnkAC)))
      if (child.length > 0) {
        actionClass = child.head
      }
    }


    null

  }

  /**
   * save all children connected to this object
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

    if (_parameters != null && _parameters.count(p => p != null) > 0) {
      //format array
      var lst = List(handle)
      //suppose all are stored in HGDB
      _parameters.foreach(vr => {
        lst ++= List(graph.getHandle(vr))
      })

      lnk = new HGValueLink(prmsTag, lst: _*)
      graph.add(lnk)
    }

    //action class
    var acLink = hg.getAll[HGValueLink](graph, hg.and(hg.incident(handle), hg.eq("value", actionClassTag)))
    if (acLink.length > 0) {
      graph.remove(graph.getHandle(acLink.head))
    }
    if (actionClass != null) {
      var newLink = new HGValueLink(prmsTag, List(handle, graph.getHandle(actionClass)): _*)
      graph.add(newLink)
    }

    null
  }

}