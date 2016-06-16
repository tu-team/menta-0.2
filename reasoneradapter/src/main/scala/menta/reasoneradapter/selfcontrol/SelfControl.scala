package menta.reasoneradapter.selfcontrol

import nars.entity.Task
import template.Template


/**
 * Provides interface to NARS self control mechanism.
 *
 * @author ayratn
 */
trait SelfControl {
  def template : Template
  def template_=(aTemplate: Template)

  /**
   * Checks if cycles processed reached the limit
   */
  def checkStop(cycleProcessed: Int) : Boolean

  /**
   *  Checks the list for a task that satisfies the current template
   *
   * @return true if there is at least one {@link Task} that matches the template
   */
  def checkTemplate(tasks: List[Task]) : Boolean

  /**
   * returns number of max allowed cycles
   */
  def getMaxAllowedCycles():Int
}