package menta.reasoneradapter.translator.ProbabilisticalLogicOperators.helper

import menta.model.howto.AddIndividual

/**
 * @author max
 * @date 2011-10-02
 * Time: 8:01 PM
 */

class AddNegationIndividualWrapper (aRightOperand: AddIndividual, aDestination: AddIndividual)
  extends AddProbabilisticLogicOperatorWrapper {
  this._destination = aDestination
  this._rightOperand = aRightOperand

  def addIndividualHelper(): AddNegationIndividualHelper = new AddNegationIndividualHelper()
  override def addIndividual(): AddIndividual = this.addIndividualHelper().apply(_rightOperand, _destination)
}