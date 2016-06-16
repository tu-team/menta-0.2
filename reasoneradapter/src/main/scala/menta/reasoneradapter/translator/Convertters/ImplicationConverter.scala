package menta.reasoneradapter.translator.Convertters

import menta.reasoneradapter.translator.ActionConverterTypes
import ActionConverterTypes._
import nars.language.Term
import menta.model.howto.HowTo


/**
 * User: toscheva
 * Date: 08.03.11
 * Time: 17:18
 */

//TODO add Scala doc here.
class ImplicationConverter extends ActionConverterFactory {
  //TODO add Scala doc here.
  override def actionType: ActionConverterTypes = {
    return ActionConverterTypes.Implication
  }

  //make nars statement
  //TODO add Scala doc here.
  override def convert(oper1: HowTo, oper2: HowTo, narsOp1: Term, narsOp2: Term): Term = {
    var operands = makeOperands(oper1, oper2, narsOp1, narsOp2)
    //return statement
    return nars.language.Implication.make(operands(0), operands(1), false, 0)
  }
}