package menta.model.howto

import collection.immutable.HashSet

/**
 * @author: talanovm
 * Date: 07.01.11
 * Time: 17:36
 */

class RemoveClass(superClass: RemoveClass, parameters: List[HowTo]) extends ActionClass(superClass, parameters) {
  def this() = this (null, null)

   override def clone(): AnyRef = {
    val copied = new AddClass
    super.mClone(copied);
    copied
  }
}