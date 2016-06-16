package menta.model.conversation

import menta.model.solutiongenerator.solutionchecker.RuleChangeSet
import java.net.URI
import java.util.Date
import menta.model.subscribercontroller.Customer
import menta.model.learner.PartialAssociation
import collection.immutable.HashSet

/**
 * The partial confirmation class.
 * @author talanovm
 * Date: 28.02.11
 * Time: 20:15
 */

class PartialClarificationResponse(author: Customer, dateTime: Date, solutionURI: URI, requirementChangeSet: RuleChangeSet, partialAssociations: HashSet[PartialAssociation])
  extends ClarificationResponse (author,  dateTime, solutionURI, requirementChangeSet) {
  def this() = this(new Customer(),  new Date(), new URI(""), new RuleChangeSet(), HashSet[PartialAssociation]())
}