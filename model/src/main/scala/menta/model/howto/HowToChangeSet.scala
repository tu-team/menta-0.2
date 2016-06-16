package menta.model.howto

/**
 * @author talanovm
 * Date: 25.02.11
 * Time: 19:17
 */

class HowToChangeSet(howTos: List[HowTo]) extends Solution(howTos) {
  def this() = this(List[HowTo]())
}