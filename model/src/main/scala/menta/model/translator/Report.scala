package menta.model.translator

import menta.model.Knowledge
import org.hypergraphdb.{HGHandle, HyperGraph}

/**
 * Parent of the reports.
 * @author: talanovm
 * Date: 07.01.11
 * Time: 18:32
 */

abstract class Report(constContents: Knowledge) extends Knowledge {

  def this() = this(null)

  private var theContents = constContents

  def contents = theContents
  def contents_(aContents: Knowledge) = theContents = aContents

  override def loadChildren(graph: HyperGraph, handle: HGHandle) = null

  override def saveChildren(graph: HyperGraph, handle: HGHandle) = null
}