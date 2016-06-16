package menta.model.util.serialization.protocols

import menta.model.KnowledgeClass
import sbinary._
import menta.model.conversation.Response
import java.net.URI
import DefaultProtocol._
import Operations._
import java.util.Date

/**
 * Created by IntelliJ IDEA.
 * User: Alexander
 * Date: 04.12.11
 * Time: 16:17
 * To change this template use File | Settings | File Templates.
 */

object ResponseProtocol extends DefaultProtocol with KnowledgeProtocol {

  implicit object ResponseFormat extends Format[Response] {
    def reads(in: Input) = {
      var res = new Response();
      readProperties(in, res);
      res.solutionURI = readURI(in);
      res.translationURI = readURI(in);
      res.originalAC = readURI(in);
      res.constantBlock=readURI(in);
      res.authorName=readString(in);
      res.confirmationStatus=read[Boolean](in);
      //res.dateTime = read[Date](in)

      res
    }

    def writes(out: Output, value: Response) = {
      writeProperties(out, value)
      //writeString(out,value.name);
      writeURI(out, value.solutionURI)
      writeURI(out, value.translationURI)
      writeURI(out, value.originalAC)
      writeURI(out, value.constantBlock)
      writeString(out,value.authorName)
      write[Boolean](out,value.confirmationStatus);
      //write[Date](out, value.dateTime)
    }
  }

}