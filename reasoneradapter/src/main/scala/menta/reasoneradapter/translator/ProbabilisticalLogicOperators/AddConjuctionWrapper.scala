package menta.model.howto.ProbabilisticalLogicOperators

import menta.reasoneradapter.translator.ActionConverter
import menta.reasoneradapter.translator.Convertters.ConjuctionConverter
import menta.reasoneradapter.constant.Constant
import menta.model.howto.HowTo

/**
 * @author alexander
 * Date: 10.03.11
 * Time: 21:42
 */
// TODO add Scala doc comments for the class and properties.
// TODO add Class diagram and wiki page with description.
class AddConjuctionWrapper(in: HowTo) extends AddProbabilisticLogicOperatorWrapper(in: HowTo) {
  def this() = this (null)

  override def converter: ActionConverter = {
    return new ConjuctionConverter
  }
}

object AddConjunction {
  def unapply(howTo: HowTo): Option[AddProbabilisticLogicOperatorWrapper] = {
    if (howTo.uri.toString.contains(Constant.NonTerminalURIs.conjunction)) {
      Some(new AddConjuctionWrapper(howTo))
    } else {
      None
    }
  }
}