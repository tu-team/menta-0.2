package menta.model.helpers.common

import menta.model.howto._

/**
 * Created by IntelliJ IDEA.
 * User: toscheva
 * Date: 10.12.11
 * Time: 14:05
 * To change this template use File | Settings | File Templates.
 */

/**
 * create individuals using specified action class
 */
object IndividualMakerFactory {


     def instantiate(cls:ActionClass):ActionIndividual=
     {
       var res:ActionIndividual=null;
       if (cls.isInstanceOf[AddClass])
       {
         res = new AddIndividual
       }
       else
       {
         res = new RemoveIndividual
       }
       res.actionClass=cls;
       return res;
     }
}