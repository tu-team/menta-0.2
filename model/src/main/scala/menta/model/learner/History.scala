package menta.model.learner

import menta.model.Knowledge

/**
 * The sequence of the historical events container.
 * @author: talanovm
 * Date: 10.01.11
 * Time: 10:29
 */

class History(events: List[Event]) extends Knowledge {
  def this() = this(List[Event]())
}