package owlimtest;

import info.aduna.iteration.CloseableIteration;
import org.openrdf.model.*;
import org.openrdf.model.impl.GraphImpl;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.query.*;
import org.openrdf.query.parser.ParsedQuery;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.RepositoryResult;
import org.openrdf.repository.config.RepositoryConfig;
import org.openrdf.repository.manager.LocalRepositoryManager;
import org.openrdf.repository.manager.RepositoryManager;
import org.openrdf.repository.sail.SailQuery;
import org.openrdf.repository.sail.SailRepositoryConnection;
import org.openrdf.rio.*;
import org.openrdf.sail.SailConnection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;

/**
 * This sample application is intended to illustrate how to prepare, configure
 * and run a Sesame repository using the OWLIM SAIL. The basic operations are
 * demonstrated in separate methods: get namespaces, evaluate queries, add and
 * delete statements, parse and load files.
 * <p/>
 * Addition and removal are performed only when the input parameter 'updates' is
 * set to 'true'. Thus, potentially slow and irrelevant delete operations are
 * avoided in case the example is adapted for loading large data-sets.
 * <p/>
 * This application can be used also as an easy test-bed for loading and
 * querying different ontologies and data-sets without need to build a separate
 * application.
 *
 * @author Damyan Ognyanoff (damyan@sirma.bg)
 */
public class GettingStarted {

    // Some system properties used to add some flexibility
    public static String PARAM_CONFIG = "config";
    public static String PARAM_SHOWRESULTS = "showresults";
    public static String PARAM_UPDATES = "updates";
    public static String PARAM_PRELOAD = "preload";
    public static String PARAM_QUERY = "queryfile";

    private Map<String, String> parameters;

    // A map of namespace-to-prefix
    private Map<String, String> namespacePrefixes = new HashMap<String, String>();

    // The repository manager
    private RepositoryManager repositoryManager;

    // From repositoryManager.getRepository(...) - the actual repository we will
    // work with
    public Repository repository;

    // From repository.getConnection() - the connection through which we will
    // use the repository
    public RepositoryConnection repositoryConnection;

    // A flag to indicate whether query results should be output.
    private boolean showResults = false;

    // Flag for executing queries using multiple threads
    private boolean doMultithreadQueryEvaluation = false;

    /**
     * Constructor - uses a map of configuration parameters to initialise the
     * application 1. retrieves the path to the configuration file used to
     * initialise the list of repositories for Sesame 2. creates an instance of
     * the RepositoryManager 3. get a reference to the pre-configured repository using its id
     * from the map 4. if the repository was configured to use OWLIM, retrieve a
     * reference to it
     *
     * @param parameters a map of configuration parameters
     */
    public GettingStarted(Map<String, String> parameters) throws Exception {

        this.parameters = parameters;

        System.out.println("===== Initialize and load imported ontologies =========");

        // Set the 'output results' flag
        showResults = "true".equals(parameters.get(PARAM_SHOWRESULTS));

        // The configuration file
        File configFile = new File(parameters.get(PARAM_CONFIG));
        System.out.println("Using configuration file: " + configFile.getAbsolutePath());

        // Parse the configuration file, assuming it is in Turtle format
        final Graph graph = parseFile(configFile, RDFFormat.TURTLE, "http://example.org#");

        // Look for the subject of the first matching statement for
        // "?s type Repository"
        Iterator<Statement> iter = graph.match(null, RDF.TYPE, new URIImpl(
                "http://www.openrdf.org/config/repository#Repository"));
        Resource repositoryNode = null;
        if (iter.hasNext()) {
            Statement st = iter.next();
            repositoryNode = st.getSubject();
        }

        // Use this class to access a remote repository advertised by an
        // instance of the Sesame HTTP Server.
        // repositoryManager = new RemoteRepositoryManager(
        // "http://192.168.1.25:8080/openrdf-sesame" );

        // Create a manager for local repositories
        repositoryManager = new LocalRepositoryManager(new File("."));

        // Initialise the repository manager
        repositoryManager.initialize();

        // Create a configuration object from the configuration file and add it
        // to the repositoryManager
        RepositoryConfig repositoryConfig = RepositoryConfig.create(graph, repositoryNode);
        repositoryManager.addRepositoryConfig(repositoryConfig);

        // Get the repository to use
        repository = repositoryManager.getRepository("owlim");

        // Open a connection to this repository
        repositoryConnection = repository.getConnection();
        repositoryConnection.setAutoCommit(false);
    }

    /**
     * Parse the given RDF file and return the contents as a Graph
     *
     * @param configurationFile The file containing the RDF data
     * @return The contents of the file as an RDF graph
     */
    private Graph parseFile(File configurationFile, RDFFormat format, String defaultNamespace) throws Exception {
        final Graph graph = new GraphImpl();
        RDFParser parser = Rio.createParser(format);
        RDFHandler handler = new RDFHandler() {
            public void endRDF() throws RDFHandlerException {
            }

            public void handleComment(String arg0) throws RDFHandlerException {
            }

            public void handleNamespace(String arg0, String arg1) throws RDFHandlerException {
            }

            public void handleStatement(Statement statement) throws RDFHandlerException {
                graph.add(statement);
            }

            public void startRDF() throws RDFHandlerException {
            }
        };
        parser.setRDFHandler(handler);
        parser.parse(new FileReader(configurationFile), defaultNamespace);
        return graph;
    }

    /**
     * Parses and loads all files specified in PARAM_PRELOAD
     */
    public void loadFiles() throws Exception {
        System.out.println("===== Load Files (from the '" + PARAM_PRELOAD + "' parameter) ==========");

        // Load all the files from the pre-load folder
        String preload = parameters.get(PARAM_PRELOAD);

        if (preload == null)
            System.out.println("No pre-load directory/filename provided.");
        else {
            FileWalker.Handler handler = new FileWalker.Handler() {

                @Override
                public void file(File file) throws Exception {
                    loadFile(file);
                }

                @Override
                public void directory(File directory) throws Exception {
                    System.out.println("Loading files from: " + directory.getAbsolutePath());
                }
            };

            FileWalker walker = new FileWalker();
            walker.setHandler(handler);
            walker.walk(new File(preload));
        }
        repositoryConnection.commit();
    }

    /**
     * Load an RDF file by trying to parse in all known formats.
     *
     * @param preloadFile The file to load in to the repository.
     */
    private void loadFile(File preloadFile) throws RepositoryException, IOException {

        // Invent a context for the graph loaded from the file.
        URI context = new URIImpl(preloadFile.toURI().toString());

        boolean loaded = false;

        // Try all formats
        for (RDFFormat rdfFormat : allFormats) {
            try {
                repositoryConnection.add(new BufferedReader(new FileReader(preloadFile), 1024 * 1024),
                        "http://example.org/owlim#", rdfFormat, context);
                //repositoryConnection.commit();
                System.out.println("Loaded file '" + preloadFile.getName() + "' (" + rdfFormat.getName() + ").");
                loaded = true;
                break;
            } catch (UnsupportedRDFormatException e) {
                // Format not supported, so try the next format in the list.
            } catch (RDFParseException e) {
                // Can't parse the file, so it is probably in another format. Try the next format.
            }
        }
        if (!loaded)
            System.out.println("Failed to load '" + preloadFile.getName() + "'.");
    }

    // A list of RDF file formats used in loadFile().
    private static final RDFFormat allFormats[] = new RDFFormat[]{
            RDFFormat.NTRIPLES,
            RDFFormat.N3,
            RDFFormat.RDFXML,
            RDFFormat.TURTLE,
            RDFFormat.TRIG,
            RDFFormat.TRIX};

    /**
     * Show some initialisation statistics
     */
    public void showInitializationStatistics(long startupTime) throws Exception {

        long explicitStatements = numberOfExplicitStatements();
        long implicitStatements = numberOfImplicitStatements();

        System.out.println("Loaded: " + explicitStatements + " explicit statements.");
        System.out.println("Inferred: " + implicitStatements + " implicit statements.");

        if (startupTime > 0) {
            double loadSpeed = explicitStatements / (startupTime / 1000.0);
            System.out.println(" in " + startupTime + "ms.");
            System.out.println("Loading speed: " + loadSpeed + " explicit statements per second.");
        } else {
            System.out.println(" in less than 1 second.");
        }
        System.out.println("Total number of statements: " + (explicitStatements + implicitStatements));
    }

    /**
     * Two approaches for finding the total number of explicit statements in a
     * repository.
     *
     * @return The number of explicit statements
     */
    private long numberOfExplicitStatements() throws Exception {

        // This call should return the number of explicit statements.
        long explicitStatements = repositoryConnection.size();

        // Another approach is to get an iterator to the explicit statements
        // (by setting the includeInferred parameter to false) and then counting
        // them.
        RepositoryResult<Statement> statements = repositoryConnection.getStatements(null, null, null, false);
        explicitStatements = 0;

        while (statements.hasNext()) {
            statements.next();
            explicitStatements++;
        }
        statements.close();
        return explicitStatements;
    }

    /**
     * A method to count only the inferred statements in the repository. No
     * method for this is available through the Sesame API, so OWLIM uses a
     * special context that is interpreted as instruction to retrieve only
     * the implicit statements, i.e. not explicitly asserted in
     * the repository.
     *
     * @return The number of implicit statements.
     */
    private long numberOfImplicitStatements() throws Exception {
        // Retrieve all inferred statements
        RepositoryResult<Statement> statements = repositoryConnection.getStatements(null, null, null,
                true, new URIImpl("http://www.ontotext.com/implicit"));
        long implicitStatements = 0;

        while (statements.hasNext()) {
            statements.next();
            implicitStatements++;
        }
        statements.close();
        return implicitStatements;
    }

    /**
     * Iterates and collects the list of the namespaces, used in URIs in the
     * repository
     */
    public void iterateNamespaces() throws Exception {
        System.out.println("===== Namespace List ==================================");

        System.out.println("Namespaces collected in the repository:");
        RepositoryResult<Namespace> iter = repositoryConnection.getNamespaces();

        while (iter.hasNext()) {
            Namespace namespace = iter.next();
            String prefix = namespace.getPrefix();
            String name = namespace.getName();
            namespacePrefixes.put(name, prefix);
            System.out.println(prefix + ":\t" + name);
        }
        iter.close();
    }

    /**
     * Demonstrates query evaluation. First we parse the query file, it should
     * be named sample-query.serql and be located in the current folder. Each of
     * the queries is executed against the prepared repository. If the
     * printResults is set to true the actual values of the bindings are output
     * to the console. We also count the time for evaluation and the number of
     * results per query and output this information.
     *
     * @throws Exception
     */
    public void evaluateQueries() throws Exception {
        System.out.println("===== Query Evaluation ======================");

        String queryFile = parameters.get(PARAM_QUERY);
        if (queryFile == null) {
            System.out.println("No query file given in parameter '" + PARAM_QUERY + "'.");
            return;
        }

        long startQueries = System.currentTimeMillis();

        // process the query file to get the queries
        String[] queries = collectQueries(queryFile);

        final CountDownLatch numberOfQueriesToProcess = new CountDownLatch(queries.length);
        // evaluate each query and, optionally, print the bindings
        for (int i = 0; i < queries.length; i++) {
            final String name = queries[i].substring(0, queries[i].indexOf(":"));
            final String query = queries[i].substring(name.length() + 2).trim();
            System.out.println("Executing query '" + name + "'");

            // this is done via invoking the respoitory's performTableQuery()
            // method
            // the first argument specifies the query language
            // the second is the actual query string
            // the result is returned in a tabular form with columns, the
            // variables in the projection
            // and each result in a separate row. these are simply enumerated
            // and shown in the console
            if (doMultithreadQueryEvaluation) {
                new Thread() {
                    public void run() {
                        executeSingleQuery(query);
                        numberOfQueriesToProcess.countDown();
                    } // run
                }.start(); // thread

            } else {
                executeSingleQuery(query);
            }
        } // for
        if (doMultithreadQueryEvaluation)
            numberOfQueriesToProcess.await();

        long endQueries = System.currentTimeMillis();
        System.out.println("Queries run in " + (endQueries - startQueries) + " ms.");
    }

    private Query prepareQuery(String query) throws Exception {

        for (QueryLanguage language : queryLanguages) {
            try {
                return repositoryConnection.prepareQuery(language, query);
            } catch (UnsupportedQueryLanguageException e) {
                // Can't use this query language, so try the next one.
            } catch (MalformedQueryException e) {
                // The query is probably not in this language. Try the next language.
            }
        }
        // Can't prepare this query in any language
        return null;
    }

    private static final QueryLanguage[] queryLanguages = new QueryLanguage[]{
            QueryLanguage.SPARQL,
            QueryLanguage.SERQL,
            QueryLanguage.SERQO
    };


    public void executeSingleQuery(String query) {
        try {
//			long start = System.nanoTime();
            Query preparedQuery = prepareQuery(query);
            if (preparedQuery == null) {
                System.out.println("Unable to parse query: " + query);
                return;
            }

            if (preparedQuery instanceof BooleanQuery) {
                System.out.println("Result: " + ((BooleanQuery) preparedQuery).evaluate());
                return;
            }
            ParsedQuery pq = ((SailQuery) preparedQuery).getParsedQuery();
            CloseableIteration<? extends BindingSet, QueryEvaluationException> result = null;
            SailRepositoryConnection sailRepositoryConnecion = (SailRepositoryConnection) repositoryConnection;
            SailConnection sailConnection = sailRepositoryConnecion.getSailConnection();
            long start = System.nanoTime();
            result = sailConnection.evaluate(pq.getTupleExpr(), pq.getDataset(), preparedQuery.getBindings(), true);
            System.out.println("query prepared in " + (System.nanoTime() - start) + "ns");

            long queryBegin = System.nanoTime();

            int rows = 0;
            if (showResults) {
                while (result.hasNext()) {
                    BindingSet tuple = (BindingSet) result.next();
                    if (rows == 0) {
                        for (Iterator<Binding> iter = tuple.iterator(); iter.hasNext();) {
                            System.out.print(iter.next().getName());
                            System.out.print("\t");
                        }
                        System.out.println();
                        System.out.println("---------------------------------------------");
                    }
                    rows++;
                    if (showResults) {
                        for (Iterator<Binding> iter = tuple.iterator(); iter.hasNext();) {
                            try {
                                System.out.print(beautifyRDFValue(iter.next().getValue()) + "\t");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        System.out.println();
                    }
                }
            }
            if (showResults)
                System.out.println();

            result.close();
            long queryEnd = System.nanoTime();
            System.out.println(rows + " result(s) in " + (queryEnd - queryBegin) / 1000000 + "ms.");
            System.out.println();
        } catch (Throwable e) {
            System.out.println("An error occurred during query execution: " + e.getMessage());
        }
    }

    /**
     * Creates a statement and adds it to the repository. Then deletes this
     * statement and checks to make sure it is gone.
     */
    public void uploadAndDeleteStatement() throws Exception {
        System.out.println("===== Upload and Delete Statements ====================");

        // Add a statement directly to the SAIL
        System.out.println("----- Upload and check --------------------------------");
        // first, create the RDF nodes for the statement
        URI subj = repository.getValueFactory().createURI("http://example.org/owlim#Pilot");
        URI pred = RDF.TYPE;
        URI obj = repository.getValueFactory().createURI("http://example.org/owlim#Human");

        repositoryConnection.add(subj, pred, obj);
        repositoryConnection.commit();

        // Now check whether the new statement can be retrieved
        RepositoryResult<Statement> iter = repositoryConnection.getStatements(subj, null, obj, true);
        boolean retrieved = false;
        while (iter.hasNext()) {
            retrieved = true;
            System.out.println(beautifyStatement(iter.next()));
        }
        // CLOSE the iterator to avoid memory leaks
        iter.close();

        if (!retrieved)
            System.out.println("**** Failed to retrieve the statement that was just added. ****");

        // Remove the above statement in a separate transaction
        System.out.println("----- Remove and check --------------------------------");
        repositoryConnection.remove(subj, pred, obj);
        repositoryConnection.commit();

        // Check whether there is some statement matching the subject of the
        // deleted one
        iter = repositoryConnection.getStatements(subj, null, null, true);
        retrieved = false;
        while (iter.hasNext()) {
            retrieved = true;
            System.out.println(beautifyStatement(iter.next()));
        }
        // CLOSE the iterator to avoid memory leaks
        iter.close();
        if (retrieved)
            System.out.println("**** Statement was not deleted properly in last step. ****");
    }

    /**
     * Shutdown the repository and flush unwritten data.
     */
    public void shutdown() {
        System.out.println("===== Shutting down ==========");
        if (repository != null) {
            try {
                repositoryConnection.close();
                repository.shutDown();
                repositoryManager.shutDown();
            } catch (Exception e) {
                System.out.println("An exception occurred during shutdown: " + e.getMessage());
            }
        }
    }

    /**
     * Auxiliary method, printing an RDF value in a "fancy" manner. In case of
     * URI, qnames are printed for better readability
     *
     * @param value The value to beautify
     */
    public String beautifyRDFValue(Value value) throws Exception {
        if (value instanceof URI) {
            URI u = (URI) value;
            String namespace = u.getNamespace();
            String prefix = namespacePrefixes.get(namespace);
            if (prefix == null) {
                prefix = u.getNamespace();
            } else {
                prefix += ":";
            }
            return prefix + u.getLocalName();
        } else {
            return value.toString();
        }
    }

    /**
     * Auxiliary method, nicely format an RDF statement.
     *
     * @param statement The statement to be formatted.
     * @return The beautified statement.
     */
    public String beautifyStatement(Statement statement) throws Exception {
        return beautifyRDFValue(statement.getSubject()) + " " +
                beautifyRDFValue(statement.getPredicate()) + " " +
                beautifyRDFValue(statement.getObject());
    }

    /**
     * Parse the query file and return the queries defined there for further
     * evaluation. The file can contain several queries; each query starts with
     * an id enclosed in square brackets '[' and ']' on a single line; the text
     * in between two query ids is treated as a SeRQL query. Each line starting
     * with a '#' symbol will be considered as a single-line comment and
     * ignored. Query file syntax example:
     * <p/>
     * #some comment [queryid1] <query line1> <query line2> ... <query linen>
     * #some other comment [nextqueryid] <query line1> ... <EOF>
     *
     * @param queryFile
     * @return an array of strings containing the queries. Each string starts
     *         with the query id followed by ':', then the actual query string
     */
    private static String[] collectQueries(String queryFile) throws Exception {
        List<String> queries = new ArrayList<String>();
        BufferedReader inp = new BufferedReader(new FileReader(queryFile));
        String nextLine = null;

        for (; ;) {
            String line = nextLine;
            nextLine = null;
            if (line == null) {
                line = inp.readLine();
            }
            if (line == null) {
                break;
            }
            line = line.trim();
            if (line.length() == 0) {
                continue;
            }
            if (line.startsWith("#")) {
                continue;
            }
            if (line.startsWith("^[") && line.endsWith("]")) {
                StringBuffer buff = new StringBuffer(line.substring(2, line.length() - 1));
                buff.append(": ");

                for (; ;) {
                    line = inp.readLine();
                    if (line == null) {
                        break;
                    }
                    line = line.trim();
                    if (line.length() == 0) {
                        continue;
                    }
                    if (line.startsWith("#")) {
                        continue;
                    }
                    if (line.startsWith("^[")) {
                        nextLine = line;
                        break;
                    }
                    buff.append(line);
                    buff.append(System.getProperty("line.separator"));
                }

                queries.add(buff.toString());
            }
        }

        String[] result = new String[queries.size()];
        for (int i = 0; i < queries.size(); i++) {
            result[i] = queries.get(i);
        }
        return result;
    }

    /**
     * This is the entry point of the example application. First, the command-line
     * parameters are intialised.
     * Then these parameters are passed to an instance of the GettingStarted application
     * and used to create, initialise and login to the local instance of Sesame.
     *
     * @param args Command line parameters
     */
    public static void main(String[] args) {

        // Special handling for JAXP XML parser that limits entity expansion
        // see http://java.sun.com/j2se/1.5.0/docs/guide/xml/jaxp/JAXP-Compatibility_150.html#JAXP_security
        System.setProperty("entityExpansionLimit", "1000000");

        // Parse all the parameters
        Parameters params = new Parameters(args);

        // Set default values for missing parameters
        params.setDefaultValue(PARAM_CONFIG, "./src/main/resources/owlim.ttl");
        params.setDefaultValue(PARAM_SHOWRESULTS, "true");
        params.setDefaultValue(PARAM_UPDATES, "false");
        params.setDefaultValue(PARAM_PRELOAD, "./src/main/resources/preload");
        params.setDefaultValue(PARAM_QUERY, "./queries/sample.sparql");

        GettingStarted gettingStartedApplication = null;
        try {
            long initializationStart = System.currentTimeMillis();
            // The ontologies and datasets specified in the 'import' parameter
            // of the Sesame configuration file are loaded during initialization.
            // Thus, for large datasets the initialisation could take
            // considerable time.
            gettingStartedApplication = new GettingStarted(params.getParameters());

            // Demonstrate the basic operations on a repository
            gettingStartedApplication.loadFiles();
            gettingStartedApplication.showInitializationStatistics(System.currentTimeMillis() - initializationStart);
            gettingStartedApplication.iterateNamespaces();
            // gettingStartedApplication.evaluateQueries();
            if ("true".equals((String) params.getValue(PARAM_UPDATES))) {
                gettingStartedApplication.uploadAndDeleteStatement();
            }
        } catch (Throwable ex) {
            System.out.println("An exception occured at some point during execution:");
            ex.printStackTrace();
        } finally {
            if (gettingStartedApplication != null)
                gettingStartedApplication.shutdown();
        }
    }

    /**
     * Utility to read parameters from a string of name-value pairs
     * or an array of string, each containing a name-value pair.
     */
    static public class Parameters {

        /**
         * Construct the parameters from a string
         *
         * @param allNameValuePairs  A string of the form "param1=name1 param2=name2".
         * @param pairSeparators     A list of characters that separate the name-value pairs, e.g. <space><tab><cr><lf>
         * @param nameValueSeparator The character that separates the name from the values, e.g. '='
         */
        public Parameters(String allNameValuePairs, String pairSeparators, char nameValueSeparator) {
            StringTokenizer tokeniser = new StringTokenizer(allNameValuePairs, pairSeparators);

            int numTokens = tokeniser.countTokens();
            String[] nameValuePairs = new String[numTokens];
            for (int i = 0; i < numTokens; ++i)
                nameValuePairs[i] = tokeniser.nextToken();
            parseNameValuePairs(nameValuePairs, nameValueSeparator, true);
        }

        /**
         * Construct the parameters from an array of name-value pairs, e.g. from "main( String[] args )"
         *
         * @param nameValuePairs The array of name-value pairs
         * @param separator      The character that separates the name from its value
         */
        public Parameters(String[] nameValuePairs, char separator) {
            parseNameValuePairs(nameValuePairs, separator, true);
        }

        /**
         * Construct the parameters from an array of name-value pairs using equals '=' as the separator.
         *
         * @param nameValuePairs The array of name-value pairs
         */
        public Parameters(String[] nameValuePairs) {
            parseNameValuePairs(nameValuePairs, '=', true);
        }

        /**
         * Get the value associated with a parameter.
         *
         * @param name The name of the parameter.
         * @return The value associated with the parameter.
         */
        public String getValue(String name) {
            return mParameters.get(name);
        }

        /**
         * Get the value associated with a parameter or return the given default if it is not available.
         *
         * @param name         The name of the parameter.
         * @param defaultValue The default value to return.
         * @return The value associated with the parameter.
         */
        public String getValue(String name, String defaultValue) {
            String value = getValue(name);

            if (value == null)
                value = defaultValue;

            return value;
        }

        /**
         * Associate the given value with the given parameter name.
         *
         * @param name  The name of the parameter.
         * @param value The value of the parameter.
         */
        public void setValue(String name, String value) {
            mParameters.put(name.trim().toLowerCase(), value);
        }

        /**
         * Set a default value, i.e. set this parameter to have the given value ONLY if it has not already been set.
         *
         * @param name  The name of the parameter.
         * @param value The value of the parameter.
         */
        public void setDefaultValue(String name, String value) {
            if (getValue(name) == null)
                setValue(name, value);
        }

        /**
         * The parse method that accepts an array of name-value pairs.
         *
         * @param nameValuePairs An array of name-value pairs, where each string is of the form: "<name>'separator'<value>"
         * @param separator      The character that separates the name from the value
         * @param overWrite      true if the parsed values should overwrite existing value
         */
        public void parseNameValuePairs(String[] nameValuePairs, char separator, boolean overWrite) {
            for (String pair : nameValuePairs) {
                int pos = pair.indexOf(separator);
                if (pos < 0)
                    throw new IllegalArgumentException("Invalid name-value pair '" + pair + "', expected <name>" + separator + "<value>");
                String name = pair.substring(0, pos).toLowerCase();
                String value = pair.substring(pos + 1);
                if (overWrite)
                    setValue(name, value);
                else
                    setDefaultValue(name, value);
            }
        }

        /**
         * Get the name-value pairs as a Map<String,String>
         *
         * @return
         */
        public Map<String, String> getParameters() {
            return mParameters;
        }

        private final Map<String, String> mParameters = new HashMap<String, String>();
    }

    /**
     * Utility for a depth first traversal of a file-system starting from a given node (file or directory).
     */
    public static class FileWalker {

        /**
         * The call back interface for traversal.
         */
        public interface Handler {
            /**
             * Called to notify that a normal file has been encountered.
             *
             * @param file The file encountered.
             */
            void file(File file) throws Exception;

            /**
             * Called to notify that a directory has been encountered.
             *
             * @param directory The directory encountered.
             */
            void directory(File directory) throws Exception;
        }

        /**
         * Set the notification handler.
         *
         * @param handler The object that receives notifications of encountered nodes.
         */
        public void setHandler(Handler handler) {
            this.handler = handler;
        }

        /**
         * Start the walk at the given location, which can be a file,
         * for a very short walk, or a directory which will be traversed recursively.
         *
         * @param node The starting point for the walk.
         */
        public void walk(File node) throws Exception {
            if (node.isDirectory()) {
                handler.directory(node);
                for (File child : node.listFiles()) {
                    walk(child);
                }
            } else {
                handler.file(node);
            }
        }

        private Handler handler;
    }
}
