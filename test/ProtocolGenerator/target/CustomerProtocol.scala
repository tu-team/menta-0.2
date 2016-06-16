package menta.model.util.serialization.protocols

import menta.model._

import sbinary._
import DefaultProtocol._
import Operations._

trait  CustomerProtocol extends KnowledgeProtocol
{
       /*
        Read properties from steam
        @param in - stream of sbinary
        @param obj - object to Update
     */
      override def readProperties(in:Input, obj:Knowledge)=
      {
          super.readProperties(in,obj);
          //cast to Customer
          var casted=obj.asInstanceOf[Customer];
          //read name
          casted.name= readString(in)
      }

      /*
        write properties to stream
        @param out - stream of sbinary
        @param obj - object to write
      */
      override def writeProperties(out:Output, obj:Knowledge)=
      {
          super.writeProperties(out,obj);
          //cast to HowTo
          var casted=obj.asInstanceOf[Customer];
          //process name
          writeString(out,casted.name);

      }

}
