package com.config;

import java.util.ResourceBundle;

public final class Configurator {
    private String file;
    private static Configurator configurator;
    
    private Configurator() {        
    }
    
    public static Configurator getInstance() {
        if(configurator == null)
            configurator = new Configurator();
        
        return configurator;
    }
    
    public void setFile(String fileName) {
        file = "com/config/" + fileName;
    }
    
    public String getFile() {
        return file;
    }
    
    public String getProperty(String property) {
        return ResourceBundle.getBundle(Configurator.getInstance().getFile()).getString("engClientName");
    }
}