package menta.model.translator

import menta.model.container.KnowledgePatch

/**
 * Patch(Code mainly) Export report.
 * @author talanovm
 * Date: 10.01.11
 * Time: 11:56
 */

class Code(contents: KnowledgePatch) extends Report(contents) {
  def this() = this(new KnowledgePatch())
}