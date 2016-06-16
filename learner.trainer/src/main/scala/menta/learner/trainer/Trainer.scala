package menta.learner.trainer

import menta.model.learner.AbstractAssociation

/**
 * Is workflow component that invokes Associator, Specificator, Generaliser sequentially.
 * @author max
 * Date: 2011-03-06
 * Time: 2:10 PM
 */

trait Trainer {
  def apply(newAssociation: AbstractAssociation, rootAssociation: AbstractAssociation): Set[AbstractAssociation]
}