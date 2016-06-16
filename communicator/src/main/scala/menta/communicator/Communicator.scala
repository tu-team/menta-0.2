package menta.communicator

import menta.model.util.pair.ConversationRuleChangeSetPair
import java.net.URI
import menta.model.howto.UpdateHowToResponse
import menta.model.conversation.{Conversation, ConfirmationResponse, Response}
import menta.model.translator.SolutionReport
import menta.model.conversation.{ConfirmationRequest, PartialClarificationResponse}
import menta.model.howto.HowToChangeSet

/**
 * @author talanovm
 * Date: 28.02.11
 * Time: 20:00
 */

trait Communicator {
  def processReply(responseFromExpert: Response): ConversationRuleChangeSetPair
  def updateAcceptanceCriteria(conversationURI: URI, response: ConfirmationResponse): ConversationRuleChangeSetPair
  def updateAcceptanceCriteria(conversationURI: URI, response: PartialClarificationResponse): ConversationRuleChangeSetPair
  def stop(communicationURI: URI): Conversation
  def createRequest(report: SolutionReport): ConfirmationRequest
  def updateHowTo(response: UpdateHowToResponse): HowToChangeSet
}