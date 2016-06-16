package menta.model.helpers.constantblock

import java.net.URI
import menta.model.helpers.acceptanceCriteria.individual.AddBlockConstantIndividualHelper
import menta.model.howto._
import menta.model.helpers.actionclass.{AddURIChecker, AddURIHelper}
import menta.model.helpers.literal.StringLiteralHelper

/**
 * Created by IntelliJ IDEA.
 * User: toscheva
 * Date: 10.12.11
 * Time: 13:22
 * To change this template use File | Settings | File Templates.
 */

//first parameter of add field is a literal with action class name
//second paramter is add individual with a individual of constant block
class AddFieldHelper(ind: ActionIndividual) {

  def apply(actionClass: AddClass, targetActionClassURI: URI, blocks: List[AddIndividual]) = {
    var prms=    List[HowTo]((new AddURIHelper).apply(targetActionClassURI));
    prms=prms:::blocks;
    val res = new AddIndividual(actionClass, prms)
    res.name = menta.model.Constant.trAddField
    res.uri = new URI(menta.model.Constant.modelNamespaceString)
    res
  }

  def actionClassURI(): URI = {
    ind.parameters.foreach(b => {
      if (ind.isInstanceOf[ActionIndividual]) {
        b match {
          case AddURIChecker(resolved) => {
              return new URI(StringLiteralHelper.firstOccurence(resolved.parameters).valueString)
          }
          case _ =>
          {
            return null;
          }
        }
      }
    });

    return null
  }

  def constantBlocks(): List[ActionIndividual] = {
    var res = List[ActionIndividual]();
    ind.parameters.foreach(f => {
      f match {
        case AddConstantBlockChecker(checked) => {
          res = res ::: List(f.asInstanceOf[ActionIndividual]);
        }
        case _ => {

        }
      }
    })
    return res
  }


}

object AddFieldChecker {
  def unapply(ind: ActionIndividual): Option[ActionIndividual] = {
    if (ind.actionClass != null && ind.actionClass.uri.toString.contains(menta.model.Constant.trAddField)) {
      Some(ind)
    } else {
      None
    }
  }
}