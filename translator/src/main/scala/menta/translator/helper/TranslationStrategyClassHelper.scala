package menta.translator.helper

import menta.model.howto.AddClass
import menta.model.howto.helper.{AddOperatorHelper, AddParameterHelper}
import menta.translator.constant
import constant.Constant


/**
 * @author talanovm
 * Date: 01.12.11
 * Time: 10:28
 */

class TranslationStrategyClassHelper extends AddOperatorHelper {

  _leftOperand = (new AddParameterHelper()).apply(menta.model.Constant.leftOperand, new AddClass(), 0)
  _rightOperand = (new AddParameterHelper()).apply(menta.model.Constant.rightOperand, new AddClass(), 1)
  _operatorClass = super.apply(this.className, this.leftOperand, this.rightOperand)

  override def className = Constant.translationStrategyClassName

  /**
   * Updates the Object of AddClass HowTo
   */
  def apply(aRightOperand: AddClass): AddClass = {
    this.rightOperand = aRightOperand
    val res: AddClass = super.apply(this.className, this._leftOperand, rightOperand)
    res
  }

  /**
   * Returns last created AddClass
   * @return last crated instance of AddClass.
   */
  def apply(): AddClass = this._operatorClass
}