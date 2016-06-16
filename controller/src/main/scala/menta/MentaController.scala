package menta



import communicator.responseanalyser.{ResponseAnalyserImpl, ResponseAnalyser}
import java.net.URI
import knowledgebaseserver.{KnowledgeBaseServerImpl, KnowledgeBaseServer}
import learner.{Learner, LearnerImpl}
import model.container.{KnowledgePatch, KnowledgeNumber, KnowledgeString}
import model.helpers.constantblock.AddFieldHelper
import model.helpers.individual.AddConstantIndividualHelper
import model.howto._
import model.howto.classifier.Classifier
import model.howto.helper.AddParameterHelper
import model.solutiongenerator.solutionchecker.{AcceptanceCriteria, RuleChange, RuleChangeSet}
import org.menta.controller.MentaServicePort
import javax.xml.ws.Holder

import org.menta.model.solutiongenerator.solutionchecker._interface.Rule
import org.menta.model.{ObjectFactory, KnowledgeClass, KnowledgeIndividual}
import collection.JavaConversions._
import collection.mutable.Buffer
import collection.immutable.HashSet
import javax.jws.WebMethod
import org.menta.model.howto._interface.{TemplateToRemove, Cardinality, TemplateToAdd}
import org.slf4j.LoggerFactory
import reasoneradapter.translator.ProbabilisticalLogicOperators.helper.{AddBlockIndividualHelper, AddConstantBlockIndividualWrapper}
import solutiongenerator.SolutionGeneratorImpl
import org.menta.model.conversation._interface._
import translator.{TranslatorImpl, TranslationStrategy}
import util.{TestDataStrategyProvider, TestDataProviderGlobal}

/**
 * @author talanovm
 * Date: 11.01.11
 * Time: 10:33
 * Main workflow class for Menta app
 */

@javax.jws.WebService(serviceName = "MentaService", endpointInterface =
  "org.menta.controller.MentaServicePort", targetNamespace = "http://www.menta.org/controller/",
  portName = "tns:MentaServicePort")
class MentaController extends MentaServicePort {
  private var theResponseAnalyser: ResponseAnalyser = new ResponseAnalyserImpl

  private var theKBServer: KnowledgeBaseServer = new KnowledgeBaseServerImpl;
  private val oF: ObjectFactory = new ObjectFactory

  private var theLeaner: Learner = new LearnerImpl

  private var theGenerator = new SolutionGeneratorImpl

  val log = LoggerFactory.getLogger(this.getClass)

  // private val rb:RequestBuilderImpl= new RequestBuilderImpl;

  private var theParameterHelper = new AddParameterHelper();

  def KBServer = theKBServer


  private def _toURI(uri: org.menta.model.conversation._interface.URIResponse): URI = {
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
    theKBServer.selectIndividual(_toURI(parameter))
    //TODO set parameters
    oF.createKnowledgeIndividual()
  }


  /*
 * select knowledge class from db
 * @param parameter - uri of knowledge class
  */
  def selectKnowledgeClass(parameter: org.menta.model.conversation._interface.URIResponse): KnowledgeClass = {
    theKBServer.selectClass(_toURI(parameter))
    //TODO set parameters
    oF.createKnowledgeClass()
  }

  /*
  * select terminal from db
  * @param parameter - uri of terminal
   */
  @WebMethod
  def selectTerminal(parameter: org.menta.model.conversation._interface.URIResponse): org.menta.model.conversation._interface.SelectTerminal = {
    val res = new org.menta.model.conversation._interface.SelectTerminal
    def setTerm(param: org.menta.model.conversation._interface.URIResponse): org.menta.model.howto._interface.Terminal = {
      val origin: menta.model.howto.Terminal = theKBServer.selectTerminal(_toURI(param))
      val target = new org.menta.model.howto._interface.Terminal
      target.setUri(origin.uri.toString)
      target.setValue(origin.value)
      target
    }
    res.setTerminal(setTerm(parameter))
    res
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
      var url = addHowTo(u.asInstanceOf[org.menta.model.conversation._interface.HowToParameterType])
      var converted = new org.menta.model.conversation._interface.URIResponse()
      converted.setUri(url.getUri)
      res.getURIResponse.add(converted)
    })
    res
  }


  /*
 * add how-to to database
 * @param parameter - target how-to
  */
  @WebMethod
  def addHowTo(parameter: org.menta.model.conversation._interface.HowToParameterType): org.menta.model.conversation._interface.URIResponse = {

    val objectToSave = parameter.getHowTo
    if (objectToSave.isInstanceOf[org.menta.model.howto._interface.Terminal]) {
      addTerminal(parameter)
    } else
    if (objectToSave.isInstanceOf[org.menta.model.howto._interface.TemplateToAdd]) {
      addTemplateToAdd(parameter)
    } else {
      throw new Exception("Unknown object class " + objectToSave.toString)
    }
    //return _fromURI(theKBServer.save(parameter.getHowTo))
  }

  /**
   * Supplemental method to copy objects of interface model to application model
   * @param obj the destination object menta.model.howto.HowTo
   * @param src source object to copy
   */
  private def _addHowTo(obj: menta.model.howto.HowTo, src: org.menta.model.howto._interface.HowTo): org.menta.model.conversation._interface.URIResponse = {
    obj.name = src.getName
    //TODO enable translation of classifiers
    val classifiers: Buffer[Classifier] = for (val classifierUri: String <- src.getClassifier) yield theKBServer.selectClassifier(new URI(classifierUri))
    obj.classifiers = HashSet[Classifier](classifiers: _*)
    _fromURI(theKBServer.save(obj))
  }


  def addTerminal(parameter: org.menta.model.conversation._interface.HowToParameterType): org.menta.model.conversation._interface.URIResponse = {
    var terminal = new menta.model.howto.Terminal
    terminal.name = parameter.getHowTo.getName
    terminal.uri = null //new URI(parameter.getHowTo.getUri)
    _addHowTo(terminal, parameter.getHowTo)
  }

  def addTemplateToAdd(parameter: org.menta.model.conversation._interface.HowToParameterType): org.menta.model.conversation._interface.URIResponse = {
    var casted = parameter.getHowTo.asInstanceOf[TemplateToAdd]
    var tmpl = new menta.model.howto.AddClass()

    tmpl.name = parameter.getHowTo.getName
    tmpl.uri = null //new URI(parameter.getHowTo.getUri)
    //(name: String, _type: HowTo, cardinality: Int)

    tmpl.parameters = casted.getParameter.map(c => theParameterHelper.apply(c.getName, new menta.model.howto.Terminal(null, new URI(c.getSet)), c.getCardinality)).toList
    //if (tmpl.parameters.length>0) tmpl.parameters.head.uri=null
    _addHowTo(tmpl, parameter.getHowTo)

  }

  def removeTerminal(parameter: org.menta.model.conversation._interface.URIResponse): Boolean = {
    //var toRemove = controller.selectTerminal(parameter).getTerminal
    //theKBServer.
    true;
  }

  def add() = null

  def getEvolutionReport(parameter: org.menta.model.conversation._interface.URIResponse): org.menta.model.conversation._interface.URIResponse = null

  def selectActionIndividual(parameter: URIResponse): SelectActionIndividual = null

  def addAssociation(parameter: AddAssociation): URIResponse = null

  def addCommonSenseRules(parameter: SetRule): SetURIResponse = null

  def partialConfirmSolution(parameter: PartialConfirmSolutionResponse): URIResponse = null

  def selectActionClass(parameter: URIResponse): SelectActionClass = {
    val res = new SelectActionClass
    def setAC(param: org.menta.model.conversation._interface.URIResponse): org.menta.model.howto._interface.TemplateToAdd = {
      val origin: menta.model.howto.ActionClass = theKBServer.selectActionClass(_toURI(param))
      val target = new TemplateToAdd
      target.setUri(origin.uri.toString)
      //todo process with parameters
      //extract holder of parameters
      if (origin.parameters != null) {
        var pairs = origin.parameters.filter(p => p.name == "parameter")
        pairs.foreach(p => {
          var casted = p.asInstanceOf[AddClass]
          var itm = new Cardinality
          //extract name
          itm.setName(casted.parameters.filter(n => n.name == "AddNameHelper").head.asInstanceOf[AddClass].parameters.head.asInstanceOf[StringLiteral].value.asInstanceOf[KnowledgeString].contents)


          //cardinality
          itm.setCardinality(scala.math.round(casted.parameters.filter(n => n.name == "AddCardinalityHelper").head.asInstanceOf[AddClass].parameters.head.asInstanceOf[NumberLiteral].value.asInstanceOf[KnowledgeNumber].contents))

          //references
          itm.setSet(casted.parameters.filter(n => n.name == "AddTypeHelper").head.asInstanceOf[AddClass].parameters.head.uri.toString)
          target.getParameter.add(itm)

        })
      }

      target
    }
    res.setActionClass(setAC(parameter))
    res
  }

  def selectAllActionClass() = {

  }

  def confirmSolution(parameter: ConfirmSolutionResponse): ReportResponse = {

    var res = new ReportResponse();
    var cnv = parameter.getConversationURI();
    var conv = theKBServer.selectConversation(new URI(cnv));

    //update confirmation status
    conv.conversationActs(0).confirmationStatus = true;
    theKBServer.save(conv.conversationActs(0));
    res
  }

  def convertConstantBlock(cnstBlocks: List[ConstantBlock]): ActionIndividual = {
    var res = List[ActionIndividual]();

    var addConstantIndividualClass = new AddClass();
    addConstantIndividualClass.name = menta.model.Constant.nameConstant;
    addConstantIndividualClass.uri = new URI(menta.model.Constant.modelNamespaceString + menta.model.Constant.nameConstant);
    addConstantIndividualClass = theKBServer.selectOrCreateActionClass(addConstantIndividualClass).asInstanceOf[AddClass];

    var addFieldClass = new AddClass();
    addFieldClass.name = menta.model.Constant.trAddField;
    addFieldClass.uri = new URI(menta.model.Constant.modelNamespaceString + menta.model.Constant.trAddField);
    addFieldClass = theKBServer.selectOrCreateActionClass(addFieldClass).asInstanceOf[AddClass];


    cnstBlocks.foreach(b => {
      if (b.getChildren.length > 0) {
        var blocks: List[AddIndividual] = List[AddIndividual]();
        //suppose this is AddField
        //first levele of children is block accumulated
        b.getChildren.toList.foreach(block => {

          //create parent block
          var blockParameters = List[ActionIndividual]();
          //block contain a list of simple add name fields
          block.getChildren.toList.foreach(singleField => {
            var extractActionsClass = theKBServer.selectActionClass(singleField.getTargetActionClassName());
            blockParameters = blockParameters ::: List((new AddConstantIndividualHelper).apply(singleField.getTargetValue, addConstantIndividualClass, extractActionsClass.uri))
          });

          //create blocks individual
          var blockInd = (new AddConstantBlockIndividualWrapper(blockParameters, null)).addIndividual();
          blocks = blocks ::: List(blockInd);

        });
        var targetActionClass = theKBServer.selectActionClass(b.getTargetActionClassName);
        //create addfield
        var addField = (new AddFieldHelper(null)).apply(addFieldClass.asInstanceOf[AddClass], targetActionClass.uri, blocks);
        res = res ::: List(addField);


      }
      else {
        //just simple constant
        var targetActionClass = theKBServer.selectActionClass(b.getTargetActionClassName);
        res = res ::: List((new AddConstantIndividualHelper).apply(b.getTargetValue, addConstantIndividualClass, targetActionClass.uri));
      }
    });

    return (new AddBlockIndividualHelper).apply(res, null)

  }

  def createRequest(parameter: StartGenerationResponse): URIResponse = {
    //extract Acceptance Criteria URI
    var acURI = parameter.getAcceptanceCriteriaURI();



    //load acceptance criteria
    var ac = theKBServer.selectAcceptanceCriteria(new URI(acURI));

    log.info("Request recieved " + ac.ruleName)

   /* var addBlock = new AddClass()
    addBlock.name = "AddBlock"
    addBlock.uri = new URI(menta.model.Constant.modelNamespaceString + addBlock.name);

    addBlock = theKBServer.selectOrCreateActionClass(addBlock).asInstanceOf[AddClass];

    var addField = new AddClass()
    addField.name = menta.translator.constant.Constant.constantAddField
    addField.uri = new URI(menta.model.Constant.modelNamespaceString + addBlock.name);

    addField = theKBServer.selectOrCreateActionClass(addBlock).asInstanceOf[AddClass];

    var addBlockClassHolder = new AddClass()
    addBlockClassHolder.name = "AddBlockClassHolder"
    addBlockClassHolder.uri = new URI(menta.model.Constant.modelNamespaceString + addBlockClassHolder.name);

    addBlockClassHolder = theKBServer.selectOrCreateActionClass(addBlockClassHolder).asInstanceOf[AddClass];


    var addBlockTerm = new AddClass()
    addBlockTerm.name = "AddBlockTerminal"
    addBlockTerm.uri = new URI(menta.model.Constant.modelNamespaceString + addBlockTerm.name);

    addBlockTerm = theKBServer.selectOrCreateActionClass(addBlockTerm).asInstanceOf[AddClass];


    //create constant block
    var constantBlock = List[ActionIndividual]();
    parameter.getConstantBlock.foreach(b => {
      def processSingleBlock(inp: ConstantBlock): AddIndividual = {
        //load target action class
        var targetActionClass: ActionClass = null;
        if (inp.getTargetActionClassName != null) {
          targetActionClass = theKBServer.selectActionClass(inp.getTargetActionClassName)
        }
        if (inp.getChildren.length <= 0) {
          //suppose this is terminal
          return (new AddConstantIndividualHelper()).apply(inp.getTargetValue, addBlockTerm, targetActionClass.uri)

        }
        //TODO (new AddConstantIndividualHelper()).apply(addBlock, addField, new URI(menta.model.Constant.modelNamespaceString + name))
        var aggregated = List[ActionIndividual](); //(new AddNameConstantIndividualHelper()).apply(addBlockClassHolder, if (targetActionClass != null) targetActionClass.uri

        //non-terminal, create block
        inp.getChildren.foreach(u => {
          aggregated = aggregated ::: List(processSingleBlock(u));

        });
        return (new AddConstantBlockIndividualWrapper(aggregated, null)).addIndividual()
      };
      constantBlock = constantBlock ::: List(processSingleBlock(b));

    }) */

    var constantBlockHolder = convertConstantBlock(parameter.getConstantBlock.toList)

    constantBlockHolder.uri = theKBServer.save(constantBlockHolder);

    var conversation = theResponseAnalyser.createRequest(new URI(acURI), constantBlockHolder.uri, parameter.getAuthorLogin)
    log.info("Request started, trying to find existing solution")
    //check learner
    var solution = theLeaner.detectAnalogy(new URI(acURI))

    if (solution == null) {
      //start generation
      var req = new RuleChangeSet(List(new RuleChange(ac.howTo)))
      req.converstionActURI = conversation.conversationActs(0).uri;
      log.info("Generation started")
      theGenerator.apply(req);

    } else {
      log.info("Existing solution found")
      //save
      conversation.conversationActs(0).solutionURI = solution.uri;
    }



    log.info("Conversation" + conversation.toString)

    val res = new URIResponse();
    res.setUri(conversation.uri.toString);

    return res;
  }


  def updateRequirements(parameter: UpdateRequirementsResponse): URIResponse = null

  def addClassifier(parameter: AddClassifier): URIResponse = null

  //def addHowToChangeSet(parameter: UpdateHowToResponse): URIResponse = null

  /*
  @param parameter - provide nothing
   */
  def selectAllActionClasses(parameter: URIResponse): KnowledgeClassSet = {
    val res = new KnowledgeClassSet;
    //load and return all action classes
    val actionCl = KBServer.selectAllActionClass();
    actionCl.foreach(c => {
      //only proper objects
      if (c.uri != null) {
        if (c.isInstanceOf[AddClass]) {

          var obj = new TemplateToAdd
          obj.setUri(c.uri.toString);
          res.getActionClass.add(obj);
        }
        else if (c.isInstanceOf[RemoveClass]) {
          var obj = new TemplateToRemove
          obj.setUri(c.uri.toString);
          res.getActionClass.add(obj);
        }
      }
    })
    res
  }

  def loadTranslationStrategy(strategyURI: URI): TranslationStrategy = {
    //TODO: Load from database
    return TestDataStrategyProvider.getGlobalStrategy();
  }

  def serverMaintenance(parameter: String): String = {
    def LoadGenerationRequestHowTo() = {
      TestDataProviderGlobal.createAddSchemaDataSetTypesSolution()
    }
    def LoadAllAcceptanceCriteria() = {
      //three kids ac
      val obj = TestDataProviderGlobal.createAcceptanceCriteriaOfThreeKids
      var ac = obj;
      //save ac
      var acHolder = new AcceptanceCriteria(
      )
      acHolder.howTo = ac;
      acHolder.ruleName = "FacadeGenerationAC"
      acHolder.uri = theKBServer.save(acHolder);

      //second ac load
      var ac2 = TestDataProviderGlobal.generateUIGeneratedSolutionAC(false);
      var acHolder2 = new AcceptanceCriteria(
      )
      acHolder2.howTo = ac2;
      acHolder2.ruleName = "EmployeeFacadeAC"
      acHolder2.uri = theKBServer.save(acHolder2);
    }
    def LoadFacadeHowTo() = {

      var sln =TestDataProviderGlobal.generateUIGeneratedSolution(false)(1);
      var sol = new Solution()
      sol.howTos = List(sln);

      var acHolder = theKBServer.selectAcceptanceCriteriaByName("EmployeeFacadeAC")

      sol.uri = theKBServer.save(sol)

      theLeaner.train(acHolder.uri, sol);
    }
    def cleanUp() = {
      theKBServer.cleanUPEverything();
    }

    def checkAllActionClasses(): String = {
      var acs = theKBServer.selectAllActionClass();
      var splited = "";
      acs.foreach(b => {
        splited = splited + b.uri + " ;";
      });
      return splited;
    }

    //try {
    if (parameter == "LoadGenerationRequestHowTo") {

      LoadGenerationRequestHowTo();
      return "How-to loaded..."
    }
    else if (parameter == "SelectAllActionClasses") {

      return checkAllActionClasses();
    }
    else if (parameter == "LoadAllAcceptanceCriteria") {


      LoadAllAcceptanceCriteria();
      return "Append: EmployeeFacadeAC; FacadeGenerationAC\n"
    }
    else if (parameter == "LoadFacadeHowTo") {

      LoadFacadeHowTo();
      return "Howto added\n"
    }
    else if (parameter == "CleanUp") {

      cleanUp();
      return "clean up done"
    }
    else if (parameter == "LoadAllData") {
      //cleanUp();
      LoadGenerationRequestHowTo();
      LoadAllAcceptanceCriteria();
      LoadFacadeHowTo();
      return "Data loaded"
    }
    else if (parameter == "GetServerInfo") {
      //cleanUp();
      return "DB location: " + theKBServer.getKBInfo();
    }


    else if (parameter == "GetAvailibleActions") {
      return "LoadGenerationRequestHowTo\nLoadAllAcceptanceCriteria\nLoadFacadeHowTo\nCleanUp\nLoadAllData"
    }

    return "null";
    //}
    /*catch {
      case e: Exception => {
        log.error(e.getMessage)
        return "Error: " + e.getMessage;
      }

    } */

  }

  def addHowToChangeSet(parameter: org.menta.model.conversation._interface.UpdateHowToResponse): URIResponse = null

  def getSolutionReport(parameter: URIResponse): SolutionReport = {

    //load conversation
    var conv = theKBServer.selectConversation(new URI(parameter.getUri));

    //load solution
    var solURI = conv.solution();

    var cnfStatus = conv.confirmationStatusAny();

    var sol: Solution = null;
    if (solURI != null) sol = theKBServer.selectSolution(solURI);

    //format solution report
    var res = new SolutionReport()
    if (sol != null) {

      if (cnfStatus) {
        res.setRequestCompleted(true);
        //learn
        theLeaner.train(conv.acceptanceCriteria(), sol)
        //check if translation already performed
        if (conv.translation() == null) {
          val t: TranslatorImpl = new TranslatorImpl()
          val blockURI = conv.getConstantBlockURI();
          val block = theKBServer.selectActionInstance(blockURI).asInstanceOf[AddIndividual]
          //TODO: check asInstanceOf[AddIndividual
          val strategy = loadTranslationStrategy(new URI(menta.model.Constant.modelNamespaceString))

          //modify solution (it in block)
          var blockSol= sol.howTos;
          var toClone=  blockSol.head.asInstanceOf[AddIndividual].parameters.head.asInstanceOf[AddIndividual];
          var newSol = toClone.clone().asInstanceOf[HowTo];

          var trsol = new Solution(List(newSol));

          val patch: KnowledgePatch = t.createPatch(trsol, strategy, block)
          //fill files
          patch.filePatchMap.foreach(u => {
            var file = new GeneratedFile();
            file.setFileName(u._1.valueString);
            file.setFileContent(u._2.valueString);
            res.getFiles.add(file);
          });
        }

      }
      res.setSolution(sol.toString())


    }
    return res;
  }

  def selectAllAcceptanceCriteria(parameter: URIResponse): AcceptanceCriteriaSet = {
    var lst = theKBServer.selectAllAcceptanceCriteria()

    if (lst == null || lst.length <= 0) return null;
    var res = new AcceptanceCriteriaSet

    lst.foreach(f => {
      var cnv = new org.menta.model.conversation._interface.AcceptanceCriteria();
      cnv.setURI(f.uri.toString);
      cnv.setName(f.ruleName);
      cnv.setRaw(f.toString());
      res.getAcceptanceCriteries.add(cnv);

    });
    res
  }
}