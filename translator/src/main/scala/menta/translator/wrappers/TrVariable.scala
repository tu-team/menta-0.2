package menta.translator.wrappers

import menta.model.howto._
import menta.translator.TranslationStrategy
import java.net.URI
import org.slf4j.LoggerFactory

/**
 * @author adel chepkunov, max talanov
 * Date: 12.11.11
 * Time: 15:29
 */

class TrVariable(aValueAddress: String) extends ActionClass with TrWrapper {

  private var _individual: AddIndividual = null

  val log = LoggerFactory.getLogger(this.getClass)

  ValueAddress = aValueAddress
  name = menta.model.Constant.trVariable

  //if(this.name != menta.model.Constant.trVariable)
  //  throw new Exception(this.name + "<>" + menta.model.Constant.trVariable)

  def this() = {
    this ("");
    ValueAddress = ""
  }

  def this(aIndividual: AddIndividual) {
    this ()
    _individual = aIndividual
  }

  def addIndividual(): TrVariable = {
    val r = new TrVariable(aValueAddress)
    r
  }

  override def apply(translationNode: AddIndividual, solutionNode: HowTo, context: Context,
                     strategy: TranslationStrategy, mode:StringLiteral = new StringLiteral(""), conditionExpression:LogicalExpression = new LETrue): HowTo = {
    //context.addEntry(solutionNodeActionIndividual.actionClass.uri, new URI(Constant.currentNode), List(solutionNodeActionIndividual), true)
    val   solutionNodeActionIndividual= solutionNode.asInstanceOf[ActionIndividual];
    val list = context.value(solutionNodeActionIndividual.actionClass.uri,new URI(ValueAddress))


    if (list.isEmpty)
    {
       log.info("Variable not found" +ValueAddress);
      return new StringLiteral(solutionNode.name)
    }// was new NilHowTo
    //try to find Add Name in this individual
    var single = list.get(0).asInstanceOf[ActionIndividual];
    log.info("Getting variable" +ValueAddress+" ; from "+single.toString);

    return single.parameters.head.asInstanceOf[ActionIndividual].parameters.head.asInstanceOf[StringLiteral];

    //else list.get(0)
  }

  def ValueAddress = parameters(0).asInstanceOf[StringLiteral].toString()

  def ValueAddress_=(arg: String) {
    parameters = List[HowTo](new StringLiteral(arg))
  }

  def getValue(node: HowTo, param: List[StringLiteral]): StringLiteral = {
    new StringLiteral("element")
    new StringLiteral(node.name)
    // ToDo: get variable with syntactics http://code.google.com/p/menta/wiki/AddVariableExtender
  }
}



object TrVariable extends TrMatcher {
  def unapply(individual: AddIndividual): Option[TrWrapper] = {
    if (individual.actionClass.uri.toString.contains(menta.model.Constant.trVariable)) {
      Some(new TrVariable(individual: AddIndividual))
    } else {
      None
    }
  }
}
