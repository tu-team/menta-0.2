package menta.translator.wrappers
import menta.model.howto._
import menta.model.Knowledge
import java.net.URI
import org.menta.model.howto._interface.Terminal
import org.slf4j.LoggerFactory

/**
 * @author Gabbasov Bulat
 * Date: 17.11.11
 * Time: 18:47
 */

//TODO add TrWrapper extension to Classes
//TODO add object extending TrMatcher to pattern match the classes.
abstract class LogicalExpression extends HowTo {
  def evaluate() : BooleanLiteral
}
class LEFalse extends LogicalExpression {
  name = "LEFalse"
  uri = new URI(menta.model.Constant.modelNamespaceString + name)

  override def evaluate() : BooleanLiteral = new BooleanLiteral(false)
}

class LETrue extends LogicalExpression {
  name = "LETrue"
  uri = new URI(menta.model.Constant.modelNamespaceString + name)

  override def evaluate() : BooleanLiteral = new BooleanLiteral(true)
}
class LEOr(aPar1 : LogicalExpression, aPar2 : LogicalExpression) extends LogicalExpression {
  private var thePar1 : LogicalExpression = aPar1
  private var thePar2 : LogicalExpression = aPar2

  Param1 = aPar1
  Param2 = aPar2

  name = "LEOr"
  uri = new URI(menta.model.Constant.modelNamespaceString + name)

  def Param1 = thePar1
  def Param1_=(par1:LogicalExpression)={this.thePar1 = par1}
  def Param2 = thePar2
  def Param2_=(par2:LogicalExpression)={this.thePar2 = par2}

  override def evaluate() : BooleanLiteral = {
    new BooleanLiteral(thePar1.evaluate().valueBoolean || thePar2.evaluate().valueBoolean)
  }
}
class LEAnd(aPar1 : LogicalExpression, aPar2 : LogicalExpression) extends LogicalExpression {
  private var thePar1 : LogicalExpression = aPar1
  private var thePar2 : LogicalExpression = aPar2

  Param1 = aPar1
  Param2 = aPar2

  name = "LEAnd"
  uri = new URI(menta.model.Constant.modelNamespaceString + name)

  def Param1 = thePar1
  def Param1_=(par1:LogicalExpression)={this.thePar1 = par1}
  def Param2 = thePar2
  def Param2_=(par2:LogicalExpression)={this.thePar2 = par2}

  override def evaluate() : BooleanLiteral = {
    new BooleanLiteral(thePar1.evaluate().valueBoolean && thePar2.evaluate().valueBoolean)
  }
}

class LEXor(aPar1 : LogicalExpression, aPar2 : LogicalExpression) extends LogicalExpression {
  private var thePar1 : LogicalExpression = aPar1
  private var thePar2 : LogicalExpression = aPar2

  Param1 = aPar1
  Param2 = aPar2

  name = "LEXor"
  uri = new URI(menta.model.Constant.modelNamespaceString + name)

  def Param1 = thePar1
  def Param1_=(par1:LogicalExpression)={this.thePar1 = par1}
  def Param2 = thePar2
  def Param2_=(par2:LogicalExpression)={this.thePar2 = par2}

  override def evaluate() : BooleanLiteral = {
    new BooleanLiteral(thePar1.evaluate().valueBoolean ^ thePar2.evaluate().valueBoolean)
  }
}

class LENot(aPar : LogicalExpression) extends LogicalExpression {
  private var thePar : LogicalExpression = aPar
  Param = aPar

  name = "LENot"
  uri = new URI(menta.model.Constant.modelNamespaceString + name)

  def Param = thePar
  def Param_=(par:LogicalExpression){ this.thePar = par }

  override def evaluate() : BooleanLiteral = {
    new BooleanLiteral(!thePar.evaluate().valueBoolean)
  }
}
class LEVariable(aValue:Knowledge) extends HowTo {
  private var theValue : Knowledge = aValue

  Value = aValue

  def this() = this(null)

  def Value : Knowledge = {this.theValue}
  def Value_=(value:Knowledge) = {this.theValue = value}

  name = "LEVariable"
  uri = new URI(menta.model.Constant.modelNamespaceString + name)
}
class LEEquals(aPar1 : LEVariable, aPar2 : LEVariable) extends LogicalExpression {
  private var thePar1 : LEVariable = aPar1
  private var thePar2 : LEVariable = aPar2

  Param1 = aPar1
  Param2 = aPar2

  name = "LEEquals"
  uri = new URI(menta.model.Constant.modelNamespaceString + name)

  def Param1 = this.thePar1
  def Param1_=(par1:LEVariable)={this.thePar1 = par1}
  def Param2 = this.thePar2
  def Param2_=(par2:LEVariable)={this.thePar2 = par2}

  override def evaluate() : BooleanLiteral = {
    if(thePar1.Value.isInstanceOf[StringLiteral] && thePar2.Value.isInstanceOf[StringLiteral]){
      new BooleanLiteral(thePar1.Value.asInstanceOf[StringLiteral].valueString == thePar2.Value.asInstanceOf[StringLiteral].valueString)
    }
    else if(thePar1.Value.isInstanceOf[StringLiteral]){
      new BooleanLiteral(thePar2.Value.uri != null && thePar2.Value.uri.toString().contains(thePar1.Value.asInstanceOf[StringLiteral].valueString))
    }
    else if(thePar2.Value.isInstanceOf[StringLiteral]){
      new BooleanLiteral(thePar1.Value.uri != null && thePar1.Value.uri.toString().contains(thePar2.Value.asInstanceOf[StringLiteral].valueString))
    }
    else {
      new BooleanLiteral(thePar1.Value.uri != null && thePar2.Value.uri != null && thePar1.Value.uri.toString() == thePar2.Value.uri.toString())
    }
  }
}
class LEExists(aVariable : LEVariable, aPredicate : LogicalExpression, aDomain : HowTo) extends LogicalExpression {
  private var theVariable : LEVariable = aVariable
  private var thePredicate : LogicalExpression = aPredicate
  private var theDomain : HowTo = aDomain

  Variable = aVariable
  Predicate = aPredicate
  Domain = aDomain

  name = "LEExists"
  uri = new URI(menta.model.Constant.modelNamespaceString + name)

  def Variable = theVariable
  def Variable_=(variable : LEVariable)={this.theVariable = variable}
  def Predicate = thePredicate
  def Predicate_=(predicate : LogicalExpression)={this.thePredicate = predicate}
  def Domain = theDomain
  def Domain_=(domain : HowTo)={this.theDomain = domain}

  override def evaluate() : BooleanLiteral = {
    new BooleanLiteral(internalEvaluate(Domain))
  }

  def internalEvaluate(currentNode:HowTo) : Boolean = {
    Variable.Value = currentNode
    if(Predicate.evaluate().valueBoolean) return true
    if(currentNode.isInstanceOf[AddIndividual] && currentNode.asInstanceOf[AddIndividual].parameters != null){
      if(internalEvaluate(currentNode.asInstanceOf[AddIndividual].actionClass)) return true
      currentNode.asInstanceOf[AddIndividual].parameters.foreach(node => {if(internalEvaluate(node)) return true} )
    }
    else if(currentNode.isInstanceOf[ActionClass] && currentNode.asInstanceOf[ActionClass].parameters != null){
      currentNode.asInstanceOf[ActionClass].parameters.foreach(node => {if(internalEvaluate(node)) return true} )
    }
    return false
  }
}