package menta.model.translator

import menta.model.container.KnowledgeXMI

/**
 * The report class for the UML export.
 * @author talanovm
 * Date: 10.01.11
 * Time: 12:02
 */

class UMLReport(contents: KnowledgeXMI) extends Report(contents) {
  def this() = this (new KnowledgeXMI)
}