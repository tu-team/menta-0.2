package menta.reasoneradapter.translator.ProbabilisticalLogicOperators.helper

import menta.model.howto.AddIndividual

/**
 * Equals operator HowTo wrapper.
 * @author toschev alexander
 * Date: 13.11.11
 * Time: 16:57
 */

class AddEqualIndividualWrapper(aLeftOperand: AddIndividual, aRightOperand: AddIndividual, @deprecated aDestination: AddIndividual) extends AddProbabilisticLogicOperatorWrapper {
  this._destination = aDestination
  this._leftOperand = aLeftOperand
  this._rightOperand = aRightOperand

  def this(aLeftOperand: AddIndividual, aRightOperand: AddIndividual) = this(aLeftOperand, aRightOperand, null)

  def addIndividualHelper(): AddProbabilisticLogicOperatorIndividualHelper = new AddEqualIndividualHelper()
}