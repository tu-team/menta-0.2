package menta.reasoneradapter.translator.ProbabilisticalLogicOperators.helper

import menta.reasoneradapter.constant.Constant
import menta.model.howto.{StringLiteral, HowTo, AddClass}

/**
 * @author max
 * @date 2011-10-02
 * Time: 4:00 PM
 */

class AddNegationHelper extends AddProbabilisticLogicOperatorHelper {

  override def className = Constant.NonTerminalURIs.negation

  /**
   * Updates the Object of AddClass HowTo
   */
  def apply(aName: String, aRightOperand: AddClass): AddClass = {
    this.rightOperand = aRightOperand
    this.name = aName
    val res: AddClass = new AddClass(new AddClass(), List[HowTo](new StringLiteral(name), this.rightOperand))
    res
  }
}