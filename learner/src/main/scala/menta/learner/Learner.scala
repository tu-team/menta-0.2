package menta.learner

import menta.model.howto.Solution
import menta.model.learner.AbstractAssociation
import menta.model.solutiongenerator.solutionchecker.AcceptanceCriteria
import java.net.URI


/**
 * User: talanov max
 * Date: 3/6/11
 * Time: 1:59 PM
 */

trait Learner {
  def train(acceptanceCriteria: AcceptanceCriteria, solution: Solution): Set[AbstractAssociation]

  def detectAnalogy(acceptanceCriteria: AcceptanceCriteria): List[AbstractAssociation]

  def train(acceptanceCriteria: URI, solution: Solution)

  def detectAnalogy(acceptanceCriteria: URI): Solution
}