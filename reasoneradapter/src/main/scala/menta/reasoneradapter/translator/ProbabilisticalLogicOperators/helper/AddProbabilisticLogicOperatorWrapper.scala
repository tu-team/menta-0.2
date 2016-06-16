package menta.reasoneradapter.translator.ProbabilisticalLogicOperators.helper

import menta.model.howto.AddIndividual

/**
 * @author talanovm
 * Date: 30.09.11
 * Time: 20:30
 */

abstract class AddProbabilisticLogicOperatorWrapper{

  var _leftOperand: AddIndividual = null
  var _rightOperand: AddIndividual = null
  var _destination: AddIndividual = null
  var _addIndividual: AddIndividual = null

  def addIndividualHelper() :AddProbabilisticLogicOperatorIndividualHelper
  def addIndividual(): AddIndividual = {
    this._addIndividual = this.addIndividualHelper().apply(_leftOperand, _rightOperand, _destination)
    this._addIndividual
  }

  def leftOperand = this._leftOperand
  def leftOperand_=(aLeftOperand: AddIndividual) {
    this._leftOperand = aLeftOperand
    this._addIndividual = this.addIndividualHelper().apply(_leftOperand, _rightOperand, _destination)
  }

  def rightOperand = this._rightOperand
  def rightOperand_=(aRightOperand : AddIndividual) {
    this._rightOperand = aRightOperand
    this._addIndividual = this.addIndividualHelper().apply(_leftOperand, _rightOperand, _destination)
  }

  def destination = this._destination
  def destination_=(aDestination : AddIndividual) {
    this._destination = aDestination
    this._addIndividual = this.addIndividualHelper().apply(_leftOperand, _rightOperand, _destination)
  }

}