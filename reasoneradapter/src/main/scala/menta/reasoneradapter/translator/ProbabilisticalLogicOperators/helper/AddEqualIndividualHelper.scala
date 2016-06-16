package menta.reasoneradapter.translator.ProbabilisticalLogicOperators.helper

import menta.model.howto.AddIndividual

/**
 * Created by IntelliJ IDEA.
 * User: Alexander
 * Date: 13.11.11
 * Time: 16:52
 * To change this template use File | Settings | File Templates.
 */

/*
   Place description here
   @author 
    Alexander 
*/
class AddEqualIndividualHelper  extends AddProbabilisticLogicOperatorIndividualHelper {

  override def actionHelperClass = new AddEqualHelper()




  val _actionClass = actionHelperClass.apply()

  override def actionClass = this._actionClass

  def leftOperand(equalIndividual: AddIndividual): AddIndividual = {
     equalIndividual.parameters(0).asInstanceOf[AddIndividual]
  }

  def rightOperand(equalIndividual: AddIndividual): AddIndividual = {
     equalIndividual.parameters(1).asInstanceOf[AddIndividual]
  }

}