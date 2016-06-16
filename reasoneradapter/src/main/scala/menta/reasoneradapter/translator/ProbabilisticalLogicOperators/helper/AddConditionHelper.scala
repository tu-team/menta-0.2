package menta.reasoneradapter.translator.ProbabilisticalLogicOperators.helper

import menta.reasoneradapter.constant.Constant
import menta.model.howto.helper.AddParameterHelper
import menta.model.howto.AddClass
import java.net.URI

/**
 * Class to create reasoner conditional HowTo.
 * meta language syntax:
 * @author semenov leonid, max talanov
 * Date: 04.10.11
 * Time: 22:38
 */

class AddConditionHelper extends AddProbabilisticLogicOperatorHelper {

  object ClassNames {
    def trueBlock = "trueBlock"

    def falseBodyBlock = "falseBodyBlock"

    def conditionBlock = "conditionBlock"
  }

  override def className = Constant.NonTerminalURIs.condition

  /**
   * true block that is interpreted if the condition expression result is more than or equals 0.5
   */
  var _trueBlock: AddClass = (new AddParameterHelper()).apply(ClassNames.trueBlock, new AddClass(), 1)

  def trueBlock = this._trueBlock

  def trueBlock_=(aBodyBlock: AddClass) {
    this._trueBlock = aBodyBlock
  }


  /**
   * false block that is interpreted if the condition expression result is less than 0.5
   */
  var _falseBodyBlock: AddClass = (new AddParameterHelper()).apply(ClassNames.falseBodyBlock, new AddClass(), 1)

  def falseBodyBlock = this._falseBodyBlock

  def falseBodyBlock_=(aBodyBlock: AddClass) {
    this._falseBodyBlock = aBodyBlock
  }

  /**
   * condition block of HowTo-s
   */
  var _conditionBlock: AddClass = (new AddParameterHelper()).apply(ClassNames.conditionBlock, new AddClass(), 1)

  def conditionBlock = this._conditionBlock

  def conditionBlock_=(aBodyBlock: AddClass) {
    this._conditionBlock = aBodyBlock
  }

  _operatorClass = {
    val res: AddClass = super.apply(this.className, this.trueBlock, this.falseBodyBlock)
    res.parameters = res.parameters ::: List[AddClass](this.conditionBlock)
    res
  }

  /**
   * Creates the AddClass instance of the Conditional HowTo class.
   * @param aName the string name of the class to be created.
   * @param aTrueBlock the AddClass to define the block fo the HowTo-s to be interpreted if the condition expression result is more than or equals 0.5
   * @param aFalseBlock the AddClass to define the block fo the HowTo-s to be interpreted if the condition expression result is less than 0.5
   * @returns AddClass
   */
  def apply(aName: String, aTrueBlock: AddClass, aFalseBlock: AddClass, aConditionExpression: AddClass): AddClass = {
    this.leftOperand = aTrueBlock
    this.rightOperand = aFalseBlock
    this.conditionBlock = aConditionExpression
    val res: AddClass = super.apply(aName, this.leftOperand, this.rightOperand)
    res.parameters = res.parameters ::: List[AddClass](this.conditionBlock)
    res.uri = new URI(menta.model.Constant.modelNamespaceString + Constant.NonTerminalURIs.condition)
    res
  }

}