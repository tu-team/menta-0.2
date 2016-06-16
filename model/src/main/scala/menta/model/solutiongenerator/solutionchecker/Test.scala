package menta.model.solutiongenerator.solutionchecker

import menta.model.Knowledge
import menta.model.util.triple.FrequencyConfidenceNegationTriple
import javax.xml.datatype.Duration
import menta.model.howto.Solution
import java.net.URI


/**
 * @author: talanovm
 * Date: 07.01.11
 * Time: 19:20
 */
class Test(var theSolution: Solution, var theRule: Rule, var theScope: URI) extends Knowledge {
  def this() = this (new Solution(), new Rule(), new URI(menta.model.Constant.modelNamespaceString))

  def this(aSolution: Solution) = this (aSolution, new Rule(), new URI(menta.model.Constant.modelNamespaceString))

  var result: FrequencyConfidenceNegationTriple = null
  var duration: Duration = null

  def solution = theSolution

  def solution_=(aSolution: Solution) = theSolution = aSolution

  def rule = theRule

  def rule_=(aRule: Rule) = theRule = aRule

  def scope = theScope

  def scope_=(aScope: URI) = theScope = aScope
}