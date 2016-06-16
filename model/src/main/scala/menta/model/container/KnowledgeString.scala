package menta.model.container

import menta.model.Knowledge
import java.net.URI

/**
 * @author: talanovm
 * Date: 07.01.11
 * Time: 16:43
 */

class KnowledgeString(_contents: String) extends Knowledge {
  def this()=this(null)

  private var theContents = this._contents

  def contents=theContents

  def contents_=(aVal: String) = {theContents = aVal}

  override def clone():AnyRef=
  {
    return new KnowledgeString(contents)
  }

}