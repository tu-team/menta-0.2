package menta.applicator.fileapplicator


import java.net.URI
import menta.applicator.Applicator
import menta.model.howto.Solution
import org.menta.model.Knowledge


/**
 * Creates the patch and applies it over the file representation of application. It applies patch wrapped into Knowledge to the catalogue.
 *
 * @author talanovm
 * Date: 07.03.11
 * Time: 18:03
 */

trait FileApplicator extends Applicator {
  def apply(solution: Solution): Knowledge
  def revertToPrehistorical(conversationURI: URI): URI
}