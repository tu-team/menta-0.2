package menta.reasoneradapter.translator.ProbabilisticalLogicOperators.helper

import menta.model.howto.{HowTo, AddIndividual}
import java.security.InvalidParameterException

/**
 * @author talanovm
 * Date: 04.10.11
 * Time: 12:45
 */

class AddBlockIndividualHelper extends AddProbabilisticLogicOperatorIndividualHelper {

  override def actionHelperClass = new AddBlockHelper()

  val _actionClass = actionHelperClass.apply()

  override def actionClass = this._actionClass

  override def apply(leftOperand: HowTo, rightOperand: HowTo, destination: AddIndividual): AddIndividual = {
    throw new InvalidParameterException("Not implemented, as there should be body parameter specified")
    null
  }

  def apply(aParameters: List[HowTo], aDestination: AddIndividual): AddIndividual = {
    val res = super.apply(this.actionClass, aParameters, aDestination)
    res
  }
}