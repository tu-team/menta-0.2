package menta.translator.wrappers

import java.net.URI
import org.slf4j.{Logger, LoggerFactory}
import menta.model.howto.{StringLiteral, Context, HowTo, AddIndividual}

/**
 * @author talanov max
 * Date: 24.11.11
 * Time: 19:36
 */

class TrAddTarget() extends TrConstantWrapper {
  val LOG: Logger = LoggerFactory.getLogger(this.getClass)
  //TODO add implementation
  def apply(translationNode: AddIndividual, solutionNode: HowTo, context: Context) = null
}

object TrAddTarget extends TrConstantURIMatcher {
  def unapply(uri: URI): Option[TrConstantWrapper] = {
    if (uri.toString.contains(menta.model.Constant.trAddTarget)) {
      Some(new TrAddTarget())
    } else {
      None
    }
  }
}