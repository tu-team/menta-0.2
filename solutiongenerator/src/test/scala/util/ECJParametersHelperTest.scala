package util

import org.junit.Test
import org.slf4j.{LoggerFactory, Logger}
import menta.solutiongenerator.util.{ECJParametersHelper, IndividualEncoder}

/**
 * Created by IntelliJ IDEA.
 * User: toscheva
 * Date: 27.10.11
 * Time: 16:13
 * To change this template use File | Settings | File Templates.
 */

@Test
class ECJParametersHelperTest {
   val log: Logger = LoggerFactory.getLogger(this.getClass)

  @Test
  def testProccInfo()=
  {
    val process=     ECJParametersHelper.systemProcessorsAvailible();
    log.info("Count of availible threads {}",process) ;

  }

  @Test
  def testParamGen()=
  {
    log.info("Generating ECJ params")
    var prms=ECJParametersHelper.loadDefaultParameters( )
    log.info("ECJ params generated {}",prms)

  }
}