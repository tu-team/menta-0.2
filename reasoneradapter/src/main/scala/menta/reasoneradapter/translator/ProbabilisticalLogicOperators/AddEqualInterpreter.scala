package menta.reasoneradapter.translator.ProbabilisticalLogicOperators

import helper.{AddEqualIndividualHelper, AddConditionIndividualHelper}
import java.net.URI
import menta.model.howto.ProbabilisticalLogicOperators.AddProbabilisticLogicOperatorWrapper
import menta.reasoneradapter.translator.HowToTranslator
import menta.reasoneradapter.interpreter.Interpreter
import org.slf4j.{LoggerFactory, Logger}
import menta.model.howto.Context
import menta.model.util.triple.FrequencyConfidenceNegationTriple
import menta.reasoneradapter.constant.Constant
import menta.model.howto.{StringLiteral, AddClass, AddIndividual, HowTo}
import nars.language.Term

/**
 * @author toschev alexander
 * Date: 13.11.11
 * Time: 16:42
 */


class AddEqualInterpreter(private var _uri: URI, _parameters: List[HowTo]) extends AddProbabilisticLogicOperatorWrapper(_uri, _parameters) {
  val translator: HowToTranslator = new HowToTranslator()
  val interpreter: Interpreter = new Interpreter()
  val equalHelper: AddEqualIndividualHelper = new AddEqualIndividualHelper()
  val log: Logger = LoggerFactory.getLogger(this.getClass)


  /**
   *
   */
  def apply(aEqualIndividual: AddIndividual, aContext: Context, aScopeURI: URI): FrequencyConfidenceNegationTriple = {

    val leftOperand = equalHelper.leftOperand(aEqualIndividual)
    val rightOperand = equalHelper.rightOperand(aEqualIndividual)

    def extract(oper: AddIndividual): String = {
      if (oper.isInstanceOf[AddIndividual]) {
        val operIndividual = oper.asInstanceOf[AddIndividual]
          val literal = operIndividual.parameters(0)
          if (literal.isInstanceOf[StringLiteral]) {
            return literal.asInstanceOf[StringLiteral].toString()
          }

      }
      return "undefined"
    }

    var leftString = extract(leftOperand);
    var rightString =extract(rightOperand);

    if (leftString ==rightString && leftOperand!="undefined")
    {
      return new FrequencyConfidenceNegationTriple(1, 1, false)
    }
    //log.debug("Variable mismatch: left:"+leftString+" right:"+rightString)
    return new FrequencyConfidenceNegationTriple(0, 1, false)

  }
}

object AddEqual {
  def unapply(howTo: AddClass): Option[AddProbabilisticLogicOperatorWrapper] = {
    if (howTo.uri.toString.contains(Constant.NonTerminalURIs.equal)) {
      Some(new AddBlockInterpreter(howTo.uri, howTo.parameters))
    } else {
      None
    }
  }

  def unapply(individual: AddIndividual): Option[AddProbabilisticLogicOperatorWrapper] = {
    if (individual.actionClass.uri.toString.contains(Constant.NonTerminalURIs.equal)) {
      Some(new AddBlockInterpreter(individual.uri, individual.parameters))
    } else {
      None
    }
  }
}