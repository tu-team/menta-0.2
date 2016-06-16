package menta.translator.wrappers

import org.slf4j.{Logger, LoggerFactory}
import menta.model.howto._
import menta.translator.TranslationStrategy
import menta.translator.constant.Constant
import java.net.URI

/**
 * @author talanov max
 * Date: 22.11.11
 * Time: 18:50
 */

class TrApplyTemplates(nodeType: HowTo, condition: HowTo) extends ActionClass with TrWrapper {
  val LOG: Logger = LoggerFactory.getLogger(this.getClass)
  name = menta.model.Constant.trApplyTemplates
  parameters = List(nodeType, condition)

  def this() = this(null, null)

  override def apply(translationNode: AddIndividual, solutionNode: HowTo, context: Context, strategy: TranslationStrategy, mode: StringLiteral = new StringLiteral(""), conditionExpression: LogicalExpression = new LETrue): HowTo = {
    if (solutionNode == null) {
      LOG.error("Null parameter solutionNode, no solution to translate.")
      solutionNode
    } else if (!solutionNode.isInstanceOf[ActionIndividual] && solutionNode.asInstanceOf[ActionIndividual].actionClass == null) {
      LOG.error("solutionNode doesn't have actionClass prameter!")
      solutionNode
    }
    if (!strategy.isInstanceOf[TranslationStrategy]) {
      LOG.warn("ApplyTemplate run without StrategyRule. Solutionnode = {}", solutionNode)
      solutionNode
    } else {
      val trStrategy: TranslationStrategy = strategy.asInstanceOf[TranslationStrategy]
      val solutionNodeActionIndividual = solutionNode.asInstanceOf[ActionIndividual]
      if (trStrategy.getTemplate(solutionNodeActionIndividual) != null) {
        val template = strategy.asInstanceOf[TranslationStrategy].getTemplate(solutionNodeActionIndividual)
        var res = ""
        //for (val template: AddIndividual <- templateOption) {
        for (val parameter <- template.parameters) {
          var str = "";
          LOG.debug("class = " + parameter.getClass.getName + "; name = " + parameter.name)
          if (parameter.name == menta.model.Constant.stringLiteral) {
            str = parameter.toString;
          } else if (parameter.name == menta.model.Constant.trVariable) {
            context.addEntryInvariant(solutionNodeActionIndividual.actionClass.uri, new URI(Constant.currentNode), List(solutionNodeActionIndividual))
            str = parameter.asInstanceOf[TrVariable].apply(translationNode, solutionNode, context, strategy, mode, conditionExpression).toString();
            //clean scope
            context.removeScope(solutionNodeActionIndividual.actionClass.uri);
          } else if (parameter.name == menta.model.Constant.trApplyTemplates) {
            //TODO: get new mode from template for mode and conditionExpression
            str = deep(translationNode, solutionNode, context, strategy, mode, conditionExpression)
          }
          res += str
        }
        new StringLiteral(res)
      } else {
        LOG.warn("ApplyTemplate, StrategyRule does not have template. Solutionnode = {}", solutionNode)
        solutionNode
      }
    }
  }

  def deep(translationNode: AddIndividual, solutionNode: HowTo, context: Context, strategy: TranslationStrategy, mode: StringLiteral, conditionExpression: LogicalExpression): String = {

    var str = ""
    if (solutionNode.isInstanceOf[ActionIndividual] && solutionNode.asInstanceOf[ActionIndividual].parameters != null) {
      for (val subNode <- solutionNode.asInstanceOf[ActionIndividual].parameters) {
        //process only children that meet condition
        //TODO refactor conditional templates
        //if (conditionExpression.evaluate().valueBoolean) {
        str += apply(translationNode, subNode, context, strategy, mode, conditionExpression)
        //}
      }
    }
    str
  }
}

object TrApplyTemplates extends TrMatcher {

  def unapply(individual: AddIndividual): Option[TrWrapper] = {
    if (individual.actionClass.uri.toString.contains(menta.model.Constant.trApplyTemplates)) {
      Some(new TrVariable(individual))
    } else {
      None
    }
  }
}