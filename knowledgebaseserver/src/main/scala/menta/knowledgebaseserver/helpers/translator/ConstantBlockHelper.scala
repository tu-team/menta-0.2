package menta.knowledgebaseserver.helpers.translator

import menta.knowledgebaseserver.KnowledgeBaseServerImpl
import menta.model.howto.{AddClass, AddIndividual, ActionIndividual}

import java.net.URI
import menta.model.Constant
import menta.model.helpers.individual.AddConstantIndividualHelper


/**
 * Created by IntelliJ IDEA.
 * User: toscheva
 * Date: 03.12.11
 * Time: 18:30
 * To change this template use File | Settings | File Templates.
 */

object ConstantBlockHelper {
  private def kbServer = new KnowledgeBaseServerImpl()

  def apply(inds: List[ActionIndividual]) = {
    var target = new AddIndividual()

    var ac = new AddClass() {}
    ac.name = menta.knowledgebaseserver.constant.Constant.NonTerminalURIs.constantBlock;
    ac.uri = new URI(menta.model.Constant.modelNamespaceString + ac.name)
    target.actionClass = kbServer.selectOrCreateActionClass(ac);
    target
  }



}