package menta.solutiongenerator.howto

import ec.gp.GPData
import menta.model.howto.{HowTo, RemoveIndividual}

/**
 * @author talanov max
 * Date: 25.10.11
 * Time: 14:10
 */

class RemoveIndividualData(private var _howTo: RemoveIndividual) extends HowToData {
  def this()=this(new RemoveIndividual())
 // private var _howTo: RemoveIndividual = new RemoveIndividual()

  def howTo_=(aHowTo: RemoveIndividual) = this._howTo = aHowTo

  def howTo = this._howTo

  def copyTo(gpd: GPData) {
    super.baseCopyTo[RemoveIndividualData](gpd)
    this.howTo
  }

  def howTo_=(aHowTo: HowTo) = null
}