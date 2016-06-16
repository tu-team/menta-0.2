package menta.model.conversation

import menta.model.subscribercontroller.Customer
import java.util.Date
import menta.model.translator.{SolutionReport, Report}

/**
 * @author: talanovm
 * Date: 07.01.11
 * Time: 18:34
 */

class ConfirmationRequest (author: Customer, conversation: Conversation, dateTime: Date, report: SolutionReport)
  extends Request(author,  dateTime, report) {
  def this() = this(new Customer(), new Conversation(), new Date(), new SolutionReport())
}