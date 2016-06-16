package samples

import org.junit.runner.RunWith
import org.specs._
import org.specs.matcher._
import org.specs.runner.{ JUnitSuiteRunner, JUnit }

import scala.collection.immutable.HashSet

//import org.scalacheck.Gen

/**
 * Sample specification.
 * 
 * This specification can be executed with: scala -cp <your classpath=""> ${package}.SpecsTest
 * Or using maven: mvn test
 *
 * For more information on how to write or run specifications, please visit: http://code.google.com/p/specs.
 *
 */
@RunWith(classOf[JUnitSuiteRunner])
class MySpecTest extends Specification with JUnit /*with ScalaCheck*/ {



}

object MySpecMain {
  def main(args: Array[String]) {
    new MySpecTest().main(args)
  }
}
