package menta.reasoneradapter.translator.Convertters

import menta.reasoneradapter.translator.ActionConverterTypes
import ActionConverterTypes._

import nars.language.Term
import menta.model.howto.HowTo

/**
 * User: alexander
 * Date: 14.03.11
 * Time: 22:01
 */
//TODO add Scala doc here.
class ConjuctionConverter extends ActionConverterFactory {
  override def actionType: ActionConverterTypes = {
    return ActionConverterTypes.Conjunction
  }

  //TODO add Scala doc here.
  override def convert(oper1: HowTo, oper2: HowTo, narsOp1: Term, narsOp2: Term): Term = {
    val opers = makeOperands(oper1, oper2, narsOp1, narsOp2)
    return nars.language.Conjunction.make(opers(0), opers(1), 0);
    //return nars.language.Implication.make(opers(0),opers(1),false,0)
  }
}