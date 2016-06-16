package menta.reasoneradapter.selfcontrol.template.impl


import menta.reasoneradapter.selfcontrol.entity.Objective
import nars.entity.Task
import menta.reasoneradapter.selfcontrol.template.Template

/**
 * {@link Template} implementation.
 *
 * @version 0.1 02.03.2011 Not implemented yet.
 *
 * @author ayratn
 */
class TemplateImpl extends Template {

  private var _objectives: List[Objective] = null
  private var _requiredFacts : List[Int] = null
  private var _atLeastOneFacts : List[Int] = null
  private var _atLeastTwoFacts : List[Int] = null
  private var _maxCyclesCount : Int = 500

  def matchTheTemplate(tasks: List[Task]): Boolean = {
    throw new RuntimeException("Not implemented.");
  }

  /*** Getters&&Setters ***/
  def objectives: List[Objective] = _objectives
  def objectives_=(value: List[Objective]) = _objectives = value
  def requiredFacts: List[Int] = _requiredFacts
  def requiredFacts_=(value: List[Int]) = _requiredFacts = value
  def atLeastOneFacts: List[Int] = _atLeastOneFacts
  def atLeastOneFacts_=(value: List[Int]) = _atLeastOneFacts = value
  def atLeastTwoFacts: List[Int] = _atLeastTwoFacts
  def atLeastTwoFacts_=(value: List[Int]) = _atLeastTwoFacts = value
  def maxCyclesCount: Int = _maxCyclesCount
  def maxCyclesCount_=(value: Int) = _maxCyclesCount = value
}