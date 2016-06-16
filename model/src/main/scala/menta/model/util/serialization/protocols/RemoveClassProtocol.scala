package menta.model.util.serialization.protocols
import sbinary._
import DefaultProtocol._
import Operations._
import menta.model.howto.RemoveClass

/**
 * User: toscheva
 * Date: 18.08.11
 * Time: 20:50
 */

object RemoveClassProtocol   extends DefaultProtocol with ActionClassProtocol
{
   implicit object RemoveClassFormat extends Format[RemoveClass]{
    def reads(in:Input)=
    {
      var res= new RemoveClass()
      readProperties(in,res)
      res
    }
    def writes(out:Output,value:RemoveClass)=
    {
      writeProperties(out,value)
      //writeString(out,value.name);
    }
  }
}