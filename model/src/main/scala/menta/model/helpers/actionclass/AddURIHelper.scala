package menta.model.helpers.actionclass

import java.net.URI
import menta.model.helpers.common.IndividualMakerFactory
import menta.model.howto._
import menta.model.helpers.literal.StringLiteralHelper

/**
 * @author toscheva
 * Date: 21.11.11
 * Time: 18:36
 */

class AddURIHelper {
  def apply(uri: URI): ActionIndividual = {
    val res: AddClass = new AddClass(null, List[HowTo]())
    res.name = "AddURIHelper"
    var ind=IndividualMakerFactory.instantiate(res)
    ind.parameters=List(new StringLiteral(uri.toString))
    ind
  }

  def storedURI(ind:ActionIndividual):URI={
    //first of all find URI helper
    if(ind.parameters==null) return null;
    ind.parameters.filter(p=>p.isInstanceOf[ActionIndividual]).foreach(b=>
    {
       b match{
         case AddURIChecker(resolved)=>
         {
           var res=StringLiteralHelper.firstOccurence(resolved.parameters)
           if (res==null) return null;
           return new URI(res.valueString);
         }
         case _ =>{

         }
       }
    });
    return null
  }

}

object AddURIChecker {
  def unapply(ind: ActionIndividual): Option[ActionIndividual] = {
    if (ind.actionClass != null &&
      ( (ind.actionClass.uri!=null && ind.actionClass.uri.toString.contains("AddURIHelper"))
        || (ind.actionClass.name!=null && ind.actionClass.name.contains("AddURIHelper") )
        )) {
      Some(ind)
    } else {
      None
    }
  }


}