package hypergrpahDB.testMaxTheory

import menta.model.howto.{ActionClass, HowTo, AddClass}
import collection.immutable.HashSet

/**
 * Author: Aidar Makhmutov
 * Date: 25.01.2011
 */

class CCC(superClass: ActionClass, parameters: List[HowTo], mandatoryParameters: HashSet[HowTo]) extends AddClass(superClass, parameters, mandatoryParameters)