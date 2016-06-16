package menta.translator.wrappers

import java.net.URI
import org.slf4j.{Logger, LoggerFactory}
import menta.model.howto.{StringLiteral, HowTo, Context, AddIndividual}
import menta.model.helpers.individual.AddConstantIndividualHelper
import menta.model.howto.helper.AddNameHelper


/**
 * @author talanov max
 * Date: 24.11.11
 * Time: 19:34
 */

class TrAddName extends TrConstantWrapper {
  val LOG: Logger = LoggerFactory.getLogger(this.getClass)

  def apply(translationNode: AddIndividual, solutionNode: HowTo, context: Context) = {
    // get value from translation Node
    val valueOption: Option[HowTo] = AddConstantIndividualHelper.getValue(translationNode)
    val targetURIOption: Option[URI] = AddConstantIndividualHelper.getTargetURI(translationNode)
    // find proper actionClass in Solution
    // add AddName node in the parameters of found node
    valueOption match {
      case Some(valueHT) => {
        targetURIOption match {
          case Some(targetURI) => {
            if (valueHT.isInstanceOf[StringLiteral]) {
              val value: StringLiteral = valueHT.asInstanceOf[StringLiteral]
              applyAddName(value, targetURI, solutionNode)
            }
          }
          case None => {
            LOG.error("translation node does not contain value")
          }
        }
      }
      case None => {
        LOG.error("translation node does not contain URI")
      }
    }
    solutionNode
  }


  private def applyAddName(value: StringLiteral, targetURI: URI, solutionNode: HowTo): HowTo = {
    if (solutionNode.isInstanceOf[AddIndividual]) {
      val individual = solutionNode.asInstanceOf[AddIndividual]
      val updatedSelf: AddIndividual = individual
      if (individual.parameters != null && individual.parameters.size > 0) {
        val updatedParameters: List[HowTo] = for (val child: HowTo <- individual.parameters) yield applyAddName(value, targetURI, child)
        updatedSelf.parameters = updatedParameters
      }
      // process self
      if (individual.actionClass != null && individual.actionClass.uri != null) {
        if (individual.actionClass.uri.toString.contains(targetURI.toString)) {
          // adding Name parameter
          val res =(new AddNameHelper).apply(value.valueString)
          if (individual.parameters != null) {
            updatedSelf.parameters = individual.parameters ::: List[HowTo](res)
          } else {
            updatedSelf.parameters = List[HowTo](res)
          }
        }
      } else {
        LOG.error(" solutionNode:AddIndividual does not contain actionClass or it's URI")
      }
      updatedSelf
    } else {
      LOG.warn(" solutionNode is not AddIndividual")
      solutionNode
    }
  }
}

object TrAddName extends TrConstantURIMatcher {
  def unapply(uri: URI): Option[TrConstantWrapper] = {
    if (uri.toString.contains(menta.model.Constant.trAddName)) {
      Some(new TrAddName())
    } else {
      None
    }
  }
}