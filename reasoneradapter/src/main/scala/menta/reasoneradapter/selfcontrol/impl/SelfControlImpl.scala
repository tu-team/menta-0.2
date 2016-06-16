package menta.reasoneradapter.selfcontrol.impl

import nars.entity.Task
import menta.reasoneradapter.selfcontrol.SelfControl
import menta.reasoneradapter.selfcontrol.template.Template

/**
 * NARS self control mechanism implementation.
 *
 * @author ayratn
 */
class SelfControlImpl extends SelfControl {

  var _template : Template = _

  /**
   * Checks whether any task from the list matches the template
   */
  def checkTemplate(tasks: List[Task]) : Boolean = {
    if (_template == null) {
      false
    } else {
      _template.matchTheTemplate(tasks)
    }
  }

  /**
   * Checks whether cycleProcessed reached the limit
   * @return true if processing should be stopped
   */
  def checkStop(cycleProcessed: Int) : Boolean = {
    cycleProcessed > _template.maxCyclesCount
  }

  /*** Getters&&Setters ***/
  def template: Template = _template
  def template_=(value: Template) = _template = value

  def getMaxAllowedCycles(): Int = _template.maxCyclesCount
}