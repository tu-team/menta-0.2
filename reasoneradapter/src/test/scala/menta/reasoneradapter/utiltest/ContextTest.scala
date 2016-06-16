package menta.reasoneradapter.utiltest

import org.junit.Test
import org.slf4j.{LoggerFactory, Logger}
import menta.reasoneradapter.loophowto.LoopHowToTest
import java.net.URI
import menta.model.howto.Context
import menta.model.howto.{AddClass, AddIndividual, HowTo}
import menta.model.Knowledge

/**
 * Created by IntelliJ IDEA.
 * User: Alexander
 * Date: 07.11.11
 * Time: 8:30
 * To change this template use File | Settings | File Templates.
 */

@Test
class ContextTest {
  val log: Logger = LoggerFactory.getLogger(this.getClass)

  val lph = new LoopHowToTest

  @Test
  def TestContext() {

    //generate individuals

    var parent = List[HowTo]();

    var addClass = new AddClass
    addClass.name = "AddFacade"

    val ind1 = new AddIndividual
    ind1.actionClass = addClass
    parent = parent ::: List(ind1)

    var map  = Map[URI,List[Knowledge]]()

    //one more cyclic
    var cyclic = new AddIndividual
    cyclic.actionClass = addClass;
    var child1 = new AddIndividual
    child1.actionClass = addClass;

    var child2 = new AddIndividual
    child2.actionClass = addClass;
    child1.parameters = List(child2);

    cyclic.parameters = List(child1);
    parent = parent ::: List(cyclic);

    var ctx = new Context
    var res = ctx._modifyValue(parent, "", 0)
      assert(res.contains(new URI("/0/name/")))
    assert(res.contains(new URI("/1/prms/0/prms/0/")))


  }
}