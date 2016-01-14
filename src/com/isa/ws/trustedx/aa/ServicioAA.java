/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isa.ws.trustedx.aa;

import org.apache.axis.AxisFault;
import org.apache.log4j.Logger;

import com.isa.ws.exceptions.WService_TXException;
import com.isa.ws.utiles.UtilesMsg;
import com.isa.ws.utiles.UtilesResources;
import com.isa.ws.utiles.UtilesSWHelper;
import com.safelayer.trustedx.client.smartwrapper.Constants;
import com.safelayer.trustedx.client.smartwrapper.SmartAttributeQueryRequest;
import com.safelayer.trustedx.client.smartwrapper.SmartAttributeQueryResponse;
import com.safelayer.trustedx.client.smartwrapper.SmartAuthNRequest;
import com.safelayer.trustedx.client.smartwrapper.SmartAuthNResponse;
import com.safelayer.trustedx.client.smartwrapper.SmartHeader;
import com.safelayer.trustedx.client.smartwrapper.SmartLogoutRequest;
import com.safelayer.trustedx.client.smartwrapper.SmartLogoutResponse;

/**
 *
 * @author JMiraballes
 */

public class ServicioAA {
    
    public static final String LOGIN_PARAM_METHOD = "urn:oasis:names:tc:SAML:1.0:am:password:clear";
    public static final String LOGIN_PARAM_POLICIY = "swHelperConfig.loginPolicy";
    public static final String LOGIN_USER_ADMIN = "swHelperConfig.adminUser";
    public static final String LOGIN_PASS_ADMIN = "swHelperConfig.adminPass";
    public static final String LOGIN_PARA_REQUEST_TYPE = "direct";
    
    private static Logger logger = Logger.getLogger(ServicioAA.class);
    
    /**
     * Login de un usuario en trustedX
     * @param user
     * @param password
     * @return 
     * @throws com.isa.WService_TXException.exceptions.SWException 
     */

    public String login( String user, String password ) throws WService_TXException {
        try{
            SmartAuthNRequest aReq = new SmartAuthNRequest(UtilesSWHelper.getURLTrustedX() );
            aReq.setHeader( UtilesSWHelper.crearSmartHeader(user, password));
            aReq.setEntityUsername(user);
            aReq.setUsernameTokenUsername( user );
            aReq.setUsernameTokenPassword( password );
            aReq.setPolicy( UtilesResources.getProperty(LOGIN_PARAM_POLICIY));
            aReq.setMethod( LOGIN_PARAM_METHOD );
            aReq.setRequestType( LOGIN_PARA_REQUEST_TYPE );  
           
            aReq.setRespondWith( Constants.AA.RespondWith.ASSERTIONARTIFACT );
            SmartAuthNResponse aResp = aReq.send();
            String artifact = aResp.getAssertionArtifact();
            System.out.println("Login: " + artifact);
            System.out.println("Usuario: " + user);
            return artifact;
        }
        catch(AxisFault ex){
            if (UtilesMsg.TX_ERROR_DE_AUTENTICACION.equals(ex.getMessage())){
                ex.printStackTrace();
                throw new WService_TXException(UtilesMsg.ERROR_AUTENTICACION_MSJ, ex.getMessage(), ex.getCause());
            }
            ex.printStackTrace();
            throw new WService_TXException(UtilesMsg.ERROR_WS, ex.getMessage(), ex.getCause());
        }         
        catch(Exception ex){
            ex.printStackTrace();
            logger.error(UtilesMsg.ERROR_AUTH, ex);
            throw new WService_TXException(UtilesMsg.ERROR_AUTH, ex.getMessage(), ex.getCause()); 
        }            
    }    
    
    
    /**
     * Logout de usuario en TrustedX
     * @param usuario
     * @param assertion
     * @throws com.isa.WService_TXException.exceptions.SWException
     */

    public void logut( String usuario, String assertion ) throws WService_TXException{
        try{
            System.out.println("Logout: " + assertion);
            System.out.println("Usuario: " + usuario);
            SmartLogoutRequest iReq = new SmartLogoutRequest( UtilesSWHelper.getURLTrustedX() );
            iReq.setHeader( UtilesSWHelper.crearSmartHeader(assertion) );
            iReq.setEntityUsername(usuario);
            iReq.setAssertionArtifact(assertion);
            
            SmartLogoutResponse iRes = iReq.send();
            if (UtilesSWHelper.RESULTMAJOR_SUCCESS.equals(iRes.getResult())){
                System.out.println("Servicio::logout: " + UtilesSWHelper.OPERATION_OK);    	
            }
            else{
                System.out.println("Servicio::logout: " + UtilesSWHelper.OPERACION_FALLIDA);
                throw new WService_TXException(UtilesMsg.ERROR_SERVICIO_AA_LOGOUT); 
            }
        }
        catch(AxisFault ex){
            if (UtilesMsg.TX_ERROR_DE_AUTENTICACION.equals(ex.getMessage())){
                ex.printStackTrace();
                throw new WService_TXException(UtilesMsg.ERROR_AUTENTICACION_MSJ, ex.getMessage(), ex.getCause());             
            }
            ex.printStackTrace();
            throw new WService_TXException(UtilesMsg.ERROR_WS, ex.getMessage(), ex.getCause());
        }         
        catch(Exception ex){
            ex.printStackTrace();
            logger.error(UtilesMsg.ERROR_AUTH, ex);
            throw new WService_TXException(UtilesMsg.ERROR_AUTH, ex.getMessage(), ex.getCause());
        }
    } 
    
    public String queryServiceAA( String usuario, String password ) throws WService_TXException{
        try{
            SmartAttributeQueryRequest aqReq = new SmartAttributeQueryRequest(UtilesSWHelper.getURLTrustedX() ); 
            SmartHeader header = UtilesSWHelper.crearSmartHeader( usuario, password );
            aqReq.setHeader(header);    
            aqReq.setSubjectUsername(usuario);
            
            SmartAttributeQueryResponse aqRes = aqReq.send(); 
            if ( "Success".equals(aqRes.getStatus()) ){ 
                System.out.println("Operation processed correctly.");
                System.out.println("Assertion attribute retrieved: "+ aqRes.getAssertionAttribute()); 
                return aqRes.getAssertionAttribute();
            }
            else
            {
                System.out.println("Error");
                return "NULL";
            }
        }
        catch(AxisFault ex){
            if (UtilesMsg.TX_ERROR_DE_AUTENTICACION.equals(ex.getMessage())){
                ex.printStackTrace();
                throw new WService_TXException(UtilesMsg.ERROR_AUTENTICACION_MSJ, ex.getMessage(), ex.getCause());             
            }
            ex.printStackTrace();
            throw new WService_TXException(UtilesMsg.ERROR_WS, ex.getMessage(), ex.getCause());
        }         
        catch(Exception ex){
            ex.printStackTrace();
            logger.error(UtilesMsg.ERROR_AUTH, ex);
            throw new WService_TXException(UtilesMsg.ERROR_AUTH, ex.getMessage(), ex.getCause());
        }
    }
    
    
}
