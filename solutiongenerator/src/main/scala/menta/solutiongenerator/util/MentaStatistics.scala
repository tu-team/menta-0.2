package menta.solutiongenerator.util

import ec.EvolutionState
import ec.simple.{SimpleProblemForm, SimpleStatistics}
import ec.gp.koza.KozaStatistics
import ec.util.Parameter
import menta.knowledgebaseserver.KnowledgeBaseServerImpl
import java.net.URI

/**
 * @author toschev Alexander
 * Date: 02.11.11
 * Time: 22:49
 */

/*
  Menta custom statistics to get access to final results
 */
class MentaStatistics extends SimpleStatistics {
      private var theKBServer=new KnowledgeBaseServerImpl
      /**Logs the best individual of the run. */
      override def finalStatistics(state: EvolutionState, result: Int): Unit =
      {

        var evaluator = new MentaProblem();
        //eval.problem
        evaluator.setup(state,new Parameter("eval.problem"));

        //load conversation place holder
        var urConv=state.parameters.getString(new Parameter(("menta.conversation.uri")),null);

        var conv= theKBServer.selectConversationAct(new URI(urConv));


        //NOTE do whatever you need
        state.output.message("\nThat's all folks ;)");

        super.finalStatistics(state, result)

        state.output.message("\n Best Individual of Run:");//, statisticslog)

          var x: Int = 0
          while (x < state.population.subpops.length) {
            {
              //save in KB
              var sln=evaluator.getSolution(best_of_run(x),state,1)
              conv.solutionURI= theKBServer.save(sln);
              //TODO: save solution
              state.output.message("Subpopulation " + x + ":");//, statisticslog)
              state.output.message("Best Individual: "+sln.toString);
              theKBServer.save(conv);
             // best_of_run(x).
             // best_of_run(x).printIndividualForHumans(state, statisticslog)
             // state.output.message("Subpop " + x + " best fitness of run: " + best_of_run(x).fitness.fitnessToStringForHumans)
              //if (state.evaluator.p_problem.isInstanceOf[SimpleProblemForm]) ((state.evaluator.p_problem.clone).asInstanceOf[SimpleProblemForm]).describe(state, best_of_run(x), x, 0, statisticslog)
            }
            ({
              x += 1; x
            })
          }
        theKBServer.save(conv);

      }

}