package wrappers

import org.slf4j.{LoggerFactory, Logger}
import menta.translator.wrappers.TrVariable
import org.junit.{Ignore, Test}
import java.net.URI
import menta.model.howto._

/**
 * @author chepkunov adel
 * Date: 12.11.11
 * Time: 23:42
 */

@Test
class TrVariableTest {
  val log: Logger = LoggerFactory.getLogger(this.getClass)

  @Test
  def TrVariableWrapperTest() {
    val v1 = new TrVariable("v1")

    assert(v1.addIndividual().parameters(0).toString == "v1")

    //val v2 = new TrVariable()
    //v2.ValueAddress = "v2"
    //assert(v2.parameters(0).toString == "v2")

    val v3 = new TrVariable("v3")
    assert(v3.parameters(0).toString == "v3")

  }

  @Ignore
  @Test
  def TrVariableGetValueTest() {

    val solution = getTestSolution()

    val v1 = new TrVariable("")
    assert(v1.getValue(solution, List()) == "")

    val v2 = new TrVariable("./name")
    assert(v2.getValue(solution, List()) == "element")

    //TODO: etc
  }

  @Ignore
  @Test
  def TrVariableWrapperApplyTest() {
    val context = new Context()
    context.scope_=(new URI("text"), Map[URI, List[HowTo]]())
    val howto = new StringLiteral("basehowto")
    context.variable_=(new URI("text"), new URI("name"), howto)
    val wrapper = new TrVariable("")
    var solutionNode = new StringLiteral("sol")
    solutionNode.uri = new URI("name")
    var translationNode = new AddIndividual()
    translationNode.uri = new URI("text")

    val returnHowTo = wrapper.apply(translationNode, solutionNode, context, null)
    assert(howto.equals(returnHowTo))

    translationNode.uri = new URI("wrong")
    val nilHowTo = wrapper.apply(translationNode, solutionNode, context, null)
    assert(nilHowTo.isInstanceOf[NilHowTo])

    translationNode.uri = new URI("text")
    solutionNode.uri = new URI("wrong")
    val nilhowTo = wrapper.apply(translationNode, solutionNode, context, null)
    assert(nilhowTo.isInstanceOf[NilHowTo])
  }

  def getTestSolution(): HowTo = {
    //TODO do it
    val s = new AddClass()
    s
  }
}

