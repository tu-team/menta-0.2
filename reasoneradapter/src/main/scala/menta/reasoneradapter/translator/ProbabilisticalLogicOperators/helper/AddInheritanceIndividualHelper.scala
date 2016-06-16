package menta.reasoneradapter.translator.ProbabilisticalLogicOperators.helper

/**
 * @author talanovm
 * Date: 30.09.11
 * Time: 20:15
 */

class AddInheritanceIndividualHelper extends AddProbabilisticLogicOperatorIndividualHelper {

  override def actionHelperClass = new AddInheritanceHelper()

  val _actionClass = actionHelperClass.apply()

  override def actionClass = this._actionClass
}