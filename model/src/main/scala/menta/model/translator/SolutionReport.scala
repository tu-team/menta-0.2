package menta.model.translator

import menta.model.howto.Solution

/**
 * The report of the solution.
 * @author: talanovm
 * Date: 07.01.11
 * Time: 18:36
 */

class SolutionReport(contents: Solution) extends Report(contents) {
  def this() = this(new Solution())
}