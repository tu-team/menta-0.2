package menta.solutiongenerator

import menta.model.solutiongenerator.solutionchecker.RuleChangeSet
import menta.knowledgebaseserver.KnowledgeBaseServerImpl
import util.{IndividualEncoder, ECJParametersHelper}
import ec.CustomizedEvolve
import scala.collection.JavaConversions._
import menta.model.Knowledge
import org.slf4j.LoggerFactory
import menta.model.howto.{ActionClass, Solution}

/**
 * @author talanov max
 * Date: 28.02.11
 * Time: 19:53
 */

class SolutionGeneratorImpl extends SolutionGenerator {
  var kbServer = new KnowledgeBaseServerImpl

  val log = LoggerFactory.getLogger(this.getClass)

  def apply(acceptanceCriteriaChanges: RuleChangeSet): Solution = {

    log.info("Start generation")

    val parameters = generateParameters(acceptanceCriteriaChanges.getAllHowTo())

    parameters("menta.conversation.uri") = acceptanceCriteriaChanges.converstionActURI.toString;

    //populate acceptance criteria
    parameters("menta.acceptance.criteria") = acceptanceCriteriaChanges.getAllHowTo().head.uri.toString;

    log.info("Declare ecj thread");
    //assert(parameters(""))
    val t: Thread = new Thread {
      override def run() {
        try {
          log.info("Starting ecj");

          CustomizedEvolve.evaluate(asJavaDictionary[String, String](parameters));

          log.info("ECJ started");
        }
        catch {
          case e: Exception => {
            log.error(e.getMessage)
          }

        }
      }
    }

    t.setDaemon(true)
    t.setName("MentaGeneration_" + parameters("menta.conversation.uri"));
    t.start();
    //t.join()

    null
  }

  def generateParameters(acceptanceCriteria: List[Knowledge]): collection.mutable.Map[String, String] = {
    //load how-tos
    var allActionClasses = kbServer.selectAllActionClass()

    //use only root
    allActionClasses = allActionClasses.filter(p => p.isRoot);

    log.info("Action Class count " + allActionClasses.length)

    //save split action classes info
    var actionClassesInfo="";


    val parameters = ECJParametersHelper.loadDefaultParameters();

    var objectID = 0;

    var processed: Set[String] = Set[String]()

    //extend parameters with real how-tos
    def extractAllClasses(targetHowTo: ActionClass): Boolean = {

      //avoid double processing
      if (processed.contains(targetHowTo.uri.toString)) return false;

      processed += targetHowTo.uri.toString;
      //encode parent object
      var encoded = IndividualEncoder.encodeHowTo(targetHowTo)


      parameters("gp.fs.0.func." + objectID) = encoded;
      actionClassesInfo=actionClassesInfo+encoded+" ; ";

      //log.info("gp.fs.0.func." + objectID + "=" + parameters("gp.fs.0.func." + objectID))

      if (targetHowTo.parameters == null) parameters("gp.fs.0.func." + objectID + ".nc") = "nc0";
      else parameters("gp.fs.0.func." + objectID + ".nc") = "nc" + targetHowTo.parameters.count(p => p.isInstanceOf[ActionClass]);

      //log.debug("gp.fs.0.func." + objectID + ".nc" + "=" + parameters("gp.fs.0.func." + objectID + ".nc"))

      objectID = objectID + 1;
      /*if (targetHowTo.parameters != null)
     {
     //process with children
       targetHowTo.parameters.foreach(c => {
         if (c.isInstanceOf[ActionClass])
           extractAllClasses(c.asInstanceOf[ActionClass]);
       })
     } */
      return true;
    }

    log.info("Action Classes :" + actionClassesInfo);

    allActionClasses.foreach(cl =>
      extractAllClasses(cl.asInstanceOf[ActionClass]));

    parameters("gp.fs.size") = "1";
    parameters("gp.fs.0") = "ec.gp.GPFunctionSet";
    parameters("gp.fs.0.name") = "f0";
    parameters("gp.fs.0.size") = (objectID).toString;

    //set up problem

    parameters("eval.problem") = "menta.solutiongenerator.util.MentaProblem";
    parameters("eval.problem.data") = "menta.solutiongenerator.howto.AddIndividualData";


    parameters
  }


}