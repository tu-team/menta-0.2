package menta.model.howto.helper

import menta.model.howto.{StringLiteral, AddClass, HowTo}
import java.net.URI
import menta.model.Constant

/**
 * @author talanov m
 * Date: 23.09.11
 * Time: 16:55
 */

class AddOperatorHelper {

  def className: String = Constant.operatorClassName

  var _leftOperand: AddClass = (new AddParameterHelper()).apply(menta.model.Constant.leftOperand, new AddClass(), 1)
  var _rightOperand: AddClass = (new AddParameterHelper()).apply(menta.model.Constant.rightOperand, new AddClass(), 1)
  var _operatorClass: AddClass = this.apply(this.className, _leftOperand, _rightOperand)

  def apply (name: String, leftOperand: AddClass, rightOperand: AddClass): AddClass = {
    val res = new AddClass(new AddClass (), List[HowTo] (new StringLiteral(name), leftOperand, rightOperand ) )
    res.uri = new URI(menta.model.Constant.modelNamespaceString + name)
    res
  }

  def leftOperand: AddClass = this._leftOperand
  def leftOperand_=(aLeftOperand: AddClass) = this._leftOperand = aLeftOperand
  def rightOperand: AddClass = this._rightOperand
  def rightOperand_=(aRightOperand: AddClass) = this._rightOperand = aRightOperand

}