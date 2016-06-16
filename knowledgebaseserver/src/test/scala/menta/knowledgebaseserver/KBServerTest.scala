package menta.knowledgebaseserver

/**
 * @author: toscheva
 * Date: 24.02.11
 * Time: 19:20
 */

import org.junit._
import java.net.URI
import org.slf4j.LoggerFactory

import menta.model.solutiongenerator.solutionchecker.AcceptanceCriteria
import menta.model.howto._
import menta.model.Knowledge

@Test
class KBServerTest {

  val LOG = LoggerFactory.getLogger(this.getClass)

  val instKBServer: KnowledgeBaseServer = new KnowledgeBaseServerImpl


  @Test
  def addClass() {
    // ...
    try {

      LOG.info("Select menta/v0.2#AddOperator")

      val cls=instKBServer.addTerminal(null)


      //try to append value
      var vl = instKBServer.addTerminal(null)
      cls.value=vl;
      instKBServer.save(cls)
      val clazz = instKBServer.selectTerminal(cls.uri)
      assert(clazz.uri.toString == cls.uri.toString)
      assert(clazz.value!=null)


    }
    catch {
      case t: Throwable => t.printStackTrace();
      throw t
    }
    finally {

    }
  }

  @Test
  def selectModifyClass() {


    // ...
    try {
      //generate simple class

      val sbc = instKBServer.addClass(null)
      val knowledgeClass = instKBServer.selectClass(sbc.uri)

      val subTest = instKBServer.addSubClass(new URI("menta/v0.2#KnowledgeClassTest"), knowledgeClass)

      var res = instKBServer.selectClass(sbc.uri)

      LOG.info("KnowledgeClass")
      assert(res != null)
      LOG.info("Modify and save KnowledgeClass")


      res.properties
      val propTest = instKBServer.addClass(null)

      res.properties=Map(propTest.uri->propTest)

      instKBServer.save(res)
      //query modified class
      res = instKBServer.selectClass(sbc.uri)
      val resCnt = res.properties(propTest.uri)!=null
      //var resSbC = res.properties.get(new URI ("menta/v0.2#AddOperator/PropertyTest"))

      assert(resCnt)
      //assert(resSbC.get.uri == "menta/v0.2#AddOperator/PropertyTest")
    }
    catch {
      case t: Throwable => t.printStackTrace();
      throw t
    }
    finally {

    }
  }

  @Test
  def selectAllOfType()
  {
    var res= instKBServer.selectAllActionClass()
    assert(res!=null)
  }

  /*
  @Test
  def addSubClass() {
    // ...
    try {
      //generate simple class
      val sbc = instKBServer.addClass(new URI("menta/v0.2#SubClass1"))
      val res = instKBServer.addClass(new URI("menta/v0.2#AddOperator"))
      val addedSubClass = instKBServer.addSubClass(new URI("menta/v0.2#SubClass1"), res);
      //update class to ensure subclass applied
      instKBServer.save(res)

      //select target child class
      val updatedClass = instKBServer.selectClass(addedSubClass.uri)

      LOG.info("Add subclass menta/v0.2#SubClass1")

      //check if super class set
      assert(updatedClass.superClasses.count(p => p.uri.toString == ("menta/v0.2#AddOperator")) > 0);
    }
    catch {
      case t: Throwable => t.printStackTrace();
      throw t
    }
    finally {

    }
  }

  @Test
  def typeClassRoutine() {
    try {
      //generate simple class
      instKBServer.addClass(new URI("menta/v0.2#TypeClassTest"))
      val typeClass = instKBServer.selectClass(new URI("menta/v0.2#TypeClassTest"))
      val res = instKBServer.addIndividual(new URI("menta/v0.2#TestIndividual"), typeClass)
      LOG.info("Add individual menta/v0.2#TestIndividual ")

      val ind = instKBServer.selectIndividual(res.uri)
      assert(ind != null && ind.types.toArray.count(p => p.uri.toString == typeClass.uri.toString) > 0)

    }
    catch {
      case t: Throwable => t.printStackTrace();
      throw t
    }
    finally {

    }
  }

  @Test
  def actionClassRoutine() {


    // ...
    try {
      //generate simple class
      var testScope = new AddClass()
      testScope.uri=new URI("menta/v0.2#HowToTest1")

      def testInstanceCommon(uri: String) {
        LOG.info("add Action class " + uri)
        //test added class
        assert(instKBServer.selectActionClass(new URI(uri)) != null)
        LOG.info("add Action instance " + uri)

        assert(instKBServer.selectActionInstance(new URI(uri + "/Instance")) != null)
      }

      val actClass = instKBServer.addAddActionClass(new URI("menta/v0.2#TestAddActionClass1"), null, List(testScope))

      //process action class instance
      val lst = List(new AddIndividual())
      instKBServer.addAddActionInstance(new URI("menta/v0.2#TestAddActionClass1/Instance"), actClass, lst)

      testInstanceCommon("menta/v0.2#TestAddActionClass1")


      //remove instance routine
      val actRemoveClass = instKBServer.addRemoveActionClass(new URI("menta/v0.2#TestRemoveActionClass1"), null, testScope)

      //process remove action class instance
      val rmvClass = new RemoveClass()
      rmvClass.uri= new URI("menta/v0.2#TestRemoveClass")
      instKBServer.addRemoveActionInstance(new URI("menta/v0.2#TestRemoveActionClass1/Instance"), actClass, rmvClass)

      testInstanceCommon("menta/v0.2#TestRemoveActionClass1")


      //      //terminal test
      //      var term = instKBServer.addTerminal(new URI("menta/v0.2#TestTerminal"))
      //      LOG.info("add Terminal menta/v0.2#TestTerminal")
      //      assert(term != null)


    }
    catch {
      case t: Throwable => t.printStackTrace();
      throw t
    }
    finally {

    }
  }

  def testClassCommon(uri: String) {
    assert(instKBServer.selectClass(new URI(uri)) != null)
  }


  @Test
  def acceptanceCriteriaIndividualRoutine() {
    try {
      //generate simple class
      var testHWT = new AddClass()
      testHWT.uri= new URI("menta/v0.2#AcHowToTest1")

      var ac = new AcceptanceCriteria
      ac.uri = new URI("menta/v0.2#AcHowToTest1")
      instKBServer.save(ac)

      var sInd = instKBServer.addAcceptanceCriteriaIndividual(ac.uri, testHWT)



      val updatedAc = instKBServer.selectHowTo(ac.uri).asInstanceOf[AcceptanceCriteria]



      LOG.info("Test addAcceptanceCriteriaIndividual")
      assert(  updatedAc.howTo!=null && ac.howTo.uri.toString==testHWT.uri.toString)
    }
    catch {
      case t: Throwable => t.printStackTrace();
      throw t
    }
    finally {

    }
  }

  @Test
  def terminalRoutine() {
    LOG.info("Test Terminal")
    val terminalURI = "menta/v0.2#Terminal1"
    val terminal = instKBServer.addTerminal(new URI(terminalURI))
    var selectedTerminal = instKBServer.selectTerminal(new URI(terminalURI))
    assert(selectedTerminal != null)
  }
  */
}