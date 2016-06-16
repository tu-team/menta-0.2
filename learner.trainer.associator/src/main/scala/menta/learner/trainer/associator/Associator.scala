package menta.learner.trainer.associator

import menta.model.howto.{Solution}
import menta.model.learner.AbstractAssociation
import menta.model.solutiongenerator.solutionchecker.AcceptanceCriteria

/**
 * Associator creates an association between acceptance criteria and some previous solution. It simply creates an object of the link between acceptance criteria and a solution or their parts.
 * @author max
 * @date 2011-03-06
 * Time: 2:20 PM
 */

trait Associator {
  def apply(acceptanceCriteria: AcceptanceCriteria, solution: Solution) : AbstractAssociation
}