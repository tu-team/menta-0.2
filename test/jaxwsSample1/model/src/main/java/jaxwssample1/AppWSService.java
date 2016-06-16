
/*
 * 
 */

package jaxwssample1;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 2.2.6
 * Thu Jun 02 14:00:14 MSD 2011
 * Generated source version: 2.2.6
 * 
 */


@WebServiceClient(name = "AppWSService", 
                  wsdlLocation = "file:/C:/XInnovation/menta/jaxwsSample1/model/src/main/resources/xsd/AppWSService.wsdl",
                  targetNamespace = "http://jaxwsSample1/") 
public class AppWSService extends Service {

    public final static URL WSDL_LOCATION;
    public final static QName SERVICE = new QName("http://jaxwsSample1/", "AppWSService");
    public final static QName AppWSPort = new QName("http://jaxwsSample1/", "AppWSPort");
    static {
        URL url = null;
        try {
            url = new URL("file:/C:/XInnovation/menta/jaxwsSample1/model/src/main/resources/xsd/AppWSService.wsdl");
        } catch (MalformedURLException e) {
            System.err.println("Can not initialize the default wsdl from file:/C:/XInnovation/menta/jaxwsSample1/model/src/main/resources/xsd/AppWSService.wsdl");
            // e.printStackTrace();
        }
        WSDL_LOCATION = url;
    }

    public AppWSService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public AppWSService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public AppWSService() {
        super(WSDL_LOCATION, SERVICE);
    }

    /**
     * 
     * @return
     *     returns AppWS
     */
    @WebEndpoint(name = "AppWSPort")
    public AppWS getAppWSPort() {
        return super.getPort(AppWSPort, AppWS.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns AppWS
     */
    @WebEndpoint(name = "AppWSPort")
    public AppWS getAppWSPort(WebServiceFeature... features) {
        return super.getPort(AppWSPort, AppWS.class, features);
    }

}
