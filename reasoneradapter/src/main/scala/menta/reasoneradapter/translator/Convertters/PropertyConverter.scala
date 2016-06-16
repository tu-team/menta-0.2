package menta.reasoneradapter.translator.Convertters

import scala.collection.JavaConversions._
import menta.reasoneradapter.translator.ActionConverterTypes._
import menta.reasoneradapter.translator.ActionConverterTypes
import java.util.ArrayList
import menta.model.howto._
import nars.language.{Variable, Term, Product}

/**
 * @author toschev a
 * Date: 14.10.11
 * Time: 10:52
 */

class PropertyConverter extends ActionConverterFactory {

  /**
   * default property of Converter
   */
  def propertyName: String = "DefaultProperty"

  override def actionType: ActionConverterTypes = {
    return ActionConverterTypes.Property
  }

  override def convert(oper1: HowTo, oper2: HowTo, narsOp1: Term, narsOp2: Term): Term = {


    //logic changed
    /*var operandTerm1: Term = null;
    var operandTerm2: Term = null;

    def instTerm(in:String):Term={
      if (in.contains("#")) return new nars.language.Variable(in);
      return new Term(in);
    } */
    val opers = makeOperands(oper1, oper2, narsOp1, narsOp2)
    return nars.language.Inheritance.make(Product.make(new ArrayList[Term](List[Term](opers(0), opers(1)))), new Term(propertyName))
    //get first of terminal
    /*if (oper1.isInstanceOf[Terminal]) {
      operandTerm1 = new nars.language.Variable(oper1.asInstanceOf[Terminal].name)
    } else if (oper1.isInstanceOf[AddIndividual]) {
      //if this
      operandTerm1 =instTerm(oper1.asInstanceOf[AddIndividual].parameters.head.asInstanceOf[StringLiteral].valueString)
    }  else if (oper1)

    //get second operand
    if (oper2.isInstanceOf[Terminal]) {
      operandTerm2 = new nars.language.Variable(oper2.asInstanceOf[Terminal].name)
    } else if (oper1.isInstanceOf[AddIndividual]) {
      operandTerm2 = instTerm(oper2.asInstanceOf[AddIndividual].parameters.head.asInstanceOf[StringLiteral].valueString)
    }

    return nars.language.Inheritance.make(Product.make(new ArrayList[Term](List[Term](operandTerm1, operandTerm2))), new Term(propertyName))
      */
    //return new nars.language.Variable(oper2.asInstanceOf[Terminal].name)
  }
}