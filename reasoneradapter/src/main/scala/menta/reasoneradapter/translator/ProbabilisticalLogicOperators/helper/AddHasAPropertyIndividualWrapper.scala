package menta.reasoneradapter.translator.ProbabilisticalLogicOperators.helper

import menta.model.howto.AddIndividual

/**
 * @author max
 * @date 2011-10-02
 * Time: 10:20 PM
 */

class AddHasAPropertyIndividualWrapper (aLeftOperand: AddIndividual, aRightOperand: AddIndividual, aDestination: AddIndividual) extends  AddProbabilisticLogicOperatorWrapper {
  this._destination = aDestination
  this._leftOperand = aLeftOperand
  this._rightOperand = aRightOperand

  def addIndividualHelper():AddHasAPropertyIndividualHelper = new AddHasAPropertyIndividualHelper()
}