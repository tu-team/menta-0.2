package menta.reasoneradapter.selfcontrol.impl


import menta.reasoneradapter.selfcontrol.SelfControl
import menta.reasoneradapter.selfcontrol.template.impl.SelfOkTemplate
import menta.model.howto.HowTo

/**
 * {@link SelfControlFactory} implementation.
 *
 * Current implementation doesn't build template from statements and acceptance criteria and always
 * uses SelfOkTemplate as a template to match NARS list of tasks.
 *
 * @ayratn
 */
// This object must extend SelfControlFactory
object SelfControlFactoryImpl {
  def apply(statement: List[HowTo], acceptanceCriteria: List[HowTo]) : SelfControl = {
    val selfControl : SelfControl = new SelfControlImpl
    selfControl.template =new  SelfOkTemplate
    selfControl
  }
}