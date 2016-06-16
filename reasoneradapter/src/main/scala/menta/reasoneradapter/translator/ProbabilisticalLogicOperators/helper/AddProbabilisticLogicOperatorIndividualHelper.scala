package menta.reasoneradapter.translator.ProbabilisticalLogicOperators.helper

import menta.model.howto.helper.AddOperatorIndividualHelper
import menta.model.howto.{AddClass, AddIndividual, HowTo}

/**
 * @author talanovm
 * Date: 27.09.11
 * Time: 18:02
 */

abstract class AddProbabilisticLogicOperatorIndividualHelper extends AddOperatorIndividualHelper {
  /**
   * cached AddConjunction Class.
   */
  def actionHelperClass: AddProbabilisticLogicOperatorHelper
  def actionClass: AddClass
  /**
   * Constructs the ProbabilisticLogicOperator HowTo with fixed actionClass.
   */
  def apply(leftOperand: HowTo, rightOperand: HowTo, destination: AddIndividual): AddIndividual = {
    val res = super.apply(actionClass, leftOperand, rightOperand, destination)
    res
  }

  def leftOperandClass = actionHelperClass.leftOperand
  def rightOperandClass = actionHelperClass.rightOperand

}