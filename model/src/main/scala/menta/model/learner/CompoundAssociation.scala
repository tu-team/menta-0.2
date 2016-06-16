package menta.model.learner

import menta.model.solutiongenerator.solutionchecker.Rule
import collection.immutable.HashSet

/**
 * @author talanovm
 * Date: 25.02.11
 * Time: 18:52
 */

class CompoundAssociation(rules: HashSet[Rule]) extends AbstractAssociation(rules) {
  def this() = this(HashSet[Rule]())
  var subAssociations: HashSet[PartialAssociation] = HashSet[PartialAssociation]()
}