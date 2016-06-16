package menta.reasoneradapter.translator.ProbabilisticalLogicOperators.helper

import menta.model.howto.{AddIndividual, HowTo}

/**
 * @author semenov leonid, talanov max
 * Date: 04.10.11
 * Time: 22:38
 */

class AddConditionIndividualHelper extends AddProbabilisticLogicOperatorIndividualHelper {

  override def actionHelperClass = new AddConditionHelper()

  val _actionClass = actionHelperClass.apply()

  override def actionClass = this._actionClass

   override def apply(leftOperand: HowTo, rightOperand: HowTo, destination: AddIndividual): AddIndividual = {
    throw new NoSuchMethodException("Not implemented, as there should be body parameter specified")
    null
  }

  /**
   * Creates the AddIndividual of AddLoopIndividual based:
   * @param aTrueBlock true block that is interpreted if the condition expression result is more than or equals 0.5.
   * @param aFalseBlock false block that is interpreted if the condition expression result is less than 0.5
   * @param aConditionExpression condition block of HowTo-s block.
   * @param aDestination the AddIndividual of the destination, mainly should be null.
   * @returns AddIndividual
   */
  def apply(aTrueBlock: HowTo, aFalseBlock: HowTo, aConditionExpression: HowTo, aDestination: AddIndividual): AddIndividual = {
    val res = super.apply(actionClass, aTrueBlock, aFalseBlock, aDestination)
    res.parameters = res.parameters ::: List(aConditionExpression)
    res
  }

  /**
   * Returns true block of conditional AddIndividual.
   * @param conditionalIndividual the individual to process as conditional
   * @returns AddIndividual of the true block
   */
  def trueBlock(conditionalIndividual: AddIndividual): AddIndividual = {
    conditionalIndividual.parameters(0).asInstanceOf[AddIndividual]
  }

  /**
   * Returns false block of conditional AddIndividual.
   * @param conditionalIndividual the individual to process as conditional
   * @returns AddIndividual of the false block
   */
  def falseBlock(anIndividual: AddIndividual): AddIndividual = {
    anIndividual.parameters(1).asInstanceOf[AddIndividual]
  }

  /**
   * Returns condition expression of conditional AddIndividual.
   * @param conditionalIndividual the individual to process as conditional
   * @returns AddIndividual of the condition expression
   */
  def conditionExpression(anIndividual: AddIndividual): AddIndividual = {
    anIndividual.parameters(2).asInstanceOf[AddIndividual]
  }

  /**
   * Returns true block members list of conditional AddIndividual.
   * @param conditionalIndividual the individual to process as conditional
   * @returns List[AddIndividual] of the trueBlock
   */
  def trueBlockAddIndividualList(aIndividual: AddIndividual): List[AddIndividual] = {
    val block:AddIndividual = trueBlock(aIndividual)
    block.parameters.asInstanceOf[List[AddIndividual]]
  }

  /**
   * Returns false block members list of conditional AddIndividual.
   * @param conditionalIndividual the individual to process as conditional
   * @returns List[AddIndividual] of the falseBlock
   */
  def falseBlockAddIndividualList(aIndividual: AddIndividual): List[AddIndividual] = {
    val block:AddIndividual = falseBlock(aIndividual)
    block.parameters.asInstanceOf[List[AddIndividual]]
  }

  /**
   * Returns condition block members list of conditional AddIndividual.
   * @param conditionalIndividual the individual to process as conditional
   * @returns List[AddIndividual] of the falseBlock
   */
  def conditionBlockAddIndividualList(aIndividual: AddIndividual): List[AddIndividual] = {
    val block:AddIndividual = conditionExpression(aIndividual)
    block.parameters.asInstanceOf[List[AddIndividual]]
  }


}