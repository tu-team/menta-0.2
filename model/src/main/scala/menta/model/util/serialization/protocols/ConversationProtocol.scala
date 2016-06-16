package menta.model.util.serialization.protocols

import sbinary._
import menta.model.conversation.Conversation
import menta.model.Knowledge
import menta.model.howto.{HowTo, AddClass}
import sbinary.Operations._

/**
 * Created by IntelliJ IDEA.
 * User: Alexander
 * Date: 04.12.11
 * Time: 15:10
 * To change this template use File | Settings | File Templates.
 */

object ConversationProtocol extends DefaultProtocol with KnowledgeProtocol
{
  implicit object ConversationFormat extends Format[Conversation]{
      def reads(in:Input)=
      {
        var res= new Conversation()
        readProperties(in,res)

        res
      }
      def writes(out:Output,value:Conversation)
      {
        writeProperties(out,value)
        //writeString(out,value.name);
      }



    }

}