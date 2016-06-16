package menta.model.translator

import menta.model.learner.History

/**
 * The report of the History(all conversations(commits) of current application).
 * @author talanovm
 * Date: 25.02.11
 * Time: 19:19
 */

class HistoricalReport(history: History) extends Report(history) {
  def this() = this(new History())
}