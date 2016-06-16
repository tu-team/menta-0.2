package menta.model

/**
 * @author talanov max
 * Date: 22.09.11
 * Time: 10:33
 */

import org.junit.runner.RunWith
import org.specs._
import org.specs.runner.{JUnitSuiteRunner, JUnit}
import org.junit.Assert
import Assert._

import menta.model.howto._
import helper._
import java.net.URI
import menta.model.util.URIUtil
import org.slf4j.LoggerFactory

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
class ModelTest extends Specification with JUnit /*with ScalaCheck*/ {

  val log = LoggerFactory.getLogger(this.getClass)

  "Model" should {
    "create AddNameHelper classes" in {
      val expectedString = "TestName"
      val res: ActionIndividual = (new AddNameHelper).apply(expectedString)
      val par = res.parameters
      if (par.length > 0) {
        log.debug("expected {}, actual: {}", expectedString, par(0).asInstanceOf[StringLiteral].valueString)
        assertTrue(par(0).asInstanceOf[StringLiteral].valueString == expectedString)
      } else {
        assertTrue(false)
      }
    }

    "create AddOperator classes" in {
      val addClass: AddClass = (new AddOperatorHelper).apply("TestOperatorName",
        (new AddParameterHelper()).apply(Constant.leftOperand, new AddClass(), 1),
        (new AddParameterHelper()).apply(Constant.rightOperand, new AddClass(), 1))
      val par = addClass._parameters
      if (par.length > 0) {
        assert(par(0).asInstanceOf[StringLiteral].valueString == "TestOperatorName")
      } else {
        assert(false)
      }
    }

    "crate AddOperator individuals" in {
      val operatorClass: AddClass = (new AddOperatorHelper).apply("TestOperatorName",
        (new AddParameterHelper()).apply(Constant.leftOperand, new AddClass(), 1),
        (new AddParameterHelper()).apply(Constant.rightOperand, new AddClass(), 1))
      val leftOperandClass = (new AddParameterHelper()).apply(Constant.leftOperand, new AddClass(), 1)
      val rightOperandClass = (new AddParameterHelper()).apply(Constant.rightOperand, new AddClass(), 1)

      val addIndividual: AddIndividual = new AddOperatorIndividualHelper().apply(operatorClass,
        (new AddParameterIndividualHelper).apply(leftOperandClass, new NumberLiteral(1), null),
        (new AddParameterIndividualHelper).apply(rightOperandClass, new NumberLiteral(2), null),
        null
      )
      assert(addIndividual != null, "Individual is not null")
    }

    "util should extract class name correctly" in {
      val uri = new URI("http://menta.org/ontologies/v.0.2#ClassName.UID")
      val expectedURI = "ClassName"
      val res = URIUtil.extractAddClassId(uri)
      log.debug("expected {}, actual: {}", expectedURI, res)
      assert(expectedURI.equals(res))
    }

    "util should extract complex class name correctly" in {
      val uri = new URI("http://menta.org/ontologies/v.0.2#ParentClassName.ClassName.UID")
      val expectedURI = "ClassName"
      val res = URIUtil.extractAddClassId(uri)
      log.debug("expected {}, actual: {}", expectedURI, res)
      assert(expectedURI.equals(res))
    }

    "util should extract class name from uri without UID correctly" in {
      val uri = new URI("http://menta.org/ontologies/v.0.2#ClassName")
      val expectedURI = "ClassName"
      val res = URIUtil.extractAddClassId(uri)
      log.debug("expected {}, actual: {}", expectedURI, res)
      assert(expectedURI.equals(res))
    }

  }
}

object ModelMain {
  def main(args: Array[String]) {
    new ModelTest().main(args)
  }
}