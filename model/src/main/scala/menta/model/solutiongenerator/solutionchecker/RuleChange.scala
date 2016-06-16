package menta.model.solutiongenerator.solutionchecker

import menta.model.howto.HowTo

/**
 * Set of HowTo-s to change the Rule-s.
 * @author: talanovm
 * Date: 07.01.11
 * Time: 19:39
 */

class RuleChange( howTo: HowTo) extends Rule(howTo) {
  def this() = this(null)
}