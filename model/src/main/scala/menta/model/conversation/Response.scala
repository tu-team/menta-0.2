package menta.model.conversation

import java.util.Date
import menta.model.subscribercontroller.Customer
import java.net.URI


/**
 * Parent for all responses.
 * @author: talanovm
 * Date: 07.01.11
 * Time: 19:00
 */

class Response(aAuthor: Customer, aDateTime: Date)
  extends ConversationAct(aAuthor, aDateTime) {
  def this() = this (new Customer(), new Date())


}