package menta.model.subscribercontroller

import menta.model.Knowledge
import java.net.URI

/**
 * @author: talanovm
 * Date: 07.01.11
 * Time: 18:11
 */

class Customer(aNetworkAddress: URI) extends Knowledge {
  def this() = this(null)

  private var theAddress:URI=aNetworkAddress

  def networkAddress = theAddress

  def networkAddress_=(aNetworkAddress: URI) = theAddress = aNetworkAddress
}
