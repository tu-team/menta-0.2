package menta.model.util

import menta.model.Knowledge

/**
 * Created by IntelliJ IDEA.
 * User: toscheva
 * Date: 15.11.11
 * Time: 15:26
 * To change this template use File | Settings | File Templates.
 */

/*
  implement clone in large object tree
 */
trait MCloneable {

  /*
    provide clone method
   */
  def mClone(trgt: Knowledge):Unit=null
}