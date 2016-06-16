package menta.model.howto.ProbabilisticalLogicOperators

import menta.reasoneradapter.translator.Convertters.InheritanceConverter
import menta.reasoneradapter.translator.ActionConverter
import menta.model.howto.HowTo
import java.net.URI

/**
 * @author alexander
 * Date: 10.03.11
 * Time: 21:48
 */
// TODO add Scala doc comments for the class and properties.
// what's that doing?

class AddInheritanceWrapper(in: HowTo) extends AddProbabilisticLogicOperatorWrapper(in: HowTo) {

  override def converter: ActionConverter = {
    return new InheritanceConverter
  }

  def this() = this (null)

   def unapply(howTo: HowTo): Option[AddProbabilisticLogicOperatorWrapper] = {
    if (howTo.uri.toString.contains(menta.reasoneradapter.constant.Constant.NonTerminalURIs.inheritance)) {
      Some(this)
    } else {
      None
    }
  }
}