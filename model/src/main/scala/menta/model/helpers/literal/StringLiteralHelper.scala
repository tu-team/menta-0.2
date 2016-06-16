package menta.model.helpers.literal

import menta.model.howto.{StringLiteral, HowTo}


/**
 * Created by IntelliJ IDEA.
 * User: Alexander
 * Date: 11.12.11
 * Time: 13:19
 * To change this template use File | Settings | File Templates.
 */

object StringLiteralHelper {
   def firstOccurence(prms:List[HowTo]):StringLiteral={
     if (prms==null) return null;
     var res = prms.filter(p=>p.isInstanceOf[StringLiteral]);
     if (res.length<=0) return null;
     return res.head.asInstanceOf[StringLiteral];
   }
}