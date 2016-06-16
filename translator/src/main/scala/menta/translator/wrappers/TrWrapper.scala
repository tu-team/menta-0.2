package menta.translator.wrappers

import java.net.URI
import org.slf4j.{LoggerFactory, Logger}
import menta.model.howto._
import menta.translator.TranslationStrategy

/**
 * Translation HowTo-s Wrapper to
 * @author talanov max
 * Date: 21.11.11
 * Time: 17:03
 */

trait TrWrapper {
  /**
   * Translates the part of solution to HowTo as the part of Report.
   * @param solutionNode the Node to be translated.
   * @param context the container for variables/constants in the scope.
   * @param translationNode the AddIndividual to be used.
   * @returns translation result, mainly string literal or AddIndividual.
   */
  def apply(translationNode: AddIndividual, solutionNode: HowTo, context: Context, strategy: TranslationStrategy, mode: StringLiteral = new StringLiteral(""), conditionExpression: LogicalExpression = new LETrue): HowTo

}

trait TrMatcher {
  /**
   * Pattern matching method to construct wrappers on base of Translation HowTo
   * @parameter translationAddIndividual the HowTo base to create Wrapper
   * @returns new instance of TrWrapper initialized with translationAddIndividual
   */
  def unapply(translationAddIndividual: AddIndividual): Option[TrWrapper]
}

trait TrURIMatcher {
  /**
   * Pattern matching method to construct wrappers on base of Translation HowTo
   * @parameter uri the HowTo base URI to create Wrapper
   * @returns new instance of TrWrapper initialized with translationAddIndividual
   */
  def unapply(uri: URI): Option[TrWrapper]
}
