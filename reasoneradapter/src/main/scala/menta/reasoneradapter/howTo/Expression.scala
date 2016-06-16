package menta.reasoneradapter.howTo

import menta.model.util.triple.FrequencyConfidenceNegationTriple
import menta.model.howto.Context

/**
 * @author talanov max
 * Date: 10.05.11
 * Time: 9:46
 */

trait Expression {
  def evaluate(context: Context): FrequencyConfidenceNegationTriple
}