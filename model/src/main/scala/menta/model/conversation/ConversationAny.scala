package menta.model.conversation

import menta.model.subscribercontroller.Customer
import menta.model.Knowledge
import java.net.URI

/**
 * Parent for all conversation possible classes.
 * @author: talanovm
 * Date: 07.01.11
 * Time: 18:13
 */

abstract class ConversationAny(aAuthor: Customer) extends Knowledge {
  def this() = this(new Customer())

  private var theAuthor:Customer=aAuthor

  def author =theAuthor

  def author_(aAuthor:Customer)=theAuthor=aAuthor

  var theConversationActs: List[ConversationAct] = List[ConversationAct]()

  def conversationActs = theConversationActs

  def conversationActs_=(aConversationActs: List[ConversationAct]) = theConversationActs = aConversationActs

  def solution():URI=
  {
    if (theConversationActs==null) return null
    val res=theConversationActs.filter(p=>p.solutionURI!=null).map(f=>f.solutionURI)
    if (res.length>0) return res.head;

    return null
  }

  def translation():URI=
  {
    if (theConversationActs==null) return null
    val res=theConversationActs.filter(p=>p.translationURI!=null).map(f=>f.translationURI)
    if (res.length>0) return res.head;

    return null
  }

  def acceptanceCriteria():URI=
  {
    if (theConversationActs==null) return null
    val res=theConversationActs.filter(p=>p.originalAC!=null).map(f=>f.originalAC)
    if (res.length>0) return res.head;

    return null
  }
   def getConstantBlockURI():URI=
  {
    if (theConversationActs==null) return null
    val res=theConversationActs.filter(p=>p.constantBlock!=null).map(f=>f.constantBlock)
    if (res.length>0) return res.head;

    return null
  }

  def confirmationStatusAny():Boolean=
  {
    if (theConversationActs==null) return false
    val res=theConversationActs.filter(p=>p.confirmationStatus!=false).map(f=>f.confirmationStatus)
    if (res.length>0) return res.head;

    return false
  }
}