package menta.model.util.serialization.protocols

import sbinary._
import DefaultProtocol._
import Operations._
import menta.model.howto.Terminal

/**
 * User: toscheva
 * Date: 17.08.11
 * Time: 18:53
 */

/**
 * @class Protocol to serialize Terminal
 * see   http://code.google.com/p/sbinary/wiki/IntroductionToSBinary for more information
 */
object TerminalProtocol extends DefaultProtocol with HowToProtocol {

  implicit object TerminalFormat extends Format[Terminal] {
    def reads(in: Input) = {
      var res = new Terminal
      readProperties(in, res)
      res
    }

    def writes(out: Output, value: Terminal) = {
      writeProperties(out, value)
      //writeString(out,value.name);
    }
  }

}