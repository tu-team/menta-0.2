package menta.reasoneradapter.constant

/**
 * @author max
 * @date 2011-05-29
 * Time: 11:54 PM
 */

object Constant {

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

  /*object SpecialVariables  {

     var first="$first()"
     var last="$last()"
     var count="$count()"
  } */

  val conditionHowToThreshold=0.5

  val maxNumCycles = 500
}