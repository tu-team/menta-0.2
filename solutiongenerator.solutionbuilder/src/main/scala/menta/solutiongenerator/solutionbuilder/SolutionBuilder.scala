package menta.solutiongenerator.solutionbuilder

import menta.model.learner.History
import menta.model.howto.Solution

/**
 * @author talanov max
 * Date: 05.03.11
 * Time: 18:09
 */

trait SolutionBuilder {
  def apply(history: History): Solution
}