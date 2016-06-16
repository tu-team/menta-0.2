package menta.reasoneradapter.translator.ProbabilisticalLogicOperators.helper

import menta.model.howto.{AddIndividual, HowTo}
import java.security.InvalidParameterException

/**
 * @author toschev alex
 * Date: 21.11.11
 * Time: 19:18
 */

class AddConstantBlockIndividualHelper  extends AddProbabilisticLogicOperatorIndividualHelper {

  override def actionHelperClass = new AddConstantBlockHelper()

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