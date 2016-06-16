package menta.model.howto.helper

import menta.model.howto.{AddClass, HowTo}
import menta.model.Constant

/**
 * @author max
 * @date 2011-09-24
 * Time: 4:09 PM
 */

class AddHowToBlock {
  def apply(howToBlock: List[HowTo]): AddClass = {
    val res = new AddClass(new AddClass(), List[HowTo](new AddNameHelper().apply(Constant.addHowToBlockName)) ::: howToBlock)
    res
  }
}