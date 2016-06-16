package org.menta.ws;


import static org.junit.Assert.*;

import org.junit.Test;

public class OperationTest {

    @Test
    public void testPerson() {
        Operation p = new Operation("add");
        assertEquals("add", p.getName());
        
        p.setName("sub");
        assertEquals("sub", p.getName());
    }

}
