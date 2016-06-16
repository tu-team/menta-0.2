package menta.model.howto

import menta.model.Knowledge
import reflect.BeanInfo
import org.hypergraphdb.HGQuery.hg
import org.hypergraphdb.{HGValueLink, HGHandle, HyperGraph}
import scala.collection.JavaConversions._
import java.net.URI

/**
 * The parent for all constant literal.
 * @author: talanovm
 * Date: 07.01.11
 * Time: 16:33
 */


@BeanInfo
class Terminal(aValue: Knowledge, aUri: URI) extends HowTo {
  def this() = this (null, null)

  def this(aValue: Knowledge) = this (aValue, null)

  uri = aUri

  override def clone():AnyRef=
  {
    var res = new Terminal();
    this.mClone(res);
    res
  }

  override def mClone(aVal:Knowledge) =
  {
      val target= aVal.asInstanceOf[Terminal]
      super.mClone(target)
      target.value=value.clone().asInstanceOf[Knowledge];
      target;
  }

  def this(nm: String, ov: Boolean) = {
    this ()
    name = nm
  }



  override def loadChildren(graph: HyperGraph, handle: HGHandle) = {
    super.loadChildren(graph, handle);
    var lnks = hg.getAll[HGValueLink](graph, hg.and(hg.incident(handle), hg.eq("value", "TerminalValue")))

    var lnk: HGValueLink = null

    if (lnks.length > 0) {
      lnk = lnks.head
    }

    if (lnk != null) {
      var child = hg.findOne[Knowledge](graph, hg.target(graph.getHandle(lnk)))
      if (child != null) {
        this.value = child;
      }
    }

    null

  }

  override def getAllChildren(): List[Knowledge] = {
    var lst = super.getAllChildren()
    if (value == null) lst

    if (lst == Nil) {
      lst = List(value)
    }
    else {
      lst = lst ::: List(value)
    }

    lst
  }

  /**
   * save all children connected to this object
   * @param graph current instance of HGDB (suggest to use local)
   * @param handle handle of parent instance
   */
  override def saveChildren(graph: HyperGraph, handle: HGHandle) = {
    super.saveChildren(graph, handle);
    var lnks = hg.getAll[HGValueLink](graph, hg.and(hg.incident(handle), hg.eq("value", "TerminalValue")))

    var lnk: HGValueLink = null

    if (lnks.length > 0) {
      lnk = lnks.head
    }

    if (lnk != null)
      graph.remove(graph.getHandle(lnk))

    if (value != null) {
      //format array
      var lst = List(handle)
      var hdl = graph.getHandleFactory.makeHandle(value.extractHandle())
      lst ++= List(hdl)

      lnk = new HGValueLink("TerminalValue", lst: _*)
      graph.add(lnk)
    }
  }

  var theValue: Knowledge = aValue

  def value = theValue

  def value_=(aValue: Knowledge) {
    theValue = aValue
  }

}