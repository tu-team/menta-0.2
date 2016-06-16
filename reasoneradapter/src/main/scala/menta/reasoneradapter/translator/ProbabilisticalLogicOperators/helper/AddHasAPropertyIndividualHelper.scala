package menta.reasoneradapter.translator.ProbabilisticalLogicOperators.helper

/**
 * @author max
 * @date 2011-10-02
 * Time: 10:12 PM
 */

class AddHasAPropertyIndividualHelper extends AddProbabilisticLogicOperatorIndividualHelper {

  override def actionHelperClass = new AddHasAPropertyHelper()

  val _actionClass = actionHelperClass.apply()

  override def actionClass = this._actionClass
}