package menta.communicator.responseanalyser

import menta.model.util.pair.ConversationRuleChangeSetPair
import java.net.URI
import menta.model.conversation.PartialClarificationResponse
import menta.model.conversation.{Conversation, Response}
import menta.model.util.triple.ConversationPartialAssociationRuleChangeSetTriple
import menta.model.howto.HowTo

/**
 * @author talanovm
 * Date: 05.03.11
 * Time: 18:25
 */

trait ResponseAnalyser {
  def apply(response: Response,requirements:HowTo): ConversationRuleChangeSetPair

  def apply(communicationURI: URI, response: Response): ConversationRuleChangeSetPair

  def apply(communicationURI: URI, response: PartialClarificationResponse): ConversationPartialAssociationRuleChangeSetTriple

  def stop(communicationURI: URI): Conversation

  def createRequest(originalAC:URI, constantBlock:URI,author:String):Conversation
}