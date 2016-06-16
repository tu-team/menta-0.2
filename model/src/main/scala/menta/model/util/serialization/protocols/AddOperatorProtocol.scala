package menta.model.util.serialization.protocols

import sbinary._
import menta.model.howto.AddOperator

/**
 * Created by IntelliJ IDEA.
 * User: toscheva
 * Date: 18.08.11
 * Time: 20:48
 * To change this template use File | Settings | File Templates.
 */

object AddOperatorProtocol extends DefaultProtocol with ActionClassProtocol {

  implicit object AddOperatorFormat extends Format[AddOperator] {
    def reads(in: Input) = {
      var res = new AddOperator
      readProperties(in, res)
      res
    }

    def writes(out: Output, value: AddOperator) = {
      writeProperties(out, value)
      //writeString(out,value.name);
    }
  }

}