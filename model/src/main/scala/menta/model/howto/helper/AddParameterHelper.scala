package menta.model.howto.helper

import menta.model.howto.{AddClass, HowTo}
import java.net.URI
import menta.model.Constant

/**
 * @author talanovm
 * Date: 23.09.11
 * Time: 17:07
 */

class AddParameterHelper {

  def apply(name: String, _type: HowTo, cardinality: Int): AddClass = {
    val res = new AddClass(new AddClass(), List[HowTo]((new AddNameHelper).apply(name), (new AddTypeHelper).apply(_type), (new AddCardinalityHelper).apply(cardinality)))
    res.name = Constant.parameter
    res.uri = new URI(menta.model.Constant.modelNamespaceString + name)
    res
  }


}