package over.config;

import java.util.ResourceBundle;

/**
 * <code>Configurator</code>.
 *
 * @author Overload Inc.
 * @version 1.0, 03 Jun 2020
 */
public final class Configurator {
    private String file;
    private final String PATH = "over/config/";
    private static Configurator configurator;

    /**
     *
     */
    private Configurator() {        
    }

    /**
     *
     * @return
     */
    public static Configurator getInstance() {
        if(configurator == null)
            configurator = new Configurator();
        
        return configurator;
    }

    /**
     *
     * @param fileName
     */
    public void setFile(String fileName) {
        file = fileName;
    }

    /**
     *
     * @return
     */
    public String getFile() {
        return file;
    }

    /**
     *
     * @param property
     * @return
     */
    public String getProperty(String property) {
        return ResourceBundle.getBundle(PATH + file).getString(property);
    }
}