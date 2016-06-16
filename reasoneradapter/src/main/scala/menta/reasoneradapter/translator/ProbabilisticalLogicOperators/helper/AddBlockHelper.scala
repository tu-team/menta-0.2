package menta.reasoneradapter.translator.ProbabilisticalLogicOperators.helper

import menta.model.howto.AddClass
import menta.model.howto.helper.AddParameterHelper
import menta.reasoneradapter.constant.Constant

/**
 * @author talanovm
 * Date: 04.10.11
 * Time: 14:06
 */

class AddBlockHelper extends AddProbabilisticLogicOperatorHelper {

  _leftOperand = (new AddParameterHelper()).apply(menta.model.Constant.leftOperand, new AddClass(), 0)
  _rightOperand = (new AddParameterHelper()).apply(menta.model.Constant.rightOperand, new AddClass(), 1)
  _operatorClass = super.apply(this.className, this.leftOperand, this.rightOperand)

  override def className = Constant.NonTerminalURIs.block

  /**
   * Updates the Object of AddClass HowTo
   */
  def apply(aRightOperand: AddClass): AddClass = {
    this.rightOperand = aRightOperand
    val res: AddClass = super.apply(this.className, this._leftOperand, rightOperand)
    res
  }

}