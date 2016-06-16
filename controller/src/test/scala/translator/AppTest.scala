package translator

import _root_.data.TestDataProvider
import org.scalatest.Assertions
import Assertions._
import org.slf4j.LoggerFactory
import menta.translator.constant.Constant
import java.net.URI
import org.junit.{Ignore, Test}
import menta.translator.{TranslationStrategy, TranslatorImpl}
import menta.model.container.KnowledgePatch
import menta.translator.wrappers.TrAddName
import menta.model.howto._
import menta.model.helpers.individual.AddConstantIndividualHelper
import menta.util.TestDataStrategyProvider

/**
 * User: toschev alex, talanov max
 * Date: 21.11.11
 * Time: 15:21
 */
@Test
class AppTest {

  val LOG = LoggerFactory.getLogger(this.getClass)

  @Test
  def testTranslation() {
    val sln = new Solution(List(TestDataProvider.createAddSchemaDataSetTypesSolution()));
    LOG.debug(" Ideal solution: {}", sln)

    val ac = TestDataProvider.createAcceptanceCriteriaOfThreeKids
    LOG.debug(" Acceptance criteria rule: {}", ac)

    //create constant block
    val cnst = TestDataProvider.createConstantBlock()
    LOG.debug(" Constant block: {}", ac)

    //inject constant to block
    ac.parameters = ac.parameters ::: List(cnst);
  }

  @Test
  def testTransfer() {
    val ac = TestDataProvider.createConstantBlock()
    LOG.debug(" Acceptance criteria rule: {}", ac)

    val t: TranslatorImpl = new TranslatorImpl()
    val c: Context = t.transferConstants(ac)
    assert(c.scope(Constant.translatorNamespace) != null)
    c.variable(Constant.translatorNamespace, new URI(menta.model.Constant.modelNamespaceString + "AddNameConstant")) match {
      case Some(listHowTo: List[HowTo]) => {
        assert(listHowTo(0).isInstanceOf[AddIndividual])
      }
      case None => assert(false, "empty HowToList")
    }
  }

  @Ignore
  @Test
  @deprecated
  def testConstantHowToApplication() {

    val cb = TestDataProvider.createConstantBlock()
    LOG.debug(" Constant block: {}", cb)

    val sol = new Solution(TestDataProvider.generateUIGeneratedSolution(true))
    LOG.debug(" Solution : {}", sol)

    val t: TranslatorImpl = new TranslatorImpl()
    val context: Context = t.transferConstants(cb)
    assert(context.scope(Constant.translatorNamespace) != null)
    val upSol: Solution = t.applyConstant2Solution(context, sol)
    assert(upSol != null)
    LOG.debug(" updated Solution : {}", upSol)
  }

  @Test
  def testConstantTranslation() {
    val cb = TestDataProvider.crateConstantBlock()
    LOG.debug(" Constant block: {}", cb)

    val sol = new Solution(TestDataProvider.generateUIGeneratedSolution(true))
    LOG.debug(" Solution : {}", sol)

    val context: Context = new Context()

    val t: TranslatorImpl = new TranslatorImpl()
    val res: Solution = t.applyConstantTranslation(cb, sol, context: Context)
    assert(res != null)
    LOG.info(" Solution with constant transformations : {}", res)
  }

  @Test
  def testMainTranslation() {
    val cb = TestDataProvider.crateConstantBlock()
    LOG.debug(" Constant block: {}", cb)

    val sol = new Solution(TestDataProvider.generateUIGeneratedSolution(true))
    LOG.debug(" Solution : {}", sol)

    val t: TranslatorImpl = new TranslatorImpl()

    val context: Context = t.transferConstants(cb)
    assert(context.scope(Constant.translatorNamespace) != null)

    val translationStrategy: TranslationStrategy = TestDataProvider.createStrategy()

    val res: KnowledgePatch = t.applyTranslation(sol, translationStrategy, context: Context)
    assert(res != null)
    LOG.info(" Knowledge Patch: {}", res)
  }

  @Test
  def testTrAddName() {
    val cb = TestDataProvider.crateConstantBlock()
    LOG.debug(" Constant block: {}", cb)

    val sol = new Solution(TestDataProvider.generateSimplestSolution())
    LOG.debug(" Solution : {}", sol)

    val context = new Context()

    val t: TrAddName = new TrAddName()
    val addName = new AddClass()
    addName.name = "AddNameConstant"
    addName.uri = new URI(menta.model.Constant.modelNamespaceString + "AddNameConstant");

    val translationNode = (new AddConstantIndividualHelper()).apply("store.xsd", addName, new URI(menta.model.Constant.modelNamespaceString + "AddFacadeName"))
    val res = t.apply(translationNode, sol.howTos(0), context)
    LOG.debug("res {}", res)
    assert(res != null)

  }

  @Test
  def testCSharpStrategy() {
    val strategy = TestDataStrategyProvider.createCSharpStrategy()

    // TestDataProvider.crateConstantBlock()

    // LOG.debug(" Constant block: {}", cb)

     val sol = new Solution(TestDataProvider.generateCSharpSolution())
    LOG.debug(" Solution : {}", sol)

       val cb = TestDataProvider.crateConstantBlock()

    val t: TranslatorImpl = new TranslatorImpl()

    //val context: Context = t.transferConstants(cb)
    //assert(context.scope(Constant.translatorNamespace) != null)

    val res: KnowledgePatch = t.createPatch(sol, strategy, cb)
    assert(res != null)
    LOG.info(" Knowledge Patch: {}", res)
  }

  @Test
  def testEmpGenAC(){
      val strategy = TestDataStrategyProvider.createFacadeGenerationACStrategy

    // TestDataProvider.crateConstantBlock()

    // LOG.debug(" Constant block: {}", cb)

     val sol = new Solution(List(TestDataProvider.createAddSchemaDataSetTypesSolution().asInstanceOf[AddIndividual].parameters.head));
    LOG.debug(" Solution : {}", sol)

   //    val cb = TestDataProvider.crateConstantBlock()

    val t: TranslatorImpl = new TranslatorImpl()

    //val context: Context = t.transferConstants(cb)
    //assert(context.scope(Constant.translatorNamespace) != null)

    val res: KnowledgePatch = t.createPatch(sol, strategy, null)
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

}