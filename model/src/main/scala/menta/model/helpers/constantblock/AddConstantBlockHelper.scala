package menta.model.helpers.constantblock

import menta.model.howto.ActionIndividual

/**
 * Created by IntelliJ IDEA.
 * User: toscheva
 * Date: 10.12.11
 * Time: 14:17
 * To change this template use File | Settings | File Templates.
 */

class AddConstantBlockHelper {

}

object AddConstantBlockChecker {
  def unapply(ind: ActionIndividual): Option[ActionIndividual] = {

    if (ind.actionClass != null &&
      ((ind.actionClass.uri != null && ind.actionClass.uri.toString.contains(menta.model.Constant.constantBlock))
        || (ind.actionClass.name != null && ind.actionClass.name.contains(menta.model.Constant.constantBlock))
        )) {

      Some(ind)
    } else {
      None
    }
  }
}