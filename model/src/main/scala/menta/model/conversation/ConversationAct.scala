package menta.model.conversation

import menta.model.subscribercontroller.Customer
import java.util.Date
import java.net.URI

/**
 * The abstract basic conversation component
 * @author: talanovm
 * Date: 07.01.11
 * Time: 18:25
 */

abstract class ConversationAct(aAuthor: Customer, aDateTime: Date)
  extends ConversationAny(aAuthor) {
  def this() = this (new Customer(), new Date())

  def customer() = author

  private var theDateTime: Date = aDateTime

  private var theOriginalAC: URI = null // Constant.modelNamespace

  private var theTranslationURI: URI = null

  private var theSolutionURI: URI = null

  private var theConstantBlock: URI = null

  private var theAuthorName: String = null

  private var theConfirmationStatus:Boolean=false

  def originalAC = theOriginalAC

  def originalAC_=(aAC: URI) = theOriginalAC = aAC

  def translationURI = theTranslationURI

  def translationURI_=(aTranslation: URI) = theTranslationURI = aTranslation

  def solutionURI = theSolutionURI

  def solutionURI_=(aSolution: URI) = theSolutionURI = aSolution

  def dateTime = theDateTime

  def dateTime_(aDateTime: Date) = theDateTime = aDateTime

  def constantBlock = theConstantBlock

  def constantBlock_=(aConstantBlock: URI) = theConstantBlock = aConstantBlock

  def authorName = theAuthorName

  def authorName_=(aAuthorName: String) = theAuthorName = aAuthorName

  def confirmationStatus = theConfirmationStatus

  def confirmationStatus_=(aStatus: Boolean) =theConfirmationStatus = aStatus

}