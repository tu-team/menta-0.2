package menta.model.util.serialization.protocols

import menta.model._

import sbinary._
import DefaultProtocol._
import Operations._

trait  AcceptanceCriteriaProtocol extends RuleProtocol
{
       /*
        Read properties from steam
        @param in - stream of sbinary
        @param obj - object to Update
     */
      override def readProperties(in:Input, obj:Knowledge)=
      {
          super.readProperties(in,obj);
          //cast to AcceptanceCriteria
          var casted=obj.asInstanceOf[AcceptanceCriteria];
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
          var casted=obj.asInstanceOf[AcceptanceCriteria];
          //process name
          writeString(out,casted.name);

      }

}
