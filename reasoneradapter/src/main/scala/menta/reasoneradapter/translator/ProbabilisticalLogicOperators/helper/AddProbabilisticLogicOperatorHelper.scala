package menta.reasoneradapter.translator.ProbabilisticalLogicOperators.helper

import menta.model.howto.helper.{AddParameterHelper, AddOperatorHelper}
import menta.model.howto.AddClass

/**
 * @author talanovm
 * Date: 26.09.11
 * Time: 10:56
 */

abstract class AddProbabilisticLogicOperatorHelper() extends AddOperatorHelper {

  override def className: String = this.getClass.getName // this is not really important here.

  /**
   * Returns reference for the Object of AddClass HowTo previously created during class initialisation.
   */
  def apply() = {
    this._operatorClass
  }

  /**
   * Updates the Object of AddClass HowTo
   */
  override def apply(aName: String, leftOperand: AddClass, rightOperand: AddClass): AddClass = {
    this._leftOperand = leftOperand
    this._rightOperand = rightOperand
    this._operatorClass = super.apply(className, this._leftOperand, this._rightOperand)
    this._operatorClass.name = aName
    this._operatorClass
  }

  def name = this._operatorClass.name

  def name_=(aName: String) {
    this._operatorClass.name = aName
  }

}