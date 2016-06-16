package menta.applicator


import menta.model.howto.Solution
import org.menta.model.Knowledge
import java.net.URI

/**
 * @author talanovm
 * Date: 28.02.11
 * Time: 19:47
 */

trait Applicator {
  def apply(solution: Solution): Knowledge

  def revertToPreHistorical(conversationURI: URI): URI
}