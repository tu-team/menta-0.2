package menta.reasoneradapter.util

import menta.model.howto._

/**
 * The util trait with mainly copying mechanisms.
 * @author talanovm
 * Date: 14.11.11
 * Time: 19:28
 */

object Util {

  /* def _clone(parameters: List[HowTo]): List[HowTo] = {

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
      res
    }
    return newParameters
  }*/
}