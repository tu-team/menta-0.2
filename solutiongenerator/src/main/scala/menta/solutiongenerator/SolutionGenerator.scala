package menta.solutiongenerator

import menta.model.solutiongenerator.solutionchecker.RuleChangeSet
import menta.model.howto.Solution

/**
 * @author talanov max
 * Date: 28.02.11
 * Time: 19:53
 */

trait SolutionGenerator {
  def apply(acceptanceCriteriaChanges: RuleChangeSet): Solution
}