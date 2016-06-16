package menta.model.util.serialization.protocols

import menta.model.Knowledge
import menta.model.howto.HowTo

import sbinary._
import DefaultProtocol._
import Operations._
import collection.immutable.HashSet
import menta.model.howto.classifier.Classifier


/**
 * User: toscheva
 * Date: 17.08.11
 * Time: 18:23
 */

/**
  @class KnowledgeProtocol helper class for
*/
trait  HowToProtocol extends KnowledgeProtocol
{
     /**
      * Read properties from steam
      * @param in - stream of sbinary
      * @param obj - object to Update
     */
      override def readProperties(in:Input, obj:Knowledge)=
      {
          super.readProperties(in,obj)
          //cast to HowTo
          var casted: HowTo = obj.asInstanceOf[HowTo]
          //read name
          casted.name = readString(in)
          //read classifiers
          //casted.classifiers=read[HashSet[Classifier]](in)
      }

      /**
      * write properties to stream
      * @param out - stream of sbinary
      * @param obj - object to write
      */
      override def writeProperties(out:Output, obj:Knowledge)=
      {
          super.writeProperties(out,obj);
          //cast to HowTo
          var casted=obj.asInstanceOf[HowTo];
          //process name
          var test=casted.name
          writeString(out,casted.name);
          //process classifiers
          //write[HashSet[Classifier]](out,casted.classifiers);

      }

}