package menta.knowledgebaseserver.constant

/**
 * @author: talanovm
 * Date: 22.04.11
 * Time: 15:20
 */

object Constant {
  val cacheSizePropertyKey = "cacheSize"
  val URIDelimiter = "."
  val UUIDDelimiter = "#"
  val packageDelimiter = "."
  val defaultURIPrefix = menta.model.Constant.modelNamespaceString
  object NonTerminalURIs {
    val property = "property"
    val negation = "negation"
    val inheritance = "inheritance"
    val implication = "implication"
    val conjunction = "conjunction"
    val variable = "variable"
    val loop = "loop"
    val condition = "if"
    val probabilisticOperator = "probabilisticOperator"
    val hasAProperty = "hasA"
    val block = "block"
    val equal = "equal"
    val narsVariable="narsvar"
    val loopVariable="loopvar"
    val constantBlock="constantBlock"
  }

}