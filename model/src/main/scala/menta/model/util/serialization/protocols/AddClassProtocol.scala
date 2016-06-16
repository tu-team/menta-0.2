package menta.model.util.serialization.protocols
import sbinary._
import DefaultProtocol._
import Operations._
import menta.model.howto.AddClass

/**
 * @author toscheva
 * Date: 18.08.11
 * Time: 20:43
 */

object AddClassProtocol extends DefaultProtocol with ActionClassProtocol
{
  implicit object AddClassFormat extends Format[AddClass]{
      def reads(in:Input)=
      {
        var res= new AddClass
        readProperties(in,res)
        res
      }
      def writes(out:Output,value:AddClass)
      {
        writeProperties(out,value)
        //writeString(out,value.name);
      }
    }

}