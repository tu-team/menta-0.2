package menta.reasoneradapter.translator.Convertters


import menta.reasoneradapter.translator.{ActionConverterTypes, ActionConverter}

import ActionConverterTypes._
import nars.language.Term
import menta.model.howto.ProbabilisticalLogicOperators.AddProbabilisticLogicOperatorWrapper

import menta.model.Constant
import menta.model.howto._

/**
 * @author alexander
 * Date: 10.03.11
 * Time: 22:46
 */

class ActionConverterFactory extends ActionConverter {

  //How-to facade of specific logic operation should hold suitable converter
  def InstantiateSuitableConverter(input: HowTo): ActionConverter = {
    (new AddProbabilisticLogicOperatorWrapper()).Construct(input, null, null).converter
  }

  //eof factory


  def actionType: ActionConverterTypes = null

  def convert(oper1: HowTo, oper2: HowTo, narsOp1: Term, narsOp2: Term): Term = null

  //TODO add Scala doc comments.
  //TODO suppose the return type should be Pair.
  def makeOperands(aOperand1: HowTo, aOperand2: HowTo, aNarsOp1: Term, aNarsOp2: Term): List[Term] = {
    var operand1: Term = null
    var operand2: Term = null
    if (aNarsOp1 != null) {
      operand1 = aNarsOp1
    }
    else {
      //make operand 1
      if (aOperand1 != null)
        operand1 = convertToNARS(aOperand1)
    }
    if (aNarsOp2 != null) {
      operand2 = aNarsOp2
    }
    else {
      //make operand 2
      if (aOperand2 != null) {
        operand2 = convertToNARS(aOperand2)
      }
    }
    return List(operand1, operand2)
  }

  //TODO add Scala doc comments here.
  def convertToNARS(oper: HowTo): Term = {
    if (oper.isInstanceOf[Terminal]) {
      return new Term(oper.asInstanceOf[Terminal].name)
    } else if (oper.isInstanceOf[AddIndividual]) {
      val operIndividual = oper.asInstanceOf[AddIndividual]
      if (operIndividual.actionClass.uri.toString.contains(Constant.leftOperand) ||
        operIndividual.actionClass.uri.toString.contains(Constant.rightOperand)) {
        val literal = operIndividual.parameters(0)
        if (literal.isInstanceOf[StringLiteral]) {
          return new Term(literal.asInstanceOf[StringLiteral].toString)
        } else if (literal.isInstanceOf[NumberLiteral]) {
          return new Term(literal.asInstanceOf[NumberLiteral].value.toString)
        } else {
          throw new IllegalArgumentException("oper is not Term " + oper.uri.toString)
        }

      } else {
        throw new IllegalArgumentException("oper is not Term " + oper.uri.toString)
      }

    } else {
      throw new IllegalArgumentException("oper is not Term " + oper.uri.toString)
    }
  }
}