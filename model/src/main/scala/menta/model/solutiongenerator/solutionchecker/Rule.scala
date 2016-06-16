package menta.model.solutiongenerator.solutionchecker

import menta.model.Knowledge
import org.hypergraphdb.HGQuery.hg
import org.hypergraphdb.{HGValueLink, HGHandle, HyperGraph}
import menta.model.howto.{ActionClass, HowTo}
import scala.collection.JavaConversions._

/**
 * Parent of all probabilistic rules in the system.
 * @author: talanovm
 * Date: 07.01.11
 * Time: 19:36
 */

class Rule(aHowTo: HowTo) extends Knowledge {

  private var theHowTo: HowTo = aHowTo;

  private var theRuleName:String=null;

  val howToTag = "linkedHowTo"

  override def loadChildren(graph: HyperGraph, handle: HGHandle) = {
    //load action class
    val howTo = hg.getAll[HGValueLink](graph, hg.and(hg.incident(handle), hg.eq("value", howToTag)))
    if (howTo.length > 0) {
      var lnkHowTo = howTo.head;
      var child = hg.getAll[HowTo](graph, hg.target(graph.getHandle(lnkHowTo)))
      if (child.length > 0) {
        theHowTo = child.head
      }
    }
  }

  override def saveChildren(graph: HyperGraph, handle: HGHandle) = {
    //action class
    var acLink = hg.getAll[HGValueLink](graph, hg.and(hg.incident(handle), hg.eq("value", howToTag)))
    if (acLink.length > 0) {
      graph.remove(graph.getHandle(acLink.head))
    }
    if (theHowTo != null) {
      var newLink = new HGValueLink(howToTag, List(handle, graph.getHandle(theHowTo)): _*)
      graph.add(newLink)
    }
  }

  def howTo = theHowTo

  def howTo_=(aHowTo: HowTo) = theHowTo = aHowTo

  def ruleName = theRuleName

  def ruleName_=(aRuleName: String) = theRuleName = aRuleName

  def this() = this (null)

  override def toString(): String = {
    var res = ""
    if (this.uri != null) {
      res += this.uri.toString
    } else {
      res += "Rule"
    }
    res += " \n["
    if (this.howTo != null) {
      res += this.howTo
    }
    res += "];\n"
    res
  }

}