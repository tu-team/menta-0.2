package menta.translator.wrappers

import java.net.URI
import org.slf4j.{Logger, LoggerFactory}
import menta.model.howto._
import menta.model.helpers.common.IndividualMakerFactory
import menta.model.helpers.individual.AddConstantIndividualHelper
import menta.model.helpers.constantblock.{AddFieldChecker, AddFieldHelper}
import menta.knowledgebaseserver.{KnowledgeBaseServerImpl, KnowledgeBaseServer}

/**
 * @author talanov max
 * Date: 24.11.11
 * Time: 19:20
 */

class TrConstantField() extends TrConstantWrapper {
  val LOG: Logger = LoggerFactory.getLogger(this.getClass)
  private var theKBServer: KnowledgeBaseServer = new KnowledgeBaseServerImpl;

  /*
   apply addfield translation
  */


  private def generateSingleNodePrms(p: ActionIndividual, context: Context): List[HowTo] = {
    var newParametersBlock = List[HowTo]();
    if (p.isInstanceOf[ActionIndividual]) {
      //enable processing
      var casted = p.asInstanceOf[ActionIndividual];
      //extract context
      var vl = context.value(new URI(menta.model.Constant.trConstantScope), casted.actionClass.uri)
      vl match {

        case Some(vl) => {
          vl.head match {
            case AddFieldChecker(addField) => {
              var helper = new AddFieldHelper(addField);

              //found let's modify solution module
              //if AddField constant we have a list of blocks,suppose this is parameters block
              var blocks = helper.constantBlocks();
              blocks.foreach(block => {

                //format parent individual
                var indBlock = IndividualMakerFactory.instantiate(casted.actionClass)
                var prms = List[ActionIndividual]();

                //process around parameters in block and modify solution element
                block.parameters.foreach(b => {
                  //process it as if it will be simple add name
                  var indToAdd = AddConstantIndividualHelper.getIndividual(b.asInstanceOf[AddIndividual], "");
                  //refresh action class
                  indToAdd.actionClass = theKBServer.selectActionClass(indToAdd.actionClass.uri).asInstanceOf[AddClass];

                  prms = prms ::: List(indToAdd);
                })
                indBlock.parameters = prms;
                newParametersBlock = newParametersBlock ::: List(indBlock);
              });

            }

            case AddConstantIndividualHelper(simpleConstant) => {
              newParametersBlock = newParametersBlock ::: List(AddConstantIndividualHelper.getIndividual(simpleConstant.asInstanceOf[AddIndividual], casted.actionClass))
            }

            case _ => {
              //just add to new parameters sub set
              newParametersBlock = newParametersBlock ::: (List(p));
            }
          }
        }

        case None => {
          //just add to new parameters sub set
          newParametersBlock = newParametersBlock ::: (List(p));
        }

      }
    }
    else {
      //just add to new parameters sub set
      newParametersBlock = newParametersBlock ::: (List(p));
    }
    newParametersBlock
  }

  def apply(translationNode: AddIndividual, solutionNode: HowTo, context: Context): HowTo = {
    if (solutionNode.isInstanceOf[ActionIndividual]) {
      var newParametersBlock = List[HowTo]();
      //check if have something for this addclass
      var castedInd = solutionNode.asInstanceOf[ActionIndividual];
      if (castedInd.parameters == null || castedInd.parameters.length<=0) {
        //may be we have something for this
        var vl = context.value(new URI(menta.model.Constant.trConstantScope), castedInd.actionClass.uri)
        if (vl != None) {
          newParametersBlock = generateSingleNodePrms(castedInd, context).head.asInstanceOf[ActionIndividual].parameters;
        }
        else {
          return null;
        }
      }
      else {
        //scan parameters to find matching parameter
        castedInd.parameters.foreach(p => {
          if (p.isInstanceOf[ActionIndividual]) {
            newParametersBlock = newParametersBlock ::: generateSingleNodePrms(p.asInstanceOf[ActionIndividual], context);
          }
          else {
            newParametersBlock = newParametersBlock ::: List(p)
          }
        });
      }


      castedInd.parameters = newParametersBlock;

    }
    return null;
  }
}

object TrConstantField extends TrConstantURIMatcher {
  def unapply(uri: URI): Option[TrConstantWrapper] = {
    if (uri.toString.contains(menta.model.Constant.trAddField)) {
      Some(new TrConstantField())
    } else {
      None
    }
  }
}