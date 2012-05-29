package settingtools;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * Instance of this class provides methods for saving settings into setting XML file
 * Using StAX method
 * Last Modified 27.5.2010
 * @author Vaclav Papez
 */
public class XMLWriter {

    private XMLOutputFactory f;
    private XMLStreamWriter w;
    private String path;
    private String encoding = "utf-8";
    private String jdbc;
    private String user;
    private String driver;
    private String outputPath;
    private String baseUri;
    private int format;

    /**
     * Creates new writer.
     * @param path Output file path
     * @param jdbc jdbc connection string attribute
     * @param user databse user attribute
     * @param driver database driver attribute
     * @param outputPath ontology output path attribute
     * @param baseUri base URI attribute
     * @param format index of selected format attribute
     */
    public XMLWriter(String path, String jdbc, String user, String driver, String outputPath, String baseUri, int format) {
        this.path = path;
        this.jdbc = jdbc;
        this.user = user;
        this.driver = driver;
        this.outputPath = outputPath;
        this.baseUri = baseUri;
        this.format = format;
    }

    /**
     * Creates new writer without parameters.
     * @param path Output file path
     */
    public XMLWriter(String path) {
        this.path = path;
    }

    /**
     * Writes settings into XML structured file
     * @return true if success
     * @throws FileNotFoundException If output path is invalid
     * @throws XMLStreamException If unexpected error during writing happened
     */
    public boolean save() throws FileNotFoundException, XMLStreamException {

        f = XMLOutputFactory.newInstance();
        w = f.createXMLStreamWriter(new FileOutputStream(path), encoding);

        w.writeStartDocument(encoding, "1.0");
        w.writeCharacters("\r\n");
        w.writeComment("DBTransformer Settings");
        w.writeCharacters("\r\n");
        w.writeStartElement("Setting");
        w.writeCharacters("\r\n");

        w.writeStartElement("jdbc");
        w.writeCharacters("\r\n");
        w.writeCharacters(jdbc);
        w.writeCharacters("\r\n");
        w.writeEndElement();
        w.writeCharacters("\r\n");

        w.writeStartElement("user");
        w.writeCharacters("\r\n");
        w.writeCharacters(user);
        w.writeCharacters("\r\n");
        w.writeEndElement();
        w.writeCharacters("\r\n");

        w.writeStartElement("driver");
        w.writeCharacters("\r\n");
        w.writeCharacters(driver);
        w.writeCharacters("\r\n");
        w.writeEndElement();
        w.writeCharacters("\r\n");

        w.writeStartElement("outputPath");
        w.writeCharacters("\r\n");
        w.writeCharacters(outputPath);
        w.writeCharacters("\r\n");
        w.writeEndElement();
        w.writeCharacters("\r\n");

        w.writeStartElement("baseUri");
        w.writeCharacters("\r\n");
        w.writeCharacters(baseUri);
        w.writeCharacters("\r\n");
        w.writeEndElement();
        w.writeCharacters("\r\n");

        w.writeStartElement("format");
        w.writeCharacters("\r\n");
        w.writeCharacters(Integer.toString(format));
        w.writeCharacters("\r\n");
        w.writeEndElement();
        w.writeCharacters("\r\n");

        w.writeEndElement();
        w.writeEndDocument();

        return true;
    }

    /**
     * Closing writer and opened file
     * @return true if success
     * @throws XMLStreamException if unexpected closing operation happend
     */
    public boolean close() throws XMLStreamException {
        w.close();
        return true;
    }

    /**
     * Set base URI attribute
     * @param baseUri base URI
     */
    public void setBaseUri(String baseUri) {
        this.baseUri = baseUri;
    }

    /**
     * Set JDBC driver attribute
     * @param driver JDBC driver attribute
     */
    public void setDriver(String driver) {
        this.driver = driver;
    }

    /**
     * Set encoding of output XML file. Default is "utf-8"
     * @param encoding encoding String (like "utf-8" or "windows-1250")
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
     * Set ontology output format atrribute
     * @param format index of selected format
     */
    public void setFormat(int format) {
        this.format = format;
    }

    /**
     * Set JDBC connection string attribute
     * @param jdbc JDBC connection string attribute
     */
    public void setJdbc(String jdbc) {
        this.jdbc = jdbc;
    }

    /**
     * Set ontology output path string attribute
     * @param outputPath ontology output path
     */
    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    /**
     * Set output path of setting XML file
     * @param path ouput path of setting XML file
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Set databse user name attribute
     * @param user databse user name
     */
    public void setUser(String user) {
        this.user = user;
    }
}
