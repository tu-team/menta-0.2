package menta.knowledgebaseserver

import menta.model.Knowledge


/**
 * The container for the result set of the KB.
 * @author talanovm
 * Date: 10.01.11
 * Time: 16:03
 */

class ResultSet {
  private var theResults: Set[Knowledge] = Set[Knowledge]()
  def results = theResults
  def results_(aResults: Set[Knowledge]) = theResults = aResults
}