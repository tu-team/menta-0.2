package menta.solutiongenerator.howto

import ec.gp.GPData
import menta.model.howto.HowTo

/**
 * @author talanov m
 * Date: 25.10.11
 * Time: 9:19
 */

trait HowToData extends GPData {
  def howTo: HowTo

  def howTo_=(aHowTo: HowTo)

  def baseCopyTo[TData <: HowToData](gpd: GPData) {
    (gpd.asInstanceOf[TData]).howTo.name = this.howTo.name
    (gpd.asInstanceOf[TData]).howTo.revision = this.howTo.revision
    //TODO this could require the regeneration of URI
    (gpd.asInstanceOf[TData]).howTo.uri = this.howTo.uri
  }
}