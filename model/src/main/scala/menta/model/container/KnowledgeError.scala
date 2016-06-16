package menta.model.container

import menta.model.Knowledge

/**
 * @author talanovm
 * Date: 25.02.11
 * Time: 19:35
 */

class KnowledgeError(error: Error) extends Knowledge {
  def this() = this(new Error(""))
}