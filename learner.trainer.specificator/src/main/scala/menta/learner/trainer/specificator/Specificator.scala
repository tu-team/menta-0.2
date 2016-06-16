package menta.learner.trainer.specificator

import menta.model.learner.AbstractAssociation

/**
 * Associates parts of the Solution with parts of the [http://code.google.com/p/menta/wiki/AcceptanceCriteria AcceptanceCriteria].
 * @author max talanov
 * @date 2011-03-06
 * Time: 2:28 PM
 */

trait Specificator {
  def apply(newAssociation: AbstractAssociation, rootAssociation: AbstractAssociation): Set[AbstractAssociation]
}