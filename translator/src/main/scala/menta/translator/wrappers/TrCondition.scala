package menta.translator.wrappers

import org.slf4j.{LoggerFactory, Logger}
import menta.translator.TranslationStrategy
import menta.model.howto._

/**
 * @author talanovm
 * Date: 05.12.11
 * Time: 16:29
 */

class TrCondition extends ActionClass with TrWrapper {
  val LOG: Logger = LoggerFactory.getLogger(this.getClass)
  name = menta.model.Constant.trCondition

  override def apply(translationNode: AddIndividual, solutionNode: HowTo, context: Context, strategy: TranslationStrategy, mode:StringLiteral = new StringLiteral(""), conditionExpression:LogicalExpression = new LETrue): HowTo = {
    LOG.debug("{}()", this.getClass.getName)
    null
  }
}

object TrCondition extends TrMatcher {

  def unapply(individual: AddIndividual): Option[TrWrapper] = {
    if (individual.actionClass.uri.toString.contains(menta.model.Constant.trCondition)) {
      Some(new TrCondition)
    } else {
      None
    }
  }
}