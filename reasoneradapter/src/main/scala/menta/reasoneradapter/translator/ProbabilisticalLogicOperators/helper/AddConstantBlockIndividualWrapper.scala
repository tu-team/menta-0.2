package menta.reasoneradapter.translator.ProbabilisticalLogicOperators.helper

import menta.model.howto.{AddIndividual, HowTo}
import java.security.InvalidParameterException

/**
 * @author toschev alex
 * Date: 21.11.11
 * Time: 19:09
 */

class AddConstantBlockIndividualWrapper(aParameters: List[HowTo], @deprecated aDestination: AddIndividual) extends AddProbabilisticLogicOperatorWrapper {
  this._destination = aDestination
  var _operands = aParameters

  def actionHelperClass = new AddBlockHelper()

  val _actionClass = actionHelperClass.apply()

  def actionClass = this._actionClass

  def apply(leftOperand: HowTo, rightOperand: HowTo, destination: AddIndividual): AddIndividual = {
    throw new InvalidParameterException("Not implemented, as there should not be left operand parameter specified")
    null
  }


  def addIndividualHelper(): AddConstantBlockIndividualHelper = new AddConstantBlockIndividualHelper()

  override def addIndividual(): AddIndividual = this.addIndividualHelper().apply(_operands, _destination)

  def operands = this._operands

  def operands_=(aParameters: List[HowTo]) {
    this._operands = aParameters
  }
}