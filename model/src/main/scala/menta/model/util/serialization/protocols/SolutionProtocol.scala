package menta.model.util.serialization.protocols

import menta.model.conversation.Response
import sbinary.Operations._
import java.net.URI
import sbinary.{Output, Input, Format, DefaultProtocol}
import menta.model.howto.Solution

/**
 * Created by IntelliJ IDEA.
 * User: toscheva
 * Date: 06.12.11
 * Time: 19:07
 * To change this template use File | Settings | File Templates.
 */

object SolutionProtocol extends DefaultProtocol with KnowledgeProtocol {

  implicit object SolutionFormat extends Format[Solution] {
    def reads(in: Input) = {
      var res = new Solution();
      readProperties(in, res);
      res
    }

    def writes(out: Output, value: Solution) = {
      writeProperties(out, value)
      //writeString(out,value.name);

      //write[Date](out, value.dateTime)
    }
  }

}