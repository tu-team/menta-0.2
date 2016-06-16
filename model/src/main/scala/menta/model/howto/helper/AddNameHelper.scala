package menta.model.howto.helper

import java.net.URI
import menta.model.howto.{ActionIndividual, StringLiteral, HowTo, AddClass}
import menta.model.helpers.common.IndividualMakerFactory

/**
 * @author talanov max
 * Date: 23.09.11
 * Time: 12:10
 */

class AddNameHelper {
  def apply(name: String): ActionIndividual = {
    val ac: AddClass = new AddClass(null, List[HowTo]())
    ac.name = menta.model.Constant.addName
    ac.uri = new URI(menta.model.Constant.modelNamespaceString + ac.name)

    val ind=IndividualMakerFactory.instantiate(ac)
    ind.parameters=List(new StringLiteral(name))


    ind
  }

  def extractValue(ind:ActionIndividual):ActionIndividual={
     //scan fo suitable parameters and return
     if (ind.parameters==null) return null;
     ind.parameters.foreach(p=>
     {
        p match {
          case AddNameChecker(resolved)=>{
             return resolved;
          }
          case _ =>{

          }
        }
     });
     return null;
  }
}
object AddNameChecker{
   def unapply(ind: ActionIndividual): Option[ActionIndividual] = {
    if (ind.actionClass != null &&
      ( (ind.actionClass.uri!=null && ind.actionClass.uri.toString.contains(menta.model.Constant.addName))
        || (ind.actionClass.name!=null && ind.actionClass.name.contains(menta.model.Constant.addName) )
        )) {
      Some(ind)
    } else {
      None
    }
  }
}

