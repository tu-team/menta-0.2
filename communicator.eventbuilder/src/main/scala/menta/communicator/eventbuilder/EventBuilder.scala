package menta.communicator.eventbuilder

import menta.model.learner.Event

/**
 * @author talanovm
 * Date: 05.03.11
 * Time: 19:10
 */

trait EventBuilder {
  def apply(eventClass: Class[Event]): Event
}