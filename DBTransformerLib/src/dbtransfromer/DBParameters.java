/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dbtransfromer;

/**
 * Instance of this class provides parameters for connection to database
 * Last Modified 27.5.2010
 * @author Vaclav Papez
 */
public class DBParameters {
    private final int DEFAULT_FETCH_SIZE = 500;
    private String driver;
    private String jdbc;
    private String username;
    private String password;
    private int fetchSize;

    /**
     * Empty constructor, every parameter needs to be declare explicitly by setter.
     */
    public DBParameters() {
    }

    /**
     * For parameter fetchSize is set default value (500)
     * @param driver driver for connection to database
     * @param jdbc jdbc connection string for particular database
     * @param username username for access to database
     * @param password password for access to database
     */
    public DBParameters(String driver, String jdbc, String username, String password){
        this.driver = driver;
        this.jdbc = jdbc;
        this.username = username;
        this.password = password;
        this.fetchSize = DEFAULT_FETCH_SIZE;
    }

    /**
     * All parameters are initilized by constructor
     * @param driver driver for connection to database
     * @param jdbc jdbc connection string for particular database
     * @param username username for access to database
     * @param password password for access to database
     * @param fetchSize fetch size for JDBC connection
     */
    public DBParameters(String driver, String jdbc, String username, String password, int fetchSize){
        this.driver = driver;
        this.jdbc = jdbc;
        this.username = username;
        this.password = password;
        this.fetchSize = fetchSize;
    }

    /**
     * Returns drivers string
     * @return string of used driver
     */
    public String getDriver() {
        return driver;
    }

    /**
     * Sets driver for connection to database
     * @param driver driver for connection to database
     */
    public void setDriver(String driver) {
        this.driver = driver;
    }

     /**
     * returns JDBC fetch size
     * @return JDBC fetch size
     */
    public int getFetchSize() {
        return fetchSize;
    }

    /**
     * Set JDBC fetch size
     * @param fetchSize fetch size
     */
    public void setFetchSize(int fetchSize) {
        this.fetchSize = fetchSize;
    }

    /**
     * returns JDBC connection string
     * @return JDBC connection string
     */
    public String getJdbc() {
        return jdbc;
    }

    /**
     * set JDBC connection string
     * @param jdbc JDBC connection string
     */
    public void setJdbc(String jdbc) {
        this.jdbc = jdbc;
    }

    /**
     * returns database password
     * @return database password
     */
    public String getPassword() {
        return password;
    }

    /**
     * set databse password
     * @param password database password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * returns database user name
     * @return databse user name
     */
    public String getUsername() {
        return username;
    }

    /**
     * set database user name
     * @param username database user name
     */
    public void setUsername(String username) {
        this.username = username;
    }
}
