package menta.reasoneradapter.selfcontrol.entity


import LogicalConnection._
import menta.model.howto.HowTo

/**
 * Holds single objective used by {@link Template} in NARS objects matching.
 *
 * @author ayratn
 */
trait Objective {
  def termType : String
  def property : HowTo
  def subject : HowTo
  def compoundProperty : Objective
  def compoundSubject : Objective
  def logicalConnection : LogicalConnection
}