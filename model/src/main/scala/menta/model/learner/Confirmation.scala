package menta.model.learner

import java.util.Date

/**
 * @author talanovm
 * Date: 25.02.11
 * Time: 19:09
 */

class Confirmation(dateTime: Date, association: CompoundAssociation) extends Event(dateTime, association) {
  def this() = this(new Date(), new CompoundAssociation())
}