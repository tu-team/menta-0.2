package menta.reasoneradapter.selfcontrol.entity

/**
 * Defines the type of connection between two entities.
 *
 * @author ayratn
 */
object LogicalConnection extends Enumeration {
  type LogicalConnection = Value
  val OR, AND = Value
}