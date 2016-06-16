package menta.model.conversation

import java.net.URI
import menta.model.subscribercontroller.Customer
import java.util.Date
import menta.model.solutiongenerator.solutionchecker.RuleChangeSet

/**
 * @author: talanovm
 * Date: 07.01.11
 * Time: 19:09
 */

class ClarificationResponse(author: Customer,  dateTime: Date, solutionURI: URI, requirementChangeSet: RuleChangeSet)
  extends ConfirmationResponse(author,  dateTime, solutionURI) {
  def this() = this(new Customer(),  new Date(), new URI(""), new RuleChangeSet())
}