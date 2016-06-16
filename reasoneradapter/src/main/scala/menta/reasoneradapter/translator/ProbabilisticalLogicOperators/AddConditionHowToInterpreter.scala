package menta.reasoneradapter.translator.ProbabilisticalLogicOperators

import helper.AddConditionIndividualHelper
import menta.model.howto.ProbabilisticalLogicOperators.AddProbabilisticLogicOperatorWrapper
import menta.reasoneradapter.constant.Constant
import java.net.URI
import menta.model.howto.Context
import menta.reasoneradapter.translator.HowToTranslator
import menta.model.util.triple.FrequencyConfidenceNegationTriple
import menta.reasoneradapter.interpreter.Interpreter
import menta.model.howto.{AddIndividual, AddClass, HowTo}

/**
 * @author talanov max
 * Date: 10.05.11
 * Time: 9:34
 */

class AddConditionHowToInterpreter(private var _uri: URI, _parameters: List[HowTo]) extends AddProbabilisticLogicOperatorWrapper(_uri, _parameters) {
  val translator: HowToTranslator = new HowToTranslator()
  val interpreter: Interpreter = new Interpreter()
  val conditionHelper: AddConditionIndividualHelper = new AddConditionIndividualHelper()

  def apply(aConditionIndividual: AddIndividual, aContext: Context, aScopeURI: URI): FrequencyConfidenceNegationTriple = {
    //var res: List[FrequencyConfidenceNegationTriple] = List[FrequencyConfidenceNegationTriple]()

    val falseBlockHowTo: AddIndividual = conditionHelper.falseBlock(aConditionIndividual)
    val trueBlockHowTo: AddIndividual = conditionHelper.trueBlock(aConditionIndividual)
    val extractedExpression: AddIndividual = conditionHelper.conditionExpression(aConditionIndividual)




    var targetBlock: List[AddIndividual] = null;


    //found condition evaluation result
    //var evalRes=interpreter.apply(conditionExpression,aContext,this.addClass().uri);

    //check freq is greater than 0.5 true otherwise false
    if (_checkCondition(aConditionIndividual, aContext,aScopeURI)) {
      targetBlock = conditionHelper.trueBlockAddIndividualList(aConditionIndividual);
    }
    else {
      targetBlock = conditionHelper.falseBlockAddIndividualList(aConditionIndividual);
    }


    //process selected block
    return interpreter(targetBlock, aContext, aScopeURI)

  }

  private def _checkCondition(expression: AddIndividual, aContext: Context,aScopeURI: URI) = {
    val extracted = conditionHelper.conditionBlockAddIndividualList(expression)
    var evaluated = interpreter.apply(extracted, aContext, aScopeURI)
    evaluated.frequency > Constant.conditionHowToThreshold
  }

  /*
    short check of expression
    @param aConditionIndividual - AddCondition, will be used only condition expression part
   */
  def checkCondition(aConditionIndividual: AddIndividual, aContext: Context,aScopeURI: URI): Boolean = {

    return _checkCondition(aConditionIndividual, aContext,aScopeURI)
  }


  /**
   *  return list of operands with specific tag
   * @param aTag the tag to specify parameter
   */
  @deprecated
  def getOperandList3: List[HowTo] = {
    getOperandList("operand3")
  }
}

object AddCondition {
  def unapply(howTo: AddClass): Option[AddProbabilisticLogicOperatorWrapper] = {
    if (howTo.uri.toString.contains(Constant.NonTerminalURIs.condition)) {
      Some(new AddConditionHowToInterpreter(howTo.uri, howTo.parameters))
    } else {
      None
    }
  }

  def unapply(individual: AddIndividual): Option[AddProbabilisticLogicOperatorWrapper] = {
    if (individual.actionClass.uri.toString.contains(Constant.NonTerminalURIs.condition)) {
      Some(new AddConditionHowToInterpreter(individual.uri, individual.parameters))
    } else {
      None
    }
  }
}