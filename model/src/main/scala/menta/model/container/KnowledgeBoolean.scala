package menta.model.container

import menta.model.Knowledge

/**
 * Created by IntelliJ IDEA.
 * User: GabbasovB
 * Date: 25.11.11
 * Time: 15:09
 * To change this template use File | Settings | File Templates.
 */

class KnowledgeBoolean(aContents: Boolean) extends Knowledge {
  def this() = this (false)

  private var theContents = aContents

  def contents = theContents

  def contents_=(aVal: Boolean) = theContents = aVal

  override def clone(): AnyRef = {
    return new KnowledgeBoolean(contents)
  }
}