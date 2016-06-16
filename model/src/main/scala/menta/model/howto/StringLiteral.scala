package menta.model.howto

import menta.model.container.KnowledgeString

/**
 * @author: talanovm
 * Date: 07.01.11
 * Time: 16:41
 */

class StringLiteral(aVal: KnowledgeString) extends Terminal(aVal) {

  this.name = menta.model.Constant.stringLiteral

  override def clone():AnyRef=
  {
    var res = new StringLiteral
    super.mClone(res)

    res.value = value.clone().asInstanceOf[KnowledgeString]
    res
  }
  def this(aVal: String) = this(new KnowledgeString(aVal))
  def this()=this("none")
  def valueString :String = if (value!=null) value.asInstanceOf[KnowledgeString].contents else null
  override def toString = if (value!=null) value.asInstanceOf[KnowledgeString].contents else null
}