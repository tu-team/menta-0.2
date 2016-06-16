package menta.solutiongenerator.util

import _root_.ec.util.Parameter
import _root_.ec.{Individual, EvolutionState}
import menta.reasoneradapter.impl.ReasonerAdapterImpl
import menta.model.solutiongenerator.solutionchecker.Rule
import ec.gp.{GPIndividual, GPProblem}
import menta.model.Constant
import org.slf4j.{LoggerFactory, Logger}
import menta.reasoneradapter.translator.ProbabilisticalLogicOperators.helper.AddBlockIndividualHelper
import menta.knowledgebaseserver.KnowledgeBaseServerImpl
import java.net.URI
import ec.simple.{SimpleFitness, SimpleProblemForm}
import menta.model.howto._
import menta.solutiongenerator.howto.{AddIndividualData, RemoveIndividualData, HowToData}
import ec.gp.koza.KozaFitness

/**
 * @author Chepkunov Adel
 * Date: 28.10.11
 * Time: 18:26
 */


class MentaProblem extends GPProblem with SimpleProblemForm {

  private var adapter = new ReasonerAdapterImpl

  private var kbServer = new KnowledgeBaseServerImpl

  var input: HowToData = null
  val LOG = LoggerFactory.getLogger(this.getClass)

  override def clone: AnyRef = {
    val newobj: MentaProblem = (super.clone).asInstanceOf[MentaProblem]
    if (input != null)
      newobj.input = input.clone().asInstanceOf[HowToData];
    newobj
  }

  override def setup(state: EvolutionState, base: Parameter) {
    super.setup(state, base)
    input = state.parameters.getInstanceForParameterEq(base.push(GPProblem.P_DATA), null, classOf[HowToData]).asInstanceOf[HowToData]
    input.setup(state, base.push(GPProblem.P_DATA))
  }

  /*override def setup(state: EvolutionState, base: Parameter) {
    super.setup(state, base)
    input = state.parameters.getInstanceForParameterEq(base.push(GPProblem.P_DATA), null, classOf[HowToData]).asInstanceOf[HowToData]
    input.setup(state, base.push(GPProblem.P_DATA))
  } */

  def evaluate(state: EvolutionState, ind: Individual, subpopulation: Int, threadnum: Int) {

    val log: Logger = LoggerFactory.getLogger(this.getClass)

    if (!ind.evaluated) {
      var result: List[HowTo] = List[HowTo]()


      val rqst = new menta.model.solutiongenerator.solutionchecker.Test

      rqst.solution = getSolution(ind, state, threadnum)
     // log.debug(" Generated solution: {}", rqst.solution)

      //extract acceptance criteria
      rqst.rule = getRule(state);
      //log.debug(" Acceptance criteria rule: {}", rqst.rule)

      //TODO think about scope
      rqst.scope = Constant.modelNamespace

      val res = adapter(rqst)

      //log.info("Generated solution check result {}", res)

      // this is ECJ-ideology result return
     // val f: KozaFitness = (ind.fitness.asInstanceOf[KozaFitness])

      val f:SimpleFitness=    (ind.fitness.asInstanceOf[SimpleFitness]);
      //check if ideal value already found (i mean 0.95)
        if (res.frequency>=1)
        {
          log.info("Found ideal solution {}", res)

        }

      var idealFitness=res.getFitnessFunction().floatValue() >= 1;
      f.setFitness(state,double2Double(res.getFitnessFunction()).floatValue(),idealFitness);
     // if (idealFitness)
    //  {
    //    f.setStandardizedFitness(state,0.0f)
    //  }
     // else
    //  {
       // f.setStandardizedFitness(state,1- double2Double(res.getFitnessFunction()).floatValue())
    //  }

      ind.evaluated = true
    }
  }


  def getSolution(ind: Individual, state: EvolutionState, threadnum: Int): Solution = {
    //convert individual
    var blockValue = List[HowTo]()
    //among all trees build block
    var casted = (ind.asInstanceOf[GPIndividual]).trees;
    casted.foreach(tree => {
      var rd = ((input).asInstanceOf[HowToData]);
      tree.child.eval(state, threadnum, input, stack, (ind.asInstanceOf[GPIndividual]), this)
      blockValue = blockValue ::: List(rd.howTo)
    })

    //create block
    var helperBlock = new AddBlockIndividualHelper()
    var blockInd = helperBlock.apply(blockValue, null)

    //create facade for block
    new Solution(List(blockInd))

  }

  def getRule(state: EvolutionState): Rule = {
    val res = new Rule() {};

    //load acceptance criteria from database
    var acceptanceCriteriaURI = state.parameters.getString(new Parameter("menta.acceptance.criteria"), null);

    if (acceptanceCriteriaURI == null) {
      throw new Exception("Missing acceptance criteria, check ECJ parameter \"menta.acceptance.criteria\" ");
      return null;
    }
    var ac = kbServer.selectHowTo(new URI(acceptanceCriteriaURI));
    res.howTo = ac;

    return res
  }

}