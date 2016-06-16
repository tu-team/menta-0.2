package menta.translator.helper

import menta.model.howto.AddIndividual
import java.net.URI
import org.slf4j.{Logger, LoggerFactory}
import menta.translator.wrappers._

/**
 * Factory to build constant translationWrappers
 * @author talanov max
 * Date: 02.12.11
 * Time: 20:46
 */

class ConstantTranslationWrapperFactory {
  val LOG: Logger = LoggerFactory.getLogger(this.getClass)

  def apply(constantAddIndividual: AddIndividual): TrConstantWrapper = {
    if (constantAddIndividual.actionClass != null && constantAddIndividual.actionClass.uri != null) {
      val actionClassURI: URI = constantAddIndividual.actionClass.uri
      LOG.debug("actionClassURI {}", actionClassURI)
      actionClassURI match {
        case TrConstantField(tr) => {
          LOG.debug(" {}", tr)
          tr
        }
        case TrAddTarget(tr) => {
          LOG.debug(" {}", tr)
          tr
        }
        case TrAddName(tr) => {
          LOG.debug(" {}", tr)
          tr
        }
      }
    } else {
      throw new UninitializedFieldError("individual does not cantain actionClass")
    }
  }
}