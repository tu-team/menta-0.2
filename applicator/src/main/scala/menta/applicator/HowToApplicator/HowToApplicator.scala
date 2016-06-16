package menta.applicator.HowToApplicator

import java.net.URI
import menta.model.howto.Solution
import org.menta.model.Knowledge

/**
 * @author talanovm
 * Date: 07.03.11
 * Time: 18:13
 */

trait HowToApplicator {
  def apply(solution: Solution): Knowledge
  def revertToPrehistorical(conversationURI: URI): URI
}