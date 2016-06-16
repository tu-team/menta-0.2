package menta.model.learner

import menta.model.Knowledge
import menta.model.solutiongenerator.solutionchecker.Rule
import collection.immutable.HashSet

/**
 * The AbstractAssociation of AcceptanceCriteria and Solution HowTo.
 * @author talanovm
 * @see menta.solutiongenerator.solutionchecker.AcceptanceCriteria
 * @see menta.model.howto.Solution
 * @see menta.model.howto.HowTo
 * Date: 10.01.11
 * Time: 10:39
 */

abstract class AbstractAssociation(rules: HashSet[Rule]) extends Knowledge {
  def this() = this(HashSet[Rule]())
  var superAssociations: HashSet[AbstractAssociation] = HashSet[AbstractAssociation]()
}