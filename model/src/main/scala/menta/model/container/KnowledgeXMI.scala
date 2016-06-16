package menta.model.container

import xml.Node

/**
 * @author talanovm
 * Date: 10.01.11
 * Time: 11:51
 */

class KnowledgeXMI(contents: Node) extends KnowledgeXML(contents) {
  def this() = this(null)
}