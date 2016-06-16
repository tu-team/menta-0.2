package menta.model.howto

import menta.model.Knowledge
import org.hypergraphdb.HGQuery.hg
import scala.collection.JavaConversions._
import org.hypergraphdb.{HGValueLink, HGHandle, HyperGraph}

/**
 * @author: talanovm
 * Date: 07.01.11
 * Time: 17:56
 */

class Solution(aHowTos: List[HowTo]) extends Knowledge {
  def this() = this (List[HowTo]())

  private var theHowTos = aHowTos

  private var howTosLink = "theSolutionHowTos";

  override def loadChildren(graph: HyperGraph, handle: HGHandle) = {
    var lnks = hg.getAll[HGValueLink](graph, hg.and(hg.incident(handle), hg.eq("value", howTosLink)))

    var lnk: HGValueLink = null

    if (lnks.length > 0) {
      lnk = lnks.head
    }

    if (lnk != null) {
      var children = hg.getAll[Knowledge](graph, hg.target(graph.getHandle(lnk)))
      theHowTos=children.map(c=>c.asInstanceOf[HowTo]).toList
    }

    null
  }

  override def saveChildren(graph: HyperGraph, handle: HGHandle) = {
    var lnks = hg.getAll[HGValueLink](graph, hg.and(hg.incident(handle), hg.eq("value", howTosLink)))

    var lnk: HGValueLink = null

    if (lnks.length > 0) {
      lnk = lnks.head
    }

    if (lnk != null)
      graph.remove(graph.getHandle(lnk))

    //format array
    var lst = List(handle)
    //suppose all are stored in HGDB
    theHowTos.foreach(vr => {
      lst ++= List(graph.getHandle(vr))
    })
    lnk = new HGValueLink(howTosLink, lst: _*)
    graph.add(lnk)
  }

   override def getAllChildren(): List[Knowledge] = {
    var lst = super.getAllChildren()
    if (lst == Nil) {
      lst = theHowTos
    }
    else {
      lst = lst ::: theHowTos
    }

    lst
  }

  def howTos = theHowTos

  def howTos_=(aVal: List[HowTo]) = theHowTos = aVal

  override def toString(): String = {
    var res = ""
    if (this.uri != null) {
      res += this.uri.toString
    } else {
      res += "Solution"
    }
    res += " \n["
    if (this.howTos != null) {
      howTos.foreach(b => {
        if (b != null) {
          res += renderIndividual("\t", b.asInstanceOf[ActionIndividual]) + "\n;"
        } else {
          res += "\t null \n;"
        }
      })
    }
    res += "\n];\n"
    res
  }

  private def renderIndividual(tabPrefix: String, ind: ActionIndividual): String = {
    val name = if (ind.name != null) {
      ind.name + "."
    } else {
      ""
    }
    var res = "";
    if (ind.actionClass == null && ind.parameters == null) {
      res = " ActionIndividual, unknown \n"; //+ Constant.modelNamespaceString + "AddClass -> unknown" + super.toString + " " + name + " " + "[]\n{"
    } else {
      res = " ActionIndividual," + ind.actionClass.name + ", " + ind.actionClass.uri + "\n"
    }
    if (ind.parameters != null) {
      res += tabPrefix + "{\n";
      var newTabPrefix = tabPrefix + "\t";
      ind.parameters.foreach(b => {

        if (b != null && b.isInstanceOf[ActionIndividual]) {
          res += tabPrefix + "\t[" + renderIndividual(newTabPrefix, b.asInstanceOf[ActionIndividual]) + "];\n";
        }
        else {
          res += tabPrefix + "\t[" + b.toString + "];\n";
        }

      });
      res += tabPrefix + "}\n";
    }
    res
  }
}
