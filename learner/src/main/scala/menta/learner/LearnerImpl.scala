package menta.learner

import menta.model.solutiongenerator.solutionchecker.AcceptanceCriteria
import menta.model.howto.Solution
import menta.model.learner.AbstractAssociation
import java.net.URI
import menta.knowledgebaseserver.{KnowledgeBaseServerImpl, KnowledgeBaseServer}

/**
 * Created by IntelliJ IDEA.
 * User: toscheva
 * Date: 05.12.11
 * Time: 20:25
 * To change this template use File | Settings | File Templates.
 */

class LearnerImpl extends Learner {
  private var theKBServer: KnowledgeBaseServer = new KnowledgeBaseServerImpl;

  def train(acceptanceCriteria: AcceptanceCriteria, solution: Solution): Set[AbstractAssociation] = null

  def detectAnalogy(acceptanceCriteria: AcceptanceCriteria): List[AbstractAssociation] = null

  def train(acceptanceCriteria: URI, solution: Solution) {
    //set association
    theKBServer.linkObjects(acceptanceCriteria, solution.uri, "connectedSolution")


  }

  def detectAnalogy(acceptanceCriteria: URI): Solution = {
    var res = theKBServer.getLinkedObjects(acceptanceCriteria, "connectedSolution")
    if (res != null)
    {
      var sln=res.filter(p=>p.isInstanceOf[Solution]).map(b=>b.asInstanceOf[Solution])
      if(sln.length>0) return sln.head

    }
    return null
  }
}