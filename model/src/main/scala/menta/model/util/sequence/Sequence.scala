package menta.model.util.sequence

import menta.model.Knowledge
import org.hypergraphdb.{HGHandle, HyperGraph}

/**
 * Container for all sequences in KB, usually should be translated in List.
 * @author talanovm
 * Date: 10.01.11
 * Time: 17:24
 */

class Sequence(fist: SequenceElement) extends Knowledge
{
  def this()=this(null)

  override def loadChildren(graph: HyperGraph, handle: HGHandle) = null

  override def  saveChildren(graph: HyperGraph, handle: HGHandle) = null
}