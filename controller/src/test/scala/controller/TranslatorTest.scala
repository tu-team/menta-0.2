package controller

import _root_.data.TestDataProvider
import org.slf4j.LoggerFactory
import org.menta.model.conversation._interface.ConstantBlock
import menta.MentaController
import menta.translator.wrappers.TrConstantField
import java.net.URI
import org.junit.{Ignore, Test}
import menta.model.howto._
import menta.util.{TestDataStrategyProvider, TestDataProviderGlobal}
import menta.translator.TranslatorImpl
import menta.model.container.KnowledgePatch

/**
 * @author toscheva
 * Date: 10.12.11
 * Time: 15:08
 * To change this template use File | Settings | File Templates.
 */

@Test
class TranslatorTest {
   val LOG = LoggerFactory.getLogger(this.getClass)
  val controller = new MentaController;

  @Test
  def testOK()
     {

     }

  @Test
  def testEmpGenAC(){
      val strategy = TestDataStrategyProvider.createStrategyForWholeSolution()

    // TestDataProvider.crateConstantBlock()

    // LOG.debug(" Constant block: {}", cb)

     val sol = new Solution(List(TestDataProvider.createAddSchemaDataSetTypesSolution().asInstanceOf[AddIndividual].parameters.head));
    LOG.debug(" Solution : {}", sol)

    val cb = TestDataProvider.createConstantBlockForEmpDataSet()

    val t: TranslatorImpl = new TranslatorImpl()

    //val context: Context = t.transferConstants(cb)
    //assert(context.scope(Constant.translatorNamespace) != null)

    val res: KnowledgePatch = t.createPatch(sol, strategy, cb)
    assert(res != null)
    LOG.info(" Knowledge Patch: {}", res)
  }

  @Test
  def testTransactSQL() {
    val strategy = TestDataStrategyProvider.createTransactSQLStrategy()

    val sol = new Solution(TestDataProvider.generateTSQLSolution())
    LOG.debug(" Solution : {}", sol)

    val cb = TestDataProvider.crateConstantBlock()

    val t: TranslatorImpl = new TranslatorImpl()

    val res: KnowledgePatch = t.createPatch(sol, strategy, cb)
    assert(res != null)
    LOG.info(" Knowledge Patch: {}", res)
  }

  @Test
  def testLargeSolution()
  {
       //clean up
    controller.serverMaintenance("CleanUp");

    //generate simple solution
    var solution = TestDataProvider.generateUIGeneratedSolution(false);

    //start with XSD
    var xsds=solution.head.asInstanceOf[ActionIndividual];

    //XSD strategy
    var strategy= TestDataStrategyProvider.createStrategyForWholeSolution();

    //test Context
    //var ctx= new Context()
    ///ctx.addEntryInvariant(new URI("$TrVariable"),new URI("$CurrentNode"),List(xsds));
    // LOG.info(ctx.toString)
    val cb = TestDataProvider.crateConstantBlockForWholeSolution();

    val t: TranslatorImpl = new TranslatorImpl()

    val res: KnowledgePatch = t.createPatch(new Solution(List(xsds)), strategy, cb)
    LOG.info("Translation: "+res.toString())
  }

 // @Ignore
  @Test
  def testTrAddField()
  {

    var controller = new MentaController;
    //load solution
    var sln = TestDataProvider.generateUIGeneratedSolution(false)(0);

    //generate test constant block and place to context
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

    var block = List(facadeBlock,sqlBlock);

    //generate
    var converted =controller.convertConstantBlock(block);

    //gerate context
    var ctx= new Context();
    ctx.addConstantBlock(menta.model.Constant.modelNamespace,converted)

    //instatiate trfield
    var trField = new TrConstantField()

    def checkSolutionElement(elem:HowTo):Boolean=
    {
      trField.apply(null,elem,ctx);
      if (elem.isInstanceOf[ActionIndividual] &&  elem.asInstanceOf[ActionIndividual].parameters!=null)
      {
           elem.asInstanceOf[ActionIndividual].parameters.foreach(b=>
           {
              checkSolutionElement(b);
           })
      }
      true;
    }
    checkSolutionElement(sln);
                         var obfus = new Solution()
    obfus.howTos=List(sln);
    LOG.info("ModifiedSolution: "+obfus.toString)

  }
}