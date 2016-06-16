package menta.reasoneradapter.translator.ProbabilisticalLogicOperators.helper

import menta.model.howto.AddIndividual

/**
 * @author talanovm
 * Date: 30.09.11
 * Time: 20:29
 */

class AddInheritanceIndividualWrapper(aLeftOperand: AddIndividual, aRightOperand: AddIndividual, aDestination: AddIndividual) extends  AddProbabilisticLogicOperatorWrapper {
  this._destination = aDestination
  this._leftOperand = aLeftOperand
  this._rightOperand = aRightOperand

  def addIndividualHelper() :AddProbabilisticLogicOperatorIndividualHelper = new AddInheritanceIndividualHelper()

}