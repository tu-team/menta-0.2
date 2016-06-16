package menta.model.howto.ProbabilisticalLogicOperators

import menta.reasoneradapter.translator.ActionConverter
import menta.reasoneradapter.translator.Convertters.{ImplicationConverter, InheritanceConverter}
import menta.model.howto.HowTo

/**
 * @author alexander
 * Date: 10.03.11
 * Time: 21:47
 */
// TODO add Scala doc comments for the class and properties.
// TODO add Class diagram and wiki page with description.
class AddImplicationWrapper(in: HowTo) extends AddProbabilisticLogicOperatorWrapper(in: HowTo) {
  def this() = this (null)

  override def converter: ActionConverter = {
    return new ImplicationConverter
  }

  def unapply(howTo: HowTo): Option[AddProbabilisticLogicOperatorWrapper] = {
    if (howTo.uri.toString.contains(menta.reasoneradapter.constant.Constant.NonTerminalURIs.implication)) {
      Some(this)
    } else {
      None
    }
  }
}