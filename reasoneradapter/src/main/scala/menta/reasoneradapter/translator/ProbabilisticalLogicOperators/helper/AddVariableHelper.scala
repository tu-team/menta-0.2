package menta.reasoneradapter.translator.ProbabilisticalLogicOperators.helper

import menta.reasoneradapter.constant.Constant
import menta.model.howto.{StringLiteral, HowTo, AddClass}

/**
 * @author talanov max
 * @date 2011-10-02
 * Time: 10:49 PM
 */

class AddVariableHelper extends AddProbabilisticLogicOperatorHelper {

  override def className = Constant.NonTerminalURIs.variable

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