package menta.reasoneradapter.translator.ProbabilisticalLogicOperators

import helper.AddLoopIndividualHelper
import menta.model.howto.ProbabilisticalLogicOperators.AddProbabilisticLogicOperatorWrapper
import menta.reasoneradapter.constant.Constant
import java.net.URI
import menta.model.howto.Context
import menta.reasoneradapter.translator.HowToTranslator
import menta.model.util.triple.FrequencyConfidenceNegationTriple
import menta.reasoneradapter.interpreter.Interpreter
import menta.model.howto.{AddIndividual, AddClass, HowTo}
import org.slf4j.{LoggerFactory, Logger}
import scala.util.Random


/**
 * @author talanov max
 * Date: 10.05.11
 * Time: 9:34
 */

class AddLoopHowToInterpreter(private var _uri: URI, _parameters: List[HowTo]) extends AddProbabilisticLogicOperatorWrapper(_uri, _parameters) {
  val translator: HowToTranslator = new HowToTranslator()
  val interpreter: Interpreter = new Interpreter()
  val loopHelper: AddLoopIndividualHelper = new AddLoopIndividualHelper()
  val log: Logger = LoggerFactory.getLogger(this.getClass)

  @deprecated
  def apply(context: Context, scopeURI: URI): FrequencyConfidenceNegationTriple = {
    var res: List[FrequencyConfidenceNegationTriple] = List[FrequencyConfidenceNegationTriple]()
    val variableHowTo: HowTo = operand1
    val listRefHowTo = operand2
    val listRef: AddVariableWrapper = new AddVariableWrapper(listRefHowTo)
    val thirdParameter: HowTo = _parameters(3)
    val bodyHowTo: List[AddIndividual] = List[AddIndividual]()

    for (val listHowTo: List[HowTo] <- listRef.value(context, scopeURI)) {
      // created scope
      context.scope_(this.addClass.uri, Map[URI, List[HowTo]]())
      for (val howTo: HowTo <- listHowTo) {
        // put var in the scope
        context.variable_(this.addClass.uri, variableHowTo.uri, howTo)
        res = res ::: List[FrequencyConfidenceNegationTriple](interpreter(bodyHowTo, context, this.addClass.uri))
      }
    }
    interpreter.combineResults(res, context, this.addClass.uri)
  }

  /**
   *
   */
  def apply(aLoopIndividual: AddIndividual, aContext: Context, aScopeURI: URI): FrequencyConfidenceNegationTriple = {
    var res: List[FrequencyConfidenceNegationTriple] = List[FrequencyConfidenceNegationTriple]()

    val variableHowTo: AddIndividual = loopHelper.valueVariable(aLoopIndividual)
    val bodyList: List[AddIndividual] = loopHelper.bodyAddIndividualList(aLoopIndividual)

    val listRefHowTo: AddIndividual = loopHelper.listReference(aLoopIndividual)
    val listRefWrapper: AddVariableWrapper = new AddVariableWrapper(listRefHowTo)
    var listVarName: URI = new URI(listRefWrapper.variableName(listRefHowTo))

    // log.debug("aLoopIndividual {}", aLoopIndividual)
    // created scope
    //make scope 100% unique
    val newScope=new URI(aLoopIndividual.actionClass.uri.toString+Random.nextInt());
    if (listVarName.toString.contains(menta.reasoneradapter.constant.Constant.NonTerminalURIs.loopVariable)) {
      listVarName = new URI(listVarName.toString.replace(menta.reasoneradapter.constant.Constant.NonTerminalURIs.loopVariable, ""));
    }
    val theListBlockOption: Option[List[HowTo]] = listRefWrapper.value(aContext, aScopeURI, listVarName)

    for (val listHowTo: List[HowTo] <- theListBlockOption) {
      // found referenced var is scope
      if (listHowTo != null && listHowTo.size > 0) {
        for (val listHowToMember: HowTo <- listHowTo) {
          val listHowToIndividual: AddIndividual = listHowToMember.asInstanceOf[AddIndividual]
          val listHowToIndividualParameters: List[HowTo] = listHowToIndividual.parameters


          aContext.scope_(newScope, Map[URI, List[HowTo]]())
          aContext.addEntry(newScope, new URI(variableHowTo.name), List(listHowToIndividual))
          val bodyListToProcess = bodyList.map(b=>b.clone().asInstanceOf[AddIndividual]);//.::(listHowToIndividual)
          //log.debug("loop body {}", bodyList)
          res = res ::: List[FrequencyConfidenceNegationTriple](interpreter.apply(bodyListToProcess, aContext, newScope,true))
          //log.debug(" loop how to result {}", res)
        }
      }
    }
    interpreter.combineResults(res, aContext,newScope)
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

object AddLoop {
  def unapply(howTo: AddClass): Option[AddProbabilisticLogicOperatorWrapper] = {
    if (howTo.uri.toString.contains(Constant.NonTerminalURIs.loop)) {
      Some(new AddLoopHowToInterpreter(howTo.uri, howTo.parameters))
    } else {
      None
    }
  }

  def unapply(individual: AddIndividual): Option[AddProbabilisticLogicOperatorWrapper] = {
    if (individual.actionClass.uri.toString.contains(Constant.NonTerminalURIs.loop)) {
      Some(new AddLoopHowToInterpreter(individual.uri, individual.parameters))
    } else {
      None
    }
  }
}