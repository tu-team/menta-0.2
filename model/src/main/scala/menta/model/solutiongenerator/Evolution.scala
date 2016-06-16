package menta.model.solutiongenerator

import menta.model.Knowledge
import menta.model.howto.Solution
import java.util.Date
import solutionchecker.Test
import javax.xml.datatype.Duration

/**
 * @author: talanovm
 * Date: 07.01.11
 * Time: 19:19
 */

class Evolution(tests: List[Test], bestSolution: Solution, duration: Duration) extends Knowledge {
  def this() = this(List[Test](), new Solution(), null)
}
