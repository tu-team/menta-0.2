package menta.model.util.serialization

/*
  registry of serialization protocol
  to add your protocol:
  1) place import of your protocol
  2) update serialize method
  3) update deserialize method
  4) place instance of your target class  to finalClasses
 */

import sbinary._
import Operations._
import menta.model.howto._
import menta.model.{KnowledgeClass, Knowledge}
import menta.model.container.{KnowledgeNumber, KnowledgeString}
import org.hypergraphdb.{HGHandle, HyperGraph}
import menta.model.subscribercontroller.{Customer}
import menta.model.solutiongenerator.solutionchecker.AcceptanceCriteria

//place here implemeted protocols
import menta.model.util.serialization.protocols.AddClassProtocol._
import menta.model.util.serialization.protocols.AddMethodActionProtocol._
import menta.model.util.serialization.protocols.AddOperatorProtocol._
import menta.model.util.serialization.protocols.AddIndividualProtocol._
import menta.model.util.serialization.protocols.RemoveClassProtocol._
import menta.model.util.serialization.protocols.TerminalProtocol._
import menta.model.util.serialization.protocols.KnowledgeClassProtocol._
import menta.model.util.serialization.protocols.KnowledgeStringProtocol._
import menta.model.util.serialization.protocols.StringLiteralProtocol._
import menta.model.util.serialization.protocols.ReferenceProtocol._
import menta.model.util.serialization.protocols.KnowledgeNumberProtocol._
import menta.model.util.serialization.protocols.NumberLiteralProtocol._
import menta.model.util.serialization.protocols.ConversationProtocol._

import menta.model.util.serialization.protocols.{ResponseProtocol}
import menta.model.util.serialization.protocols.ResponseProtocol.{ResponseFormat}

import menta.model.util.serialization.protocols.{CustomerProtocol}
import menta.model.util.serialization.protocols.CustomerProtocol.{CustomerFormat}

import menta.model.util.serialization.protocols.{SolutionProtocol}
import menta.model.util.serialization.protocols.SolutionProtocol.{SolutionFormat}

import menta.model.util.serialization.protocols.{AcceptanceCriteriaProtocol}
import menta.model.util.serialization.protocols.AcceptanceCriteriaProtocol.{AcceptanceCriteriaFormat}


import menta.model.conversation.{Conversation,Response}
import menta.model.subscribercontroller.{Customer}

import java.net.URISyntaxException

/**.
 * User: toscheva
 * Date: 23.08.11
 * Time: 18:40
 */

object ProtocolRegistry
{

  /**
    array of instance of final classes that will be placed to HGDB
    Place classes here
   */
  var finalClasses=List(new AcceptanceCriteria(),new Solution(),new Response(),new Customer(),new menta.model.conversation.Conversation(),new AddIndividual(), new AddClass(),new Terminal(),new KnowledgeClass(),new StringLiteral(), new KnowledgeString(),new Reference())

  /**
   * array instances of action classes
   */
  var actionClasses=List(new AddClass,new RemoveClass)

  /*
    serialize entity using specific protocol
   */
  def serialize(input: AnyRef): Array[Byte] = {
    if (input.isInstanceOf[StringLiteral]) {
      return toByteArray(input.asInstanceOf[StringLiteral])
    } else if (input.isInstanceOf[Reference]) {
      return toByteArray(input.asInstanceOf[Reference])
    }else if (input.isInstanceOf[NumberLiteral]) {
      return toByteArray(input.asInstanceOf[NumberLiteral])
    } else  if (input.isInstanceOf[Terminal]) {
      return toByteArray(input.asInstanceOf[Terminal])
    } else if (input.isInstanceOf[AddClass]) {
      return toByteArray(input.asInstanceOf[AddClass])

    } else if (input.isInstanceOf[AddMethodAction]) {
      return toByteArray(input.asInstanceOf[AddMethodAction])

    } else if (input.isInstanceOf[AddOperator]) {
      return toByteArray(input.asInstanceOf[AddOperator])

    } else if (input.isInstanceOf[RemoveClass]) {
      return toByteArray(input.asInstanceOf[RemoveClass])

    } else if (input.isInstanceOf[AddIndividual]) {
      return toByteArray(input.asInstanceOf[AddIndividual])
    } else if (input.isInstanceOf[KnowledgeClass]) {
      return toByteArray(input.asInstanceOf[KnowledgeClass])
    } else if (input.isInstanceOf[KnowledgeString]) {
      return toByteArray(input.asInstanceOf[KnowledgeString])
    }   else if (input.isInstanceOf[KnowledgeNumber]) {
      return toByteArray(input.asInstanceOf[KnowledgeNumber])
    }

      else if (input.isInstanceOf[Conversation]) {
      return toByteArray(input.asInstanceOf[Conversation])
    }

    else if (input.isInstanceOf[Response]) {
      return toByteArray(input.asInstanceOf[Response])
    }

     else if (input.isInstanceOf[Customer]) {
      return toByteArray(input.asInstanceOf[Customer])
    }

     else if (input.isInstanceOf[Solution]) {
      return toByteArray(input.asInstanceOf[Solution])
    }
     else if (input.isInstanceOf[AcceptanceCriteria]) {
      return toByteArray(input.asInstanceOf[AcceptanceCriteria])
    }


    throw new Exception("Can't serialize type " + input.toString + ". Check ProtocolRegistry")
    return null
  }

   /*
    deserialize entity using specific protocol
   */
  def deserialize(raw: Array[Byte], input: Knowledge): menta.model.Knowledge = {

     if (input.isInstanceOf[StringLiteral]) {
       return fromByteArray[StringLiteral](raw)
     } else if (input.isInstanceOf[AddOperator]) {
       return fromByteArray[AddOperator](raw)

     } else if (input.isInstanceOf[RemoveClass]) {
       return fromByteArray[RemoveClass](raw)

     } else if (input.isInstanceOf[KnowledgeClass]) {
       return fromByteArray[KnowledgeClass](raw)
     } else if (input.isInstanceOf[KnowledgeString]) {
       return fromByteArray[KnowledgeClass](raw)
     } else if (input.isInstanceOf[KnowledgeNumber]) {
       return fromByteArray[KnowledgeNumber](raw)
     }
     else if (input.isInstanceOf[AddIndividual]) {
       return fromByteArray[AddIndividual](raw)
     }

     else if (input.isInstanceOf[Reference]) {
       return fromByteArray[Reference](raw)
     } else if (input.isInstanceOf[NumberLiteral]) {
       return fromByteArray[NumberLiteral](raw)
     } else if (input.isInstanceOf[Terminal]) {
       return fromByteArray[Terminal](raw)
     } else if (input.isInstanceOf[AddClass]) {
       return fromByteArray[AddClass](raw)

     } else if (input.isInstanceOf[AddMethodAction]) {
       return fromByteArray[AddMethodAction](raw)

     }

     else if (input.isInstanceOf[Conversation]) {
       return fromByteArray[Conversation](raw)
     }
     else if (input.isInstanceOf[Customer]) {
       return fromByteArray[Customer](raw)
     }

     else if (input.isInstanceOf[Response]) {
       return fromByteArray[Response](raw)
     }
     else if (input.isInstanceOf[Solution]) {
       return fromByteArray[Solution](raw)
     }

     else if (input.isInstanceOf[AcceptanceCriteria]) {
       return fromByteArray[AcceptanceCriteria](raw)
     }

     throw new Exception("Can't deserialize type " + input.toString + ". Check ProtocolRegistry")

   }


  /*
    register type handlers in hypergraphdb
   */
  def registerClasses(graph:HyperGraph)=
  {
      finalClasses.foreach(cl=>
      {

          val custHndl = graph.getHandleFactory.makeHandle      ;
          graph.getTypeSystem.addPredefinedType(custHndl, cl, cl.getClass) ;

      })
  }

  /**
   *  get handler reference for class
   */
  def getHandleForClass(graph:HyperGraph, trgt:Knowledge):HGHandle=
  {
    return graph.getTypeSystem.getTypeHandle(trgt.getClass)
  }

  private def handlerExist(graph:HyperGraph,cls:Class[_]):Boolean=
  {
    try
    {
     var hdl=graph.getTypeSystem.getTypeHandle(cls)
     hdl!=null
    }
    catch
    {
       case e: Exception => {
        return false
      }
    }
  }
}