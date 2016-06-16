package menta.model.howto

import menta.model.container.KnowledgeNumber
/**
 * @author: talanovm
 * Date: 07.01.11
 * Time: 16:47
 */

class NumberLiteral(_value: KnowledgeNumber) extends Terminal(_value) {
  def this(number: Float) = this (new KnowledgeNumber(number))

  def this() = this (0.0F)

  def valueNumber: Float = _value.contents

  override def toString = _value.contents.toString

  override def clone(): AnyRef = {
    var res = new NumberLiteral()
    super.mClone(res)
    res.value = this.value.clone().asInstanceOf[KnowledgeNumber];
    res

  }
}