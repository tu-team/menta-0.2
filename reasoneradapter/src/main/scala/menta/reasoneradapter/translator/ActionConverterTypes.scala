package menta.reasoneradapter.translator

/**
 * @author toscheva
 * Date: 07.03.11
 * Time: 17:06
 * Enum for action converter     types
 */

// TODO add Class diagram and wiki page with description.
object ActionConverterTypes extends Enumeration {
  type ActionConverterTypes = Value
  // TODO Is it ok to have Negation and Not in one enumeration?
  val Property, Negation, Implication, Inheritance, Conjunction, Not, Variable = Value
}