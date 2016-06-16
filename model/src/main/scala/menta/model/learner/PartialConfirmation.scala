package menta.model.learner

import java.util.Date

/**
 * @author talanovm
 * Date: 25.02.11
 * Time: 19:10
 */

class PartialConfirmation(dateTime: Date, association: PartialAssociation) extends Event(dateTime, association) {
  def this() = this(new Date, new PartialAssociation)
}