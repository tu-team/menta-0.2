package menta.reasoneradapter.translator

import Convertters.ActionConverterFactory
import nars.language.Term
import nars.entity._
import menta.model.howto.Context
import java.net.URI
import menta.model.util.triple.FrequencyConfidenceNegationTriple
import java.lang.IllegalArgumentException
import menta.model.Constant
import menta.model.howto.{AddClass, AddIndividual, Terminal, HowTo}

/**
 * Converts a graph of HowTo to NARS Task.
 * author: toschev alexander
 * Date: 07.03.11
 * Time: 16:45
 */

// TODO add Class diagram and wiki page with description.
class HowToTranslator {

  private var convertersFactory: ActionConverterFactory = new ActionConverterFactory()

  /**
   * Translate How-To to Task
   * @param input incoming how-to graph
   */
  def Translate(input: HowTo, context: Context, scope: URI): Task = {

    //make sentence
    val sent = new Judgment(extractStatement(input, context, scope), '.', new TruthValue(1.0f, 0.99f), new Stamp(), null, null)

    //make task
    return new Task(sent, new BudgetValue()) //new ShortFloat(1),(float2Float("0.9")),new ShortFloat(0.01f)))
  }

  /**
   * Translate How-To to Task
   * @param input inbound how-to and FrequencyConfidenceNegationTriple
   * @param
   */
  def Translate(input: Pair[HowTo, FrequencyConfidenceNegationTriple], context: Context, scope: URI): Task = {

    //make sentence
    val extractedStatement = extractStatement(input._1, context, scope)
    val truthValue = new TruthValue(input._2.frequency.toFloat, input._2.confidence.toFloat)
    val sent = new Judgment(extractedStatement, '.', truthValue, new Stamp(), null, null)

    //make task
    return new Task(sent, new BudgetValue)
  }

  private def extractStatement(input: HowTo, context: Context, scope: URI): Term = {

    if (!input.isInstanceOf[AddIndividual]) {
      throw new IllegalArgumentException("the input parameter is not AddIndividual")
    } else {
      //extract proper class from how to
      val individual: AddIndividual = input.asInstanceOf[AddIndividual]
      var params: List[HowTo] = individual.parameters

      def isVariable(ind: AddIndividual): Boolean = {
        if (ind.parameters.length <= 0) return false;
        if (ind.parameters.head.isInstanceOf[AddIndividual]!=true || ind.parameters.head.asInstanceOf[AddIndividual].actionClass.isInstanceOf[AddClass]!=true) return false;
        if (ind.parameters.head.asInstanceOf[AddIndividual].actionClass.asInstanceOf[AddClass].uri.toString.contains(Constant.variable)) return true;
        return false;
      };

      if (params != null && params.size > 1) {
        //drill down through tree to find terminal + terminal
        val operand1 = params(0)
        var operand1Term: Term = null
        //process operand 1
        if (operand1 != null) {

          if (!operand1.isInstanceOf[Terminal] && !operand1.isInstanceOf[AddIndividual]) {
            operand1Term = extractStatement(operand1, context, scope)
          }
          if (operand1.isInstanceOf[AddIndividual]) {
            val addIndividualOperand = operand1.asInstanceOf[AddIndividual]
            if (!addIndividualOperand.actionClass.uri.toString.contains(Constant.leftOperand) || (isVariable(addIndividualOperand))) {
              operand1Term = extractStatement(operand1, context, scope)
            }
          }
        }
        val operand2 = params(1)
        var operand2Term: Term = null

        //process operand 2
        if (operand2 != null) {
          if (!operand2.isInstanceOf[Terminal] && !operand2.isInstanceOf[AddIndividual]) {
            operand2Term = extractStatement(operand2, context, scope)
          }
          if (operand2.isInstanceOf[AddIndividual]) {
            val addIndividualOperand = operand2.asInstanceOf[AddIndividual]
            if (!addIndividualOperand.actionClass.uri.toString.contains(Constant.rightOperand) || (isVariable(addIndividualOperand) )) {
              operand2Term = extractStatement(operand2, context, scope)
            }
          }
        }
        //compound operands by logic type
        val c = convertersFactory.InstantiateSuitableConverter(input)
        return c.convert(operand1, operand2, operand1Term, operand2Term)
      } else if (params != null && params.size > 0) {
        // HowTo parameter case
        val operand1 = params(0)
        var operand1Term: Term = null
        //process operand 1
        if (operand1 != null) {
          if (!operand1.isInstanceOf[Terminal] && !operand1.isInstanceOf[AddIndividual]) {
            operand1Term = extractStatement(operand1, context, scope)
          }

        }
        val c = convertersFactory.InstantiateSuitableConverter(operand1)
        // parameter case
        // TODO
         return c.convert(operand1,null, operand1Term,null)
        null
      } else {
        throw new IllegalArgumentException("there should be at least 2 parameters in AddIndividual " + individual.actionClass.uri.toString + " " + params.toString())
      }
    }
  }
}