package menta.model.util.serialization.protocols


import sbinary._
import DefaultProtocol._
import Operations._
import menta.model.Knowledge
import java.net.URI
import menta.model.container.KnowledgeNumber

/**
 * Created by IntelliJ IDEA.
 * User: toscheva
 * Date: 05.10.11
 * Time: 11:02
 * To change this template use File | Settings | File Templates.
 */

object KnowledgeNumberProtocol extends DefaultProtocol with KnowledgeProtocol
{
   implicit object KnowledgeNumberFormat extends Format[KnowledgeNumber]{
    def reads(in:Input)=
    {
      var res= new KnowledgeNumber()
      readProperties(in,res)
      res.contents=read[Float](in)

      res
    }
    def writes(out:Output,value:KnowledgeNumber)=
    {
      writeProperties(out,value)
      write[Float](out,value.contents)
      //writeString(out,value.name);
    }
  }
}