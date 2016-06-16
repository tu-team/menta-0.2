package menta.reasoneradapter.translator

/**
 * @author talanovm
 * Date: 22.09.11
 * Time: 10:33
 */

import menta.model.howto.helper.AddParameterIndividualHelper
import org.junit.Test
import ProbabilisticalLogicOperators.helper._
import java.net.URI
import java.util.UUID

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
@Test
class HelpersTest /* extends Specification with JUnit with ScalaCheck*/ {
  @Test
  def addConjunctionIndividualTest() {
    val leftOperand = (new AddParameterIndividualHelper).apply((new AddConjunctionIndividualHelper).leftOperandClass, "Value", null)
    val res = new AddConjunctionIndividualWrapper(
      leftOperand,
      (new AddParameterIndividualHelper).apply((new AddConjunctionIndividualHelper).rightOperandClass, "Value", null),
      null)
    assert(res != null)
    assert(res.destination == null)
    assert(res.leftOperand == leftOperand)
  }

  @Test
  def addImplicationIndividualTest() {
    val leftOperand = (new AddParameterIndividualHelper).apply((new AddImplicationIndividualHelper).leftOperandClass, "Value", null)
    val res = new AddImplicationIndividualWrapper(
      leftOperand,
      (new AddParameterIndividualHelper).apply((new AddImplicationIndividualHelper).rightOperandClass, "Value", null),
      null)
    assert(res != null)
    assert(res.destination == null)
    assert(res.leftOperand == leftOperand)
  }

  @Test
  def addInheritanceIndividualTest() {
    val leftOperand = (new AddParameterIndividualHelper).apply((new AddInheritanceIndividualHelper).leftOperandClass, "Value", null)
    val res = new AddInheritanceIndividualWrapper(
      leftOperand,
      (new AddParameterIndividualHelper).apply((new AddInheritanceIndividualHelper).rightOperandClass, "Value", null),
      null)
    assert(res != null)
    assert(res.destination == null)
    assert(res.leftOperand == leftOperand)
  }


  @Test
  def addLoopIndividualTest() {
    val leftOperand = (new AddParameterIndividualHelper).apply((new AddLoopIndividualHelper).leftOperandClass, "Value", null)
    val body = (new AddParameterIndividualHelper).apply((new AddLoopIndividualHelper).leftOperandClass, "Value", null)
    val res = new AddLoopIndividualWrapper(
      leftOperand,
      (new AddParameterIndividualHelper).apply((new AddLoopIndividualHelper).rightOperandClass, "Value", null),
      body,
      null)
    assert(res != null)
    assert(res.destination == null)
    assert(res.leftOperand == leftOperand)
  }

  @Test
  def addNegationIndividualTest() {
    val rightOperand = (new AddParameterIndividualHelper).apply((new AddNegationIndividualHelper).leftOperandClass, "Value", null)
    val res = new AddNegationIndividualWrapper(rightOperand, null)
    assert(res != null)
    assert(res.destination == null)
    assert(res.rightOperand == rightOperand)
  }

  @Test
  def addHasAPropertyIndividualTest() {
    val leftOperand = (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).leftOperandClass, "Value", null)
    val res = new AddHasAPropertyIndividualWrapper(
      leftOperand,
      (new AddParameterIndividualHelper).apply((new AddHasAPropertyIndividualHelper).rightOperandClass, "Value", null),
      null)
    assert(res != null)
    assert(res.destination == null)
    assert(res.leftOperand == leftOperand)
  }


  def generateFakeURI():URI=
   {
          var postFix= UUID.randomUUID().toString
          return new URI("menta.org/test/#"+postFix)
   }


  @Test
  def addVariableIndividualTest() {
    val name = "$Var"
    val value = (new AddParameterIndividualHelper).apply((new AddVariableIndividualHelper).rightOperandClass, "Value", null)
    val res = new AddVariableIndividualWrapper(name, value, null,generateFakeURI())
    assert(res != null)
    assert(res.destination == null)
    assert(res.name == name)
    assert(res.value == value)
  }
}