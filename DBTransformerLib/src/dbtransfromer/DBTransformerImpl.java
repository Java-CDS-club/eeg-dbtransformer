package dbtransfromer;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFWriter;
import com.hp.hpl.jena.util.FileManager;

import de.fuberlin.wiwiss.d2rq.ModelD2RQ;
import de.fuberlin.wiwiss.d2rq.map.Database;
import de.fuberlin.wiwiss.d2rq.map.Mapping;
import de.fuberlin.wiwiss.d2rq.mapgen.MappingGenerator;
import de.fuberlin.wiwiss.d2rq.parser.MapParser;
import de.uulm.ecs.ai.owlapi.krssparser.KRSS2OntologyFormat;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import org.coode.owl.krssparser.KRSSOntologyFormat;
import org.coode.owlapi.latex.LatexAxiomsListOntologyFormat;
import org.coode.owlapi.latex.LatexOntologyFormat;
import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntaxOntologyFormat;
import org.coode.owlapi.obo.parser.OBOOntologyFormat;
import org.coode.owlapi.turtle.TurtleOntologyFormat;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLFunctionalSyntaxOntologyFormat;
import org.semanticweb.owlapi.io.OWLXMLOntologyFormat;
import org.semanticweb.owlapi.io.RDFXMLOntologyFormat;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.vocab.PrefixOWLOntologyFormat;
import uk.ac.manchester.cs.owlapi.dlsyntax.DLSyntaxHTMLOntologyFormat;
import uk.ac.manchester.cs.owlapi.dlsyntax.DLSyntaxOntologyFormat;

/**
 * Class implemets BDTransformer interface and provides methods for transformation
 * data from RDB to semantic web resources.
 * Last Modified 17.5.2010
 * @author Vaclav Papez (parts of code was created by Richard Cyganiak as part of D2RQ's dump_rdf.java)
 */
public class DBTransformerImpl implements DBTransformer {

    private final String[] includedDrivers = {
        "com.mysql.jdbc.Driver"
    };
    /**
     * Constant reresents RDF / XML output format
     */
    public final int TYPE_RDFXML = 1;
    /**
     * Constant reresents OWL / XML format
     */
    public final int TYPE_OWLXML = 2;
    /**
     * Constant reresents Turtle format
     */
    public final int TYPE_TURTLE = 3;
    /**
     * Constant reresents DL Syntax format
     */
    public final int TYPE_DLSYNTAX = 4;
    /**
     * Constant reresents OWL Functional syntax
     */
    public final int TYPE_OWLFUNCTIONAL = 5;
    /**
     * Constant reresents DL Syntax HTML format
     */
    public final int TYPE_DLSYNTAXHTML = 6;
    /**
     * Constant reresents KRSS format
     */
    public final int TYPE_KRSS = 7;
    /**
     * Constant reresents KRSS2 format
     */
    public final int TYPE_KRSS2 = 8;
    /**
     * Constant reresents Latex format
     */
    public final int TYPE_LATEX = 9;
    /**
     * Constant reresents Latex Axiom List format
     */
    public final int TYPE_LATEXAXIOMSLIST = 10;
    /**
     * Constant reresents Manchester format
     */
    public final int TYPE_MANCHESTER = 11;
    /**
     * Constant reresents OBO format
     */
    public final int TYPE_OBO = 12;
    /**
     * Constant reresents Prefix OWL format
     */
    public final int TYPE_PREFIXOWL = 13;
    /**
     * Constant reresents unknown format
     */
    public final int TYPE_UNKNOWN = 0;
    
    private String mapping;
    private String base;
    private String format;
    private String output;
    private DBParameters dbparam;

    /**
     * Creates new transformer
     * @param dbparam database connection parameters
     * @param mapping D2RQ mapping
     * @param base base URI
     * @param format output format
     * @param output output path
     */
    public DBTransformerImpl(DBParameters dbparam,String mapping, String base, String format, String output) {
        this.dbparam = dbparam;
        this.mapping = mapping;
        this.base = base;
        this.format = format;
        this.output = output;
    }

    /**
     * Creates new transformer without parameters
     */
    public DBTransformerImpl() {
        dbparam = new DBParameters();
        this.format = "RDF/XML";
        this.output = "";
        this.base = "";
    }

    /**
     * Transform data from RDB into RDF graph
     * @param dbParameters database connection parameters
     * @param baseURI base RDF URI
     * @return RDF graph in byte[]
     * @throws dbtransfromer.DBTransformerImpl.DumpParameterException
     * @throws UnsupportedEncodingException
     * @throws FileNotFoundException
     */
    public byte[] transform(DBParameters dbParameters, String baseURI) throws DumpParameterException, UnsupportedEncodingException, FileNotFoundException {
        
        dbparam = dbParameters;
        for (int i = 0; i < includedDrivers.length; i++) {
            Database.registerJDBCDriverIfPresent(includedDrivers[i]);
        }

        RDFDump dump = new RDFDump();
        dump.setUser(dbparam.getUsername());
        dump.setPassword(dbparam.getPassword());
        dump.setDriverClass(dbparam.getDriver());
        dump.setJDBCURL(dbparam.getJdbc());
        dump.setFetchSize(dbparam.getFetchSize());
        dump.setOutputFile(output);
        dump.setBaseURI(baseURI);
        
        dump.doDump();
        return dump.getByteArray();
    }

    /**
     * Convert RDF into another semantic web resource (OWL / XML)
     * @param type number representation of conversion type
     * @param  byteArray RDF graph in byte[]
     * @return true if convert was successful
     * @throws OWLOntologyCreationException if unexpected error during convertion happened
     * @throws OWLOntologyStorageException if error during saving ontology happened
     */
    public boolean convert(int type, byte[] byteArray) throws OWLOntologyCreationException, OWLOntologyStorageException {
        
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        ByteArrayInputStream bin = new ByteArrayInputStream(byteArray);

        OWLOntology ontology = manager.loadOntologyFromOntologyDocument(bin);

        IRI documentIRIoutput = IRI.create("file:" + output + "");
        switch (type) {
            case TYPE_RDFXML:
                manager.saveOntology(ontology, new RDFXMLOntologyFormat(), documentIRIoutput);
                break;
            case TYPE_OWLXML:
                manager.saveOntology(ontology, new OWLXMLOntologyFormat(), documentIRIoutput);
                break;
            case TYPE_DLSYNTAX:
                manager.saveOntology(ontology, new DLSyntaxOntologyFormat(), documentIRIoutput);
                break;
            case TYPE_TURTLE:
                manager.saveOntology(ontology, new TurtleOntologyFormat(), documentIRIoutput);
                break;
            case TYPE_OWLFUNCTIONAL:
                manager.saveOntology(ontology, new OWLFunctionalSyntaxOntologyFormat(), documentIRIoutput);
                break;
            case TYPE_DLSYNTAXHTML:
                manager.saveOntology(ontology, new DLSyntaxHTMLOntologyFormat(), documentIRIoutput);
                break;
            case TYPE_KRSS:
                manager.saveOntology(ontology, new KRSSOntologyFormat(), documentIRIoutput);
                break;
            case TYPE_KRSS2:
                manager.saveOntology(ontology, new KRSS2OntologyFormat(), documentIRIoutput);
                break;
            case TYPE_LATEX:
                manager.saveOntology(ontology, new LatexOntologyFormat(), documentIRIoutput);
                break;
            case TYPE_LATEXAXIOMSLIST:
                manager.saveOntology(ontology, new LatexAxiomsListOntologyFormat(), documentIRIoutput);
                break;
            case TYPE_MANCHESTER:
                manager.saveOntology(ontology, new ManchesterOWLSyntaxOntologyFormat(), documentIRIoutput);
                break;
            case TYPE_OBO:
                manager.saveOntology(ontology, new OBOOntologyFormat(), documentIRIoutput);
                break;
            case TYPE_PREFIXOWL:
                manager.saveOntology(ontology, new PrefixOWLOntologyFormat(), documentIRIoutput);
                break;
            default:
                manager.saveOntology(ontology, new RDFXMLOntologyFormat(), documentIRIoutput);
        }
        manager.removeOntology(ontology);
        return true;
    }

    /**
     * Returns base URI
     * @return base URI
     */
    public String getBase() {
        return base;
    }

    /**
     * Set base URI
     * @param base base URI
     */
    public void setBase(String base) {
        this.base = base;
    }

    /**
     * returns output format
     * @return output format
     */
    public String getFormat() {
        return format;
    }

    /**
     * set output format
     * @param format output format
     */
    public void setFormat(String format) {
        this.format = format;
    }

    /**
     * returns D2RQ mapping file
     * @return D2RQ mapping file
     */
    public String getMapping() {
        return mapping;
    }

    /**
     * set D2RQ mapping file
     * @param mapping D2RQ mapping file
     */
    public void setMapping(String mapping) {
        this.mapping = mapping;
    }

    /**
     * returns output path
     * @return output path
     */
    public String getOutput() {
        return output;
    }

    /**
     * returns database parameters
     * @return database parameters
     */
    public DBParameters getDbparam() {
        return dbparam;
    }

    /**
     * sets database parameters
     * @param dbparam database parameters
     */
    public void setDbparam(DBParameters dbparam) {
        this.dbparam = dbparam;
    }

    /**
     * set output path
     * @param output output path
     */
    public void setOutput(String output) {
        if (!output.isEmpty()) {
            output = output.substring(Math.max(output.indexOf('/'), output.indexOf('\\')));
            output = output.replace('\\', '/');
        }
        this.output = output;
    }

    /**
     * Class provides transformation from RDB to RDF Jena Graph. Code was mainly taken
     * from original D2RQ dump_rdf.java source file.
     */
    protected class RDFDump {

        private ByteArrayOutputStream bout;
        private String user = null;
        private String password = null;
        private String driverClass = null;
        private String jdbcURL = null;
        private String mapURL = null;
        private String baseURI = null;
        private String format = "N-TRIPLE";
        private String outputFile = null;
        private Integer fetchSize = null;

        /**
         * Creates mapping, RDF model and write RDF model as RDF / XML
         * @throws dbtransformer.DBTransformerImpl.DumpParameterException
         * @throws UnsupportedEncodingException
         * @throws FileNotFoundException
         */
        public void doDump() throws DumpParameterException, UnsupportedEncodingException, FileNotFoundException {
            Model mapModel = makeMapModel();
            Mapping mapping = new MapParser(mapModel, baseURI()).parse();
            Iterator it = mapping.databases().iterator();
            bout = new ByteArrayOutputStream();


            while (it.hasNext()) {
                Database db = (Database) it.next();
                db.setResultSizeLimit(Database.NO_LIMIT);
                if (this.fetchSize != null) {
                    db.setFetchSize(this.fetchSize.intValue());
                } else {
                    if (db.getFetchSize() == Database.NO_FETCH_SIZE) {
                        db.setFetchSize(db.getJDBCDSN() != null && db.getJDBCDSN().contains(":mysql:") ? Integer.MIN_VALUE : dbparam.getFetchSize());
                    }
                }
            }

            Model d2rqModel = new ModelD2RQ(mapping);
            String absoluteBaseURI = MapParser.absolutizeURI(baseURI());
            RDFWriter writer = d2rqModel.getWriter(this.format);
            if (this.format.equals("RDF/XML") || this.format.equals("RDF/XML-ABBREV")) {
                writer.setProperty("showXmlDeclaration", "true");
                if (this.baseURI != null) {
                    writer.setProperty("xmlbase", this.baseURI);
                }
                writer.write(d2rqModel, bout, absoluteBaseURI);
            } else {
                writer.write(d2rqModel, bout, absoluteBaseURI);
            }


            d2rqModel.close();
        }

        /*
         * Returns byte array from output stream
         */
        public byte[] getByteArray() {
            return bout.toByteArray();
        }

        /**
         * Creates new map model which is next used for creation RDF model
         * @return map model
         * @throws dbtransfromer.DBTransformerImpl.DumpParameterException if parameters are invalid
         */
        private Model makeMapModel() throws DumpParameterException {
            if (hasMappingFile()) {
                return FileManager.get().loadModel(this.mapURL, baseURI(), null);
            }
            if (this.jdbcURL == null) {
                throw new DumpParameterException("Must specify either -j or -m parameter");
            }
            MappingGenerator gen = new MappingGenerator(this.jdbcURL);
            if (this.user != null) {
                gen.setDatabaseUser(this.user);
            }
            if (this.password != null) {
                gen.setDatabasePassword(this.password);
            }
            if (this.driverClass != null) {
                gen.setJDBCDriverClass(this.driverClass);
            }
            gen.setMapNamespaceURI("file:tmp#");
            gen.setInstanceNamespaceURI("");
            gen.setVocabNamespaceURI("http://localhost/vocab/");
            return gen.mappingModel(baseURI(), System.err);
        }

        /**
         * Check if is explicitly given mapping file
         * @return true if is explicitly given mapping file
         */
        private boolean hasMappingFile() {
            return this.mapURL != null;
        }

        /**
         * Make base URI. If isn't given explicitly returns default value.
         * @return value of base URI
         */
        private String baseURI() {
            if (this.baseURI != null) {
                return this.baseURI;
            }
            if (this.outputFile != null) {
                return "file:" + this.outputFile + "#";
            }
            return "http://localhost/";
        }

        /**
         * set database user name
         * @param user database user name
         */
        void setUser(String user) {
            this.user = user;
        }

        /**
         * set databse password
         * @param password database password
         */
        void setPassword(String password) {
            this.password = password;
        }

        /**
         * Set JDBC driver string
         * @param driverClass JDBC driver
         */
        void setDriverClass(String driverClass) {
            this.driverClass = driverClass;
        }

        /**
         * set JDBC connection string
         * @param jdbcURL JDBC connection string
         */
        void setJDBCURL(String jdbcURL) {
            this.jdbcURL = jdbcURL;
        }

        /**
         * Set JDBC fetch size
         * @param fetchsize fetch size
         */
        void setFetchSize(Integer fetchSize) {
            this.fetchSize = fetchSize;
        }

        /**
         * set D2RQ mapping file
         * @param mapURL D2RQ mapping file
         */
        void setMapURL(String mapURL) {
            this.mapURL = mapURL;
        }

        /**
         * Set base URI
         * @param baseURI base URI
         */
        void setBaseURI(String baseURI) {
            this.baseURI = baseURI;
        }

        /**
         * set output format
         * @param format output format
         */
        void setFormat(String format) {
            this.format = format;
        }

        /**
         * set output path
         * @param output output path
         */
        void setOutputFile(String outputFile) {
            this.outputFile = outputFile;
        }
    }

    /**
     * Class represents exception thrown in case of invalid parameters.
     */
    public static class DumpParameterException extends Exception {

        DumpParameterException(String message) {
            super(message);
        }
    }
}
