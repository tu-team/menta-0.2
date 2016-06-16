package menta.reasoneradapter.selfcontrol

import menta.model.howto.HowTo


/**
 * Interface for self control factory.
 *
 * @author ayratn
 */
trait SelfControlFactory {
  def apply(statement: List[HowTo], acceptanceCriteria: List[HowTo]) : SelfControl
}