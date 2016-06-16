package menta.model.conversation

import java.util.Date
import menta.model.subscribercontroller.Customer
import java.net.URI

/**
 * @author: talanovm
 * Date: 07.01.11
 * Time: 19:02
 */

class ConfirmationResponse(author: Customer,  dateTime: Date, solutionURI: URI)
  extends Response (author,  dateTime) {
  def this() = this(new Customer(),  new Date(), new URI(""))
}