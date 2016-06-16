package menta.communicator.requestbuilder

import menta.model.translator.Translation
import org.menta.model.conversation._interface.Request

/**
 * @author talanovm
 * Date: 05.03.11
 * Time: 18:22
 */

trait RequestBuilder {
  def apply(report: Translation): Request
}