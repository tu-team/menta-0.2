package hypergrpahDB;

import menta.model.howto.AddClass;
import org.hypergraphdb.HyperGraph;

import java.util.ArrayList;

/**
 * Author: Aidar Makhmutov
 * Date: 25.01.2011
 */
public class JavaTest {
    public void testJavaClass(HyperGraph graph){
        AddClass addClass_SuperClass = new AddClass(null, null, null);
        graph.add(addClass_SuperClass);
    }
}

