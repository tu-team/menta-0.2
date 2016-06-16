package menta.reasoneradapter.loophowto

import menta.model.howto.ProbabilisticalLogicOperators.AddInheritanceWrapper
import java.net.URI
import menta.model.howto.helper.AddParameterIndividualHelper
import menta.model.howto.Context
import menta.reasoneradapter.interpreter.Interpreter
import menta.model.Constant
import nars.main.Memory
import org.junit.{Before, Test}
import java.util.UUID
import menta.reasoneradapter.translator.ProbabilisticalLogicOperators.AddConditionHowToInterpreter
import org.slf4j.{LoggerFactory, Logger}
import menta.model.howto.{AddIndividual, HowTo, Terminal}
import menta.reasoneradapter.translator.ProbabilisticalLogicOperators.helper._

/**
 * Test for Conditional HowTo.
 * @author Aidar M
 * Date: 14.10.11
 */

@Test
class ConditionalHowToTest {

  val log: Logger = LoggerFactory.getLogger(this.getClass)
  val lht = new LoopHowToTest()

  /**
   * NARS command as workaround for proper usage. It initialize inner variables for NARS.
   */
  @Before
  def prep() {
    Memory.init()
  }

  @Test
  def addConditionalHowToTest() {
    val res = (new AddConditionHelper).apply()
    log.debug("res {}", res)
    assert(res.uri == new URI(menta.model.Constant.modelNamespaceString + menta.reasoneradapter.constant.Constant.NonTerminalURIs.condition))
  }

  @Test
  def addConditionIndividualTest() {
    val trueBlock = generateFalseBlock()
    val falseBlock = lht.genSelfOk()
    val conditionExpression = generateCondition()
    val individualHelper = new AddConditionIndividualHelper()
    val res = individualHelper.apply(trueBlock.addIndividual(), falseBlock, conditionExpression.addIndividual(), null)
    assert(res != null)
  }

  /**
   * Try to create simple conditional HowTo Wrapper.
   * No much sense in this test.
   */
 @Test
  def addConditionalHowToWrapperCreationTest() {
    // Create three Terminals - wrapper for end point entities in NARS.
    val terminal_1 = new Terminal()
    terminal_1.name = "terminal 1"

    val terminal_2 = new Terminal()
    terminal_2.name = "terminal 2"

    val terminal_3 = new Terminal()
    terminal_3.name = "terminal 3"

    // Create compound terminal as simple inheritance wrapper. Compound terminal consists of several terminals
    // and some specific operand between them, for instance (Terminal_1 => Terminal_2), where '=>' is defined by class
    // name and methods inside.
    val compoundTerminal = new AddInheritanceWrapper()
    compoundTerminal.operand1_(terminal_3)
    compoundTerminal.operand2_(terminal_2)
    compoundTerminal.uriS_=(new URI("logic/inheritance"))

    val howToSt = new AddInheritanceWrapper()
    howToSt.uriS_=(new URI("logic/inheritance")) // todo: wtf
    howToSt.operand1_(terminal_1)
    howToSt.operand2_(compoundTerminal.addClass)

    // At the end we have some thing like this:
    // (terminal_1 -> (terminal_3 -> terminal_2))

    val condition = new AddConditionHowToInterpreter(compoundTerminal.addClass.uri, compoundTerminal.addClass.parameters)
    assert(condition != null)
  }

  /**
   * AddConditionIndividualWrapper creation test.
   */
  @Test
  def addConditionIndividualWrapperCreationTest() {
    // condition variable definition
    val $a = new AddVariableIndividualWrapper("$a", null, null, generateFakeURI()) // todo comments!
    val $SolutionHowTos = new AddVariableIndividualWrapper(Constant.Solution, null, null, generateFakeURI()) // the reference to the Solution in the context

    //Acceptance criteria
    val body = new AddBlockIndividualWrapper(createSolution, null)
    // val res = new AddConditionIndividualWrapper($a.addIndividual(), $SolutionHowTos.addIndividual(), body.addIndividual())
    // assert(res != null)
    /*    assert(res.destination == null)
  assert(res.leftOperand.name.equals($a.addIndividual().name))
  assert(res.rightOperand.name equals $SolutionHowTos.addIndividual().name)
  assert(res._addIndividual.parameters == body.addIndividual().parameters)*/
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

   @Test
  def testConditionalHowToRun() {
    val interpreter = new Interpreter
    val solution: List[HowTo] = createSolution()
    val context: Context = new Context()
    context.addEntry(Constant.modelNamespace, new URI("$a"), solution)
    val scope = Constant.modelNamespace
    val acceptanceCriteria=createAcceptanceCriteria

    val res = interpreter.apply(acceptanceCriteria, context, Constant.modelNamespace)
    assert(res.frequency>=0.9)
  }

  def createAcceptanceCriteria: AddIndividual = {
   val falseBlock = generateFalseBlock()
    val trueBlock = generateTrueBlock()
    val conditionExpression = generateCondition()
    val individualHelper = new AddConditionIndividualHelper()
   return individualHelper.apply(trueBlock, falseBlock.addIndividual(), conditionExpression.addIndividual(), null)
  }


  def generateTrueBlock()=
  {
    val slOK =lht.genSelfOk();
    new AddBlockIndividualWrapper(List[HowTo](slOK), null).addIndividual()
  }

  def createSolution(): List[HowTo] = {
    val leftOperand_1 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).leftOperandClass, "Schema0", null)
    val rightOperand_1 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).rightOperandClass, "InventoryFacade", null)
    val hasAProperty_1 = new AddHasAPropertyIndividualWrapper(leftOperand_1, rightOperand_1, null)



    //val leftOperand_2 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).leftOperandClass, "Schema1", null)
    //val rightOperand_2 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).rightOperandClass, "InventoryFacade", null)
   // val hasAProperty_2 = new AddHasAPropertyIndividualWrapper(leftOperand_2, rightOperand_2, null)

    List[HowTo](hasAProperty_1.addIndividual())//, hasAProperty_2.addIndividual())
  }

  // todo: move to common usage
  def generateFakeURI(): URI = {
    val postFix = UUID.randomUUID().toString
    return new URI("menta.org/test/#" + postFix)
  }


  def generateFalseBlock(): AddBlockIndividualWrapper = {
    val lht = new LoopHowToTest()

    //for each $a from $Solution - not foreach
    val $a = new AddVariableIndividualWrapper("$a", null, null, generateFakeURI())
    //if InventoryFacade hasA #schema then Self -> Ok
    val l0 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).leftOperandClass, new AddVariableIndividualWrapper("$schema", null, null, generateFakeURI()).addIndividual(), null)
    val r0 = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).rightOperandClass, "InventoryFacade", null)
    val prop0 = new AddHasAPropertyIndividualWrapper(l0, r0, null)
    val sok0 = (new AddImplicationIndividualHelper).apply((new AddImplicationHelper).apply(), prop0.addIndividual(), lht.genSelfOk(), null)

    val body = new AddBlockIndividualWrapper(List[HowTo]($a.addIndividual(),sok0), null)
    body
  }

  /*
  if $element is Facade(parent) then Self -> Ok
  if $element not is Facade(parent) then not (Self -> Ok)
   */
  def generateCondition(): AddBlockIndividualWrapper = {
    val lht = new LoopHowToTest()

    val $a = new AddVariableIndividualWrapper("$a", null, null, generateFakeURI())
    // condition
    val $element = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).leftOperandClass, new AddVariableIndividualWrapper("$schema", null, null, generateFakeURI()).addIndividual(), null)
    val facade = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).rightOperandClass, "InventoryFacade", null)
    val is = new AddHasAPropertyIndividualWrapper($element, facade, null)
    val ifThen = (new AddImplicationIndividualHelper).apply((new AddImplicationHelper).apply(), is.addIndividual(), lht.genSelfOk(), null)
    //closure
    val notIsParameter = (new AddParameterIndividualHelper).apply((new AddNegationIndividualHelper).leftOperandClass, is.addIndividual(), null)
    val notIs = new AddNegationIndividualWrapper(notIsParameter, null)
    val notSelfOk = (new AddParameterIndividualHelper).apply((new AddNegationIndividualHelper).leftOperandClass, lht.genSelfOk(), null)
    val ifThenClosure = (new AddImplicationIndividualHelper).apply((new AddImplicationHelper).apply(), is.addIndividual(), lht.genSelfOk(), null)

    val body = new AddBlockIndividualWrapper(List[HowTo]($a.addIndividual(),ifThen), null)
    body
  }

}
