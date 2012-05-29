package settingtools;

import java.io.*;
import javax.xml.stream.*;

/**
 * Instance of this class provides methods for reading settings from setting XML file
 * Using StAX method
 * Last Modified 17.5.2010
 * @author Vaclav Papez
 */
public class XMLReader {

    private String path;
    private String jdbc;
    private String user;
    private String driver;
    private String outputPath;
    private String baseUri;
    private int format;

    /**
     * Creates new reader
     * @param path path of input file
     */
    public XMLReader(String path) {
        this.path = path;
    }

    /**
     * Reading paramters from XML structured file
     * @return true if success
     * @throws FileNotFoundException If input path is invalid
     * @throws XMLStreamException If unexpected error during reading happened
     */
    public boolean parse() throws FileNotFoundException, XMLStreamException {
        XMLInputFactory f = XMLInputFactory.newInstance();
        XMLStreamReader r = f.createXMLStreamReader(new FileReader(path));

        while (r.hasNext()) {
            r.next();
            if (r.isStartElement() == true) {

                if (r.getLocalName().equals("jdbc") == true) {
                    jdbc = r.getElementText().trim();
                } else if (r.getLocalName().equals("user") == true) {
                    user = r.getElementText().trim();
                } else if (r.getLocalName().equals("driver") == true) {
                    driver = r.getElementText().trim();
                } else if (r.getLocalName().equals("outputPath") == true) {
                    outputPath = r.getElementText().trim();
                } else if (r.getLocalName().equals("baseUri") == true) {
                    baseUri = r.getElementText().trim();
                } else if (r.getLocalName().equals("format") == true) {
                    format = Integer.parseInt(r.getElementText().trim());
                }
            }
        }
        return true;
    }

    /**
     * Get base URI attribute
     * @return baseUri base URI
     */
    public String getBaseUri() {
        return baseUri;
    }

    /**
     * Get JDBC driver attribute
     * @return driver JDBC driver attribute
     */
    public String getDriver() {
        return driver;
    }

    /**
     * Get ontology output format atrribute
     * @return format index of selected format
     */
    public int getFormat() {
        return format;
    }

    /**
     * Get JDBC connection string attribute
     * @return jdbc JDBC connection string attribute
     */
    public String getJdbc() {
        return jdbc;
    }

    /**
     * Get ontology output path string attribute
     * @return outputPath ontology output path
     */
    public String getOutputPath() {
        return outputPath;
    }

    /**
     * Get databse user name attribute
     * @return user databse user name
     */
    public String getUser() {
        return user;
    }
}