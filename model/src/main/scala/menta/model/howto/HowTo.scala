package menta.model.howto

import classifier.Classifier
import menta.model.Knowledge
import java.net.URI
import collection.immutable.HashSet
import org.hypergraphdb.HGQuery.hg
import scala.collection.JavaConversions._
import reflect.BeanInfo
import org.hypergraphdb.{HGValueLink, HGHandle, HyperGraph}

/**
 * @author: talanovm
 * Date: 07.01.11
 * Time: 16:25
 */

@BeanInfo
abstract class HowTo(protected var theName: String, private var _revision: String) extends Knowledge {

  @deprecated
  protected var theVariables = Map[URI, Knowledge]()

  /**
   * empty constructor
   */
  def this() = this ("", "")

  def this(aName: String) = this (aName, "")

  override def clone()=super.clone()

  @deprecated
  def variables = theVariables

  @deprecated
  def variables_(aVariables: Map[URI, Knowledge]) = theVariables = aVariables

  def revision: String = _revision

  def revision_=(aRevision: String) {
    this._revision = aRevision
  }

  override def mClone(aVal: Knowledge)=
  {
    val trgt=aVal.asInstanceOf[HowTo]
    super.mClone(aVal);
    trgt.name=name;
    trgt.revision=revision;
    //todo classifiers
  }

  /**
   *  load all children connected to this object
   * @param graph current instance of HGDB (suggest to use local)
   * @param handle handle of parent instance
   */
  override def loadChildren(graph: HyperGraph, handle: HGHandle) = {

    var lnks = hg.getAll[HGValueLink](graph, hg.and(hg.incident(handle), hg.eq("value", "theVariables")))

    var lnk: HGValueLink = null

    if (lnks.length > 0) {
      lnk = lnks.head
    }

    if (lnk != null) {
      var childs = hg.getAll[Knowledge](graph, hg.target(graph.getHandle(lnk)))
      childs.foreach(g => {
        variables(g.uri) = g
      })
    }

    null

  }

  def name = theName

  def name_(aName: String) = theName = aName

  def name_=(aName: String) = theName = aName

  //identify tag to search across collection
  protected var theTag: String = ""

  def tag = theTag

  def tag_(aTag: String) = theTag = aTag

  def tag_=(aTag: String) = theTag = aTag


  protected var theClassifiers: HashSet[Classifier] = HashSet[Classifier]()

  def classifiers = theClassifiers

  def classifiers_(aClassifiers: HashSet[Classifier]) = theClassifiers = aClassifiers

  def classifiers_=(aClassifiers: HashSet[Classifier]) = theClassifiers = aClassifiers

  /**
   * save all children connected to this object
   * @param graph current instance of HGDB (suggest to use local)
   * @param handle handle of parent instance
   */
  override def saveChildren(graph: HyperGraph, handle: HGHandle) = {

    var lnks = hg.getAll[HGValueLink](graph, hg.and(hg.incident(handle), hg.eq("value", "theVariables")))

    var lnk: HGValueLink = null

    if (lnks.length > 0) {
      lnk = lnks.head
    }

    if (lnk != null)
      graph.remove(graph.getHandle(lnk))

    //format array
    var lst = List(handle)
    //suppose all are stored in HGDB
    variables.foreach(vr => {
      lst ++= List(graph.getHandle(vr._2))
    })
    lnk = new HGValueLink("theVariables", lst: _*)
    graph.add(lnk)
  }
}