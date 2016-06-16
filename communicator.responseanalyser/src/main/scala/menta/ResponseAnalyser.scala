package menta.communicator.responseanalyser

import java.net.URI
import menta.model.util.triple.ConversationPartialAssociationRuleChangeSetTriple
import menta.model.util.pair.ConversationRuleChangeSetPair
import menta.model.howto.HowTo
import menta.model.solutiongenerator.solutionchecker.{RuleChange, RuleChangeSet}
import menta.model.conversation.{ConversationAct, Response, PartialClarificationResponse, Conversation}
import menta.knowledgebaseserver.{KnowledgeBaseServerImpl, KnowledgeBaseServer}

/**
 * Created by IntelliJ IDEA.
 * User: toscheva
 * Date: 03.12.11
 * Time: 12:25
 * To change this template use File | Settings | File Templates.
 */

class ResponseAnalyserImpl extends ResponseAnalyser {

   private var theKBServer: KnowledgeBaseServer = new KnowledgeBaseServerImpl;

  /**
   * Creates conversation with specified requirements
   * @params Response
   */
  def apply(response: Response,requirements:HowTo): ConversationRuleChangeSetPair = {
    val conversation = new Conversation(response.customer(),null)
    val rules = new RuleChangeSet(List(new RuleChange(requirements)));
    val conversationPair=new  ConversationRuleChangeSetPair(conversation,rules);
    return conversationPair
  }


  def createRequest(originalAC:URI, constantBlock:URI,author:String):Conversation=
  {
    val conversation = new Conversation()
    val act= new Response()
    act.originalAC=originalAC;
    act.constantBlock=constantBlock:URI ;
    act.authorName=author;
    act.uri=theKBServer.save(act);

    conversation.conversationActs=List(act);
    //save Conversation and return
    conversation.uri=theKBServer.save(conversation) ;
    conversation
  }

  //will not be used in current version
  def apply(communicationURI: URI, response: Response): ConversationRuleChangeSetPair = {
    return null ;
  }

  def apply(communicationURI: URI, response: PartialClarificationResponse): ConversationPartialAssociationRuleChangeSetTriple = null

  def stop(communicationURI: URI): Conversation = null


}