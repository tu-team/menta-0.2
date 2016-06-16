package menta.model.howto


import java.util.Date
import menta.model.subscribercontroller.Customer
import menta.model.conversation.{Response, Conversation}

/**
 * @author talanovm
 * Date: 28.02.11
 * Time: 20:30
 */

class UpdateHowToResponse(author: Customer,  dateTime: Date, howToChangeSet: HowToChangeSet) extends Response (author, dateTime)