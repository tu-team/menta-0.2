package jaxwsSample1;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for Web Service generated by Maven archetype (mycompany-archetype-j2ee1.5-jaxws)
 *
 * @author RANGINY1
 */

public class AppWSClientTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppWSClientTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppWSClientTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true);
    }

    public void testAccessWebService() {

//        boolean resultValue = new AppWSClient().accessWebService();
//        org.junit.Assert.assertEquals("Accessed WebService Successfully ",
//                true, resultValue);
    }
}
