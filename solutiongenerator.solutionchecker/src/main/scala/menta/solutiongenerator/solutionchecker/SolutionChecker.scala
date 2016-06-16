package menta.solutiongenerator.solutionchecker

import menta.model.howto.Solution
import menta.model.util.triple.FrequencyConfidenceNegationTriple

/**
 * @author talanov max
 * Date: 28.02.11
 * Time: 19:55
 */

trait SolutionChecker {
  def  apply(solution: Solution): FrequencyConfidenceNegationTriple
}