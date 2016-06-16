package menta.solutiongenerator.howto

import ec.util.Parameter
import ec.gp.{ADFStack, GPData, GPIndividual, GPNode}
import ec.{Problem, EvolutionState}
import menta.solutiongenerator.util.IndividualEncoder
import menta.model.howto.{RemoveClass, RemoveIndividual}

/**
 * @author talanov max
 * Date: 25.10.11
 * Time: 16:37
 */

class RemoveIndividualWrapper(private val actionClass: RemoveClass) extends GPNode {
  var _removeIndividual = if (actionClass!=null) {
    new RemoveIndividualData   (IndividualEncoder.createIndividual(actionClass).asInstanceOf[RemoveIndividual] )
  } else {
    new RemoveIndividual()
  }

  def RemoveIndividual = this._removeIndividual

  def RemoveIndividual_=(aIndividual: RemoveIndividualData) = this._removeIndividual = aIndividual

  override def toString: String = {
    return actionClass.toString + _removeIndividual
  }

  override def checkConstraints(state: EvolutionState, tree: Int, typicalIndividual: GPIndividual, individualBase: Parameter): Unit = {
    super.checkConstraints(state, tree, typicalIndividual, individualBase)
    if (children.length != 0) state.output.error("Incorrect number of children for node " + toStringForError + " at " + individualBase)
  }

  def eval(state: EvolutionState, thread: Int, input: GPData, stack: ADFStack, individual: GPIndividual, problem: Problem): Unit = {
    var rd: StringLiteralData = ((input).asInstanceOf[StringLiteralData])
  }
}