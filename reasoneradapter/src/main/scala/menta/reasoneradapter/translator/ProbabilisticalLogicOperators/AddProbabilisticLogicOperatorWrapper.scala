package menta.model.howto.ProbabilisticalLogicOperators

import menta.reasoneradapter.translator.ActionConverter
import menta.model.howto.classifier.Classifier
import java.net.URI
import menta.model.howto.Context
import menta.reasoneradapter.translator.ProbabilisticalLogicOperators.AddVariableWrapper
import org.slf4j.{Logger, LoggerFactory}
import menta.model.Constant
import java.security.InvalidParameterException
import menta.model.howto._
import java.lang.IllegalArgumentException

/**
 * @author toschev alexander
 * Date: 10.03.11
 * Time: 21:38
 * Base logic operator translator to NARS statement.
 */
// TODO add Class diagram and wiki page with description.
class AddProbabilisticLogicOperatorWrapper(private val _uri: URI, _parameters: List[HowTo]) {

  val LOG: Logger = LoggerFactory.getLogger(this.getClass)

  var _addClass: AddClass = new AddClass(new AddOperator(), _parameters)
  _addClass.uri = _uri

  def addClass() = _addClass

  def addClass_=(aAddClass: AddClass) {
    _addClass = aAddClass
  }

  def uriS: URI = _addClass.uri

  def uriS_=(value: URI) = setUri(value)

  def getUri = addClass.uri.toString

  def setUri(value: URI) = addClass.uri = value

  /*
   facade for name
  */
  def nameS: String = getName

  def nameS_(value: String) = setName(value)

  def setName(value: String) {
    addClass.name = value
  }

  def getName = addClass.name

  /**
   * Empty constructor
   */
  def this() {
    this (new URI(Constant.modelNamespaceString + menta.reasoneradapter.constant.Constant.NonTerminalURIs.probabilisticOperator), List[HowTo]())
  }

  /**
   * construct class using existing how-to
   * @param in
   * input how-to
   */

  def this(in: HowTo) {
    this ()
    if (in != null) {
      //copy parameters
      if (in.isInstanceOf[AddClass]) {
        this.addClass.parameters = in.asInstanceOf[AddClass].parameters
      } else if (in.isInstanceOf[AddIndividual]) {
        this.addClass().parameters = in.asInstanceOf[AddIndividual].parameters
      } else {
        throw new IllegalArgumentException("in parameter is not AddClass or AddIndividual")
      }
      //LOG.debug("parameter {}", this.addClass.parameters)
    }
  }

  /*
   read-only property that should overwritten by child classes
    hols suitable converter instance
  */
  def converter: ActionConverter = {
    throw new Exception("Can't find converter for: " + this.toString())
  }

  /**
  Automatically find proper facade over how-to (using it URI)
   @param in
   represent input how-to
   */
  def Construct(in: HowTo, context: Context, scope: URI): AddProbabilisticLogicOperatorWrapper = {

    var uri: String = ""
    if (in == null) {
      throw new InvalidParameterException("Null HowTo parameter")
    }

    if (in.isInstanceOf[AddIndividual]) {
      val addIndividual: AddIndividual = in.asInstanceOf[AddIndividual]
      uri = addIndividual.actionClass.uri.toString
    } else if (in.isInstanceOf[AddClass] && in.uri != null) {
      uri = in.uri.toString
    }

    // TODO rewrite to pattern matching unapply
    //conjunction
    if (uri.contains(menta.reasoneradapter.constant.Constant.NonTerminalURIs.conjunction)) {
      return new AddConjuctionWrapper(in)
    }
    else if
    (uri.contains(menta.reasoneradapter.constant.Constant.NonTerminalURIs.implication)) {
      return new AddImplicationWrapper(in)
    } else if
    (uri.contains(menta.reasoneradapter.constant.Constant.NonTerminalURIs.inheritance)) {
      return new AddInheritanceWrapper(in)
    }
    else if
    (uri.contains(menta.reasoneradapter.constant.Constant.NonTerminalURIs.negation)) {
      return new AddNegationWrapper(in)
    }
    else if (uri.contains(menta.reasoneradapter.constant.Constant.NonTerminalURIs.property) ||
    uri.contains(menta.reasoneradapter.constant.Constant.NonTerminalURIs.hasAProperty)) {
      return new AddProbabilisticPropertyWrapper(in)
    }
    else if (uri.contains(menta.reasoneradapter.constant.Constant.NonTerminalURIs.variable)) {
      return  new AddVariableWrapper(in)
      //v.value(context, scope)
    }
    throw new Exception("Mapping for " + uri + " not found")
  }

  /*
   * return specific operand
  */
  def getOperands(aTag: String): HowTo = {
    if (this.addClass.parameters == null) this.addClass.parameters = List[HowTo]()

    if (this.addClass.parameters.exists(o => o.classifiers.exists(oc => oc.name.equals(aTag)))) {
      return this.addClass.parameters.find(o => o.classifiers.exists(oc => oc.name.equals(aTag))).head
    }
    return null
  }

  /*
   *  return list of operands with specific tag
   *  @param aTag the tag to specify parameter
  */
  def getOperandList(aTag: String): List[HowTo] = {
    if (this.addClass.parameters == null) this.addClass.parameters = List[HowTo]()

    if (this.addClass.parameters.exists(o => o.classifiers.exists(oc => oc.name.equals(aTag)))) {
      return this.addClass.parameters.filter(o => o.classifiers.exists(oc => oc.name.equals(aTag)))
    }
    return this.addClass.parameters
  }

  /**
   * set operation operands
   * @param in input operand (should have tag)
   */
  private def setOperands(in: HowTo, tagOperator: String) = {
    if (this.addClass.parameters == null) {
      this.addClass.parameters = List[HowTo]()
    }

    //replace old operator
    var parBuffer: List[HowTo] = this.addClass.parameters
    parBuffer = this.addClass.parameters.filterNot(p => p.classifiers.exists(c => c == tagOperator))
    this.addClass.parameters = parBuffer
    this.addClass.parameters = this.addClass.parameters ::: List[HowTo](in)
  }

  /*
     return operand 1 of Operation
  */
  def operand1: HowTo = {
    return getOperands("operand1")
  }

  /*
     Set operand 1
     @param vl
     How-to which represents operand of logical operation
     can be another operation or Terminal if it end of tree
  */
  def operand1_(vl: HowTo) = {
    val op1 = new Classifier()
    op1.name = "operand1"
    vl.classifiers += op1
    setOperands(vl, "operand1")
  }

  /*
      return operand 2 of Operation
  */
  def operand2: HowTo = {
    return getOperands("operand2")
  }

  /*
      Set operand 2
      @param vl
      How-to which represents operand of logical operation
      can be another operation or Terminal if it end of tree
  */
  def operand2_(vl: HowTo) = {
    val op1 = new Classifier
    op1.name = "operand2"
    vl.classifiers += op1
    setOperands(vl, "operand2")

  }


}