package menta.reasoneradapter.translator.ProbabilisticalLogicOperators.helper

/**
 * @author talanovm
 * Date: 29.09.11
 * Time: 9:35
 */

class AddConjunctionIndividualHelper extends AddProbabilisticLogicOperatorIndividualHelper {
  /**
   * cached AddConjunction Class.
   */
  override def actionHelperClass = new AddConjunctionHelper()

  val _actionClass = actionHelperClass.apply()

  override def actionClass = this._actionClass

}