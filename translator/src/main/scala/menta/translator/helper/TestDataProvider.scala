package menta.translator.helper

import menta.model.howto.helper.AddParameterHelper
import org.slf4j.{Logger, LoggerFactory}
import java.net.URI
import menta.model.howto.{AddIndividual, ActionIndividual, AddClass, HowTo}



/**
 * @author toschev alexander, talanov max
 * Date: 30.10.11
 * Time: 11:34
 */

/*
  provide data for solution generator
 */
object TestDataProvider {
 // val controller = new MentaController
       private var theParameterHelper = new AddParameterHelper();
   def generateAddClass(nm: String, prms: Map[String, HowTo]): AddClass = {

    //check if class already exist in database and load it
    //var res = kbServer.selectActionClass(nm);
    //if (res != null) return res.asInstanceOf[AddClass];
    val addClass = new AddClass
    addClass.name = nm
    addClass.isRoot = true

    if (prms != null) {
      addClass.parameters = prms.map(b => theParameterHelper(b._1, b._2, 1)).toList
    }
    addClass.uri = new URI(menta.model.Constant.modelNamespaceString + nm)


    //kbServer.save(addClass)
    addClass
  }

  def generateAddIndividual(actionClass: AddClass, prms: List[HowTo]): ActionIndividual = {
    var res = new AddIndividual
    res.actionClass=actionClass;
    if (prms != null) {
      res.parameters = prms;
    }
    return res;
  }

   def generateAddIndividualList(in: List[AddClass]): List[ActionIndividual] = {
    return in.map(b => generateAddIndividual(b, null))
  }
  def generateSimplestSolution(): List[HowTo] = {
    val facadeName = generateAddClass("AddFacadeName", null);
    val facadeNameInd = generateAddIndividual(facadeName, null);
    List[HowTo](facadeNameInd)
  }

}