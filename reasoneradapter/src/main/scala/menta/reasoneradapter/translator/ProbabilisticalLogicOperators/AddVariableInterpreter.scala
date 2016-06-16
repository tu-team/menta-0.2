package menta.reasoneradapter.translator.ProbabilisticalLogicOperators

import menta.model.howto.ProbabilisticalLogicOperators.AddProbabilisticLogicOperatorWrapper
import menta.reasoneradapter.constant.Constant
import menta.model.howto.{AddIndividual, AddClass}

/**
 * @author toschev alex
 * Date: 21.10.11
 * Time: 17:19
 */

class AddVariableInterpreter {

}

object AddVariable {
  def unapply(howTo: AddClass): Option[AddProbabilisticLogicOperatorWrapper] = {
    if (howTo.uri.toString.contains(Constant.NonTerminalURIs.variable)) {
      Some(new AddVariableWrapper(howTo))
    } else {
      None
    }
  }

  def unapply(individual: AddIndividual): Option[AddProbabilisticLogicOperatorWrapper] = {
    if (individual.actionClass.uri.toString.contains(Constant.NonTerminalURIs.variable)) {
      Some(new AddVariableWrapper(individual))
    } else {
      None
    }
  }
}