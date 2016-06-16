package menta.reasoneradapter.translator.ProbabilisticalLogicOperators.helper

import menta.model.howto.{AddIndividual, HowTo}

/**
 * @author talanovm
 * Date: 04.10.11
 * Time: 12:17
 */

class AddBlockIndividualWrapper(aParameters: List[HowTo], aDestination: AddIndividual) extends AddProbabilisticLogicOperatorWrapper {
  this._destination = aDestination
  var _operands = aParameters

  def addIndividualHelper(): AddBlockIndividualHelper = new AddBlockIndividualHelper()
  override def addIndividual(): AddIndividual = this.addIndividualHelper().apply(_operands, _destination)

  def operands = this._operands

  def operands_=(aParameters: List[HowTo]) {
    this._operands = aParameters
  }
}