package menta.model.learner

import menta.model.Knowledge
import java.util.Date

/**
 * The Event of the Solution confirmation mainly
 * @author talanovm
 * @see menta.model.howto.Solution
 * @see
 * Date: 10.01.11
 * Time: 10:30
 */

class Event(dateTime: Date, association: AbstractAssociation) extends Knowledge {
  def this() = this(new Date, null)
}