package menta.model.howto

/**
 * @author: talanovm
 * Date: 07.01.11
 * Time: 17:47
 */

class RemoveIndividual (actionClass: RemoveClass, parameters: List[HowTo]) extends ActionIndividual(actionClass, parameters) {
  def this()=this(null,null)
  override def clone():AnyRef=
  {
      val res = new RemoveIndividual()
      this.mClone(res)
      return res
  }
  var Parameters = parameters
}