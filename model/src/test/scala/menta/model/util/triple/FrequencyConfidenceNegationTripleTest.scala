package menta.model.util.triple

import org.junit.Test
import java.net.URI
//import menta.model.Constant
import menta.model.howto.{HowTo, AddIndividual, AddClass}
import sbinary.Operations._

/**
 * Created by IntelliJ IDEA.
 * User: ChepkunovA
 * Date: 02.11.11
 * Time: 18:19
 * To change this template use File | Settings | File Templates.
 */

@Test
class FrequencyConfidenceNegationTripleTest {

  @Test
  def testFitnessFunction() {
    val epsilon = 0.0001
    val min = new FrequencyConfidenceNegationTriple(1, 0.5, true);
    val minus0 = new FrequencyConfidenceNegationTriple(0, 0.5, true);
    val plus0 = new FrequencyConfidenceNegationTriple(0, 0.5, false);
    val max = new FrequencyConfidenceNegationTriple(1, 0.5, false);
    assert(min.getFitnessFunction() == 0);
    assert(max.getFitnessFunction() == 1);
    assert(minus0.getFitnessFunction() > epsilon);
    assert(plus0.getFitnessFunction() < 1-epsilon);
    assert(math.abs(minus0.getFitnessFunction() - plus0.getFitnessFunction()) < epsilon);
  }

}