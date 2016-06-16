package menta.reasoneradapter.reasonerinterface

import menta.reasoneradapter.selfcontrol.SelfControl
import menta.model.solutiongenerator.solutionchecker.Test
import menta.model.util.triple.FrequencyConfidenceNegationTriple

/**
 * Interface to reasoner
 *
 * @author ayratn
 */
trait NARSInterface {
  def handle(request: Test): FrequencyConfidenceNegationTriple

  def setSelfControl(selfControl: SelfControl)
}