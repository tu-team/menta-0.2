package samples

import org.junit.runner.RunWith
import org.specs._
import org.specs.matcher._
import org.specs.runner.{ JUnitSuiteRunner, JUnit }

import scala.collection.immutable.HashSet
import java.net.URI
import menta.model.util.URIUtil
import org.junit.Assert._
import org.slf4j.LoggerFactory
import menta.model.howto.helper._
import menta.model.howto.{NumberLiteral, AddIndividual, StringLiteral, AddClass}

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

  val log = LoggerFactory.getLogger(this.getClass)

  "My" should {
    "allow " in {

      //0
    }
    "deny " in {
      assert(true)
    }
  }

  "A List" should {
    "have a size method returning the number of elements in the list" in {
      List(1, 2, 3).size must_== 3
    }
    // add more examples here
    // ...
  }

}

object MySpecMain {
  def main(args: Array[String]) {
    new MySpecTest().main(args)
  }
}
