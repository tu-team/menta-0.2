package menta.model.helpers.acceptanceCriteria.individual

import java.net.URI
import menta.model.howto.{AddIndividual, StringLiteral, HowTo, AddClass}

/**
 * @author max
 * @date 2011-11-27
 * Time: 11:28 PM
 */

class AddBlockConstantIndividualHelper {
  def apply(block: AddIndividual): AddClass = {
    val res: AddClass = new AddClass(new AddClass(), List[HowTo](block))
    res.name = "AddBlockHelper"
    res
  }
}