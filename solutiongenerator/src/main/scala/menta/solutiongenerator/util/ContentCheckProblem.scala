package menta.solutiongenerator.util

import ec.simple.SimpleProblemForm
import ec.util.Parameter
import ec.{Individual, EvolutionState}
import ec.gp.koza.KozaFitness
import ec.gp.{GPIndividual, GPProblem}
import menta.solutiongenerator.howto.HowToData
import menta.model.howto.helper.AddParameterIndividualHelper
import menta.reasoneradapter.translator.ProbabilisticalLogicOperators.helper.{AddHasAPropertyIndividualHelper, AddHasAPropertyIndividualWrapper}
import menta.model.howto.{RemoveIndividual, AddIndividual, HowTo}
import scala.math._
import org.slf4j.LoggerFactory

/*
  Copyright 2006 by Sean Luke
  Licensed under the Academic Free License version 3.0
  See the file "LICENSE" for more information
*/

class ContentCheckProblem extends GPProblem with SimpleProblemForm {

  var input: HowToData = null
   val LOG = LoggerFactory.getLogger(this.getClass)

  override def clone: AnyRef = {
    val newobj: ContentCheckProblem = (super.clone).asInstanceOf[ContentCheckProblem]
    newobj.input = (input.clone).asInstanceOf[HowToData]
    newobj
  }

  override def setup(state: EvolutionState, base: Parameter) {
    super.setup(state, base)
    input = state.parameters.getInstanceForParameterEq(base.push(GPProblem.P_DATA), null, classOf[HowToData]).asInstanceOf[HowToData]
    input.setup(state, base.push(GPProblem.P_DATA))
  }

  /*override def describe(state: EvolutionState, ind: Individual, subpopulation: Int, threadnum: Int, log: Int): Unit=
  {
    LOG.debug("Evalution finished");
    LOG.debug("Best individual: ")
    LOG.debug(state.toString)  ;
    super.describe(state,ind,subpopulation,threadnum,log)
  } */

  def evaluate(state: EvolutionState, ind: Individual, subpopulation: Int, threadnum: Int) {
    if (!ind.evaluated) {
      val hits: Int = 0
      var penalty: Double = 0.0
      var result: List[HowTo] = List[HowTo]()

      (ind.asInstanceOf[GPIndividual]).trees(0).child.eval(state, threadnum, input, stack, (ind.asInstanceOf[GPIndividual]), this)

      var howToSet = collection.mutable.Set[HowTo]()
      result = processInputHowTo(input.howTo, howToSet)

      // calculate penalty
      penalty = abs(result.length - generateExpectedListHowTo.length)
      val noRepetitive: Set[HowTo] = result.toSet
      penalty += abs(noRepetitive.size - result.length)
      penalty += abs(noRepetitive.size - generateExpectedListHowTo.length)

      /*
      result = Math.abs(expectedResult - input.x)
      if (result <= 0.01) ({
            hits += 1;
            hits
          })
          sum += result*/


      // val expectedResult: Double = .0
      // val result: Double = .0
      // var y: Int = 0
      /* while (y < 10) {
        {
          currentX = state.random(threadnum).nextDouble
          currentY = state.random(threadnum).nextDouble
          expectedResult = currentX * currentX * currentY + currentX * currentY + currentY
          (ind.asInstanceOf[GPIndividual]).trees(0).child.eval(state, threadnum, input, stack, (ind.asInstanceOf[GPIndividual]), this)
          result = Math.abs(expectedResult - input.x)
          if (result <= 0.01) ({
            hits += 1;
            hits
          })
          sum += result
        }
        ({
          y += 1;
          y
        })

        }*/

      val f: KozaFitness = (ind.fitness.asInstanceOf[KozaFitness])
      f.setStandardizedFitness(state, penalty.asInstanceOf[Float])
      f.hits = hits
      ind.evaluated = true
    }
  }

  def generateExpectedListHowTo: List[HowTo] = {
    val l0 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).leftOperandClass, "Schema0", null)
    val r0 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).rightOperandClass, "InventoryFacade", null)
    val l1 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).leftOperandClass, "Schema1", null)

    List[HowTo](l0, r0, l1)
  }

  def processInputHowTo(in: HowTo, processed: collection.mutable.Set[HowTo]): List[HowTo] = {
    var res: List[HowTo] = List[HowTo](in)
    in match {
      case ind: AddIndividual => {
        for (val par: HowTo <- ind.parameters) {
          if (!processed.contains(par)) {
            processed += par;
            res = res ::: processInputHowTo(par, processed)
          }
        }
      }
      case ind: RemoveIndividual => {
        for (val par: HowTo <- ind.parameters) {
          if (!processed.contains(par)) {
            processed += par;
            res = res ::: processInputHowTo(par, processed)
          }
        }
      }
      case _ => {

      }
    }
    res
  }

}


