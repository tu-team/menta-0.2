package menta.reasoneradapter.interpreter

import menta.reasoneradapter.translator.HowToTranslator
import java.net.URI
import menta.model.howto.Context
import menta.model.howto.HowTo

/**
 * Created by IntelliJ IDEA.
 * User: toscheva
 * Date: 20.10.11
 * Time: 20:15
 * To change this template use File | Settings | File Templates.
 */

/**
 * Interpreter for Conditional HowTo
 */
class ConditionInterpreter {
  val TRANSLATOR: HowToTranslator = new HowToTranslator()
  val URI = new URI("menta/test")
  var CONTEXT = new Context(Map[URI, Map[URI, List[HowTo]]]())
}