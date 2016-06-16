package menta.reasoneradapter.translator

import org.junit._
import java.net.URI

import nars.main.Memory
import menta.model.howto.ProbabilisticalLogicOperators._
import org.slf4j.{Logger, LoggerFactory}
import menta.model.howto.Context
import menta.model.howto.{HowTo, Terminal}
import menta.reasoneradapter.constant.Constant
import scala.collection.JavaConversions._
import java.util.ArrayList
import nars.language.{Term, Product, Inheritance}

/**
 * @author: alexander
 * Date: 13.03.11
 * Time: 10:21
 */
// TODO add descriptions of the tests.
// TODO add logging of test results.
// @Test
class TranslatorTest {
  var translator: HowToTranslator = new HowToTranslator()
  val log: Logger = LoggerFactory.getLogger(this.getClass)
  val URI = new URI("menta/test")

  @Before
  def prep() {
    Memory.init()
  }

  @Test
  def ok(){
    assert(true)
  }

  @Test
  def testCommonNars()
  {
    var prms = List(new Term("self"),new Term("ok"))
    var prmsJ=new ArrayList[Term](prms)

    var res=Product.make(prmsJ);

    var test = Inheritance.make(res,new Term("hold"))
    assert(test.toString!=null)
    //Inheritance.make()
  }

  // @Test
  def testInhS1() {

    log.info("Simple inheritance test")
    //simple inheritance test
    var howToSt = new AddInheritanceWrapper()
    howToSt.uriS_=(new URI(Constant.NonTerminalURIs.inheritance))

    var trm1 = new Terminal()
    trm1.name = "test1"
    howToSt.operand1_(trm1)

    var trm2 = new Terminal()
    trm2.name = "test2"
    howToSt.operand2_(trm2)

    var res = translator.Translate(howToSt.addClass, new Context(Map[URI, Map[URI, List[HowTo]]]()), URI)

    assert(res.toString.contains("$0.0100;0.0100;0.0100$ <test1 --> test2>. %1.0000;0.9999%"))

  }

  // @Test
  def testInhC1() {
    log.info("Complex inheritance test")

    //simple inheritance test
    var howToSt = new AddInheritanceWrapper()
    howToSt.uriS_=(new URI(menta.reasoneradapter.constant.Constant.NonTerminalURIs.inheritance))

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
    cmpTerm.uriS_=(new URI(menta.reasoneradapter.constant.Constant.NonTerminalURIs.inheritance))
    howToSt.operand2_(cmpTerm.addClass)

    val res = translator.Translate(howToSt.addClass, new Context(Map[URI, Map[URI, List[HowTo]]]()), URI)

    assert(res.toString.contains("$0.0100;0.0100;0.0100$ <test1 --> <child1 --> test2>>. %1.0000;0.9999%"))

  }

  // @Test
  def testDifTermsC1() {
    log.info("Difference terms complex test")
    var howToSt = new AddInheritanceWrapper()
    howToSt.uriS_=(new URI(menta.reasoneradapter.constant.Constant.NonTerminalURIs.inheritance))

    var trm1 = new Terminal()
    trm1.name = "test1"
    howToSt.operand1_(trm1)

    var trm2 = new Terminal()
    trm2.name = "test2"
    var cmpTerm = new AddImplicationWrapper()

    //negation as oper 1
    var neg = new AddNegationWrapper()
    neg.uriS_=(new URI(menta.reasoneradapter.constant.Constant.NonTerminalURIs.negation))
    var chld2 = new Terminal
    chld2.name = "negation"
    neg.operand1_(chld2)


    cmpTerm.operand1_(neg.addClass)
    //variable as trm 2
    var vr = new AddProbabilisticPropertyWrapper()

    var chld3 = new Terminal
    chld3.name = "vr1"
    vr.operand1_(chld3)
    vr.uriS_=(new URI(menta.reasoneradapter.constant.Constant.NonTerminalURIs.property))
    cmpTerm.operand2_(vr.addClass)
    cmpTerm.uriS_=(new URI(menta.reasoneradapter.constant.Constant.NonTerminalURIs.implication))


    howToSt.operand2_(cmpTerm.addClass)

    var res = translator.Translate(howToSt.addClass, new Context(Map[URI, Map[URI, List[HowTo]]]()), URI)

    assert(res.toString.contains("$0.0100;0.0100;0.0100$ <test1 --> <(--,negation) ==> vr1>>. %1.0000;0.9999%"))
    log.info(res.toString)

  }

  // @Test
  def testDifTermsC2() {
    log.info("Difference terms complex test 2")
    var howToSt = new AddInheritanceWrapper()
    howToSt.uriS_=(new URI(menta.reasoneradapter.constant.Constant.NonTerminalURIs.inheritance))

    var trm1 = new Terminal()
    trm1.name = "test1"
    howToSt.operand1_(trm1)

    var trm2 = new Terminal()
    trm2.name = "test2"
    var cmpTerm = new AddConjuctionWrapper()

    //negation as oper 1
    var neg = new AddNegationWrapper()
    neg.uriS_=(new URI(menta.reasoneradapter.constant.Constant.NonTerminalURIs.negation))
    neg.operand1_(constructTerminal("negation"))


    cmpTerm.operand1_(neg.addClass)
    //variable as trm 2
    var vr = new AddProbabilisticPropertyWrapper()

    vr.operand1_(constructTerminal("vr1"))
    vr.uriS_=(new URI(menta.reasoneradapter.constant.Constant.NonTerminalURIs.property))
    cmpTerm.operand2_(vr.addClass)
    cmpTerm.uriS_=(new URI(menta.reasoneradapter.constant.Constant.NonTerminalURIs.conjunction))


    howToSt.operand2_(cmpTerm.addClass)

    var res = translator.Translate(howToSt.addClass, new Context(Map[URI, Map[URI, List[HowTo]]]()), URI)

    assert(res.toString.contains("$0.0100;0.0100;0.0100$ <test1 --> (&|,(--,negation),vr1)>. %1.0000;0.9999%"))

  }

  private def constructTerminal(name: String): Terminal = {

    var trm = new Terminal
    trm.name = name
    trm
  }
}
