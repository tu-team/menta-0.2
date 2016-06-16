package menta.model.util.serialization.protocols

import sbinary._
import DefaultProtocol._
import Operations._
import menta.model.howto.AddMethodAction

/**
 * Created by IntelliJ IDEA.
 * User: toscheva
 * Date: 18.08.11
 * Time: 20:47
 * To change this template use File | Settings | File Templates.
 */

object AddMethodActionProtocol     extends DefaultProtocol with ActionClassProtocol
{
    implicit object AddMethodActionFormat extends Format[AddMethodAction]{
      def reads(in:Input)=
      {
        var res= new AddMethodAction
        readProperties(in,res)
        res
      }
      def writes(out:Output,value:AddMethodAction)=
      {
        writeProperties(out,value)
        //writeString(out,value.name);
      }
    }
}