package menta.model

import java.net.URI
import org.hypergraphdb.{HGHandle, HyperGraph}

/**
 * Class that stores instances of the KnowledgeClasses.
 * @author: talanovm
 * Date: 07.01.11
 * Time: 15:31
 */

class KnowledgeIndividual extends Knowledge {
  var types: Set[KnowledgeClass] = Set[KnowledgeClass]()
  var properties: Map[URI, KnowledgeIndividual] = Map[URI, KnowledgeIndividual]()

  override def loadChildren(graph: HyperGraph, handle: HGHandle) = null

  override  def saveChildren(graph: HyperGraph, handle: HGHandle) = null
}