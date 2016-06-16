package menta.reasoneradapter.translator.ProbabilisticalLogicOperators.helper

import menta.model.howto.AddIndividual

/**
 * @author talanovm
 * Date: 29.09.11
 * Time: 13:17
 */

class AddConjunctionIndividualWrapper(var _leftOperand: AddIndividual, var _rightOperand: AddIndividual, var _destination: AddIndividual) {
  val _addIndividualHelper: AddConjunctionIndividualHelper = new AddConjunctionIndividualHelper()
  var _addIndividual: AddIndividual = this._addIndividualHelper.apply(_leftOperand, _rightOperand, _destination)
  def leftOperand = this._leftOperand
  def leftOperand_=(aLeftOperand: AddIndividual) {
    this._leftOperand = aLeftOperand
    this._addIndividual = this._addIndividualHelper.apply(_leftOperand, _rightOperand, _destination)
  }

  def rightOperand = this._rightOperand
  def rightOperand_=(aRightOperand : AddIndividual) {
    this._rightOperand = aRightOperand
    this._addIndividual = this._addIndividualHelper.apply(_leftOperand, _rightOperand, _destination)
  }

  def destination = this._destination
  def destination_=(aDestination : AddIndividual) {
    this._destination = aDestination
    this._addIndividual = this._addIndividualHelper.apply(_leftOperand, _rightOperand, _destination)
  }

}