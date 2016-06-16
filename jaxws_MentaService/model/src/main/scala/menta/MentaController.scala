
package menta

import java.net.URI
import menta.knowledgebaseserver.{KnowledgeBaseServerImpl, KnowledgeBaseServer}
import org.menta.controller.MentaServicePort
import org.menta.model.{KnowledgeClass, KnowledgeIndividual}

import javax.jws.WebMethod;
import javax.xml.ws.Holder
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

import org.menta.model.solutiongenerator.solutionchecker._interface.Rule
import org.menta.model.howto.ActionClass
import org.menta.model.howto._interface.{ExpressionTemplate, Terminal}
import org.menta.model.conversation._interface._

//import org.menta.model.conversation._
//import _interface._

/**
 * @author talanovm
 * Date: 11.01.11
 * Time: 10:33
 * Main workflow class for Menta app
 */

@javax.jws.WebService(serviceName = "MentaService", endpointInterface = 
"org.menta.controller.MentaServicePort ", targetNamespace="http://www.menta.org/controller/", 
portName="MentaServicePort")
class MentaController extends MentaServicePort {

  private var theKBServer: KnowledgeBaseServer = new KnowledgeBaseServerImpl;

  def KBServer = theKBServer


  private def _toURI(uri:org.menta.model.conversation._interface.URIResponse): URI = {
    return URI.create(uri.getUri)
  }

  private def _fromURI(uri: URI): org.menta.model.conversation._interface.URIResponse = {
    val res = new org.menta.model.conversation._interface.URIResponse()
    res.setUri(uri.toString)
    return res
  }

  def revertToPerHistorical(parameter: Holder[org.menta.model.conversation._interface.URIResponse]): Unit = {}

  /*
  * select action class
  * @param parameter URI of action class
   */
  //TODO


  def addCommonSenseRule(parameter: Rule): org.menta.model.conversation._interface.URIResponse = null


  def importApplication(parameter: String): org.menta.model.conversation._interface.URIResponse = null


  def getHistoricalReport(parameter: org.menta.model.conversation._interface.URIResponse): org.menta.model.conversation._interface.URIResponse = null



  /*
  * select knowledge individual from db
  * @param parameter - uri of knowledge ind class
   */
  def selectKnowledgeIndividual(parameter: org.menta.model.conversation._interface.URIResponse): KnowledgeIndividual = {
    return theKBServer.selectIndividual(_toURI(parameter))
  }


   /*
  * select knowledge class from db
  * @param parameter - uri of knowledge class
   */
  def selectKnowledgeClass(parameter: org.menta.model.conversation._interface.URIResponse): KnowledgeClass = {
    return ( theKBServer.selectClass(_toURI(parameter)))
  }

  /*
  * select terminal from db
  * @param parameter - uri of terminal
   */
  def selectTerminal(parameter: org.menta.model.conversation._interface.URIResponse): org.menta.model.conversation._interface.SelectTerminal = {
    var res= new org.menta.model.conversation._interface.SelectTerminal

    def setTerm(param:org.menta.model.conversation._interface.URIResponse):org.menta.model.howto._interface.Terminal=
    {
      val origin=theKBServer.selectTerminal(_toURI(param))
      val target = new org.menta.model.howto._interface.Terminal
      target.setUri(origin.getUri)
      target.setValue(origin.getValue)
      return target
    }

    res.setTerminal(setTerm(parameter))

    return res
  }

  /*
  * select action class from db
  * @param parameter - uri of Action class
   */
  //TODO it seems to be action class doesn't exist
  // please use ExpressionTemplate instead.
 // def selectActionClass(parameter: org.menta.model.conversation._interface.URIResponse): SelectActionClass = {
 //   return null;
    /*val res = new SelectActionClass
    val orig=theKBServer.selectActionClass(_toURI(parameter))

    val trgt=new  ExpressionTemplate
    trgt.setUri(orig.getUri)
    trgt.setSuperClass(orig.getSuperClass.getUri)
    res.setActionClass(trgt)

    return res
      */
  //}


   /*
  * add how-to set to database
  * @param parameter - target how-to  list
   */
  //NOTE: i have changed the type of parameter, please review
  def addHowTos(parameter: org.menta.model.conversation._interface.SetHowToParameterType): org.menta.model.conversation._interface.SetURIResponse = {
    var res = new org.menta.model.conversation._interface.SetURIResponse()

    parameter.getHowToParameter.toArray().foreach(u => {
      var url= addHowTo(u.asInstanceOf[org.menta.model.conversation._interface.HowToParameterType])
      var converted=new org.menta.model.conversation._interface.URIResponse()
      converted.setUri(url.getUri)
      res.getURIResponse.add(converted)
    })
    return res
  }

  def getSolutionReport(parameter: org.menta.model.conversation._interface.URIResponse):org.menta.model.conversation._interface.URIResponse = null


  /*
  * add how-to to database
  * @param parameter - target how-to
   */
  @WebMethod
  def addHowTo(parameter:org.menta.model.conversation._interface.HowToParameterType): org.menta.model.conversation._interface.URIResponse = {
    return _fromURI(theKBServer.save(parameter.getHowTo))
  }


  def getEvolutionReport(parameter:org.menta.model.conversation._interface.URIResponse): org.menta.model.conversation._interface.URIResponse = null

  def selectActionIndividual(parameter: URIResponse): SelectActionIndividual = null

  def addAssociation(parameter: AddAssociation): URIResponse = null

  def addCommonSenseRules(parameter: SetRule): SetURIResponse = null

  def partialConfirmSolution(parameter: PartialConfirmSolutionResponse): URIResponse = null

  def selectActionClass(parameter: URIResponse): SelectActionClass = null

  def confirmSolution(parameter: ConfirmSolutionResponse): ReportResponse = null

  def createRequest(parameter: StartGenerationResponse): URIResponse = null

  def updateRequirements(parameter: UpdateRequirementsResponse): URIResponse = null

  def addClassifier(parameter: AddClassifier): URIResponse = null

  def addHowToChangeSet(parameter: UpdateHowToResponse): URIResponse = null
}