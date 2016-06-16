package menta.model

import org.junit._

import sbinary._
import DefaultProtocol._
import Operations._
import menta.model.util.serialization.protocols.TerminalProtocol._
import menta.model.util.serialization.protocols.AddClassProtocol._
import menta.model.util.serialization.protocols.AddIndividualProtocol._

import java.net.URI
import org.slf4j.LoggerFactory
import menta.model.howto._

/**
 * @author: toschev alexander
 * Date: 17.08.11
 * Time: 19:33
 */

@Test
class SerializationTest {
  val log = LoggerFactory.getLogger(this.getClass)

  @Test
  def testTerminalSimple() {
    var term = new Terminal
    term.name = "MyTerminal"
    term.uri = new URI("http://menta.org/TestURL")
    val data = toByteArray(term)

    val obj = fromByteArray[Terminal](data)

    log.info("Serialize/Deserialize name {} and uri {} of Terminal", obj.name, obj.uri)
    assert(obj.uri == term.uri && obj.name == term.name) //.asInstanceOf[Knowledge]
  }

  @Test
  def testActionIndividual() {
    val aC = new AddClass()//template
    aC.name = "TestTemplate"
    aC.uri = new URI(Constant.modelNamespaceString + "TestClass")
    val aI = new AddIndividual(aC, List[HowTo](aC))
    aI.uri=aC.uri;
    aI.uri = new URI(Constant.modelNamespaceString + "TestIndividual")
    val data = toByteArray(aI)

    val obj = fromByteArray[AddIndividual](data)

    log.info("Serialize/Deserialize name and uri of Terminal")
    assert(obj.uri == aI.uri)
  }

  @Test
  def testActionClass() {
    val aC = new AddClass()
    aC.name = "TestSerializeTemplate"
    aC.uri = new URI(Constant.modelNamespaceString + "TestSerializeTemplate")
    val data = toByteArray(aC)

    val obj = fromByteArray[AddIndividual](data)

    log.info("Serialize/Deserialize name {}", aC.name)
    assert(obj.name == aC.name)
  }
}