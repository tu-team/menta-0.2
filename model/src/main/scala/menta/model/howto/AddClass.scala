package menta.model.howto

import java.net.URI
import menta.model.Constant

/**
 * @author: talanovm
 * Date: 07.01.11
 * Time: 16:52
 */

class AddClass(_superClass: ActionClass, _parameters: List[HowTo]) extends ActionClass(_superClass, _parameters) {

  def this() = this (null, null)

  def this(superClass: ActionClass, parameters: List[HowTo], uri: String) {
    this (superClass, parameters)
    this.uri = new URI(uri)
  }

  private var _scope: Set[AddClass] = Set[AddClass]()

  def scope = _scope

  def scope_=(aScope: Set[AddClass]) {
    _scope = aScope
  }

  override def toString: String = {
    var res = ""
    if (this.uri == null) {
      res = Constant.modelNamespaceString + "AddClass"
    } else {
      res = this.uri.toString
    }
    res = this.getClass.getName + " " + res
    res
  }

  override def clone(): AnyRef = {
    val copied = new AddClass
    if (scope != null)
      copied.scope = scope.map(b => b.clone.asInstanceOf[AddClass]);
    super.mClone(copied);
    copied
  }

}