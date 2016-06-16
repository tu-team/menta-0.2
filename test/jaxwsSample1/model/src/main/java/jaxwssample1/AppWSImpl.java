package jaxwssample1;

import javax.jws.WebService;

@WebService
public class AppWSImpl implements AppWS {
    public String sayHello(String param) {
        return "Hello " + param;
    }
}