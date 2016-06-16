package menta.model.util.triple

/**
 * @author: talanovm
 * Date: 07.01.11
 * Time: 19:29
 */

class FrequencyConfidenceNegationTriple(val frequency: Double, val confidence: Double, val negation: Boolean) {
  override def toString: String = frequency.toString + ";" + confidence.toString + ";" + negation.toString + " "

  /*
    we have a line from 0 to 1. Using negation left frame is -1 , so we should extrapolate in to 0,1.
    [-1,1]->[0,1]. Currently zeroPoint of left gap is equal to 0.2 point from right gap
   */
  private val zeroPointConst: Double = 0.2

  /*
    return fitness function (zeroPoint + frequence * (negation?-zeroPoint:(1-zeroPoint))
   */
  def getFitnessFunction(): Double = {
    var negEq: Double = 0;
    if (negation) negEq = -zeroPointConst;
    else negEq = 1 - zeroPointConst;
    zeroPointConst + frequency * (negEq)
  }
}