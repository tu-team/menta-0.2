package menta.reasoneradapter.translator.ProbabilisticalLogicOperators.helper

import menta.model.howto.AddIndividual

/**
 * @author max
 * @date 2011-10-01
 * Time: 4:23 PM
 */

class AddLoopIndividualWrapper(aLeftOperand: AddIndividual, aRightOperand: AddIndividual, aBody: AddIndividual, aDestination: AddIndividual)
  extends AddProbabilisticLogicOperatorWrapper {
  this._destination = aDestination
  this._leftOperand = aLeftOperand
  this._rightOperand = aRightOperand
  var _body = aBody

  def addIndividualHelper(): AddLoopIndividualHelper = new AddLoopIndividualHelper()
  override def addIndividual(): AddIndividual = this.addIndividualHelper().apply(_leftOperand, _rightOperand, _body, _destination)

  def body = this._body

  def body_=(aBody: AddIndividual) {
    this._body = aBody
  }

}