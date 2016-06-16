package controller

import data.TestDataProvider
import menta.MentaController
import org.slf4j.LoggerFactory
import org.menta.model.howto._interface.{Cardinality, TemplateToAdd, StringLiteral, Terminal}
import menta.knowledgebaseserver.KnowledgeBaseServerImpl
import java.net.URI
import menta.solutiongenerator.SolutionGeneratorImpl
import menta.model.Constant
import menta.model.howto.{Solution, AddClass}
import menta.model.solutiongenerator.solutionchecker.{Rule, RuleChange, RuleChangeSet}
import menta.reasoneradapter.impl.ReasonerAdapterImpl
import org.menta.model.conversation._interface.{ConstantBlock, StartGenerationResponse, URIResponse, HowToParameterType}
import org.junit.{Ignore, Test}

/**
 * @author toschev alex, talanov max
 * Date: 01.08.11
 * Time: 21:52
 */

class ControllerTest {
  val LOG = LoggerFactory.getLogger(this.getClass)
  var generator = new SolutionGeneratorImpl
  var kbServer = new KnowledgeBaseServerImpl

  def controller = new MentaController()

  //@Test
  def saveHowToTest() {

    var urStr = "menta/v0.2#/testHowToTerm"
    var param = new HowToParameterType

    var hwt = new Terminal()
    hwt.setUri(urStr)

    param.setHowTo(hwt)
    var res = controller.addTerminal(param)


    //try to get saved how-to
    var url = new URIResponse
    url.setUri(res.getUri)
    var returned = controller.selectTerminal(url)

    LOG.debug("Save terminal")

    assert(returned.getTerminal.getUri == res.getUri)
  }

  //@Test
  def addHowToTest {
    var urStr = "menta/v0.2#/testHowToTerm"
    var param = new HowToParameterType

    var hwt = new StringLiteral()
    hwt.setUri(urStr)

    param.setHowTo(hwt)
    var res = controller.addTerminal(param)


    //try to get saved how-to
    var url = new URIResponse
    url.setUri(res.getUri)
    var returned = controller.selectTerminal(url)

    LOG.debug("add HowTo")

    assert(returned.getTerminal.getUri == res.getUri)
  }

  //@Test
  def removeHowToTest {
    var urStr = "menta/v0.2#/testHowToTerm"
    var param = new HowToParameterType

    var hwt = new Terminal()
    hwt.setUri(urStr)

    param.setHowTo(hwt)
    var res = controller.addTerminal(param)


    //try to remove saved how-to
    var url = new URIResponse
    url.setUri(res.getUri)
    var removed = controller.removeTerminal(url)

    LOG.debug("remove terminal")

    assert(removed)
  }


  //@Test
  def AddClassSimpleTest {

    var kb = new KnowledgeBaseServerImpl
    var cl = new AddClass()
    var tmpFacade = new AddClass()
    tmpFacade.uri = new URI("menta.org/#AddFacadeClass")
    cl.parameters = List(tmpFacade)
    kb.save(cl)


  }

  def prepareData() {

    kbServer.printKBInfo();
    var checkTo = kbServer.selectAllActionClass()
    //setup db
    checkTo.foreach(c => {
      kbServer.removeObject(c);
    })
    //check if nothing in db
    var res = kbServer.selectAllActionClass()
    assert(res.length <= 0)


  }

  //@Test
  def AddClassTest {


    var urStr = "menta/v0.2#/testHowToTerm"
    var param = new HowToParameterType

    var hwt = new TemplateToAdd()

    var trmHolder = new HowToParameterType

    var trm = new Terminal()
    trmHolder.setHowTo(trm)
    var added = controller.addHowTo(trmHolder)

    //object exist in db
    trm.setUri(added.getUri)

    var content = new Cardinality
    content.setCardinality(1)
    content.setName("AddFacadeClass")
    content.setSet(added.getUri)

    hwt.getParameter.add(content)
    param.setHowTo(hwt)
    var res = controller.addHowTo(param)

    //try to find added
    var found = controller.selectActionClass(res)


    LOG.debug("add HowTo")

    assert(found != null && !found.getActionClass.getParameter.isEmpty)
  }

  //@Test
  def SelectAllActionClasses() {

    var res = controller.selectAllActionClasses(null);
    LOG.debug("Select all action classes")

    assert(res != null)
  }


  //@Test
  def testXsd() {

    // this is complex test
    prepareData();

    TestDataProvider.createAddSchemaDataSetTypesSolution()

    val obj = TestDataProvider.createAcceptanceCriteriaOfThreeKids
    assert(obj != null)
    LOG.debug("Test acceptance criteria {}", obj)

    var rls = new RuleChangeSet(List(new RuleChange(obj)))
    rls.uri = obj.uri

    generator.apply(rls)

  }

  @Test
  def testXSDDemoData() {

    // this is complex test
    //prepareData();

    var res = TestDataProvider.generateUIGeneratedSolution(true);

    var hwt = res(0);
    var sln = new Solution(List(res(1)));



    assert(sln != null)
    LOG.debug("Solution simple" + sln)

    /*
    res = TestDataProvider.generateUIGeneratedSolution(false);

    hwt = res(0);
    sln = new Solution(List(res(1)));

    assert(sln != null)
    LOG.debug("Solution extended" + sln)
    */
  }

  @Test
  def testIdealIndividual() {
    val rqst = new menta.model.solutiongenerator.solutionchecker.Test

    rqst.solution = new Solution(List(TestDataProvider.createAddSchemaDataSetTypesSolution()))
    LOG.debug(" Ideal solution: {}", rqst.solution)

    rqst.rule = new Rule(TestDataProvider.createAcceptanceCriteriaOfThreeKids)
    LOG.debug(" Acceptance criteria rule: {}", rqst.rule)

    rqst.scope = Constant.modelNamespace

    val adapter = new ReasonerAdapterImpl
    val res = adapter(rqst)

    LOG.debug("Generated solution check result {}", res)
  }

  @Ignore
  @Test
  def testDemoSolutionGeneration() {
    // this is complex test
    prepareData();

    var sln = TestDataProvider.generateUIGeneratedSolution(false)


    val obj = TestDataProvider.generateUIGeneratedSolutionAC(false)
    assert(obj != null)
    LOG.debug("Test acceptance criteria {}", obj)

    var rls = new RuleChangeSet(List(new RuleChange(obj)))
    rls.uri = obj.uri

    generator.apply(rls)

  }

  @Test
  def testIdealIndividualDemoData() {

    prepareData();
    val rqst = new menta.model.solutiongenerator.solutionchecker.Test

    rqst.solution = new Solution(List(TestDataProvider.generateUIGeneratedSolution(false)(1)))
    LOG.debug(" Ideal solution: {}", rqst.solution)

    rqst.rule = new Rule(TestDataProvider.generateUIGeneratedSolutionAC(false))
    LOG.debug(" Acceptance criteria rule: {}", rqst.rule)

    rqst.scope = Constant.modelNamespace

    val adapter = new ReasonerAdapterImpl
    val res = adapter(rqst)

    LOG.debug("Generated solution check result {}", res)
  }


  @Test
  def testConstantBlockGeneration() {
    var testXSDSchema = TestDataProvider.createConstantAddXSDField()
    var testFacadeCode = TestDataProvider.createConstantAddFacadeBlock()
    var testDAC = TestDataProvider.createConstantForAddDAC()

    var sqlTable = TestDataProvider.createConstantForAddSQLTable();
    var sqlTableFields = TestDataProvider.createConstantAddSQLField();
    var datasetPLU = TestDataProvider.createConstantAddDataSetElement();

  }

  //@Test
  def testCreateRequest() {
    prepareData()
    TestDataProvider.generateUIGeneratedSolution(false)
    var ac = TestDataProvider.generateUIGeneratedSolutionAC(false);
    var req = new StartGenerationResponse
    req.setAcceptanceCriteriaURI(ac.uri.toString)

    //define constant block
    var facadeBlock = new ConstantBlock
    facadeBlock.setTargetActionClassName("AddFacadeName")
    facadeBlock.setTargetValue("PLUFacade")

    var sqlBlock = new ConstantBlock
    sqlBlock.setTargetActionClassName("AddSQLField")
    var field1Type = new ConstantBlock
    field1Type.setTargetActionClassName("AddSQLFieldType")
    field1Type.setTargetValue("int")

    var field1Name = new ConstantBlock
    field1Name.setTargetActionClassName("AddSQLFieldName")
    field1Name.setTargetValue("ObjectID")

    var field1Holder= new ConstantBlock
    field1Holder.getChildren.add(field1Name)
    field1Holder.getChildren.add(field1Type)

    var field2Holder= new ConstantBlock

    var field2Type = new ConstantBlock
    field2Type.setTargetActionClassName("AddSQLFieldType")
    field2Type.setTargetValue("decimal")

    var field2Name = new ConstantBlock
    field2Name.setTargetActionClassName("AddSQLFieldName")
    field2Name.setTargetValue("Price")

    field2Holder.getChildren.add(field2Name)
    field2Holder.getChildren.add(field2Type)

    sqlBlock.getChildren.add(field1Holder)
    sqlBlock.getChildren.add(field2Holder)

    req.getConstantBlock.add(facadeBlock)
    req.getConstantBlock.add(sqlBlock)


    controller.createRequest(req)
  }

}


