package menta.model.solutiongenerator.solutionchecker

import menta.model.Knowledge
import menta.model.howto.HowTo
import java.net.URI

/**
 * The set of changes for the Rules of the AcceptanceCriteria.
 * @author: talanovm
 * Date: 07.01.11
 * Time: 19:12
 */

class RuleChangeSet(howTos: List[RuleChange]) extends Knowledge {
  def this() = this(List[RuleChange]())

  def getAllHowTo():List[HowTo]=
  {
    howTos.map(b=>b.howTo)
  }

  private var theConversationActURI:URI=null;

  def converstionActURI =theConversationActURI;
  def converstionActURI_=(aVal:URI)=theConversationActURI=aVal;
}
