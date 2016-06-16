package menta.model.learner

import menta.model.howto.HowToChangeSet
import java.util.Date

/**
 * @author talanovm
 * Date: 25.02.11
 * Time: 19:15
 */

class HowToUpdate(howToChangeSet: HowToChangeSet, dateTime: Date, association: AbstractAssociation)
  extends Event(dateTime: Date, association: AbstractAssociation) {
  def this() = this(new HowToChangeSet, new Date(), new PartialAssociation())
}