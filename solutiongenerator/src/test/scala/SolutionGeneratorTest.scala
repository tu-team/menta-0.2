package main

import org.junit._
import org.slf4j.{LoggerFactory, Logger}
import menta.solutiongenerator.SolutionGeneratorImpl
import menta.knowledgebaseserver.KnowledgeBaseServerImpl
import java.net.URI
import menta.model.howto.{StringLiteral, AddClass}
import menta.model.howto.helper.AddParameterHelper

/**
 * @author toschev alex
 * Date: 27.10.11
 * Time: 13:37
 */

@Test
class SolutionGeneratorTest {
  val log: Logger = LoggerFactory.getLogger(this.getClass)
  var kbServer = new KnowledgeBaseServerImpl
  var generator = new SolutionGeneratorImpl
  private var theParameterHelper = new AddParameterHelper();

  @Test
  def testSolutionGenerator() = {

  }

  def prepareData() {

    kbServer.printKBInfo();
    var checkTo = kbServer.selectAllActionClass()
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


  @Ignore
  @Test
  def testParameterizing() = {

    prepareData();

    var checkTo = kbServer.selectAllActionClass()
    //use only root
    checkTo = checkTo.filter(p => p.isRoot);

    var prms = generator.generateParameters(null);
    //gp.fs.0.func.
    //check prms
    var indexer = 0;

    log.info("Action Class count " + checkTo.length)

    checkTo.foreach(c => {
      if (c.uri.toString.contains("#")) {
        assert(prms.count(p => p._1.startsWith("gp.fs.0.func." + indexer)) > 0)
        indexer = indexer + 1;
      }
    })
  }
}