package menta.model.howto.ProbabilisticalLogicOperators

import menta.model.howto.HowTo
import menta.reasoneradapter.translator.ActionConverter
import menta.reasoneradapter.translator.Convertters.{HasAPropertyConverter, PropertyConverter, VariableConverter, NegationConverter}

/**
 * @author alexander
 * Date: 10.03.11
 * Time: 21:50
 */
// TODO add Scala doc comments for the class and properties.
// TODO add Class diagram and wiki page with description.
class AddProbabilisticPropertyWrapper(in: HowTo) extends AddProbabilisticLogicOperatorWrapper(in: HowTo) {
  def this() = this (null)

  override def converter: ActionConverter = {
    return new HasAPropertyConverter
  }

  def unapply(howTo: HowTo): Option[AddProbabilisticLogicOperatorWrapper] = {
    if (howTo.uri.toString.contains(menta.reasoneradapter.constant.Constant.NonTerminalURIs.property)) {
      Some(this)
    } else {
      None
    }
  }
}