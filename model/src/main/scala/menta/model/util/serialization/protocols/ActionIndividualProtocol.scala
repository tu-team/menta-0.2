package menta.model.util.serialization.protocols

import sbinary._
import DefaultProtocol._
import Operations._

import menta.model.Knowledge
import sbinary.{Output, Input}
import menta.model.howto.{ActionIndividual, HowTo}

/**
 * @author talanovm
 * Date: 07.09.11
 * Time: 18:54
 */

trait ActionIndividualProtocol extends HowToProtocol {

  override def readProperties(in: Input, obj: Knowledge) {
    super.readProperties(in, obj);
    //cast to HowTo
    var casted: ActionIndividual = obj.asInstanceOf[ActionIndividual];
    // read properties
    // actionClass: ActionClass, parameters: List[HowTo]
    // casted.actionClass_=(read[ActionClass](in))
  }

  override def writeProperties(out: Output, obj: Knowledge) {
    super.writeProperties(out, obj);
    //cast to HowTo
    var casted = obj.asInstanceOf[HowTo];
  }

}