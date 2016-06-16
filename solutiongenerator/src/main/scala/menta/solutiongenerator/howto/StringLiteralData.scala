package menta.solutiongenerator.howto

import ec.gp.GPData
import menta.model.howto.{HowTo, StringLiteral}

/**
 * @author talanov max
 * Date: 25.10.11
 * Time: 16:26
 */

class StringLiteralData extends HowToData {
  private var _howTo: StringLiteral = new StringLiteral()

  def howTo_=(aHowTo: StringLiteral) = this._howTo = aHowTo
  def howTo_=(aString: String) = this._howTo = new StringLiteral(aString)

  def howTo = this._howTo

  def copyTo(gpd: GPData) = {
    super.baseCopyTo[StringLiteralData](gpd)
    this.howTo
  }

  def howTo_=(aHowTo: HowTo) = {
    this.howTo = aHowTo.asInstanceOf[StringLiteral]
  }
}