package menta.model.howto.helper

import menta.model.howto.{StringLiteral, HowTo, AddClass}

/**
 * Created by IntelliJ IDEA.
 * User: toscheva
 * Date: 25.11.11
 * Time: 19:21
 * To change this template use File | Settings | File Templates.
 */

class AddEmptyHowToClass {

  def apply(): AddClass = {
    val res: AddClass = new AddClass(new AddClass (), null )
    res.name="EmptyHowTo"
    res
  }
}