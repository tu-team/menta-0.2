package controller


import data.TestDataProvider
import menta.MentaController
import org.slf4j.LoggerFactory
import menta.knowledgebaseserver.KnowledgeBaseServerImpl
import menta.solutiongenerator.SolutionGeneratorImpl
import menta.model.howto.Solution
import menta.learner.LearnerImpl
import menta.model.solutiongenerator.solutionchecker.AcceptanceCriteria
import org.junit.{Ignore, Test}
import menta.reasoneradapter.impl.ReasonerAdapterImpl
import org.menta.model.conversation._interface.{ConfirmSolutionResponse, ConstantBlock, StartGenerationResponse}

/**
 * @author toschev alex
 * Date: 06.12.11
 * Time: 18:45
 */
@Test
class WorkflowTest {
  val LOG = LoggerFactory.getLogger(this.getClass)
  var generator = new SolutionGeneratorImpl
  var kbServer = new KnowledgeBaseServerImpl

  var theLearner = new LearnerImpl

  def controller = new MentaController()

  @Test
  def testOK() {

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

  @Ignore
  @Test
  def testRequest() {

    prepareData();
    val sln = TestDataProvider.generateUIGeneratedSolution(false)(1);
    val sol = new Solution()
    sol.howTos = List(sln);

    val ac = TestDataProvider.generateUIGeneratedSolutionAC(false);
    //save ac
    val acHolder = new AcceptanceCriteria()

    acHolder.howTo = ac;

    sol.uri = kbServer.save(sol)
    acHolder.uri = kbServer.save(acHolder);

    theLearner.train(acHolder.uri, sol);

    //save solution
    var req = new StartGenerationResponse
    req.setAcceptanceCriteriaURI(acHolder.uri.toString)
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

    var field1Holder = new ConstantBlock
    field1Holder.getChildren.add(field1Name)
    field1Holder.getChildren.add(field1Type)

    var field2Holder = new ConstantBlock

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

    //req.getConstantBlock.add(facadeBlock)
    //req.getConstantBlock.add(sqlBlock)

    var block = TestDataProvider.crateConstantBlockForWholeSolutionClnt();
    block.foreach(b=>
    {
      req.getConstantBlock.add(b);
    })

    var uid = controller.createRequest(req);

    //checl status
    var exit = false;
    while (!exit) {
      var rpt = controller.getSolutionReport(uid)
      LOG.debug(rpt.getSolution)
      if (rpt.getSolution!=null)
      {
        var resp= new ConfirmSolutionResponse();
        resp.setConversationURI(uid.getUri);
        controller.confirmSolution(resp);
      }
      if (rpt.getFiles.size()>0)
      {
        exit=true;
      }
    }

    LOG.debug("Done");
  }

  @Ignore
  @Test
  def testRequestWithGeneration() {

    prepareData();
    TestDataProvider.createAddSchemaDataSetTypesSolution()

    val obj = TestDataProvider.createAcceptanceCriteriaOfThreeKids


    assert(obj != null)
    LOG.debug("Test acceptance criteria {}", obj)

    var ac = obj;
    //save ac
    var acHolder = new AcceptanceCriteria()

    acHolder.howTo = ac;

    acHolder.uri = kbServer.save(acHolder);

    //theLearner.train(acHolder.uri, sol);

    //save solution
    var req = new StartGenerationResponse
    req.setAcceptanceCriteriaURI(acHolder.uri.toString)
    //var block = new ConstantBlock;
    //block.getChildren.add(TestDataProvider.createConstantBlockForEmpDataSetClient())
    req.getConstantBlock.add(TestDataProvider.createConstantBlockForEmpDataSetClient());

    var uid = controller.createRequest(req);

    //checl status
    var exit = false;
    while (!exit) {
      var rpt = controller.getSolutionReport(uid)
      LOG.debug(rpt.getSolution)
      Thread.sleep(20 * 1000)
      if (rpt.getSolution!=null)
      {
        var resp= new ConfirmSolutionResponse();
        resp.setConversationURI(uid.getUri);
        controller.confirmSolution(resp);
      }
      if (rpt.getFiles.size()>0)
      {
        exit=true;
      }
   }

    LOG.debug("Done");

  }

  @Test
  def testAcLoad = {
    controller.serverMaintenance("CleanUp");
    controller.serverMaintenance("LoadAllAcceptanceCriteria")

    var res = controller.selectAllAcceptanceCriteria(null);
  }


}

