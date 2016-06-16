package menta.reasoneradapter.translator.ProbabilisticalLogicOperators.helper

/**
 * @author talanov max
 * Date: 29.09.11
 * Time: 14:43
 */

class AddImplicationIndividualHelper extends AddProbabilisticLogicOperatorIndividualHelper {

  override def actionHelperClass = new AddImplicationHelper()

  val _actionClass = actionHelperClass.apply()

  override def actionClass = this._actionClass
}