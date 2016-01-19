/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isa.ws.utiles;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.safelayer.trustedx.client.smartwrapper.SmartHeader;
import com.safelayer.trustedx.client.smartwrapper.SmartSignatureResult;

/**
 *
 * @author JMiraballes
 */
public class UtilesSWHelper {
        
    public static final String RESULTMAJOR_SUCCESS = "success";
    public static final String RESULTMAJOR_SUCCESS_FIRMA = "urn:oasis:names:tc:dss:1.0:resultmajor:Success";
    public static final String RESULTMAJOR_SUCCESS_KM = "http://www.w3.org/2002/03/xkms#Success";
    public static final String SERVICE_POLICY_KM = "urn:safelayer:km:politica";
    public static final String SERVICE_POLICY_DS = "urn:safelayer:tws:policies:generation:igdoc";
    public static final String STATUS_VALID_KM = "http://www.w3.org/2002/03/xkms#Valid";
    public static final String RESULTMINOR_SUCCESS_VERIFY = "urn:oasis:names:tc:dss:1.0:resultminor:ValidSignature_OnAllDocuments";
    public static final String RESULTMINOR_SUCCESS_VERIFY_MULTISIGN_PDF = "urn:oasis:names:tc:dss:1.0:resultminor:ValidSignature_RevisionsInSomeDocuments";
    public static final String RESULT_SUCCESS_VERIFY_INDIVIDUAL_SIGNATURE = "urn:oasis:names:tc:dss:1.0:resultminor:ValidSignature_RevisionsAddedAfterSignature";
    
    public static final String OPERATION_OK = "Operci�n Correcta.";
    public static final String OPERACION_FALLIDA = "Operaci�n Fallida.";
    
    //Contraseña y password de usuario consumidor de ws.
    public static final String ADMIN_USUARIO = "super";
    public static final String ADMIN_PASSWORD = "super";
    
    public static final String ADMIN_VERIFY_USUARIO = "trustedx";
    public static final String ADMIN_VERIFY_PASSWORD = "trustedx";    
    
    public static DateFormat DATE_TRUSTEDX = new SimpleDateFormat("yyyy-MM-dd");
    public static DateFormat DATE_SIMPLE = new SimpleDateFormat("dd-MM-yyyy");
    
    private static URL codeBase;    
    
    public static void setCodeBase( URL codebase ){
        codeBase = codebase;
    }
    
    public static URL getCodeBase(){
        return codeBase;
    }
    
    public static boolean isNullOrEmpty(String str){
        return (str == null || str.isEmpty());
    }
    
    
    /**
     * Crea la cabecera del servicio de smartwrapper por usuario y contraseña.
     * @param usuario
     * @param password
     * @return SmartHeader
     * @throws java.lang.Exception 
    **/
    public static SmartHeader crearSmartHeader(String usuario, String password) throws Exception{
        
        SmartHeader smartHeader = new SmartHeader();
        smartHeader.setUsername( usuario );
        smartHeader.setPassword( password );
        
        return smartHeader;
    }
   
    
    /**
     * 
     * @param artifact
     * @return 
     * @throws java.lang.Exception
     * Obtiene la aserción SMAL
     */
    public static SmartHeader crearSmartHeader( String artifact ) throws Exception{
        SmartHeader header = new SmartHeader(); 
        header.setAssertionArtifact(artifact);
        return header;
    }
    
    public static String getURLTrustedX() throws IOException{
        return UtilesResources.getProperty("swHelperConfig.trustexURL");
    }
    
    public static String getAdminUsuario() throws IOException{
        
        if (isAdminFromProperties()){
            return UtilesResources.getProperty("swHelperConfig.adminUser");
        }
        else{
            return ADMIN_USUARIO;
        }
    }
    
    public static String getAdminPassword() throws IOException{
        
        if (isAdminFromProperties()){
            return UtilesResources.getProperty("swHelperConfig.adminPass");
        }
        else{
            return ADMIN_PASSWORD;
        }
    }    public static String getUserVerify() throws IOException{
        
        if (isAdminFromProperties()){
            return UtilesResources.getProperty("swHelperConfig.userVerify");
        }
        else{
            return ADMIN_VERIFY_USUARIO;
        }
    }
    
    public static String getPasswdVerify() throws IOException{
        
        if (isAdminFromProperties()){
            return UtilesResources.getProperty("swHelperConfig.passVerify");
        }
        else{
            return ADMIN_VERIFY_PASSWORD;
        }
    }
    
    
    
    public static boolean isAdminFromProperties() throws IOException{
        String adminFromProperties = UtilesResources.getProperty("swHelperConfig.adminFromProperties");
        return adminFromProperties.equals("true");
    }
    
    
    /**
     * Función para obtener el nombre identificado por CN= 
     * @return String
     * @param nombre
     */
    public static String getCN(String nombre){
        String[] arreglo;
        arreglo = nombre.split(",");
        for (int i = 0; i < arreglo.length; i++){
            if(arreglo[i].startsWith(" CN=")||arreglo[i].startsWith("CN=")){
                if(arreglo[i].startsWith(" CN="))
                    return arreglo[i].replace(" CN=", "");
                else
                    return arreglo[i].replace("CN=", "");
            }
        }
        return "";
    }   
    
    public static String getO(String nombre){
        String[] arreglo;
        arreglo = nombre.split(",");
        for (int i = 0; i < arreglo.length; i++){
            if(arreglo[i].startsWith(" O=")||arreglo[i].startsWith("O=")){
                if(arreglo[i].startsWith(" O="))
                    return arreglo[i].replace(" O=", "");
                else
                    return arreglo[i].replace("O=", "");
            }
        }
        return "";
    }   
    
    
    public static String getOU(String nombre){
        String[] arreglo;
        arreglo = nombre.split(",");
        for (int i = 0; i < arreglo.length; i++){
            if(arreglo[i].startsWith(" OU=")||arreglo[i].startsWith("OU=")){
                if(arreglo[i].startsWith(" OU="))
                    return arreglo[i].replace(" OU=", "");
                else
                    return arreglo[i].replace("OU=", "");
            }
        }
        return "";
    }     
    
    /**
     * Converts a byte array to a X509Certificate instance.
     * @param bytes the byte array
     * @return a X509Certificate instance
     * @throws CertificateException if the conversion fails
     */
    public static X509Certificate fromByteArrayToX509Certificate(byte[] bytes) throws CertificateException {
        CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
        InputStream in = new ByteArrayInputStream(bytes);
        X509Certificate cert = (X509Certificate) certFactory.generateCertificate(in);
        return cert;
    }  
    
    public static byte[] convertBase64ToBytes( String base64){
    	 return Base64.decodeBase64(base64.getBytes());
    }
    
    public static String convertBase64ToHexa ( String base64 ){
    	byte[] decodeBase64 = convertBase64ToBytes(base64);
    	char[] decodedHex = Hex.encodeHex(decodeBase64, false);
    	return new String(decodedHex);
    }
    
    public static boolean verifySmartSignature (SmartSignatureResult signature){
        return (UtilesSWHelper.RESULTMAJOR_SUCCESS_FIRMA.equals(signature.getResultMajor()) && 
                (UtilesSWHelper.RESULTMINOR_SUCCESS_VERIFY.equals(signature.getResultMinor()) ||
                UtilesSWHelper.RESULT_SUCCESS_VERIFY_INDIVIDUAL_SIGNATURE.equals(signature.getResultMinor())));
    }
    
	public static String getNodeValue( String xml, String node){
		//get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		try {

			//Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			//parse using builder to get DOM representation of the XML file
			InputStream is = new ByteArrayInputStream( xml.getBytes() );
			Document dom = db.parse(is);
			NodeList nodelist =  dom.getElementsByTagName(node);
			Node node1 = nodelist.item(0);
			String value = null;
			if (node1.getFirstChild() != null){
				if (node.equals("css:validity")){
					value = "";
					value += node1.getChildNodes().item(0).getFirstChild().getNodeValue();
					value += ",";
					value += node1.getChildNodes().item(1).getFirstChild().getNodeValue();
				}
				else value = node1.getFirstChild().getNodeValue();
			}
			return value;
			
		}
		catch(ParserConfigurationException pce) {
			pce.printStackTrace();
			return null;
		}
		catch(SAXException se) {
			se.printStackTrace();
			return null;
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
			return null;
		}
	}    
    
}
