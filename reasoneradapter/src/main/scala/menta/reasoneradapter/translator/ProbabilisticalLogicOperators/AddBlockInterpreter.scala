package menta.reasoneradapter.translator.ProbabilisticalLogicOperators

import helper.{AddBlockIndividualHelper, AddLoopIndividualHelper}
import java.net.URI
import menta.model.howto.ProbabilisticalLogicOperators.AddProbabilisticLogicOperatorWrapper
import menta.reasoneradapter.translator.HowToTranslator
import menta.reasoneradapter.interpreter.Interpreter
import org.slf4j.{LoggerFactory, Logger}
import menta.model.howto.Context
import menta.model.util.triple.FrequencyConfidenceNegationTriple

import menta.model.howto.{AddClass, AddIndividual, HowTo}
import menta.reasoneradapter.constant.Constant

/**
 * @author toschev alexander
 * Date: 12.11.11
 * Time: 11:16
 */

/**
   Logic to process blocks
   @author 
    Alexander 
*/
class AddBlockInterpreter (private var _uri: URI, _parameters: List[HowTo]) extends AddProbabilisticLogicOperatorWrapper(_uri, _parameters) {
  val translator: HowToTranslator = new HowToTranslator()
  val interpreter: Interpreter = new Interpreter()
  val blockHelper: AddBlockIndividualHelper = new AddBlockIndividualHelper()
  val log: Logger = LoggerFactory.getLogger(this.getClass)


  /**
   *
   */
  def apply(aLoopIndividual: AddIndividual, aContext: Context, aScopeURI: URI): FrequencyConfidenceNegationTriple = {
    val listOfAddIndividual=aLoopIndividual.parameters.map(b=>b.asInstanceOf[AddIndividual]);

    return interpreter.apply(listOfAddIndividual,aContext,aScopeURI)

  }
}
object AddBlock {
  def unapply(howTo: AddClass): Option[AddProbabilisticLogicOperatorWrapper] = {
    if (howTo.uri.toString.contains(Constant.NonTerminalURIs.block)) {
      Some(new AddBlockInterpreter(howTo.uri, howTo.parameters))
    } else {
      None
    }
  }

  def unapply(individual: AddIndividual): Option[AddProbabilisticLogicOperatorWrapper] = {
    if (individual.actionClass.uri.toString.contains(Constant.NonTerminalURIs.block)) {
      Some(new AddBlockInterpreter(individual.uri, individual.parameters))
    } else {
      None
    }
  }
}