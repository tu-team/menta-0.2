package menta.model.learner

import menta.model.howto.{HowTo, Solution}
import menta.model.solutiongenerator.solutionchecker.Rule
import collection.immutable.HashSet

/**
 * @author talanovm
 * Date: 25.02.11
 * Time: 18:57
 */

class PartialAssociation(howTos: HashSet[HowTo], solution: Solution, rules: HashSet[Rule]) extends AbstractAssociation(rules) {
  def this() = this(HashSet[HowTo](), new Solution(), HashSet[Rule]())
}