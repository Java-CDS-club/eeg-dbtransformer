package dbtransformergui;

import dbtransfromer.DBTransformerImpl;

/**
 * Main class. Execute GUI
 * Last Modified 25.4.2010
 * @author Vaclav Papez
 */
public class Main {
    private static MainForm mainForm;
    private static DBTransformerImpl dbtransformer;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        dbtransformer = new DBTransformerImpl();
        mainForm = new MainForm(dbtransformer);
        mainForm.setVisible(true);
    }

}
