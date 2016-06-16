package menta.model.container

import menta.model.Knowledge
import menta.model.howto.StringLiteral

/**
 * @author talanov max
 * Date: 10.01.11
 * Time: 11:38
 */

class KnowledgePatch(var filePatchMap: Map[StringLiteral, StringLiteral]) extends Knowledge {
  def this() = this(Map[StringLiteral, StringLiteral]())

  def this(list: List[Pair[StringLiteral, StringLiteral]]) = {
    this ()
    this.filePatchMap = this.filePatchMap ++ list
  }

  def add(filename: String, patch: String): KnowledgePatch = {
    this.filePatchMap += new StringLiteral(filename) -> new StringLiteral(patch)
    this
  }

  override def toString() = {
    val res: String = "Patch: \n" + filePatchMap.toString()
    res
  }
}