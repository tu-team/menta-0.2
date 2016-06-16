package menta.model.howto.helper

import menta.model.howto.{Reference, AddClass, StringLiteral, HowTo}

/**
 * @author talanovm
 * Date: 23.09.11
 * Time: 18:24
 */

class AddTypeHelper {
  def apply(_type: HowTo): AddClass = {
    val res: AddClass = new AddClass(new AddClass (), List[HowTo] (new Reference(_type) ) )
    res.name="AddTypeHelper"
    res
  }
}