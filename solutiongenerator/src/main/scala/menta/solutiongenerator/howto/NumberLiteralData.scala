package menta.solutiongenerator.howto

import ec.gp.GPData
import menta.model.howto.{HowTo, NumberLiteral}

/**
 * @author talanov max
 * Date: 25.10.11
 * Time: 16:26
 */

class NumberLiteralData extends HowToData {
  private var _howTo: NumberLiteral = new NumberLiteral()

  def howTo_=(aHowTo: NumberLiteral) = this._howTo = aHowTo

  def howTo_=(aNumber: Float) = this._howTo = new NumberLiteral(aNumber)

  def howTo = this._howTo

  def copyTo(gpd: GPData) = {
    super.baseCopyTo[NumberLiteralData](gpd)
    this.howTo
  }

  def howTo_=(aHowTo: HowTo) = {
    this.howTo = aHowTo.asInstanceOf[NumberLiteral]
  }
}