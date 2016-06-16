package menta.model.howto.helper

import menta.model.howto.{AddIndividual, HowTo, AddClass}

/**
 * @author talanov max
 * @date 2011-09-25
 * Time: 10:20 PM
 */

class AddOperatorIndividualHelper {
  /**
   * Creates the AddAddIndividual using actionClass, leftOperand, rightOperand, destination.
   * @actionClass the AddClass of AddIndividual.
   * @leftOperand the value of leftOperand.
   * @rightOperand the value of rightOperand.
   * @destination the destination AddAddIndividual for the HowTo to be used.
   */
  def apply(actionClass: AddClass, leftOperand: HowTo, rightOperand: HowTo, destination: AddIndividual): AddIndividual = {
    val res = new AddIndividual(actionClass, List[HowTo](leftOperand, rightOperand), destination)
    res
  }

  /**
   * Creates the AddAddIndividual using actionClass, leftOperand, rightOperand, destination.
   * @actionClass the AddClass of AddIndividual.
   * @aOperands the list of operands values.
   * @rightOperand the value of rightOperand.
   * @destination the destination AddAddIndividual for the HowTo to be used.
   */
  def apply(actionClass: AddClass, aOperands: List[HowTo], destination: AddIndividual): AddIndividual = {
    val res = new AddIndividual(actionClass, aOperands, destination)
    res
  }
}