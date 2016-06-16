package menta.reasoneradapter.translator.ProbabilisticalLogicOperators

import menta.model.howto.ProbabilisticalLogicOperators.AddProbabilisticLogicOperatorWrapper
import java.net.URI
import menta.model.howto.Context
import menta.model.howto.{AddIndividual, HowTo}
import org.slf4j.{Logger, LoggerFactory}
import menta.reasoneradapter.translator.ActionConverter
import menta.reasoneradapter.translator.Convertters.VariableConverter

/**
 * Creates the variable description.
 * @author talanov max
 * Date 24.05.11
 */

class AddVariableWrapper(howTo: HowTo) extends AddProbabilisticLogicOperatorWrapper {

  val log: Logger = LoggerFactory.getLogger(this.getClass)

  def value(context: Context, scope: URI): Option[List[HowTo]] = {
    for (val scope: Map[URI, List[HowTo]] <- context.contents.get(scope)) {
      return scope.get(howTo.uri)
    }
    None
  }

  override def converter: ActionConverter = {
    return new VariableConverter
  }

  def value(aContext: Context, aScope: URI, aVariableURI: URI): Option[List[HowTo]] = {

    //log.debug("scope: {}, context {}", aScope, aContext.contents.get(aScope))
    return aContext.value(aScope,aVariableURI)
  }

  def variableName(individual: AddIndividual): String = {
    individual.name
  }

  def unapply(howTo: HowTo): Option[AddProbabilisticLogicOperatorWrapper] = {
    if (howTo.uri.toString.contains(menta.reasoneradapter.constant.Constant.NonTerminalURIs.variable)) {
      Some(this)
    } else {
      None
    }
  }
}