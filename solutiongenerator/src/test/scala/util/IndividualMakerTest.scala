package util

/**
 * User: toschev alex
 * Date: 24.10.11
 * Time: 18:50
 */

import org.junit._
import org.slf4j.{LoggerFactory, Logger}
import menta.solutiongenerator.util.IndividualEncoder
import menta.knowledgebaseserver.KnowledgeBaseServerImpl
import menta.model.howto.{ActionClass, AddClass, AddIndividual}
import menta.solutiongenerator.howto.{RemoveIndividualWrapper, AddIndividualWrapper}

@Test
class IndividualMakerTest {

  val log: Logger = LoggerFactory.getLogger(this.getClass)
  val kbServer = new KnowledgeBaseServerImpl

//   var classes = kbServer.selectAllActionClass()

  /*@Ignore
  @Test
  def testMaker() = {
    log.debug("Creating test class")

    //add something to be sure
    kbServer.addAddActionClass(null,null,null)
    //decode
    var targetClass = classes.head
    val encoded = IndividualEncoder.encodeHowTo(targetClass.asInstanceOf[ActionClass])

    //load our class
    val res = IndividualEncoder.createIndividual(encoded)
    if (targetClass.isInstanceOf[AddClass])
    {
      assert(res.isInstanceOf[AddIndividualWrapper])
      log.debug("Check additional parameters")
      assert(res.asInstanceOf[AddIndividualWrapper].addIndividualData.howTo.actionClass.getClass==targetClass.getClass)
    }
    else
    {
      assert(res.isInstanceOf[RemoveIndividualWrapper])
      log.debug("Check additional parameters")
      assert(res.asInstanceOf[AddIndividualWrapper].addIndividualData.howTo.actionClass.getClass==targetClass.getClass)

    }
  }*/

  @Test
  def testOK() = assert(true)

}