package menta.model

import java.net.URI
import java.lang.String

/**
 * Build in defaults.
 * @author: talanov max
 * Date: 07.01.11
 * Time: 16:10
 */

object Constant {
  def modelNamespaceString = "http://menta.org/ontologies/v.0.2/"
  //def modelPrefix=  "menta/v0.2"
  def modelNamespace = URI.create(modelNamespaceString)
  def ruleURI = URI.create("Rule")
  def addHowToBlockName = "AddBlockHowTo"
  def leftOperand = "leftOperand"
  def rightOperand = "rightOperand"
  def bodyOperand = "bodyOperand"
  def URIClassDelimiter = "."
  def URINamespaceDelimiter = "#"
  def Solution = "$Solution"
  def parameter = "parameter"
  def variable="variable"
  val stringLiteral ="StringLiteral"
  val rootElementID="rootelement"
  val trVariable ="TrVariable"
  val trApplyTemplates ="TrApplyTemplates"
  val block = "block"
  val constantBlock="constantBlock"
  val nameConstant="nameConstant"
  val trAddField = "AddFieldConstant"
  val trAddName = "AddNameConstant"
  val trAddTarget = "AddTargetConstant"
  val trRule = "trRule"
  val trCondition = "TrCondition"
  val operatorClassName = "AddOperator"
  val addName="AddName"

  val trConstantScope=modelNamespaceString+"TrConstantScope"
}