package menta.model.util.serialization.protocols

import sbinary._
import sbinary.Operations._
import menta.model.subscribercontroller.{Customer}
import java.net.URI


/**
 * Created by IntelliJ IDEA.
 * User: Alexander
 * Date: 04.12.11
 * Time: 15:30
 * To change this template use File | Settings | File Templates.
 */

object CustomerProtocol extends DefaultProtocol with KnowledgeProtocol
{
  implicit object CustomerFormat extends Format[Customer]{
      def reads(in:Input)=
      {
        var res= new Customer ();
        readProperties(in,res);
        res.networkAddress=readURI(in);
        res
      }
      def writes(out:Output,value:Customer)
      {
        writeProperties(out,value);
        writeURI(out,value.networkAddress);

      }
    }

}