package menta.solutiongenerator.howto

import ec.gp.GPData
import menta.model.howto._

/**
 * @author talanovm
 * Date: 25.10.11
 * Time: 14:10
 */

class AddIndividualData(private var _howTo: AddIndividual) extends HowToData {

  def this() = this (new AddIndividual())

  /*def howTo_=(aHowTo: AddIndividual) {
    this._howTo = aHowTo
  } */

  def howTo = this._howTo

  def copyTo(gpd: GPData) {
    super.baseCopyTo[AddIndividualData](gpd)
    (gpd.asInstanceOf[AddIndividualData]).howTo.actionClass = this.howTo.actionClass
    val parameters: List[HowTo] = this.howTo.parameters
    val newParameters: List[HowTo] = for (val par <- parameters) yield {
      val res: HowTo = par match {
        case p: AddIndividual => {
          val parData = new AddIndividualData(p)
          val n = new AddIndividualData()
          parData.baseCopyTo(n)
          n.howTo
        }
        case p: RemoveIndividual => {
          val parData = new RemoveIndividualData(p)
          val n = new RemoveIndividualData()
          parData.baseCopyTo(n)
          n.howTo
        }
        case p: StringLiteral => {
          val sl = new StringLiteral(p.valueString)
          sl
        }
        case p: NumberLiteral => {
          val nl = new NumberLiteral((p.valueNumber))
          nl
        }
      }
      return res
    }
    (gpd.asInstanceOf[AddIndividualData]).howTo.parameters = newParameters
    this.howTo
  }

  def howTo_=(aHowTo: HowTo) {
    this._howTo = aHowTo.asInstanceOf[AddIndividual]
  }
}