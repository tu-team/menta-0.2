package menta.solutiongenerator.util

import menta.model.howto._
import menta.knowledgebaseserver.KnowledgeBaseServerImpl
import java.net.URI
import menta.solutiongenerator.howto.{RemoveIndividualWrapper, AddIndividualWrapper}
import org.slf4j.{Logger, LoggerFactory}

/**
 * @author toschev alex
 * Date: 24.10.11
 * Time: 18:03
 */

object IndividualEncoder {

  val kbServer = new KnowledgeBaseServerImpl
  val LOG: Logger = LoggerFactory.getLogger(this.getClass)
  def mentaPrefix = menta.model.Constant.modelNamespaceString

  /**
   * create menta individual
   * example of class name menta;model;<URI>
   * @param prmHolder class name   menta;model;<URI>
   */
  def createIndividual(prmHolder: String): Object = {
    LOG.debug("createIndividual(prmHolder: String) {}", prmHolder)
    val prms = prmHolder;
    LOG.debug("prms {}", prms)
    //we need only 2d parameter
    //inst primary class
    val prmClass = primaryClass(prmHolder);
    LOG.debug("prmClass {}", prmClass)
    prmClass
  }

  def createIndividual(actionClass: ActionClass): ActionIndividual = {
    if (actionClass.isInstanceOf[AddClass]) {
      val res = new AddIndividual();
      res.actionClass = actionClass;
      return res;

    }
    else if (actionClass.isInstanceOf[RemoveClass]) {
      val res = new RemoveIndividual();
      res.actionClass = actionClass;
      return res;

    }

    throw new Exception("Class " + actionClass  + " not supported")
  }


  /**
   * create string for class encoding
   * example of class name menta;model;<URI>
   * @param single class name    menta;model;<URI>
   */
  def encodeHowTo(single: ActionClass): String = {
   single.uri.toString
  }

  private def primaryClass(className: String): Object = {
    //load class from kbserver
    var cls = kbServer.selectActionClass(new URI(className))

    if (cls.isInstanceOf[AddClass])
    {
       return new AddIndividualWrapper(cls.asInstanceOf[AddClass]);
    } else if (cls.isInstanceOf[RemoveClass])
    {
       return new RemoveIndividualWrapper(cls.asInstanceOf[RemoveClass]);
    }

    throw new Exception("Class " + className + " not found in KB")

  }

  private def actionClass(className: String): ActionClass = {
    if (className == "AddClass") return new AddClass()
    if (className == "RemoveClass") return new RemoveClass()
    throw new Exception("Class " + className + " not found")

  }
}