/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isa.ws.utiles;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author JMiraballes
 * 
 * Clase que encapsula el mecanismo de acceso a un archivo de properties que
 * se encuentra en la la web donde se embebe el applet.
 * 
 */
public class UtilesResources {
    
    public static final String PROP_WS_ENDPOINT = "appletConfig.WSEndpoint";	
	public static final String PROP_WS_CERT_VALUES = "appletConfig.CertificateValues";
    
    private static UtilesResources instance;
    private Properties appProperties = null;
    
    private UtilesResources() throws IOException{
        try{
            appProperties = new Properties();
            InputStream is = this.getClass().getResourceAsStream("/swHelper.properties");
            appProperties.load(( is ));
            
        }
        catch(IOException ex){
            Logger.getLogger(UtilesResources.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;            
        }
    }
    
    private static UtilesResources getInstance() throws IOException{
        if (instance == null){
            instance = new UtilesResources();
        }
        return instance;
    }
     
    public static String getProperty(String key) throws IOException{
        return getInstance().getProperties().getProperty(key);
    }
    
    private Properties getProperties(){
        return this.appProperties;
    }
}
