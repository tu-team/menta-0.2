package menta.model.helpers.individual

import java.net.URI
import menta.model.howto.helper.AddNameHelper
import menta.model.helpers.actionclass.AddURIHelper
import menta.model.helpers.acceptanceCriteria.individual.AddBlockConstantIndividualHelper
import org.slf4j.{LoggerFactory, Logger}
import menta.model.howto._
import menta.model.helpers.common.IndividualMakerFactory

/**
 * @author toschev alex, talanov max
 * Date: 21.11.11
 * Time: 18:34
 */

class AddConstantIndividualHelper {

  /*
  def apply (actionClass: AddClass, targetActionClassURI: URI): AddIndividual = {
    val res = new AddIndividual(actionClass, List[HowTo](new StringLiteral(targetActionClassURI.toString)))
    res.name = "simpleStringHolder"
    res.uri = new URI(menta.model.Constant.modelNamespaceString + res.name)
    res
  }
  */

  /**
   * Creates AddIndividual of a specified actionClass, with specified targetActionClassURI parameter with parameter Block.
   * @param name the name of the Constant
   * @param actionClass the Class of the constant.
   * @param targetActionClassURI the URI of the class to be replaced with constant value(name)
   * @returns AddIndividual
   */
  def apply(block: AddIndividual, actionClass: AddClass, targetActionClassURI: URI): AddIndividual = {
    val res = new AddIndividual(actionClass, List[HowTo]((new AddBlockConstantIndividualHelper).apply(block), (new AddURIHelper).apply(targetActionClassURI)))
    res.name = menta.model.Constant.constantBlock
    res.uri = new URI(menta.model.Constant.modelNamespaceString)
    res
  }

  /**
   * Creates AddIndividual of a specified actionClass, with specified targetActionClassURI parameter with name
   * @param name the name of the Constant
   * @param actionClass the Class of the constant.
   * @param targetActionClassURI the URI of the class to be replaced with constant value(name)
   * @returns AddIndividual
   */
  def apply(name: String, actionClass: AddClass, targetActionClassURI: URI): AddIndividual = {
    val res = new AddIndividual(actionClass, List[HowTo]((new AddNameHelper).apply(name), (new AddURIHelper).apply(targetActionClassURI)))
    res.name = menta.model.Constant.nameConstant
    res.uri = new URI(menta.model.Constant.modelNamespaceString + name)
    res
  }

}

object AddConstantIndividualHelper {

  val LOG: Logger = LoggerFactory.getLogger(this.getClass)


  def unapply(ind: ActionIndividual): Option[ActionIndividual] = {
    if (ind.actionClass != null &&
      ((ind.actionClass.uri != null && ind.actionClass.uri.toString.contains(menta.model.Constant.nameConstant))
        || (ind.actionClass.name != null && ind.actionClass.name.contains(menta.model.Constant.nameConstant))
        )) {
      Some(ind)
    } else {
      None
    }
  }

  def getIndividual(cnstIND: AddIndividual, originalAddClassName: String): ActionIndividual = {
    //first parameter is a target Add Name with Name
    var nameHolder = (new AddNameHelper).extractValue(cnstIND);

    //find uri helper

    //secons is a action class
    var actionCls = new AddClass();
    actionCls.uri = (new AddURIHelper()).storedURI(cnstIND); // new URI(cnstIND.parameters(1).asInstanceOf[ActionClass].parameters(0).asInstanceOf[StringLiteral].valueString);
    if (originalAddClassName != null)
      actionCls.name = originalAddClassName;
    var res = IndividualMakerFactory.instantiate(actionCls);
    res.parameters = List(nameHolder);
    res

  };
  def getIndividual(cnstIND: AddIndividual, originalActionClass: ActionClass): ActionIndividual = {
    //first parameter is a target Add Name with Name
    var nameHolder = (new AddNameHelper).extractValue(cnstIND);

    //find uri helper

    //secons is a action class

    var res = IndividualMakerFactory.instantiate(originalActionClass);
    res.parameters = List(nameHolder);
    res

  };


  def getValue(individual: AddIndividual): Option[HowTo] = {
    if (individual != null && individual.name != null) {
      individual.name match {
        case menta.model.Constant.constantBlock => {
          // first parameter should be block then
          Some(individual.parameters(0))
        }
        case menta.model.Constant.nameConstant => {
          if (individual.parameters != null && individual.parameters.size > 0) {
            val nameHelper = individual.parameters(0)
            if (nameHelper.isInstanceOf[AddClass]) {
              val nameAddClass = nameHelper.asInstanceOf[AddClass]
              if (nameAddClass.parameters.size > 0) {
                return Some(nameAddClass.parameters(0))
              }
            }
          }
          None
        }
        case _ => None
      }
    } else {
      LOG.error("null inbound parameter")
      None
    }
  }

  def getTargetURI(individual: AddIndividual): Option[URI] = {
    if (individual != null && individual.parameters != null) {
      if (individual.parameters.size > 1) {
        val URIHelper = individual.parameters(1)
        if (URIHelper.isInstanceOf[AddClass]) {
          val addClass: AddClass = URIHelper.asInstanceOf[AddClass]
          if (addClass.parameters != null && addClass.parameters.size > 0) {
            if (addClass.parameters(0).isInstanceOf[StringLiteral]) {
              val sl = addClass.parameters(0).asInstanceOf[StringLiteral]
              return Some(new URI(sl.valueString))
            }
          }
        }
        None
      } else {
        None
      }
    } else {
      LOG.error("null inbound parameter")
      None
    }
  }

}