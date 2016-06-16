package menta.learner.trainer.generaliser

import menta.model.learner.AbstractAssociation

/**
 * Replaces common parts of [http://code.google.com/p/menta/wiki/HowTo HowTos] with variables.
 * @author max
 * @date 2011-03-06
 * Time: 2:31 PM
 */

trait Generaliser {
  def apply(newAssociation: AbstractAssociation, rootAssociation: AbstractAssociation): Set[AbstractAssociation]
}