package menta.model.container

import menta.model.Knowledge

/**
 * @author: talanovm
 * Date: 07.01.11
 * Time: 16:46
 */

class KnowledgeNumber(aContents: Float) extends Knowledge {
  def this() = this (0)

  private var theContents = aContents

  def contents = theContents

  def contents_=(aVal: Float) = theContents = aVal

  override def clone(): AnyRef = {
    return new KnowledgeNumber(contents)
  }
}