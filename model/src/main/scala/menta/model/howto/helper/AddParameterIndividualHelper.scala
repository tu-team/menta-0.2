package menta.model.howto.helper

import menta.model.howto._

/**
 * @author max
 * @date 2011-09-25
 * Time: 10:04 PM
 */

class AddParameterIndividualHelper {
  def apply(actionClass: AddClass, value: HowTo, destination: AddIndividual) = {
    val res = new AddIndividual(actionClass, List[HowTo](value), destination)
    res
  }

  def apply(actionClass: AddClass, value: String, destination: AddIndividual) = {
    val res = new AddIndividual(actionClass, List[HowTo](new StringLiteral(value)), destination)
    res
  }

  def apply(actionClass: AddClass, value: String) = {
    val res = new AddIndividual(actionClass, List[HowTo](new StringLiteral(value)), null)
    res
  }

  def apply(actionClass: AddClass, value: Float, destination: AddIndividual) = {
    val res = new AddIndividual(actionClass, List[HowTo](new NumberLiteral(value)), destination)
    res
  }

  def apply(actionClass: AddClass, value: AddIndividual, destination: AddIndividual) = {
    val res = new AddIndividual(actionClass, List[HowTo](value), destination)
    res
  }

}