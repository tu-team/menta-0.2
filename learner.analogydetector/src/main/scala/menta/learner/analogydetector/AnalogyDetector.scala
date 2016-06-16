package menta.learner.analogydetector

import menta.model.learner.AbstractAssociation
import menta.model.solutiongenerator.solutionchecker.AcceptanceCriteria

/**
 * Identifies similar to inbound [http://code.google.com/p/menta/wiki/AcceptanceCriteria AcceptanceCriteria] alreday processed and infere the Solution of the AcceptanceCriteria Association.
 * @author max
 * @date 2011-03-06
 * Time: 2:38 PM
 */

trait AnalogyDetector {
  def apply(accptanceCriteria: AcceptanceCriteria): List[AbstractAssociation]
}