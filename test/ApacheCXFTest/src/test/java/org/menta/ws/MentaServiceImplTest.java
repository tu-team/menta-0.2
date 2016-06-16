package org.menta.ws;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class MentaServiceImplTest {

    private MentaService service;
    
    @Before
    public void init() {
        service = new MentaServiceImpl();
    }
    
    @Test
    public void testGreetPerson() {
        String expected = "Operation add has been executed.";
        
        String actual = service.sendOperation("add");
        
        assertEquals(expected, actual);
    }
    
}
