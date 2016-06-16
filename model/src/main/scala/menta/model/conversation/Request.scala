package menta.model.conversation

import menta.model.subscribercontroller.Customer
import java.util.Date
import menta.model.translator.Report

/**
 * Abstract class base for all Requests.
 * @author: talanovm
 * Date: 07.01.11
 * Time: 18:29
 */

abstract class Request(author: Customer,  dateTime: Date, report: Report)
  extends ConversationAct(author,  dateTime)