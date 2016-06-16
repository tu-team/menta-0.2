package menta.knowledgebaseserver


import org.hypergraphdb.HGPersistentHandle
import java.net.URI
import menta.model.{KnowledgeIndividual, Knowledge, KnowledgeClass}


import menta.model.howto._
import classifier.Classifier
import menta.model.solutiongenerator.solutionchecker.AcceptanceCriteria
import menta.model.conversation.{Conversation, ConversationAct}

/**
 * The interface for the KBServer.
 * @author talanovm
 * Date: 10.01.11
 * Time: 15:52
 */

trait KnowledgeBaseServer {
  def processRequest(requestString: String): ResultSet

  def save(entity: Knowledge): URI

  def addClass(uri: URI): KnowledgeClass

  def addSubClass(uri: URI, superClass: KnowledgeClass): KnowledgeClass

  def addIndividual(uri: URI, typeClass: KnowledgeClass): KnowledgeIndividual

  def addAddActionClass(uri: URI, parent: ActionClass, scope: List[ActionClass]): AddClass

  def addAddActionInstance(uri: URI, actionClass: ActionClass, subExpressions: List[ActionIndividual]): AddIndividual

  def addRemoveActionClass(uri: URI, parent: ActionClass, removeObject: HowTo): RemoveClass

  def addRemoveActionInstance(uri: URI, actionClass: ActionClass, removeObject: HowTo): RemoveIndividual

  def addTerminal(uri: URI): Terminal

  def addAcceptanceCriteriaIndividual(uri: URI, howTo: HowTo): AcceptanceCriteria

  def selectHowTo(uri: URI): HowTo

  def selectClassifier(uri: URI): Classifier

  /**
   * Checks the consistency of the HowTo and all Actions and Terminal and sub-HowTo-s.
   */
  def checkConsistency(howToElement: HowTo): Boolean

  def selectClass(uri: URI): KnowledgeClass

  def selectIndividual(uri: URI): KnowledgeIndividual

  def selectSubClasses(classURI: URI): Set[KnowledgeClass]

  def selectIndividualsOfClass(classURI: URI): Set[KnowledgeIndividual]

  def selectActionClass(name: String): ActionClass

  def selectActionClass(uri: URI): ActionClass

  def selectActionInstance(uri: URI): ActionIndividual

  def selectAcceptanceCriteria(uri: URI): AcceptanceCriteria

  def selectTerminal(terminalURI: URI): Terminal


  def selectConversationAct(uri: URI): ConversationAct

  /**
   * return converstion by uri
   * @param uri URI of how-to
   */
  def selectConversation(uri: URI): Conversation

  def selectSolution(uri: URI): Solution

  def selectAcceptanceCriteriaByName(name:String):AcceptanceCriteria

  def selectAllAcceptanceCriteria():List[AcceptanceCriteria]

  /*
   remove object from database
   @param obj - object to be removed
  */
  def removeObject(obj: Knowledge): Boolean;

  /**
   * @returns all registered in KB classes
   */
  def selectAllActionClass(): List[Knowledge]

  /**
   * Generates new URI according to Knowledge class.
   * @param individual Knowledge to generate URI.
   * @param HGPersistentHandle to be used as unique part of URI
   * @returns generated new URI.
   */
  def generateURI(individual: Knowledge, handler: HGPersistentHandle): URI

  /**
   * Validates URI: 1) checks the symbols and format, 2) If is found in storage is the specified entity. 3) If if URI ends with handler
   * 1. Individual should have standard prefix (menta/0.2#), 2. ClassURI, unique id: persistence handler. 3. Class and Knowledge should have standard prefix and unique name (if found it should be the specified Knowledge
   * @param entity Knowledge the URI to check.
   * @param handler HGPersistentHandle URI must ends with it.
   * @return true if test succeeds false if fails.
   */
  def validateURI(entity: Knowledge, handler: HGPersistentHandle): Boolean

  /**
   * create or find action class
   */

  def selectOrCreateActionClass(target: ActionClass): ActionClass
  def linkObjects(src: URI, target: URI, linkName: String)

  def getLinkedObjects(src: URI,linkName:String):List[Knowledge]

   def cleanUPEverything()
  def getKBInfo():String
}