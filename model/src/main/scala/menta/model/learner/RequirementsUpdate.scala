package menta.model.learner

import menta.model.solutiongenerator.solutionchecker.RuleChangeSet
import java.util.Date

/**
 * @author talanovm
 * Date: 25.02.11
 * Time: 19:11
 */

class RequirementsUpdate(ruleChangeSet: RuleChangeSet, dateTime: Date, association: AbstractAssociation)
  extends Event (dateTime, association) {
  def this() = this(new RuleChangeSet(), new Date(), new PartialAssociation())
}