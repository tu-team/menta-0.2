package menta.reasoneradapter.selfcontrol.template

import nars.entity.Task
import menta.reasoneradapter.selfcontrol.entity.Objective
import menta.model.Knowledge


/**
 * Templates.
 * Contains information needed to match NARS objects.
 *
 * @author ayratn
 */
trait Template extends Knowledge {

  def matchTheTemplate(tasks: List[Task]) : Boolean

  var objectives : List[Objective]
  var requiredFacts : List[Int]
  var atLeastOneFacts : List[Int]
  var atLeastTwoFacts : List[Int]
  var maxCyclesCount : Int
}