package menta.model.util.sequence

import menta.model.howto.HowTo
import menta.model.Knowledge
import org.hypergraphdb.{HGHandle, HyperGraph}

/**
 * @author talanovm
 * Date: 10.01.11
 * Time: 17:26
 */

class SequenceElement(next: SequenceElement, contents: HowTo) extends Knowledge
{
  def this()=this(null,null)

  override  def loadChildren(graph: HyperGraph, handle: HGHandle) = null

  override  def saveChildren(graph: HyperGraph, handle: HGHandle) = null
}