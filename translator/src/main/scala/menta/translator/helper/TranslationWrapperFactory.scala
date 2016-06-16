package menta.translator.helper

import org.slf4j.{Logger, LoggerFactory}
import menta.translator.wrappers._
import java.net.URI
import menta.translator.AddTranslationHowTo

/**
 * Creates TrVariable, TrApplyTemplates, TrCondition, LEAnd, LEEquals, LEExists, LENot, LEOr, LEVariable, LEXor
 * @author talanov max
 * Date: 05.12.11
 * Time: 10:30
 */

class TranslationWrapperFactory {

  //TODO test it
  val LOG: Logger = LoggerFactory.getLogger(this.getClass)

  def apply(addIndividual: AddTranslationHowTo): TrWrapper = {
    if (addIndividual.actionClass != null && addIndividual.actionClass.uri != null) {
      addIndividual match {
        case TrVariable(tr: TrVariable) => {
          tr
        }
        case TrApplyTemplates(tr: TrApplyTemplates) => {
          tr
        }
        case TrCondition(tr: TrCondition) => {
          tr
        }
        //TODO add LEAnd, LEEquals, LEExists, LENot, LEOr, LEVariable, LEXor processing
      }
    }
    throw new UninitializedFieldError("individual does not cantain actionClass or/and it's URI " + addIndividual.toString)
  }


}