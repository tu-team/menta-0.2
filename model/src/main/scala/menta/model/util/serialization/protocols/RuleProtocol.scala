package menta.model.util.serialization.protocols

import sbinary.{DefaultProtocol, Operations, Output, Input}
import DefaultProtocol._
import sbinary.Operations._
import menta.model.solutiongenerator.solutionchecker.Rule
import menta.model.Knowledge
import Operations._
import menta.model.howto.HowTo

/**
 * Created by IntelliJ IDEA.
 * User: toscheva
 * Date: 07.12.11
 * Time: 18:39
 * To change this template use File | Settings | File Templates.
 */

trait RuleProtocol extends KnowledgeProtocol {
  /**
   * Read properties from steam
   * @param in - stream of sbinary
   * @param obj - object to Update
   */
  override def readProperties(in: Input, obj: Knowledge) = {
    super.readProperties(in, obj)
    //cast to HowTo
    var casted: Rule = obj.asInstanceOf[Rule]
    casted.ruleName=readString(in)
    //read name
    //read classifiers
    //casted.classifiers=read[HashSet[Classifier]](in)
  }

  /**
   * write properties to stream
   * @param out - stream of sbinary
   * @param obj - object to write
   */
  override def writeProperties(out: Output, obj: Knowledge) = {
    super.writeProperties(out, obj);
    var casted: Rule = obj.asInstanceOf[Rule]
    writeString(out,casted.ruleName)
  }

}
