package simple;

import ec.CustomizedEvolve;
import org.junit.Test;

import java.util.Dictionary;
import java.util.Hashtable;
//import Assert.*;

/**
 * Unit test for simple App.
 */
public class SimpleTest {

    @Test
    public void testMultiply() throws InterruptedException {
        Dictionary<String, String> parameters = new Hashtable<String, String>();

        /*parameters.put("parent.0", "./src/main/resources/ecj.koza.params");

        parameters.put("gp.fs.size", "1");
        parameters.put("gp.fs.0", "ec.gp.GPFunctionSet");

        parameters.put("gp.fs.0.name", "f0");
        parameters.put("gp.fs.0.info", "ec.gp.GPFuncInfo");

        parameters.put("gp.fs.0.size", "2");
        parameters.put("gp.fs.0.func.0", "menta.sg.geneticgenerator.EndNode");
        parameters.put("gp.fs.0.func.0.nc", "nc0");
        parameters.put("gp.fs.0.func.1", "menta.sg.geneticgenerator.CustomNode");
        parameters.put("gp.fs.0.func.1.nc", "nc1");

        parameters.put("eval.problem", "menta.sg.geneticgenerator.MultiValuedRegression");
        parameters.put("eval.problem.data", "menta.sg.geneticgenerator.AlleleListData");
        parameters.put("eval.problem.stack.context.data", "menta.sg.geneticgenerator.AlleleListData");  */

        parameters.put("parent.0", "./src/main/resources/helloworld.params");
        parameters.put("pop.subpop.0.species.pipe", "ec.vector.breed.VectorMutationPipeline");
        parameters.put("pop.subpop.0.species.pipe.source.0", "ec.vector.breed.VectorCrossoverPipeline");
        parameters.put("pop.subpop.0.species.pipe.source.0.source.0", "ec.select.TournamentSelection");
        parameters.put("pop.subpop.0.species.pipe.source.0.source.1", "ec.select.TournamentSelection");
        parameters.put("select.tournament.size", "2");
        parameters.put("eval.problem", "simple.HelloWorld");
        parameters.put("breed.elites.0", "1");

        final Dictionary<String, String> myParameters = parameters;

        Thread t = new Thread() {
            @Override
            public void run() {
                CustomizedEvolve.evaluate(myParameters);
            }
        };
        t.start();
        t.join();
    }
}
