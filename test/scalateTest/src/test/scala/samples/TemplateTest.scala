package samples

import org.junit.Test
import org.fusesource.scalate.TemplateEngine
import org.junit.Assert._

/**
 * Created by IntelliJ IDEA.
 * User: GabbasovB
 * Date: 08.11.11
 * Time: 18:30
 * To change this template use File | Settings | File Templates.
 */

@Test
class TemplateTest {
     @Test
    def testScalate() = {
      val engine = new TemplateEngine
      assertTrue(true)
    }
}