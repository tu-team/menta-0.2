package menta.model.util.serialization.protocols
import sbinary._
import DefaultProtocol._
import Operations._
import menta.model.howto.RemoveClass
import menta.model.KnowledgeClass

/**
 * Created by IntelliJ IDEA.
 * User: toscheva
 * Date: 08.09.11
 * Time: 19:35
 * To change this template use File | Settings | File Templates.
 */

object KnowledgeClassProtocol  extends DefaultProtocol with KnowledgeProtocol
{
   implicit object KnowledgeClassFormat extends Format[KnowledgeClass]{
    def reads(in:Input)=
    {
      var res= new KnowledgeClass()
      readProperties(in,res)
      res
    }
    def writes(out:Output,value:KnowledgeClass)=
    {
      writeProperties(out,value)
      //writeString(out,value.name);
    }
  }
}