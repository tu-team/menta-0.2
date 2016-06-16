package menta.model.util.serialization.protocols

import sbinary._
import DefaultProtocol._
import Operations._
import menta.model.Knowledge
import java.net.URI
import menta.model.container.KnowledgeString

/**
 * Created by IntelliJ IDEA.
 * User: toscheva
 * Date: 05.10.11
 * Time: 9:37
 * To change this template use File | Settings | File Templates.
 */

object KnowledgeStringProtocol  extends DefaultProtocol with KnowledgeProtocol
{
   implicit object KnowledgeStringFormat extends Format[KnowledgeString]{
    def reads(in:Input)=
    {
      var res= new KnowledgeString()
      readProperties(in,res)
      res.contents=readString(in)

      res
    }
    def writes(out:Output,value:KnowledgeString)=
    {
      writeProperties(out,value)
      writeString(out,value.contents)
      //writeString(out,value.name);
    }
  }
}