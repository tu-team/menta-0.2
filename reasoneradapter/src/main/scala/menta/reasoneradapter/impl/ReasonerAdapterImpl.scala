package menta.reasoneradapter.impl

import menta.reasoneradapter.reasonerinterface.NARSInterface
import menta.reasoneradapter.ReasonerAdapter
import menta.reasoneradapter.selfcontrol.impl.SelfControlFactoryImpl
import menta.reasoneradapter.reasonerinterface.impl.NARSHelper
import menta.model.solutiongenerator.solutionchecker.Test
import menta.reasoneradapter.translator.HowToTranslator
import menta.model.util.triple.FrequencyConfidenceNegationTriple
import menta.reasoneradapter.interpreter.Interpreter
import menta.model.howto.Context
import menta.model.Constant
import java.net.URI
import menta.model.howto.{ActionClass, AddIndividual, HowTo}
import nars.main.Memory
import menta.reasoneradapter.translator.ProbabilisticalLogicOperators.AddConstantBlock

/**
 * {@link ReasonerAdapter} implementation.
 */
class ReasonerAdapterImpl extends ReasonerAdapter {

  def apply(request: Test): FrequencyConfidenceNegationTriple = {
    //Init NARS
    Memory.init();

    val t = new Interpreter
    val solution: List[HowTo] =request.solution.howTos
    val context:Context=new Context()
    context.addEntry(Constant.modelNamespace, new URI(Constant.Solution), solution)

    //TODO: copy ac, using AddIndividualWrapper._clone
    val ac =List[AddIndividual]( request.rule.howTo.asInstanceOf[AddIndividual].clone().asInstanceOf[AddIndividual])

    //modify acceptance criteria to exclude constant block,
    //assume location of constant block on top level
    val updatedAC=ac.filter(b=>
    {
       b match {
         case AddConstantBlock(ignr)=>false
         case _ => true
       }
    });

    // val acCopy =
    val scope=request.scope

    t.apply(updatedAC, context, scope,true)
  }
}