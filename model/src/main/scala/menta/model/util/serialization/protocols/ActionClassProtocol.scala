package menta.model.util.serialization.protocols

import menta.model.Knowledge
import menta.model.howto.{ActionClass, HowTo}
import sbinary._
import Operations._
import DefaultProtocol._


/**
 * @author toscheva
 * Date: 18.08.11
 * Time: 20:39
 */

trait ActionClassProtocol extends HowToProtocol {
  /**
   * Read properties from steam
   * @param in - stream of sbinary
   * @param obj - object to Update
   */
  override def readProperties(in: Input, obj: Knowledge) = {
    super.readProperties(in, obj);
    //cast to HowTo
    var casted = obj.asInstanceOf[ActionClass];
    //read name

  }

  /**
   * writes properties into a stream.
   * @param out - stream of sbinary
   * @param obj - object to write
   */
  override def writeProperties(out: Output, obj: Knowledge) = {
    super.writeProperties(out, obj);
    //cast to HowTo
    var casted = obj.asInstanceOf[HowTo];

  }

}