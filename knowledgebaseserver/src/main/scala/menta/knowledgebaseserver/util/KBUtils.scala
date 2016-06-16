package menta.knowledgebaseserver.util

import menta.model.Knowledge
import org.hypergraphdb.HGPersistentHandle
import java.net.URI
import menta.knowledgebaseserver.constant.Constant
import menta.model.howto.{AddClass, ActionClass, AddIndividual}
import menta.knowledgebaseserver.exception.InvalidParameterException
import org.slf4j.LoggerFactory

/**
 * @author Alexander
 * Date: 07.11.11
 * Time: 8:52
 */

/*
    contains helper methods for kb
 */
object KBUtils {

  val log = LoggerFactory.getLogger(this.getClass)

  def appendUUID(src: String, handler: HGPersistentHandle, entity: Knowledge): String = {
    if (handler == null) return src
    if (entity.isRoot) {
      return src + Constant.UUIDDelimiter + menta.model.Constant.rootElementID + Constant.URIDelimiter + handler.toString
    }
    return src + Constant.UUIDDelimiter + Constant.URIDelimiter + handler.toString
  }

  def generateURI(entity: Knowledge, handler: HGPersistentHandle): URI = {
    //log.debug("generateURI(entity {})", entity)

    entity match {
      case individual: AddIndividual => {
        // menta/v0.2#AddOperator.322223 (AddOperator is the URI of the class of individual)
        val actionClass: ActionClass = individual.getActionClass
        var uriString = menta.model.Constant.modelNamespaceString +"AddIndividual"
        if (actionClass != null) {
          val className: String = actionClass.getClass.getName.substring(actionClass.getClass.getName.lastIndexOf(Constant.packageDelimiter) + 1)

          uriString = menta.model.Constant.modelNamespaceString + className;
        }
        uriString = appendUUID(uriString, handler, entity)

        //log.debug("generatede URI AddIndividual {}", uriString)
        new URI(uriString)
      }
      case clazz: AddClass => {
        // menta/v0.2#AddOperator.322223  (AddOperator is the URI of  the class)
        //truncate package
        val className: String = clazz.getClass.getName.substring(clazz.getClass.getName.lastIndexOf(Constant.packageDelimiter) + 1)
        if (className == null && className.length < 1) {
          throw new InvalidParameterException("No class name found")
        }
        var uriString = menta.model.Constant.modelNamespaceString + className
        uriString = appendUUID(uriString, handler, entity)
        //log.debug("generatede URI AddClass {}", uriString)
        new URI(uriString)
      }
      case knowledge: Knowledge => {
        val className: String = knowledge.getClass.getName.substring(knowledge.getClass.getName.lastIndexOf(Constant.packageDelimiter) + 1)
        var uriString = menta.model.Constant.modelNamespaceString + className
        uriString = appendUUID(uriString, handler, entity)
        //log.debug("generatede URI Knowledge {}", uriString)
        new URI(uriString)
      }
    }
  }
}