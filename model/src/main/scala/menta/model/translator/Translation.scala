package menta.model.translator

import menta.model.Knowledge

/**
 * The class-pair of the input and output  of the translator.
 * @author talanovm
 * Date: 10.01.11
 * Time: 11:53
 */

class Translation(in: Knowledge, out: Report) extends Knowledge {
  def this() = this(null, null)
}