package menta.model.util.serialization.protocols

import menta.model.Knowledge
import menta.model.howto.HowTo
import sbinary.Operations._
import sbinary.{Output, Input}
import menta.model.conversation.ConversationAny

/**
 * Created by IntelliJ IDEA.
 * User: Alexander
 * Date: 04.12.11
 * Time: 15:15
 * To change this template use File | Settings | File Templates.
 */

trait ConversationAnyProtocol extends KnowledgeProtocol{


   /**
      * Read properties from steam
      * @param in - stream of sbinary
      * @param obj - object to Update
     */
      override def readProperties(in:Input, obj:Knowledge)=
      {
          super.readProperties(in,obj)
          //cast to HowTo
      }

      /**
      * write properties to stream
      * @param out - stream of sbinary
      * @param obj - object to write
      */
      override def writeProperties(out:Output, obj:Knowledge)=
      {
          super.writeProperties(out,obj);

      }
}