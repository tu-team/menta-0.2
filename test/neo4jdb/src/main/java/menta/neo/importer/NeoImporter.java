package menta.neo.importer;

import com.hp.hpl.jena.graph.Node_URI;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.FileManager;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.neo4j.graphdb.*;
import org.neo4j.index.IndexService;
import org.neo4j.index.lucene.LuceneIndexService;
import org.neo4j.kernel.EmbeddedGraphDatabase;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static menta.neo.importer.NeoImporter.ImportState.DATA_EXISTS;
import static menta.neo.importer.NeoImporter.ImportState.IMPORTED;


/**
 * Imports data from owl file to database.
 * Runs tests.
 *
 * @author ayratn
 */
public class NeoImporter {

	private final static Logger log = Logger.getLogger(NeoImporter.class);

	private static final String FIELD_ENTITY_NAME = "name";
	public static final String FIELD_ENTITY_URI = "uri";
	private static final String FIELD_ENTITY_TYPE = "type";
	private static final String FIELD_RELATIONSHIP_NAME = "name";

	private String filePath;
	private String dbPath;
	private String ontologyName;
	private String refNodeName;

	public enum ImportState {
		IMPORTED,
		ERROR,
		DATA_EXISTS
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public void setDbPath(String dbPath) {
		this.dbPath = dbPath;
	}

	public void setOntologyName(String ontologyName) {
		this.ontologyName = ontologyName;
	}

	public void setRefNodeName(String refNodeName) {
		this.refNodeName = refNodeName;
	}

	public ImportState importData() throws Exception {
		GraphDatabaseService neoService = null;
		IndexService indexService = null;
		try {
			// set up an embedded instance of neo database
			neoService = new EmbeddedGraphDatabase(dbPath);
			// set up index service for looking up node by name
			indexService = new LuceneIndexService(neoService);

			// check if already loaded
			if (isLoaded(neoService)) {
				log.info("   The data from owl already exists. Skipping.");
				return DATA_EXISTS;
			}

			// set up top-level pseudo nodes for navigation
			Node refNode = getReferenceNode(neoService);
			Node fileNode = getFileNode(neoService, indexService, refNode);
			// parse the owl rdf file
			Model model = ModelFactory.createDefaultModel();
			//model.read("file://" + filePath);
			InputStream in = FileManager.get().open(filePath);
			if (in == null) {
				throw new IllegalArgumentException("File " + filePath + " not found");
			}
			model.read(in, null);
			// iterate through all triples in the file, and set up corresponding
			// nodes in the neo database.
			StmtIterator it = model.listStatements();
			while (it.hasNext()) {
				Statement st = (Statement)it.next();
				Triple triple = st.asTriple();
				insertIntoDb(neoService, indexService, fileNode, triple);
			}
			return IMPORTED;
		} finally {
			if (indexService != null) {
				indexService.shutdown();
			}
			if (neoService != null) {
				neoService.shutdown();
			}
		}
	}

	/**
	 * Inserts selected entities and relationships from Triples extracted
	 * from the OWL document by the Jena parser. Only entities which have
	 * a non-blank node for the subject and object are used. Further, only
	 * relationship types listed in OntologyRelationshipTypes enum are
	 * considered. In addition, if the enum specifies that certain
	 * relationship types have an inverse, the inverse relation is also
	 * created here.
	 *
	 * @param neoService   a reference to the Neo service.
	 * @param indexService a reference to the Index service (for looking
	 *                     up Nodes by name).
	 * @param fileNode     a reference to the Node that is an entry point into
	 *                     this ontology. This node will connect to both the subject and object
	 *                     nodes of the selected triples via a CONTAINS relationship.
	 * @param triple       a reference to the Triple extracted by the Jena parser.
	 * @throws Exception if thrown.
	 */
	private void insertIntoDb(GraphDatabaseService neoService,
	                          IndexService indexService,
	                          Node fileNode,
	                          Triple triple) throws Exception {
		com.hp.hpl.jena.graph.Node subject = triple.getSubject();
		com.hp.hpl.jena.graph.Node predicate = triple.getPredicate();
		com.hp.hpl.jena.graph.Node object = triple.getObject();
		if ((subject instanceof Node_URI) &&
				(object instanceof Node_URI)) {
			// get or create the subject and object nodes
			Node subjectNode =
					getEntityNode(neoService, indexService, subject);
			Node objectNode =
					getEntityNode(neoService, indexService, object);
			if (subjectNode == null || objectNode == null) {
				return;
			}
			Transaction tx = neoService.beginTx();
			try {
				// hook up both nodes to the fileNode
				if (!isConnected(neoService, fileNode,
						OntologyRelationshipType.CONTAINS,
						Direction.OUTGOING, subjectNode)) {
					logTriple(fileNode, OntologyRelationshipType.CONTAINS, subjectNode);
					Relationship rel = fileNode.createRelationshipTo(subjectNode, OntologyRelationshipType.CONTAINS);
					rel.setProperty(FIELD_RELATIONSHIP_NAME, OntologyRelationshipType.CONTAINS.name());
				}
				if (!isConnected(neoService, fileNode,
						OntologyRelationshipType.CONTAINS,
						Direction.OUTGOING, objectNode)) {
					logTriple(fileNode, OntologyRelationshipType.CONTAINS, objectNode);
					Relationship rel = fileNode.createRelationshipTo(objectNode, OntologyRelationshipType.CONTAINS);
					rel.setProperty(FIELD_RELATIONSHIP_NAME, OntologyRelationshipType.CONTAINS.name());
				}
				// hook up subject and object via predicate
				OntologyRelationshipType type =
						OntologyRelationshipType.fromName(predicate.getLocalName());
				if (type != null) {
					logTriple(subjectNode, type, objectNode);
					Relationship rel = subjectNode.createRelationshipTo(objectNode, type);
					rel.setProperty(FIELD_RELATIONSHIP_NAME, type.name());
				}
				// create reverse relationship
				OntologyRelationshipType inverseType =
						OntologyRelationshipType.inverseOf(predicate.getLocalName());
				if (inverseType != null) {
					logTriple(objectNode, inverseType, subjectNode);
					Relationship inverseRel = objectNode.createRelationshipTo(subjectNode, inverseType);
					inverseRel.setProperty(FIELD_RELATIONSHIP_NAME, inverseType.name());
				}
				tx.success();
			} catch (Exception e) {
				tx.failure();
				throw e;
			} finally {
				tx.finish();
			}
		} else {
			return;
		}
	}

	private Node getEntityNode(GraphDatabaseService neoService,
	                           IndexService indexService, com.hp.hpl.jena.graph.Node entity) throws Exception {
		String uri = ((Node_URI) entity).getURI();
		if (uri.indexOf('#') == -1) {
			return null;
		}
		//String[] parts = StringUtils.split(uri, "#");
		String[] parts = uri.split("#");
		String type = parts[0].substring(0, parts[0].lastIndexOf('/'));
		Transaction tx = neoService.beginTx();
		try {
			Node entityNode =
					indexService.getSingleNode(FIELD_ENTITY_NAME, parts[1]);
			if (entityNode == null) {
				entityNode = neoService.createNode();
				entityNode.setProperty(FIELD_ENTITY_NAME, parts[1]);
				entityNode.setProperty(FIELD_ENTITY_TYPE, type);
				indexService.index(entityNode, FIELD_ENTITY_NAME, parts[1]);
				indexService.index(entityNode, FIELD_ENTITY_URI, uri);
			}
			tx.success();
			return entityNode;
		} catch (Exception e) {
			tx.failure();
			throw e;
		} finally {
			tx.finish();
		}
	}

	/**
	 * Loops through the relationships and returns true if the source
	 * and target nodes are connected using the specified relationship
	 * type and direction.
	 *
	 * @param neoService       a reference to the NeoService.
	 * @param sourceNode       the source Node object.
	 * @param relationshipType the type of relationship.
	 * @param direction        the direction of the relationship.
	 * @param targetNode       the target Node object.
	 * @return true or false.
	 * @throws Exception if thrown.
	 */
	private boolean isConnected(GraphDatabaseService neoService,
	                            Node sourceNode,
	                            OntologyRelationshipType relationshipType, Direction direction,
	                            Node targetNode) throws Exception {
		boolean isConnected = false;
		Transaction tx = neoService.beginTx();
		try {
			for (Relationship rel : sourceNode.getRelationships(
					relationshipType, direction)) {
				Node endNode = rel.getEndNode();
				if (endNode.getProperty(FIELD_ENTITY_NAME).equals(
						targetNode.getProperty(FIELD_ENTITY_NAME))) {
					isConnected = true;
					break;
				}
			}
			tx.success();
		} catch (Exception e) {
			tx.failure();
			throw e;
		} finally {
			tx.finish();
		}
		return isConnected;
	}

	/**
	 * Get the reference node if already available, otherwise create it.
	 *
	 * @param neoService the reference to the Neo service.
	 * @return a Neo4j Node object reference to the reference node.
	 * @throws Exception if thrown.
	 */
	private Node getReferenceNode(GraphDatabaseService neoService)
			throws Exception {
		Node refNode = null;
		Transaction tx = neoService.beginTx();
		try {
			refNode = neoService.getReferenceNode();
			if (!refNode.hasProperty(FIELD_ENTITY_NAME)) {
				refNode.setProperty(FIELD_ENTITY_NAME, refNodeName);
				refNode.setProperty(FIELD_ENTITY_TYPE, "Thing");
			}
			tx.success();
		} catch (NotFoundException e) {
			tx.failure();
			throw e;
		} finally {
			tx.finish();
		}
		return refNode;
	}

	/**
	 * Checks if owl has been already loaded.
	 * Prevents from loading owl model twice.
	 *
	 * @param neoService the reference to the Neo service.
	 * @return
	 */
	private boolean isLoaded(GraphDatabaseService neoService) {
		Node refNode = neoService.getReferenceNode();
		return refNode.hasProperty(FIELD_ENTITY_NAME) && refNode.getProperty(FIELD_ENTITY_NAME).equals(refNodeName);
	}

	/**
	 * Creates a single node for the file. This method is called once
	 * per file, and the node should not exist in the Neo4j database.
	 * So there is no need to check for existence of the node. Once
	 * the node is created, it is connected to the reference node.
	 *
	 * @param neoService the reference to the Neo service.
	 * @param refNode    the reference to the reference node.
	 * @return the "file" node representing the entry-point into the
	 *         entities described by the current OWL file.
	 * @throws Exception if thrown.
	 */
	private Node getFileNode(GraphDatabaseService neoService, IndexService indexService,
	                         Node refNode) throws Exception {
		Node fileNode = null;
		Transaction tx = neoService.beginTx();
		try {
			fileNode = neoService.createNode();
			fileNode.setProperty(FIELD_ENTITY_NAME, ontologyName);
			fileNode.setProperty(FIELD_ENTITY_TYPE, "Class");
			Relationship rel = refNode.createRelationshipTo(
					fileNode, OntologyRelationshipType.CATEGORIZED_AS);
			logTriple(refNode,
					OntologyRelationshipType.CATEGORIZED_AS, fileNode);
			rel.setProperty(
					FIELD_RELATIONSHIP_NAME,
					OntologyRelationshipType.CATEGORIZED_AS.name());
			indexService.index(fileNode, FIELD_ENTITY_NAME, ontologyName);
			tx.success();
		} catch (Exception e) {
			tx.failure();
			throw e;
		} finally {
			tx.finish();
		}
		return fileNode;
	}

	/**
	 * Convenience method to log the triple when it is inserted into the
	 * database.
	 *
	 * @param sourceNode               the subject of the triple.
	 * @param ontologyRelationshipType the predicate of the triple.
	 * @param targetNode               the object of the triple.
	 */
	private void logTriple(Node sourceNode,
	                       OntologyRelationshipType ontologyRelationshipType,
	                       Node targetNode) {
		log.debug("(" + sourceNode.getProperty(FIELD_ENTITY_NAME) +
				"," + ontologyRelationshipType.name() +
				"," + targetNode.getProperty(FIELD_ENTITY_NAME) + ")");
	}

	private static LuceneIndexService index;
	private static final String DB_PATH = "/tmp/neodb";
	private static final String ONTOLOGY_NAME = "Menta";

	public ImportState importDataFromOwl(String dbPath) throws Exception {
		Date prev = new Date();
		log.info("Processing OWL [Started]");

		setRefNodeName("MentaThing");
		setFilePath("storage.test.0.2.owl");
		setDbPath(dbPath);
		setOntologyName(ONTOLOGY_NAME);
		ImportState state = importData();
		log.info("Processing OWL [Done]");
		Date intermediate = new Date();
		log.info("PROFILE: importData (ms):" + (intermediate.getTime() - prev.getTime()));
		return state;
	}

	/**
	 * Loop through the tree in database logging tree hierarchy.
	 *
	 * @param db        Database to get data from.
	 * @param startFrom Node name to start iterating from.
	 * @throws IllegalArgumentException
	 */
	private static void testFindAll(GraphDatabaseService db, String startFrom) throws IllegalArgumentException {
		index = new LuceneIndexService(db);
		Node node = index.getSingleNode(FIELD_ENTITY_NAME, startFrom);
		if (node != null) {
			log.debug("");
			log.debug(startFrom);
			findSubclass(node, 1);
		} else {
			throw new IllegalArgumentException("couldn't find a node");
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
	 * @param db  Database to get data from.
	 * @param uri Node uri to find.
	 * @throws IllegalArgumentException if node wasn't found
	 */
	private static void testSelectByURI(GraphDatabaseService db, String uri) throws IllegalArgumentException {
		LuceneIndexService index = new LuceneIndexService(db);
		Node node = index.getSingleNode(FIELD_ENTITY_URI, uri);
		log.debug("");
		log.debug("Searching for " + uri);
		log.debug("Found: " + node);
		if (node != null) {
			log.debug("   name     " + node.getProperty(FIELD_ENTITY_NAME));
			log.debug("   type     " + node.getProperty(FIELD_ENTITY_TYPE));
		} else {
			throw new IllegalArgumentException("Couldn't find a node: " + uri);
		}
	}

	public static void main(String[] args) throws Exception {

		log.setLevel(Level.INFO);

		NeoImporter importer = new NeoImporter();

		// skip running tests on first run after data loading
		if (importer.importDataFromOwl(DB_PATH).equals(DATA_EXISTS)) {
			GraphDatabaseService db = null;
			try {
				db = new EmbeddedGraphDatabase(DB_PATH);
				// First test case: loop through the data model tree
				long time1 = System.nanoTime();
				testFindAll(db, "HowToGroup");
				long time2 = System.nanoTime();
				log.info("PROFILE: testFindAll (ms):" + (time2 - time1) / 1000000);

				// Second test case: select 10 different nodes
				time1 = System.nanoTime();
				testSelectByURI(db, "http://menta.org/ontologies/v.0.2#HowTo.Action.Add.Module");
				testSelectByURI(db, "http://menta.org/ontologies/v.0.2#HowTo.Action.Add.Scope");
				testSelectByURI(db, "http://menta.org/ontologies/v.0.2#HowTo.Action.Add.Project");
				testSelectByURI(db, "http://menta.org/ontologies/v.0.2#HowTo.Action.Add.Method");
				testSelectByURI(db, "http://menta.org/ontologies/v.0.2#HowTo.Action.Add.MethodAction.Operator.Negation");
				testSelectByURI(db, "http://menta.org/ontologies/v.0.2#SolutionGeneratorGroup");
				testSelectByURI(db, "http://menta.org/ontologies/v.0.2#SolutonCheckerGroup");
				testSelectByURI(db, "http://menta.org/ontologies/v.0.2#Rule");
				testSelectByURI(db, "http://menta.org/ontologies/v.0.2#Sequence");
				testSelectByURI(db, "http://menta.org/ontologies/v.0.2#SequenceElement");
				time2 = System.nanoTime();

				log.info("PROFILE: running 10 tests (ms):" + (time2 - time1) / 1000000);
			} finally {
				if (db != null) {
					db.shutdown();
				}
			}
		}
	}
}
