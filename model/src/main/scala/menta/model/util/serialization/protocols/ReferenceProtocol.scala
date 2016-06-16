package menta.model.util.serialization.protocols

import menta.model.howto.Reference
import sbinary._
import DefaultProtocol._
import Operations._

/**
 * Created by IntelliJ IDEA.
 * User: toscheva
 * Date: 05.10.11
 * Time: 10:28
 * To change this template use File | Settings | File Templates.
 */

object ReferenceProtocol extends DefaultProtocol with HowToProtocol {

  implicit object ReferenceFormat extends Format[Reference] {
    def reads(in: Input) = {
      var res = new Reference
      readProperties(in, res)
      res
    }

    def writes(out: Output, value: Reference) = {
      writeProperties(out, value)
      //writeString(out,value.name);
    }
  }

}