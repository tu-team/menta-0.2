package menta.subscribercontroller

import java.net.URI
import org.menta.model.conversation._interface.Request

/**
 * @author talanovm
 * Date: 07.03.11
 * Time: 18:18
 */

trait Subscriber {
  def apply(conversationURI: URI): Request
}