package menta.translator.wrappers

import menta.model.howto.{Context, HowTo, AddIndividual}
import java.net.URI


/**
 * @author talanov max
 * Date: 08.12.11
 * Time: 17:36
 */

trait TrConstantWrapper {

  /**
   * Translates the part of solution to HowTo as the part of Report.
   * @param translationNode the AddIndividual to be used.
   * @param solutionNode the Node to be translated.
   * @param context the container for variables/constants in the scope.
   * @returns translation result, mainly string literal or AddIndividual.
   */
  def apply(translationNode: AddIndividual, solutionNode: HowTo, context: Context): HowTo

}

trait TrConstantURIMatcher {
  /**
   * Pattern matching method to construct wrappers on base of Translation HowTo
   * @parameter uri the HowTo base URI to create Wrapper
   * @returns new instance of TrWrapper initialized with translationAddIndividual
   */
  def unapply(uri: URI): Option[TrConstantWrapper]
}