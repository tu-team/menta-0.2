package menta.subscribercontroller

import menta.model.conversation.{Conversation, Response}
import menta.model.learner.Request

/**
 * Is a Publisher-Subscriber design pattern implemented via web-services.
 * This component should be used to notify several users of the system.
 * Subscriber are a Menta-client. Conversation with Menta-Client should be open until the moment when user would confirm that the Solution fits him/her
 *
 * @author talanov max
 * Date: 07.03.11
 * Time: 18:15
 */

trait SubscriberController {

  def subscribe(response: Response, conversation: Conversation): Conversation

  def unsubscribe(subscriber: Subscriber, conversation: Conversation): Subscriber

  def publish(request: Request, conversation: Conversation): Request
}