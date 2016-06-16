package menta.reasoneradapter.translator.Convertters

import menta.reasoneradapter.translator.{ActionConverterTypes, ActionConverter}
import ActionConverterTypes._
import nars.language.{Statement, Term}
import menta.model.howto.{AddIndividual, Terminal, HowTo}

/**
 * @author alexander
 * Date: 14.03.11
 * Time: 22:08
 */

class VariableConverter extends ActionConverterFactory {
  override def actionType: ActionConverterTypes = {
    return ActionConverterTypes.Variable
  }

  override def convert(oper1: HowTo, oper2: HowTo, narsOp1: Term, narsOp2: Term): Term = {

    if (narsOp1 != null || narsOp2 != null
      || (!oper1.isInstanceOf[Terminal] && !oper2.isInstanceOf[Terminal])
      && (!oper1.isInstanceOf[AddIndividual] && !oper2.isInstanceOf[AddIndividual])) {
      throw new Exception("Variable required only How-To type of Terminal or AddIndividual")
    }
    //get first of terminal
    if (oper1.isInstanceOf[Terminal] ) {
      return new nars.language.Variable(oper1.asInstanceOf[Terminal].name)
    } else if (oper1.isInstanceOf[AddIndividual]) {
      return new nars.language.Variable(oper1.asInstanceOf[AddIndividual].name)
    }

    //get first of terminal
    if (oper2.isInstanceOf[Terminal] ) {
      return new nars.language.Variable(oper2.asInstanceOf[Terminal].name)
    } else {
      return new nars.language.Variable(oper2.asInstanceOf[AddIndividual].name)
    }


  }
}