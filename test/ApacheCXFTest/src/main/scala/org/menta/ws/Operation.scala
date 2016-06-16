package org.menta.ws;

/**
 * Pojo for WS.
 */
class Operation(value: String) {

  var name = value

  def setName(value: String) {
    name = value
  }

  def getName() : String = {
    name
  }
}
