package menta.reasoneradapter.translator.ProbabilisticalLogicOperators.helper

import menta.model.howto.{AddIndividual}
import java.net.URI
import menta.model.Constant

/**
 * @author talanov max
 * @date 2011-10-02
 * Time: 10:51 PM
 */

class AddVariableIndividualWrapper(aName: String, aValue: AddIndividual, @deprecated aDestination: AddIndividual, aURI:URI) extends AddProbabilisticLogicOperatorWrapper {
  this._destination = aDestination
  this._rightOperand = aValue
  this._leftOperand = null

  def this(aName: String, aValue: AddIndividual) = this(aName, aValue, null, null)

  def name = this.addIndividual().name
  def value = this._rightOperand
  this.addIndividual().name = aName
  this.addIndividual().uri=if(aURI == null){ new URI(Constant.modelNamespaceString + aName)} else {aURI}

  def addIndividualHelper(): AddVariableIndividualHelper = new AddVariableIndividualHelper()
  override def addIndividual(): AddIndividual = {
    this._addIndividual = this.addIndividualHelper().apply(this._rightOperand, this._destination)
    this._addIndividual.name = aName
    this._addIndividual.uri=aURI
    this._addIndividual
  }
}