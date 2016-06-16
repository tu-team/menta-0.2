package menta.model.util.serialization.protocols

import sbinary._
import DefaultProtocol._
import Operations._
import menta.model.howto.{StringLiteral, Terminal}

/**
 * Created by IntelliJ IDEA.
 * User: toscheva
 * Date: 05.10.11
 * Time: 9:20
 * To change this template use File | Settings | File Templates.
 */

object StringLiteralProtocol   extends DefaultProtocol with HowToProtocol
{
   implicit object StringLiteralFormat extends Format[StringLiteral]{
    def reads(in:Input)=
    {
      var res= new StringLiteral()
      readProperties(in,res)
      res
    }
    def writes(out:Output,value:StringLiteral)=
    {
      writeProperties(out,value)
      //writeString(out,value.name);
    }
  }
}