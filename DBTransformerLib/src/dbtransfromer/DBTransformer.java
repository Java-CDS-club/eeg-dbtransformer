package dbtransfromer;

import dbtransfromer.DBTransformerImpl.DumpParameterException;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

/**
 * The interface to be satisfied by implementations transforming data from RDB into semantic web resources (RDF, OWL).
 * Last Modified 18.5.2010
 * @author Vaclav Papez
 */
public interface DBTransformer {

    /**
     * Convert RDF into another semantic web resource (OWL / XML)
     * @param rdfByteField RDF graph in byte[]
     * @return true if convert was successful
     * @throws OWLOntologyCreationException if unexpected error during convertion happened
     * @throws OWLOntologyStorageException if error during saving ontology happened
     */
    public boolean convert(int outputFormat, byte[] rdfByteField) throws OWLOntologyCreationException, OWLOntologyStorageException;

    /**
     * Transform data from RDB into RDF graph
     * @param dbParameters database connection parameters
     * @param baseURI base RDF URI
     * @return RDF graph in byte[]
     * @throws dbtransfromer.DBTransformerImpl.DumpParameterException
     * @throws UnsupportedEncodingException
     * @throws FileNotFoundException
     */
    public byte[] transform(DBParameters dbParameters, String baseURI) throws DumpParameterException, UnsupportedEncodingException, FileNotFoundException;
}
