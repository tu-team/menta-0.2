package menta.model.howto.helper

import menta.model.howto.{AddIndividual, HowTo, AddClass}

/**
 * Created by IntelliJ IDEA.
 * User: toscheva
 * Date: 25.11.11
 * Time: 19:22
 * To change this template use File | Settings | File Templates.
 */

class AddEmptyHowToIndividualHelper {
  def apply() = {
    var acCl=(new AddEmptyHowToClass).apply();
    val res = new AddIndividual(acCl,null,null)
    res
  }
}