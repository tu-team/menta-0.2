package menta.model.learner

import java.util.Date
import menta.model.translator.{EvolutionReport, Report}

/**
 * @author talanovm
 * Date: 25.02.11
 * Time: 19:13
 */

class Request(evolutionReport: Report, dateTime: Date, association: AbstractAssociation)
  extends Event (dateTime, association) {
  def this() = this(new EvolutionReport(), new Date, new PartialAssociation())
}