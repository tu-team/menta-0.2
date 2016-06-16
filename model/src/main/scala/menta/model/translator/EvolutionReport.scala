package menta.model.translator

import menta.model.solutiongenerator.Evolution

/**
 * Current solution generation report.
 * @author talanovm
 * Date: 10.01.11
 * Time: 11:54
 */

class EvolutionReport(contents: Evolution) extends Report(contents) {
  def this() = this(null)
}