package menta.solutiongenerator.howto

/**
 * @author talanovm
 * Date: 28.10.11
 * Time: 9:42
 */


import org.junit._
import Assert._
import org.slf4j.LoggerFactory
import java.util.{Dictionary, Hashtable}
import ec.CustomizedEvolve
import menta.solutiongenerator.util.ECJParametersHelper
import collection.JavaConversions._
import menta.knowledgebaseserver.KnowledgeBaseServerImpl
import menta.solutiongenerator.SolutionGeneratorImpl
import menta.model.howto.helper.AddParameterHelper
import java.net.URI
import menta.knowledgebaseserver
import knowledgebaseserver.constant.Constant
import menta.model.howto.{StringLiteral, AddClass}

@Test
class ContentCheckProblemTest {

  val LOG = LoggerFactory.getLogger(this.getClass)
  var kbServer = new KnowledgeBaseServerImpl
  var checkTo = kbServer.selectAllActionClass()
  val sg = new SolutionGeneratorImpl
  private var theParameterHelper = new AddParameterHelper();

  @Before
  def setUp {

  }

  def prepareData() {
    //setup db
    checkTo.foreach(c => {
      kbServer.removeObject(c);
    })
    //check if nothing in db
    var res = kbServer.selectAllActionClass()
    assert(res.length <= 0)

    var cls1 = new AddClass
    cls1.uri = new URI(menta.model.Constant.modelNamespaceString + "AddFacadeClass")
    cls1.parameters = List(theParameterHelper("name", new StringLiteral(), 1))

    kbServer.save(cls1)

    cls1 = new AddClass
    cls1.uri = new URI(menta.model.Constant.modelNamespaceString + "AddField")
    cls1.parameters = List(theParameterHelper("name", new StringLiteral(), 1))
    kbServer.save(cls1)
    //fill with our proper objects

  }

  //@Test
  def testFitnessFunction() {
    try {
      prepareData();
      val parameters: Dictionary[String, String] = asJavaDictionary(sg.generateParameters(null)) //new Hashtable[String, String]()
      parameters.put("eval.problem", "menta.solutiongenerator.util.ContentCheckProblem")
      parameters.put("eval.problem.data", "menta.solutiongenerator.howto.AddIndividualData")
      parameters.put("stat.gather-full", "true")
      val myParameters: Dictionary[String, String] = parameters
      val start = System.nanoTime
      LOG.debug("ECJ libs. Add/Remove field problem.")

      val t: Thread = new Thread {
        override def run {
          CustomizedEvolve.evaluate(myParameters)
        }
      }

      t.start()
      t.join()

      LOG.info("End task in:" + (System.nanoTime - start) / 1000000000d + "s")
    }
    catch {
      case ex: Exception => {
        ex.printStackTrace
        assert(false)
      }
    }
  }

  @After
  def shutDown {
  }

  @Test
  def testOK() = assertTrue(true)

}