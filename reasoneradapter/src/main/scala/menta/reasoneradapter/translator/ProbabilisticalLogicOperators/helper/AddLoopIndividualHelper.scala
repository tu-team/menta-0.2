package menta.reasoneradapter.translator.ProbabilisticalLogicOperators.helper

import menta.model.howto.{AddIndividual, HowTo}
import collection.immutable.List
import java.security.InvalidParameterException

/**
 * @author max
 * @date 2011-10-01
 * Time: 4:16 PM
 */

class AddLoopIndividualHelper extends AddProbabilisticLogicOperatorIndividualHelper {

  override def actionHelperClass = new AddLoopHelper()

  val _actionClass = actionHelperClass.apply()

  override def actionClass = this._actionClass

  override def apply(leftOperand: HowTo, rightOperand: HowTo, destination: AddIndividual): AddIndividual = {
    throw new InvalidParameterException("Not implemented, as there should be body parameter specified")
    null
  }

  /**
   * Creates the AddIndividual of AddLoopIndividual based:
   * @param aTrueBlock AddVariableIndividual the variable used in list as iterator value.
   * @param aFalseBlock AddVariableIndividual the reference to the list.
   * @param aConditionExpression the list of AddIndividual-s to execute in loop.
   * @param aDestination the AddIndividual of the destination, mainly should be null.
   * @returns AddIndividual AddLoopIndividual
   */
  def apply(aLeftOperand: HowTo, aRightOperand: HowTo, aBody: HowTo, aDestination: AddIndividual): AddIndividual = {
    val res = super.apply(actionClass, aLeftOperand, aRightOperand, aDestination)
    res.parameters = res.parameters ::: List(aBody)
    res
  }

   /**
   * Returns the value variable of loop(static).
   * @param loopIndividual is the Instance of AddLoopIndividual
   * @returns AddIndividual of value variable
   */
  def valueVariable(loopIndividual: AddIndividual): AddIndividual = {
    loopIndividual.parameters(0).asInstanceOf[AddIndividual]
  }

  /**
   * Returns the reference to list of loop(static).
   * @param loopIndividual is the Instance of AddLoopIndividual
   * @returns AddIndividual of list reference
   */
  def listReference(loopIndividual: AddIndividual): AddIndividual = {
    var res=loopIndividual.parameters(1)
    res.asInstanceOf[AddIndividual]

  }

  /**
   * Returns the body block(static).
   * @param loopIndividual is the Instance of AddLoopIndividual
   * @returns AddIndividual of body
   */
  def bodyBlock(loopIndividual: AddIndividual): AddIndividual = {
    loopIndividual.parameters(2).asInstanceOf[AddIndividual]
  }

  /**
   * Returns the list of members of body block(static).
   * @param loopIndividual is the Instance of AddLoopIndividual
   * @returns List[AddIndividual] of body
   */
  def bodyAddIndividualList(loopIndividual: AddIndividual): List[AddIndividual] = {
    val block:AddIndividual = bodyBlock(loopIndividual)
    block.parameters.asInstanceOf[List[AddIndividual]]
  }

}