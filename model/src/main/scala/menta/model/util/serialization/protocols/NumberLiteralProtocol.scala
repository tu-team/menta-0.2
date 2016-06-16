package menta.model.util.serialization.protocols


import sbinary._
import DefaultProtocol._
import Operations._
import menta.model.howto.NumberLiteral

/**
 * Created by IntelliJ IDEA.
 * User: toscheva
 * Date: 05.10.11
 * Time: 11:18
 * To change this template use File | Settings | File Templates.
 */

object NumberLiteralProtocol  extends DefaultProtocol with HowToProtocol
{
   implicit object NumberLiteralFormat extends Format[NumberLiteral]{
    def reads(in:Input)=
    {
      var res= new NumberLiteral()
      readProperties(in,res)
      res
    }
    def writes(out:Output,value:NumberLiteral)=
    {
      writeProperties(out,value)
      //writeString(out,value.name);
    }
  }
}