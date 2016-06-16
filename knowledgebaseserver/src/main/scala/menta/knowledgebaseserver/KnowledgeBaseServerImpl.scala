package menta.knowledgebaseserver

import constant.Constant
import java.util.Properties
import java.net.{URISyntaxException, URI}
import org.slf4j.LoggerFactory
import menta.model.howto._
import classifier.Classifier
import menta.model.solutiongenerator.solutionchecker.AcceptanceCriteria
import menta.model.{KnowledgeIndividual, KnowledgeClass, Knowledge}
import menta.knowledgebaseserver.dao.impl.EntityManagerFactoryImpl
import menta.dao.config.impl.ConfigurationImpl
import menta.exception.EntityNotFoundException
import menta.dao.Utils
import menta.model.util.serialization.ProtocolRegistry
import util.KBUtils
import menta.model.conversation.{Conversation, ConversationAct}
import org.hypergraphdb.HGPersistentHandle

/**
 * Provides storage access operations.
 * @author: alexander toschev
 * Date: 13.04.11
 * Time: 22:06
 */

class KnowledgeBaseServerImpl extends KnowledgeBaseServer {

  /**
   *  instance of DAO
   */
  val configuration = new ConfigurationImpl
  configuration.loadFromClasspath()
  val entityManager = EntityManagerFactoryImpl.createEntityManager(configuration)
  val cacheMapPath = "cache.map.properties"
  var cacheMap: Properties = (new ConfigurationImpl).loadFromClasspath(cacheMapPath)
  val log = LoggerFactory.getLogger(this.getClass)

  if (configuration.getProperties.get("tagsT") == null)
  {
    // TODO: create root tag
  }

  configuration.setProperties()
  printKBInfo();

  /**
   *  return terminal by url
   * @param terminalURI URI of terminal
   */
  def selectTerminal(terminalURI: URI): Terminal = {

    val res = entityManager.find(extractHandle(terminalURI));
    res match {
      case None => null
      case Some(k: Knowledge) => k.asInstanceOf[Terminal]
    }
  }

  def getKBInfo():String= {
    return "Database location: " + configuration.getDataSourcePath;
  }


  def printKBInfo() = {
    //var dir = new File (System.getProperty("user.dir"));
    log.info("Database location: " + configuration.getDataSourcePath)
  }

  /**
   * return how-to by uri
   * @param uri URI of how-to
   */
  def selectHowTo(uri: URI): HowTo = {

    val res = entityManager.find(extractHandle(uri));
    res match {
      case None => null
      case Some(k: Knowledge) => k.asInstanceOf[HowTo]
    }
  }

  /**
   * return converstion act by uri
   * @param uri URI of how-to
   */
  def selectConversationAct(uri: URI): ConversationAct = {

    val res = entityManager.find(extractHandle(uri));
    res match {
      case None => null
      case Some(k: Knowledge) => k.asInstanceOf[ConversationAct]
    }
  }

  def selectSolution(uri: URI): Solution = {

    val res = entityManager.find(extractHandle(uri));
    res match {
      case None => null
      case Some(k: Knowledge) => k.asInstanceOf[Solution]
    }
  }

  def linkObjects(src: URI, target: URI, linkName: String) {

    entityManager.linkObjects(extractHandle(src), extractHandle(target), linkName)

  }

  def getLinkedObjects(src: URI, linkName: String): List[Knowledge] = {
    entityManager.getLinkedObjects(extractHandle(src), linkName)
  }


  /**
   * return converstion by uri
   * @param uri URI of how-to
   */
  def selectConversation(uri: URI): Conversation = {

    val res = entityManager.find(extractHandle(uri));
    res match {
      case None => null
      case Some(k: Knowledge) => k.asInstanceOf[Conversation]
    }
  }

  /**
   * return Classifier by uri
   * @param uri URI of how-to
   */
  def selectClassifier(uri: URI): Classifier = {

    val res = entityManager.find(extractHandle(uri));
    res match {
      case None => null
      case Some(k: Knowledge) => k.asInstanceOf[Classifier]
    }
  }

  /**
   *  return Action instance
   * @param uri URI of ActionInstance
   */
  def selectActionInstance(uri: URI): ActionIndividual = {

    val res = entityManager.find(extractHandle(uri));
    res match {
      case None => null
      case Some(k: Knowledge) => k.asInstanceOf[ActionIndividual]
    }

  }

  def extractHandle(uri: URI): String = {
    if (uri == null) return null
    val stringURI = uri.toString
    val delimiterIndex = stringURI.lastIndexOf(".")
    if (delimiterIndex > 0 && delimiterIndex < stringURI.length()) {
      return stringURI.substring(delimiterIndex + 1)
    }
    ""

  }

  /**
   * create or find action class
   */

  def selectOrCreateActionClass(target: ActionClass): ActionClass = {
    var existing = selectActionClass(target.name);
    if (existing != null) return existing;
    save(target);
    return selectOrCreateActionClass(target)
  }

  /**
   *  return ActionClass
   * @param uri URI of Action class
   */
  def selectActionClass(uri: URI): ActionClass = {

    val res = entityManager.find(extractHandle(uri));
    res match {
      case None => null
      case Some(k: Knowledge) => k.asInstanceOf[ActionClass]
    }

  }


  def selectActionClass(name: String): ActionClass = {
    //TODO: http://code.google.com/p/menta/wiki/TODO?ts=1322309826&updated=TODO 1
    val res = selectAllActionClass();
    var trgt = res.filter(p => p.isInstanceOf[ActionClass] && p.asInstanceOf[ActionClass].name == name)
    if (trgt.length <= 0) return null;
    return trgt.head.asInstanceOf[ActionClass];

  }

  /**
   *  return Action instance
   * @param classURI URI of Class
   */
  def selectIndividualsOfClass(uri: URI): Set[KnowledgeIndividual] = {

    val res = entityManager.find(extractHandle(uri));

    if (res == null) {
      return null;
    }
    res.asInstanceOf[ActionClass].parameters.toArray.find(p => p.isInstanceOf[KnowledgeIndividual]).map(b => b.asInstanceOf[KnowledgeIndividual]).toSet;
  }

  /**
   * select subclasses of class
   * @param calssURI URI of classes
   */
  def selectSubClasses(uri: URI): Set[KnowledgeClass] = {
    val res = entityManager.find(extractHandle(uri));

    if (res == null)
      return null;

    return res.asInstanceOf[ActionClass].parameters.toArray.find(p => p.isInstanceOf[KnowledgeClass]).map(b => b.asInstanceOf[KnowledgeClass]).toSet;

  }

  /**
   * select individual by URI
   * @param uri URI of knowledge individual
   */
  def selectIndividual(uri: URI): KnowledgeIndividual = {
    val res = entityManager.find(extractHandle(uri));

    res match {
      case None => null
      case Some(k: Knowledge) => k.asInstanceOf[KnowledgeIndividual]
    }
  }

  /**
   * select class by URI
   * @param uri URI of knowledge class
   */
  def selectClass(uri: URI): KnowledgeClass = {
    val res = entityManager.find(extractHandle(uri));
    res match {
      case None => null
      case Some(k: Knowledge) => k.asInstanceOf[KnowledgeClass]
    }
  }

  def checkConsistency(howToElement: HowTo): Boolean = {
    throw new Exception("Method not implemented")
  }


  /**
   * store terminal using provided URI
   * @param uri URI of terminal
   */
  def addTerminal(uri: URI): Terminal = {
    //preprate object
    var obj = new Terminal()
    obj.uri = save(obj)
    //select object and return
    return selectTerminal(obj.uri);
  }

  /**
   * Add remove instance
   * @param uri URI of remove instance
   * @param actionClass parent action class
   * @param removeObject target object for removing
   */
  def addRemoveActionInstance(uri: URI, actionClass: ActionClass, removeObject: HowTo): RemoveIndividual = {
    var obj = new RemoveIndividual()

    obj.Parameters ::: List(actionClass)
    obj.Parameters ::: List(removeObject)

    obj.uri = save(obj)
    return obj

  }

  /**
   * Add remove action class
   * @param uri URI of Remove action class
   * @param parent Parent Action Class
   * @param removeObject target object to be removed
   *
   */
  def addRemoveActionClass(uri: URI, parent: ActionClass, removeObject: HowTo): RemoveClass = {
    val obj: RemoveClass = new RemoveClass()
    setURI(uri, obj)
    obj.parameters = obj.parameters ::: List(parent, removeObject)
    obj.uri = save(obj)
    obj
  }

  /**
   * store add action instance
   * @param uri URI of instance
   * @param actionClass Action Class of this instance
   * @param subExpressions the list of parameters (sub-expressions of the expression)
   */
  def addAddActionInstance(uri: URI, actionClass: ActionClass, subExpressions: List[ActionIndividual]): AddIndividual = {
    var obj = new AddIndividual()

    obj.parameters = obj.parameters ::: List(actionClass)
    obj.parameters = obj.parameters ::: subExpressions

    obj.uri = save(obj)

    return obj
  }

  def addAddActionClass(uri: URI, parent: ActionClass, scope: List[ActionClass]): AddClass = {
    //create object
    val obj = new AddClass()

    obj.superClass = parent

    //obj.getScope.addAll(scala.collection.JavaConversions.asJavaCollection(scope))


    //save object
    obj.uri = save(obj)
    return obj
  }

  def addSubClass(uri: URI, aSuperClass: KnowledgeClass): KnowledgeClass = {
    val clazz = new KnowledgeClass()

    var superClass = clazz.superClasses
    superClass += aSuperClass

    //save object
    clazz.uri = save(clazz)
    clazz
  }

  def addClass(uri: URI): KnowledgeClass = {
    val res = new KnowledgeClass()

    res.uri = save(res)
    res
  }

  /**
   * save selected knowledge to DB
   * @param entity Knowledge to save
   */
  def save(entity: Knowledge): URI = {
    def uid(): String = {
      if (!entity.checkIfSaved) return null;
      return entity.extractHandle();
    }

    //prepare children
    if (entity.getAllChildren() != Nil && entity.getAllChildren() != null) {
      entity.getAllChildren().foreach(k => {
        if (k != null && !k.checkIfSaved()) {
          k.uri = save(k);

        }
      })
    }
    val isNew = !entity.checkIfSaved();


    var handler: HGPersistentHandle = null;
    if (entity.isInstanceOf[ActionClass]) {
      //try to load class
      if (entity.asInstanceOf[ActionClass].name != null) {
        //exclusion
        var nm = entity.asInstanceOf[ActionClass].name;
        if (nm != "AddNameHelper" && nm != "parameter" && nm!="variable") {

          //var tAC = selectActionClass(entity.asInstanceOf[ActionClass].name);
         // if (tAC != null) {
          //  entity.uri = tAC.uri;
          //}
        }
      }
    }

    handler = entityManager.persist(entity, uid)

    //e.g. generation of URI
    val uri = entity.uri
    if (!isNew) {
      validateURI(entity, handler)
    } else {

      if (uri == null) {
        entity.uri = generateURI(entity, handler)
      }
      else {
        //append uid post fix
        entity.uri = new URI(KBUtils.appendUUID(entity.uri.toString, handler, entity))
      }
      //save uri
      entityManager.persist(entity, uid)
    }




    return getURI(entity.uri)
  }


  /**
   * save a part of Acceptance Criteria
   * @param uri URI of Acceptance Criteria
   * @param howTo a part of Acceptance Criteria which should be stored
   */
  def addAcceptanceCriteriaIndividual(uri: URI, howTo: HowTo): AcceptanceCriteria = {
    //extract AC from db
    entityManager.find(uri) match {
      case None => throw new EntityNotFoundException("Acceptance criteria not found")
      case Some(ac: Knowledge) => {
        if (ac.isInstanceOf[AcceptanceCriteria]) {
          //store how to

          // ac.asInstanceOf[AcceptanceCriteria].Parameters ::: List(howTo)
          save(ac)
          ac.asInstanceOf[AcceptanceCriteria]
        } else {
          throw new ClassCastException("Found object could not be casted to AcceptanceCriteria" + ac)
        }
      }
    }
  }

  def processRequest(requestString: String): ResultSet = null

  /**
   * add individual of type
   * @param typeClass typeClass of individual
   */
  def addIndividual(uri: URI, typeClass: KnowledgeClass): KnowledgeIndividual = {
    val knowledgeIndividual = new KnowledgeIndividual
    this.setURI(uri, knowledgeIndividual)
    knowledgeIndividual.types += typeClass
    knowledgeIndividual.uri = save(knowledgeIndividual)
    knowledgeIndividual
  }

  //Helpers collection

  private def setURI(uri: URI, obj: Knowledge) {
    obj.uri = uri
  }

  private def getURI(uri: String): URI = {
    return Utils.stringToURI(uri)
  }

  private def getURI(uri: URI): URI = {
    return Utils.stringToURI(uri.toString)
  }

  def generateURI(entity: Knowledge, handler: HGPersistentHandle): URI = {

    return KBUtils.generateURI(entity, handler);
  }

  /*
    extract handle from URI and return it

   */


  /**
   * 1) Checks whether the URI has standard prefix
   * 2) The URI of specified entity if searched in KB points to same entity.
   * 3) Individual has unique HGPersistentHandle postfix.
   */
  def validateURI(entity: Knowledge, handler: HGPersistentHandle): Boolean = {
    try {
      val uri = new URI(entity.uri.toString)
      // checks the standard prefix 1)
      if (!uri.toString.startsWith(Constant.defaultURIPrefix)) {
        log.error("URI does not conform standard prefix {}", Constant.defaultURIPrefix)
        return false
      } else {
        // checks uniqueness of the URI 2)
        entityManager.find(extractHandle(uri)) match {
          case Some(k: Knowledge) => {
            log.debug("found entity", k.toString)
            if (k equals entity) {
              //checks postfix 3)
              entity match {
                case individual: AddIndividual => {
                  if (!uri.toString.endsWith(handler.toString)) {
                    log.error("URI does not conform standard postix {}", handler.toString)
                    return false
                  } else {
                    return true
                  }
                }
                case knowledge: Knowledge => {
                  return true
                }
              }
            } else {
              return false
            }
          }
          case None => {
            return false
          }
        }
      }
    } catch {
      case e: URISyntaxException => {
        log.error(e.getMessage)
        return false
      }
    }
    true
  }

  def cleanUPEverything()
  {
    //get all registeresd menta Entities
    var target= ProtocolRegistry.finalClasses;
    target.foreach(b=>
    {
       var toDelete= entityManager.findOfType(b);
       toDelete.foreach(f=>
       {
          if (f.uri==null) f.uri=generateURI(f, entityManager.getPersistentHandler(f))
         entityManager.remove(f.extractHandle())
       })
    });

    //clean up links

  }

  /**
   * find all action classes
   */
  def selectAllActionClass(): List[Knowledge] = {
    var res: List[ActionClass] = null
    ProtocolRegistry.actionClasses.foreach(cl => {
      var part = entityManager.findOfType(cl);
      if (cl.isInstanceOf[ActionClass]) {
        if (res == null) {
          res = (part.map(b => b.asInstanceOf[ActionClass]));
        }
        else {
          res = res ::: (part.map(b => b.asInstanceOf[ActionClass]));

        }
      }

    })

    //post process result to generate fake uri with handles
    res.foreach(b => {
      if (b.uri == null && b.isInstanceOf[Knowledge])
        b.uri = generateURI(b, entityManager.getPersistentHandler(b));
    });

    res
  }

  def selectAcceptanceCriteriaByName(name:String):AcceptanceCriteria=
  {
    var res= entityManager.findOfType(new AcceptanceCriteria())
    var filtered= res.filter(p=>p.isInstanceOf[AcceptanceCriteria] && p.asInstanceOf[AcceptanceCriteria].ruleName==name).map(p=>p.asInstanceOf[AcceptanceCriteria]);
    if (filtered.length>0) return filtered.head
    return null
  }

  def selectAllAcceptanceCriteria():List[AcceptanceCriteria]=
  {
    var res= entityManager.findOfType(new AcceptanceCriteria())
    return res.filter(p=>p.isInstanceOf[AcceptanceCriteria]).map(p=>p.asInstanceOf[AcceptanceCriteria]);

  }

  /*
    remove object from database
    @param obj - object to be removed
   */
  def removeObject(obj: Knowledge): Boolean = {
    if (!obj.checkIfSaved()) return false;

    entityManager.remove(obj.extractHandle());
    true
  }

  def selectAcceptanceCriteria(uri: URI): AcceptanceCriteria = {
    val res = entityManager.find(extractHandle(uri));
    res match {
      case None => null
      case Some(k: Knowledge) => k.asInstanceOf[AcceptanceCriteria]
    }
  }
}