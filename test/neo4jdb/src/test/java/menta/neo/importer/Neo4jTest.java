package menta.neo.importer;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.index.lucene.LuceneIndexService;
import org.neo4j.kernel.EmbeddedGraphDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests model importing and data fetching.
 *
 * @author nantuko
 */
public class Neo4jTest {

	private final static Logger log = Logger.getLogger(Neo4jTest.class);

	private static final String DB_PATH = "/tmp/neodb";
	private static final String START_SEARCH_FROM = "HowToGroup";

	private static final String FIELD_ENTITY_NAME = "name";
	private static final String FIELD_ENTITY_TYPE = "type";
	public static final String FIELD_ENTITY_URI = "uri";

	private static NeoImporter importer;
	private static LuceneIndexService index;

	@BeforeClass
	public static void init() throws Exception {
		log.setLevel(Level.INFO);
		importer = new NeoImporter();
		importer.importDataFromOwl(DB_PATH);
	}

	/**
	 * Fetch the whole data model tree.
	 */
	@Test
	public void fetchAll() {
		GraphDatabaseService db = null;
		try {
			db = new EmbeddedGraphDatabase(DB_PATH);
			// First test case: loop through the data model tree
			long time1 = System.nanoTime();
			index = new LuceneIndexService(db);
			Node node = index.getSingleNode(FIELD_ENTITY_NAME, START_SEARCH_FROM);
			if (node != null) {
				log.debug("");
				log.debug(START_SEARCH_FROM);
				findSubclass(node, 1);
			} else {
				throw new IllegalArgumentException("couldn't find a node");
			}
			long time2 = System.nanoTime();
			log.info("PROFILE: testFindAll (ms):" + (time2 - time1) / (1024*1024));
		} finally {
			if (db != null) {
				db.shutdown();
			}
		}
	}

	/**
	 * Fetch 10 different nodes.
	 */
	@Test
	public void fetch10() {
		GraphDatabaseService db = null;
		try {
			db = new EmbeddedGraphDatabase(DB_PATH);
			LuceneIndexService index = new LuceneIndexService(db);
			long time1 = System.nanoTime();
			testSelectByURI(index,  "http://menta.org/ontologies/v.0.2#HowTo.Action.Add.Module");
			testSelectByURI(index,  "http://menta.org/ontologies/v.0.2#HowTo.Action.Add.Scope");
			testSelectByURI(index,  "http://menta.org/ontologies/v.0.2#HowTo.Action.Add.Project");
			testSelectByURI(index,  "http://menta.org/ontologies/v.0.2#HowTo.Action.Add.Method");
			testSelectByURI(index,  "http://menta.org/ontologies/v.0.2#HowTo.Action.Add.MethodAction.Operator.Negation");
			testSelectByURI(index,  "http://menta.org/ontologies/v.0.2#SolutionGeneratorGroup");
			testSelectByURI(index,  "http://menta.org/ontologies/v.0.2#SolutonCheckerGroup");
			testSelectByURI(index,  "http://menta.org/ontologies/v.0.2#Rule");
			testSelectByURI(index,  "http://menta.org/ontologies/v.0.2#Sequence");
			testSelectByURI(index,  "http://menta.org/ontologies/v.0.2#SequenceElement");
			long time2 = System.nanoTime();
			log.info("PROFILE: running 10 tests without cache (ms): " + (time2 - time1) / (1000));

			Node node = index.getSingleNode(FIELD_ENTITY_NAME, START_SEARCH_FROM);
			findSubclass(node, 1);

			time1 = System.nanoTime();
			testSelectByURI(index,  "http://menta.org/ontologies/v.0.2#HowTo.Action.Add.Module");
			testSelectByURI(index,  "http://menta.org/ontologies/v.0.2#HowTo.Action.Add.Scope");
			testSelectByURI(index,  "http://menta.org/ontologies/v.0.2#HowTo.Action.Add.Project");
			testSelectByURI(index,  "http://menta.org/ontologies/v.0.2#HowTo.Action.Add.Method");
			testSelectByURI(index,  "http://menta.org/ontologies/v.0.2#HowTo.Action.Add.MethodAction.Operator.Negation");
			testSelectByURI(index,  "http://menta.org/ontologies/v.0.2#SolutionGeneratorGroup");
			testSelectByURI(index,  "http://menta.org/ontologies/v.0.2#SolutonCheckerGroup");
			testSelectByURI(index,  "http://menta.org/ontologies/v.0.2#Rule");
			testSelectByURI(index,  "http://menta.org/ontologies/v.0.2#Sequence");
			testSelectByURI(index,  "http://menta.org/ontologies/v.0.2#SequenceElement");
			time2 = System.nanoTime();
			log.info("PROFILE: running 10 tests (ms) - cached: " + (time2 - time1) / (1000));
		} finally {
			if (db != null) {
				db.shutdown();
			}
		}
	}

	/**
	 * Recursively displays all sub nodes of specified node.
	 *
	 * @param node  Node to loop children for
	 * @param depth Not used.
	 */
	private static void findSubclass(Node node, int depth) {
		String name = (String) node.getProperty(FIELD_ENTITY_NAME);
		List<Node> subClasses = new ArrayList<Node>();
		for (Relationship r : node.getRelationships(Direction.OUTGOING)) {
			Node endNode = r.getEndNode();
			StringBuilder sb = new StringBuilder();
			// apply logging offset
			for (int i = 0; i < depth; i++) {
				sb.append("   |");
			}
			sb.append("-- " + r.getStartNode().getProperty(FIELD_ENTITY_NAME) + " " + endNode.getProperty(FIELD_ENTITY_NAME));

			if (r.getProperty(FIELD_ENTITY_NAME).equals(OntologyRelationshipType.SUPERCLASS_OF.name())) {
				if (!endNode.getProperty(FIELD_ENTITY_NAME).equals(name)) {
					log.debug(sb);
					findSubclass(endNode, depth + 1);
				}
			}
		}
	}

	/**
	 * Tries to find node by uri.
	 *
	 * @param index
	 * @param uri Node uri to find.
	 * @throws IllegalArgumentException if node wasn't found
	 */
	private static void testSelectByURI(LuceneIndexService index, String uri) throws IllegalArgumentException {
		Node node = index.getSingleNode(FIELD_ENTITY_URI, uri);
		if (node.getProperty("object") == null) {
			throw new IllegalArgumentException("Couldn't find an object for uri: " + uri);
		}
		/*log.debug("");
		log.debug("Searching for " + uri);
		log.debug("Found: " + node);
		if (node != null) {
			log.debug("   name     " + node.getProperty(FIELD_ENTITY_NAME));
			log.debug("   type     " + node.getProperty(FIELD_ENTITY_TYPE));
		} else {
			throw new IllegalArgumentException("Couldn't find a node: " + uri);
		}*/
	}
}
