package menta.reasoneradapter

import menta.model.solutiongenerator.solutionchecker.Test
import menta.model.util.triple.FrequencyConfidenceNegationTriple


/**
 * The interface for reasoner statements
 */
trait ReasonerAdapter {
  def apply(request: Test): FrequencyConfidenceNegationTriple
}