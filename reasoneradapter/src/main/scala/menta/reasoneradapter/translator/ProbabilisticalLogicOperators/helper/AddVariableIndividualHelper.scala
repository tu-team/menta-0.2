package menta.reasoneradapter.translator.ProbabilisticalLogicOperators.helper

import menta.model.howto.{HowTo, AddIndividual}
import java.security.InvalidParameterException

/**
 * @author max talanov
 * @date 2011-10-02
 * Time: 10:44 PM
 */

class AddVariableIndividualHelper extends AddProbabilisticLogicOperatorIndividualHelper {

  override def actionHelperClass = new AddVariableHelper()

  val _actionClass = actionHelperClass.apply()

  override def actionClass = this._actionClass

  override def apply(leftOperand: HowTo, rightOperand: HowTo, destination: AddIndividual): AddIndividual = {
    throw new InvalidParameterException("Not implemented, as there should not be left operand parameter specified")
    null
  }

  /**
   * Creates AddIndividual with one operand, destination.
   * @param aFalseBlock HowTo, value
   * @param aDestination AddIndividual
   * @return AddIndividual
   */
  def apply(aRightOperand: HowTo, aDestination: AddIndividual): AddIndividual = {
    val res = new AddIndividual(actionClass, List[HowTo](aRightOperand), aDestination)
    res
  }

}