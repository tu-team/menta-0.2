package org.menta.ws;

import javax.jws.WebService;

/**
* Test web service endpoint for Menta.
*
* @author ayratn
*/
@WebService(endpointInterface = "org.menta.ws.MentaService")
class MentaServiceImpl extends MentaService {

    def sendOperation(name : String) : String = {
        val operation = new Operation(name)

        return "Operation " + operation.getName() + " has been executed.";
    }

}
