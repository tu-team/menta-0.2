package menta.model.howto

import menta.model.container.KnowledgeBoolean

/**
 * Created by IntelliJ IDEA.
 * User: GabbasovB
 * Date: 25.11.11
 * Time: 15:08
 * To change this template use File | Settings | File Templates.
 */

class BooleanLiteral(_value: KnowledgeBoolean) extends Terminal(_value) {
  def this(value: Boolean) = this (new KnowledgeBoolean(value))

  def this() = this (false)

  def valueBoolean: Boolean = _value.contents

  override def toString = _value.contents.toString

  override def clone(): AnyRef = {
    var res = new BooleanLiteral()
    super.mClone(res)
    res.value = this.value.clone().asInstanceOf[KnowledgeBoolean];
    res
  }
}