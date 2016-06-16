package menta.reasoneradapter.translator.ProbabilisticalLogicOperators.helper

import menta.reasoneradapter.constant.Constant
import menta.model.howto.AddClass
import menta.model.howto.helper.AddParameterHelper
import java.net.URI

/**
 * @author talanov max
 * @date 2011-10-01
 * Time: 4:20 PM
 */

class AddLoopHelper extends AddProbabilisticLogicOperatorHelper {

  _leftOperand = (new AddParameterHelper()).apply(menta.model.Constant.leftOperand, new AddClass(), 1)
  _rightOperand = (new AddParameterHelper()).apply(menta.model.Constant.rightOperand, new AddClass(), 1)
  var _bodyOperand: AddClass = (new AddParameterHelper()).apply(menta.model.Constant.bodyOperand, new AddClass(), 1)
  _operatorClass = {
    val res: AddClass = super.apply(this.className, this.leftOperand, this.rightOperand)
    res.parameters = res.parameters ::: List[AddClass](this.bodyOperand)
    res
  }

  override def className = Constant.NonTerminalURIs.loop

  /**
   * Updates the Object of AddClass HowTo
   */
  def apply(aName: String, aLeftOperand: AddClass, aRightOperand: AddClass, aBody: AddClass): AddClass = {
    this.leftOperand = aLeftOperand
    this.rightOperand = aRightOperand
    this.bodyOperand = (new AddParameterHelper()).apply(menta.model.Constant.bodyOperand, new AddClass(), 1)
    val res: AddClass = super.apply(aName, this.leftOperand, this.rightOperand)
    res.parameters = res.parameters ::: List[AddClass](this.bodyOperand)
    res.uri = new URI(menta.model.Constant.modelNamespaceString + Constant.NonTerminalURIs.loop)
    res
  }

  def bodyOperand = this._bodyOperand
  def bodyOperand_=(aBodyOperand: AddClass) {
    this._bodyOperand = aBodyOperand
  }
}