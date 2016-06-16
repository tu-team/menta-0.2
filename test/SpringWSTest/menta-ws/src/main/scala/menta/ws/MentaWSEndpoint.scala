package menta.ws

import org.springframework.ws.server.endpoint.annotation.{PayloadRoot, Endpoint}
import menta.model.{MentaResponse, MentaRequest, HowTo}

/**
 * @author ayratn
 */
@Endpoint
class MentaWSEndpoint {

  @PayloadRoot(localPart = "MentaRequest", namespace = "http://www.menta.org/WSSchema")
  def doit(request : MentaRequest) : MentaResponse = {
      val response : MentaResponse = new MentaResponse;
      response.setStringArgument("Hello from Menta!");
      response;
  }

   /*@PayloadRoot(localPart = "HowTo", namespace = "http://www.menta.org/WSSchema")
   def addHowTo(howTo: HowTo) = {

   }*/
}