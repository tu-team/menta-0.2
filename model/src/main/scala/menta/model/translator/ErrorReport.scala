package menta.model.translator

import menta.model.container.KnowledgeError

/**
 * Error message wrapped in report.
 * @author talanovm
 * Date: 25.02.11
 * Time: 18:30
 */

class ErrorReport(error: KnowledgeError) extends Report (error) {
  def this() = this(new KnowledgeError())
}