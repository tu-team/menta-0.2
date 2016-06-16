package menta.translator.wrappers


import org.junit._
import junit.framework.JUnit4TestAdapter
import menta.model.Knowledge
import menta.model.howto._
import menta.translator.wrappers._
import menta.translator.helper.TestDataProvider

/**
 * @author GabbasovB
 * Date: 29.11.11
 * Time: 18:33

 */

class LogicalExpressionTest {
  @Test
  def testNot(){
    var var1 : LEVariable = new LEVariable(new StringLiteral("test1"))
    var var2 : LEVariable = new LEVariable(new StringLiteral("test1"))
    var var3 : LEVariable = new LEVariable(new StringLiteral("test2"))
    val expr1 = new LENot(
      new LEEquals(var1, var2)
    )
    assert(expr1.evaluate().valueBoolean == false)
    val expr2 = new LENot(
      new LEEquals(var1, var3)
    )
    assert(expr2.evaluate().valueBoolean == true)
  }
  @Test
  def testEquals(){
    var var1:LEVariable = new LEVariable(new StringLiteral("test1"))
    var var2:LEVariable = new LEVariable(new StringLiteral("test2"))
    var var3:LEVariable = new LEVariable(new AddIndividual(new AddClass, List(), null,
      menta.model.Constant.modelNamespaceString + "AddNameConstant"))
    //var v = new AddClassIndividual()
    //v .uri = menta.model.Constant.modelNamespaceString + "AddNameConstant"
    var equals:LEEquals = new LEEquals(var1, var1)
    assert(equals.evaluate().valueBoolean == true)
    equals.Param2 = var2
    assert(equals.evaluate().valueBoolean == false)
    equals.Param1 = var2
    assert(equals.evaluate().valueBoolean == true)
    equals.Param2 = var1
    assert(equals.evaluate().valueBoolean == false)
    equals.Param1 = var3
    assert(equals.evaluate().valueBoolean == false)
    equals.Param2 = var3
    assert(equals.evaluate().valueBoolean == true)
  }

  @Test
  def testExists {
    //  expr1 = (x == y) or Exists z (z = w)
    val z:LEVariable = new LEVariable()//new LEVariable(new StringLiteral("test2"))
    val w:LEVariable = new LEVariable(new StringLiteral("addXSDFieldName"))
    val solution:HowTo = createSolution()//TestDataProvider.generateUIGeneratedSolution(false);
    val expr1:LogicalExpression =  new LEExists(z, new LEEquals(z, w), solution)

    assert(expr1.evaluate().valueBoolean == true)
  }

  @Test
  def testExpression {
    //  expr1 = (x == y) or Exists z (z = w)
    val x:LEVariable = new LEVariable(new StringLiteral("test1"))
    val y:LEVariable = new LEVariable(new StringLiteral("test2"))
    val z:LEVariable = new LEVariable()//new LEVariable(new StringLiteral("test2"))
    val w:LEVariable = new LEVariable(new StringLiteral("A"))
    val solution:HowTo = createSolution()//TestDataProvider.generateUIGeneratedSolution(false);
    val expr1:LogicalExpression =  new LEOr(
      new LEEquals(x, y),
      new LEExists(z, new LEEquals(z, w), solution)
    )

    assert(expr1.evaluate().valueBoolean == true)
  }

  def createSolution():HowTo = {
    //ADD XSD Fields
    val addXSDFieldType = TestDataProvider.generateAddClass("AddXSDFieldType", null)
    val addXSDFieldName = TestDataProvider.generateAddClass("AddXSDFieldName", null)
    //val addXSDBaseFieldType = generateAddClass("AddXSDBaseFieldType", null)
    val xsdFieldOperations = TestDataProvider.generateAddIndividualList(List(addXSDFieldType, addXSDFieldName));

    //add XSD Field Operation
    val addFieldName = TestDataProvider.generateAddClass("AddXSDField", Map("addXSDFieldType" -> addXSDFieldType, "addXSDFieldName" -> addXSDFieldName));
    val addXSDFieldIndividual = TestDataProvider.generateAddIndividual(addFieldName, xsdFieldOperations);

    addXSDFieldIndividual
  }

}