package org.menta.ws;

import javax.jws.WebService;

/**
* Menta web service interface.
*
* @author ayratn
*/
@WebService
trait MentaService {
    def sendOperation(name : String) : String;
}
