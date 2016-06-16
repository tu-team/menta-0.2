package menta.translator.wrappers

import java.net.URI
import java.lang.IllegalArgumentException

/**
 * @author talanov max
 * Date: 24.11.11
 * Time: 19:47
 */

class TrConstantWrappersFactory {
  def apply(uri: URI): TrConstantWrapper = {
    uri match {
      case TrAddTarget(wrapper) => {
        wrapper
      }
      case TrAddName(wrapper) => {
        wrapper
      }
      case TrConstantField(wrapper) => {
        wrapper
      }
      case _ => {
        throw new IllegalArgumentException("Specified URI " + uri.toString + " does not have proper constant translation ")
      }
    }
  }
}