package menta.reasoneradapter.translator.Convertters


import nars.language.{Statement, Term}

import menta.reasoneradapter.translator.{ActionConverterTypes, ActionConverter}
import ActionConverterTypes._
import menta.model.howto.HowTo

/**
 * @author toscheva
 * Date: 09.03.11
 * Time: 18:47
 */

class InheritanceConverter extends ActionConverterFactory {
  override def actionType: ActionConverterTypes = {
    return ActionConverterTypes.Inheritance
  }

  override def convert(oper1: HowTo, oper2: HowTo, narsOp1: Term, narsOp2: Term): Term = {

    val opers = makeOperands(oper1, oper2, narsOp1, narsOp2)
    return nars.language.Inheritance.make(opers(0), opers(1))
  }
}