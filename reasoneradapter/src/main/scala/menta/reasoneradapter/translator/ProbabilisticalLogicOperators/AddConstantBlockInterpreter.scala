package menta.reasoneradapter.translator.ProbabilisticalLogicOperators

import menta.model.howto.ProbabilisticalLogicOperators.AddProbabilisticLogicOperatorWrapper
import menta.reasoneradapter.constant.Constant
import menta.model.howto.{AddIndividual, AddClass}

/**
 * Created by IntelliJ IDEA.
 * User: Alexander
 * Date: 20.11.11
 * Time: 10:08
 * To change this template use File | Settings | File Templates.
 */

/*
   Place description here
   @author 
    Alexander 
*/
class AddConstantBlockInterpreter {

}
object AddConstantBlock {
  def unapply(howTo: AddClass): Option[AddProbabilisticLogicOperatorWrapper] = {
    if (howTo.uri.toString.contains(Constant.NonTerminalURIs.constantBlock)) {
      Some(new AddBlockInterpreter(howTo.uri, howTo.parameters))
    } else {
      None
    }
  }

  def unapply(individual: AddIndividual): Option[AddProbabilisticLogicOperatorWrapper] = {
    if (individual.actionClass.uri.toString.contains(Constant.NonTerminalURIs.constantBlock)) {
      Some(new AddBlockInterpreter(individual.uri, individual.parameters))
    } else {
      None
    }
  }
}