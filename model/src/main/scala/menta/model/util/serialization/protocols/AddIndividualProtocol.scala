package menta.model.util.serialization.protocols

import sbinary._
import DefaultProtocol._
import Operations._
import menta.model.howto.AddIndividual

/**
 * @class Protocol to serialize AddIndividual
 * @see http://code.google.com/p/sbinary/wiki/IntroductionToSBinary for more information
 * @author talanovm
 * Date: 08.09.11
 * Time: 17:49
 */

object AddIndividualProtocol extends DefaultProtocol with ActionIndividualProtocol {

  implicit object AddIndividualFormat extends Format[AddIndividual] {
    def reads(in: Input): AddIndividual = {
      val res: AddIndividual = new AddIndividual()
      readProperties(in, res)
      res
    }

    def writes(out: Output, value: AddIndividual) {
      writeProperties(out, value)
      //writeString(out,value.name);
    }
  }

}