package menta.reasoneradapter.translator.ProbabilisticalLogicOperators.helper

import menta.model.howto.AddIndividual

/**
 * @author semenov leonid
 * Date: 04.10.11
 * Time: 22:38
 */

class AddConditionIndividualWrapper( var _trueBlock: AddIndividual,var _falseBlock: AddIndividual, var _condition: AddIndividual,var _destination:AddIndividual) {

  /**
   * true case
   */
  val _addIndividualHelper: AddConditionIndividualHelper = new AddConditionIndividualHelper()
  var _addIndividual: AddIndividual = this._addIndividualHelper.apply(_trueBlock, _falseBlock, _condition,_destination)
  def trueBlock = this._trueBlock
  def trueBlock_=(aTrueBlock: AddIndividual) {
    this._trueBlock = aTrueBlock
    this._addIndividual = this._addIndividualHelper.apply(_trueBlock, _falseBlock, _condition,_destination)
  }

  /**
   * false case
   */
  def falseBlock = this._falseBlock
  def falseBlock_=(aFalseBlock : AddIndividual) {
    this._falseBlock = aFalseBlock
    this._addIndividual = this._addIndividualHelper.apply(_trueBlock, _falseBlock, _condition,_destination)
  }

  /**
   * apply destination
   */
  def destination = this._destination
  def destination_=(aDestination : AddIndividual) {
    this._destination = aDestination
    this._addIndividual = this._addIndividualHelper.apply(_trueBlock, _falseBlock, _condition,_destination)
  }

  /**
   * condition of statement
   */
  def condition=this._condition
  def condition_=(aCondition:AddIndividual)
  {
      this._condition= aCondition
      this._addIndividual = this._addIndividualHelper.apply(_trueBlock, _falseBlock, _condition,_destination)
  }

}