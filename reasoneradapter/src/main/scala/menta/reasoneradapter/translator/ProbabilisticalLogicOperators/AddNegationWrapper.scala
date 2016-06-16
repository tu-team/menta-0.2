package menta.model.howto.ProbabilisticalLogicOperators

import menta.model.howto.HowTo
import menta.reasoneradapter.translator.ActionConverter
import menta.reasoneradapter.translator.Convertters.{NegationConverter, ConjuctionConverter}

/**
 * @author alexander
 * Date: 10.03.11
 * Time: 21:48
 */
// TODO add Scala doc comments for the class and properties.
// TODO add Class diagram and wiki page with description.
class AddNegationWrapper(in: HowTo) extends AddProbabilisticLogicOperatorWrapper(in: HowTo) {
  def this() = this (null)

  override def converter: ActionConverter = {
    return new NegationConverter
  }

  def unapply(howTo: HowTo): Option[AddProbabilisticLogicOperatorWrapper] = {
    if (howTo.uri.toString.contains(menta.reasoneradapter.constant.Constant.NonTerminalURIs.negation)) {
      Some(this)
    } else {
      None
    }
  }

}