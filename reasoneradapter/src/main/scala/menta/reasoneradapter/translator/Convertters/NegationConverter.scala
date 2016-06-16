package menta.reasoneradapter.translator.Convertters

import menta.reasoneradapter.translator.{ActionConverterTypes, ActionConverter}
import ActionConverterTypes._

import nars.language.{Term}
import menta.model.howto.HowTo


/**
 * Created by IntelliJ IDEA.
 * User: alexander
 * Date: 14.03.11
 * Time: 22:05
 * To change this template use File | Settings | File Templates.
 */

class NegationConverter extends ActionConverterFactory {
  override def actionType: ActionConverterTypes = {
    return ActionConverterTypes.Negation
  }

  override def convert(oper1: HowTo, oper2: HowTo, narsOp1: Term, narsOp2: Term): Term = {
    //Negation required single operand
    val opers = makeOperands(oper1, oper2, narsOp1, narsOp2)
    var res: Term = null
    opers.foreach(f => {
      if (f != null) {
        res = nars.language.Negation.make(f)
        return res
      }
    })

    return res
  }
}