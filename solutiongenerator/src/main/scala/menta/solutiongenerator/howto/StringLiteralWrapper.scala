package menta.solutiongenerator.howto

import ec.app.tutorial4.{MultiValuedRegression, DoubleData}
import ec.util.Parameter
import ec.gp.{ADFStack, GPData, GPIndividual, GPNode}
import ec.{Problem, EvolutionState}
import menta.model.howto.{StringLiteral, HowTo}

/**
 * @author talanov max
 * Date: 25.10.11
 * Time: 9:15
 */

class StringLiteralWrapper(private val value: String) extends GPNode {
  override def toString: String = {
    return value
  }

  override def checkConstraints(state: EvolutionState, tree: Int, typicalIndividual: GPIndividual, individualBase: Parameter): Unit = {
    super.checkConstraints(state, tree, typicalIndividual, individualBase)
    if (children.length != 0) state.output.error("Incorrect number of children for node " + toStringForError + " at " + individualBase)
  }

  def eval(state: EvolutionState, thread: Int, input: GPData, stack: ADFStack, individual: GPIndividual, problem: Problem): Unit = {
    var rd: StringLiteralData = ((input).asInstanceOf[StringLiteralData])
  }
}