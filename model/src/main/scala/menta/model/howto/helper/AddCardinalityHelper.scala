package menta.model.howto.helper

import menta.model.howto.{NumberLiteral, HowTo, AddClass}

/**
 * @author talanovm
 * Date: 23.09.11
 * Time: 18:32
 */

class AddCardinalityHelper {
  def apply(cardinality: Int): AddClass = {
    val res = new AddClass( new AddClass(), List[HowTo](new NumberLiteral(cardinality)))
    res.name="AddCardinalityHelper"
    res
  }
}