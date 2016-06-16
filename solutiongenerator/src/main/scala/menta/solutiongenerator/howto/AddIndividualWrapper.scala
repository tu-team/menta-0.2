package menta.solutiongenerator.howto

import ec.util.Parameter
import ec.gp.{ADFStack, GPData, GPIndividual, GPNode}
import ec.{Problem, EvolutionState}
import menta.solutiongenerator.util.IndividualEncoder
import menta.model.howto._

/**
 * @author talanovm
 * Date: 25.10.11
 * Time: 16:37
 */

class AddIndividualWrapper(private val actionCls: AddClass) extends GPNode {
  var _addIndividualData: AddIndividualData = if (actionCls != null) {
    new AddIndividualData(IndividualEncoder.createIndividual(actionCls).asInstanceOf[AddIndividual])
  } else {
    new AddIndividualData(new AddIndividual())
  }

  def addIndividualData: AddIndividualData = this._addIndividualData

  def addIndividualData_=(aIndividual: AddIndividualData) = this._addIndividualData = aIndividual

  override def toString: String = {
    var res = ""
    if (_addIndividualData != null && _addIndividualData.howTo != null) {
      res += _addIndividualData.howTo.toString
    } else {
      res += ".null"
    }
    if (children != null) {
      res += " ch " + children.length
    }
    res
  }

  override def checkConstraints(state: EvolutionState, tree: Int, typicalIndividual: GPIndividual, individualBase: Parameter): Unit = {
    super.checkConstraints(state, tree, typicalIndividual, individualBase)
    if (addIndividualData.howTo.actionClass != null) {
      if (!((addIndividualData.howTo.actionClass.parameters == null && children.length == 0) || (addIndividualData.howTo.actionClass.parameters != null && addIndividualData.howTo.actionClass.parameters.count(p => p.isInstanceOf[ActionClass]) == children.length))) {
        var errorTxt = "Incorrect number of children for node " + toStringForError + " at " + individualBase + " expected: ";
        if (addIndividualData.howTo.actionClass.parameters == null) {
          state.output.error(errorTxt)
        } else {

          state.output.error("Incorrect number of children for node " + toStringForError + " at " + individualBase
            + " expected: " + addIndividualData.howTo.actionClass.parameters.count(p => p.isInstanceOf[ActionClass]) + " actual: " + children.length)
        }
      }
    }
    else if (children.length != 0) {
      state.output.error("Incorrect number of children for node " + toStringForError + " at " + individualBase)
    }
  }

  def _clone(parameters: List[HowTo]): List[HowTo] = {

   val newParameters: List[HowTo] = for (val par <- parameters) yield {
      val res: HowTo = par match {
        case p: AddIndividual => {
          val parData = new AddIndividualData(p)
          val n = new AddIndividualData()
          parData.baseCopyTo(n)
          n.howTo
        }
        case p: RemoveIndividual => {
          val parData = new RemoveIndividualData(p)
          val n = new RemoveIndividualData()
          parData.baseCopyTo(n)
          n.howTo
        }
        case p: StringLiteral => {
          val sl = new StringLiteral(p.valueString)
          sl
        }
        case p: NumberLiteral => {
          val nl = new NumberLiteral((p.valueNumber))
          nl
        }
      }
      res
    }
    return newParameters
  }


  def eval(state: EvolutionState, thread: Int, input: GPData, stack: ADFStack, individual: GPIndividual, problem: Problem): Unit = {

    // tutorial4.java
    /* double result;
    DoubleData rd = ((DoubleData)(input));
    children[0].eval(state,thread,input,stack,individual,problem);
    result = rd.x;
    children[1].eval(state,thread,input,stack,individual,problem);
    rd.x = result + rd.x;*/

    var result: List[HowTo] = List[HowTo]()
    if (input == null) return;
    var rd: HowToData = ((input).asInstanceOf[HowToData])
    for (val ch: GPNode <- children) {
      ch.eval(state, thread, input, stack, individual, problem)
      result = result ::: List(rd.howTo)
    }

    //var newList = List[HowTo]();
    //result.foreach(b=>
   // {
    //   newList=newList:::List(_clone(b.asInstanceOf[AddIndividual]))
    //});
    this.addIndividualData.howTo.parameters = result.map(b=>if (b!=null) b.clone().asInstanceOf[HowTo] else b);
    if (this.addIndividualData != null)
      rd.howTo = this.addIndividualData.howTo
  }

}