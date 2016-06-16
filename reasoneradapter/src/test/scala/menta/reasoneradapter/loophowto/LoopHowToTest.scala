package menta.reasoneradapter.loophowto

import menta.model.howto.ProbabilisticalLogicOperators.AddInheritanceWrapper
import java.net.URI
import menta.reasoneradapter.translator.ProbabilisticalLogicOperators.AddLoopHowToInterpreter
import menta.model.howto.helper.AddParameterIndividualHelper
import menta.reasoneradapter.translator.ProbabilisticalLogicOperators.helper._
import menta.model.howto.Context
import menta.reasoneradapter.interpreter.Interpreter
import menta.model.Constant
import nars.main.Memory
import java.util.UUID
import org.slf4j.{LoggerFactory, Logger}
import menta.reasoneradapter.reasonerinterface.impl.NARSHelper
import nars.entity.Task
import menta.model.util.triple.FrequencyConfidenceNegationTriple
import menta.reasoneradapter.translator.HowToTranslator
import menta.model.howto._
import menta.model.container.KnowledgeString
import org.junit.{Ignore, Before, Test}

/**
 * Test of LoopHowTo.
 * @author talanov max
 * Date: 02.06.11
 * Time: 9:36
 */

@Test
class LoopHowToTest {

  val log: Logger = LoggerFactory.getLogger(this.getClass)

  @Before
  def prep() {
    Memory.init()
  }

  @Test
  def testOK()
  {
    assert(true);
  }
  @Test
  def loopHowToTest() {
    //simple inheritance test
    var howToSt = new AddInheritanceWrapper()
    howToSt.uriS_=(new URI("logic/inheritance"))

    val trm1 = new Terminal()
    trm1.name = "test1"
    howToSt.operand1_(trm1)

    val trm2 = new Terminal()
    trm2.name = "test2"
    var cmpTerm = new AddInheritanceWrapper()

    val chldTerm = new Terminal
    chldTerm.name = "child1"

    cmpTerm.operand1_(chldTerm)
    cmpTerm.operand2_(trm2)
    cmpTerm.uriS_=(new URI("logic/inheritance"))
    howToSt.operand2_(cmpTerm.addClass)

    val loop = new AddLoopHowToInterpreter(cmpTerm.addClass.uri, cmpTerm.addClass.parameters)
    assert(loop != null)
  }

  @Test
  def testSimpleHowToCreation() {
    // the loop variable definition
    val $a = new AddVariableIndividualWrapper("$a", null, null, generateFakeURI())
    val $SolutionHowTos = new AddVariableIndividualWrapper(Constant.Solution, null, null, generateFakeURI()) // the reference to the Solution in the context

    //Acceptance criteria
    val l0 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).leftOperandClass, "Schema0", null)
    val r0 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).rightOperandClass, "InventoryFacade", null)
    val prop0 = new AddHasAPropertyIndividualWrapper(l0, r0, null)

    val l1 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).leftOperandClass, "Schema1", null)
    val r1 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).rightOperandClass, "InventoryFacade", null)
    val prop1 = new AddHasAPropertyIndividualWrapper(l1, r1, null)

    val body = new AddBlockIndividualWrapper(List[HowTo](prop0.addIndividual(), prop1.addIndividual()), null)
    val res = new AddLoopIndividualWrapper($a.addIndividual(), $SolutionHowTos.addIndividual(), body.addIndividual(), null)
    assert(res != null)
    assert(res.destination == null)
    assert(res.leftOperand.name.equals($a.addIndividual().name))
    assert(res.rightOperand.name equals $SolutionHowTos.addIndividual().name)
    assert(res.body.parameters == body.addIndividual().parameters)
  }

  @Ignore
  @Test
  def testLoopHowToRun() {
    val t = new Interpreter
    val solution: List[HowTo] = createSolution()
    val context: Context = new Context()
    context.addEntry(Constant.modelNamespace, new URI(Constant.Solution), solution)
    val acceptanceCriteria: List[AddIndividual] = List[AddIndividual](createAcceptanceCriteria.addIndividual())
    val scope = Constant.modelNamespace

    //temp for test
    acceptanceCriteria.head.uri = generateFakeURI()

    val res = t.apply(acceptanceCriteria, context, scope)
    log.info("res {}", res)
    assert(res != null)
    assert(res.frequency == 1.0)
    assert(res.confidence >= 0.9)
    assert(!res.negation)
  }

  @Test
  def testContextVariables()
  {
    val solution: List[HowTo] = createSolution()
    val context: Context = new Context()

    var testActionClass= new AddClass
    testActionClass.name="AddClass"

    solution.head.asInstanceOf[AddIndividual].actionClass=testActionClass;

    context.addEntry(Constant.modelNamespace, new URI(Constant.Solution), solution)

    //try to find solution actionClass
    var name = context.value(Constant.modelNamespace,new URI(Constant.Solution+"/0/actionClass/0/name/"))

    assert(name.get.head.asInstanceOf[StringLiteral].value.asInstanceOf[KnowledgeString].contents==testActionClass.name)
  }


  @Ignore
  @Test
  def testLoopHowTo0_5() {
    val t = new Interpreter
    val solution: List[HowTo] = createSolution0_5()
    val context: Context = new Context()
    context.addEntry(Constant.modelNamespace, new URI(Constant.Solution), solution)
    val acceptanceCriteria: List[AddIndividual] = List[AddIndividual](createAcceptanceCriteria_0_5.addIndividual())
    val scope = Constant.modelNamespace

    //temp for test
    acceptanceCriteria.head.uri = generateFakeURI()

    val res = t.apply(acceptanceCriteria, context, scope)
    log.info("the FCN triple result {}", res)
    assert(res != null)
    assert(res.frequency == 0.5)
    // assert(res.confidence == 0.9998999834060669)
    assert(!res.negation)
  }

  @Test
  def testSelfOk() {

    val in = List(Pair(genSelfOk(), new FrequencyConfidenceNegationTriple(0.0, 0.9, false)), Pair(genSelfOk(), new FrequencyConfidenceNegationTriple(1.0, 0.9, false)))
    val TRANSLATOR: HowToTranslator = new HowToTranslator()
    val CONTEXT = new Context(Map[URI, Map[URI, List[HowTo]]]())
    var tasks: List[Task] = List[Task]()
    for (val ht: Pair[AddIndividual, FrequencyConfidenceNegationTriple] <- in) {
      val task: Task = TRANSLATOR.Translate(ht, CONTEXT, menta.model.Constant.modelNamespace)
      tasks = tasks ::: List[Task](task)
    }
    val resNotLoop: FrequencyConfidenceNegationTriple = NARSHelper(tasks, menta.reasoneradapter.constant.Constant.maxNumCycles)
    log.debug(" res self --> ok {}", resNotLoop)
    assert(resNotLoop != null)
  }

  def createSolution(): List[HowTo] = {
    val l0 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).leftOperandClass, "Schema0", null)
    val r0 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).rightOperandClass, "InventoryFacade", null)
    val prop0 = new AddHasAPropertyIndividualWrapper(l0, r0, null)

    val l1 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).leftOperandClass, "Schema1", null)
    val r1 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).rightOperandClass, "InventoryFacade", null)
    val prop1 = new AddHasAPropertyIndividualWrapper(l1, r1, null)

    List[HowTo](prop0.addIndividual(), prop1.addIndividual())
  }

  def createSolution0_5(): List[HowTo] = {
    val l0 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).leftOperandClass, "Schema0", null)
    val r0 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).rightOperandClass, "InventoryFacade", null)
    val prop0 = new AddHasAPropertyIndividualWrapper(l0, r0, null)

    val l1 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).leftOperandClass, "Schema1", null)
    val r1 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).rightOperandClass, "TillFacade", null)
    val prop1 = new AddHasAPropertyIndividualWrapper(l1, r1, null)

    List[HowTo](prop1.addIndividual(), prop0.addIndividual())
  }


  def generateFakeURI(): URI = {
    var postFix = UUID.randomUUID().toString
    return new URI("menta.org/test/#" + postFix)
  }

  /**
   * Generates NARS Self --> Ok statement
   */
  def genSelfOk(): AddIndividual = {
    val l1 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).leftOperandClass, "Self", null)
    val r1 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).rightOperandClass, "Ok", null)
    var selfOk = new AddInheritanceIndividualWrapper(l1, r1, null).addIndividual()
    selfOk
  }

  def createAcceptanceCriteria: AddLoopIndividualWrapper = {
    val variable: AddVariableIndividualWrapper = new AddVariableIndividualWrapper("$a", null, null, generateFakeURI())
    // the loop variable definition
    val $a = new AddVariableIndividualWrapper("$a", null, null, generateFakeURI())
    val $SolutionHowTos = new AddVariableIndividualWrapper("$Solution", null, null, generateFakeURI()) // the reference to the Solution in the context


    //Acceptance criteria
    val l0 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).leftOperandClass, "Schema0", null)
    val r0 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).rightOperandClass, "InventoryFacade", null)
    val prop0 = new AddHasAPropertyIndividualWrapper(l0, r0, null)

    val sok0 = (new AddImplicationIndividualHelper).apply((new AddImplicationHelper).apply(), prop0.addIndividual(), genSelfOk(), null)

    val l1 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).leftOperandClass, "Schema1", null)
    val r1 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).rightOperandClass, "InventoryFacade", null)
    val prop1 = new AddHasAPropertyIndividualWrapper(l1, r1, null)

    val sok1 = (new AddImplicationIndividualHelper).apply((new AddImplicationHelper).apply(), prop1.addIndividual(), genSelfOk(), null)


    val body = new AddBlockIndividualWrapper(List[HowTo](sok0, sok1), null)
    new AddLoopIndividualWrapper($a.addIndividual(), $SolutionHowTos.addIndividual(), body.addIndividual(), null)
  }

  def createAcceptanceCriteria_0_5: AddLoopIndividualWrapper = {
    val variable: AddVariableIndividualWrapper = new AddVariableIndividualWrapper("$a", null, null, generateFakeURI())
    // the loop variable definition
    val $a = new AddVariableIndividualWrapper("$a", null, null, generateFakeURI())
    val $SolutionHowTos = new AddVariableIndividualWrapper("$Solution", null, null, generateFakeURI()) // the reference to the Solution in the context


    //Acceptance criteria
    // new AddVariableIndividualWrapper("#schema", null, null, generateFakeURI()).addIndividual(), null)
    val l0 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).leftOperandClass, new AddVariableIndividualWrapper("$schema", null, null, generateFakeURI()).addIndividual(), null)
    val r0 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).rightOperandClass, "InventoryFacade", null)
    val prop0 = new AddHasAPropertyIndividualWrapper(l0, r0, null)

    val sok0 = (new AddImplicationIndividualHelper).apply((new AddImplicationHelper).apply(), prop0.addIndividual(), genSelfOk(), null)

    val body = new AddBlockIndividualWrapper(List[HowTo](sok0), null)
    new AddLoopIndividualWrapper($a.addIndividual(), $SolutionHowTos.addIndividual(), body.addIndividual(), null)
  }
}
