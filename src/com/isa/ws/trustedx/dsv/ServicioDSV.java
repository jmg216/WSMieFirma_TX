package com.isa.ws.trustedx.dsv;

import java.io.IOException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.isa.ws.entities.Signature;
import com.isa.ws.entities.VerifyResponse;
import com.isa.ws.exceptions.WService_TXException;
import com.isa.ws.utiles.UtilesMsg;
import com.isa.ws.utiles.UtilesResources;
import com.isa.ws.utiles.UtilesSWHelper;
import com.safelayer.trustedx.client.smartwrapper.Constants;
import com.safelayer.trustedx.client.smartwrapper.SmartSignatureResult;
import com.safelayer.trustedx.client.smartwrapper.SmartVerifyRequest;
import com.safelayer.trustedx.client.smartwrapper.SmartVerifyResponse;


public class ServicioDSV {


	/**
	 * Servicio de verificación, con autenticación implícita.
	 * */
	public VerifyResponse verifySignedPdf (String dataSigned) throws WService_TXException{
        try{
            SmartVerifyRequest iReq = new SmartVerifyRequest( UtilesSWHelper.getURLTrustedX() );
            iReq.setHeader( UtilesSWHelper.crearSmartHeader( UtilesSWHelper.getAdminUsuario(), UtilesSWHelper.getAdminPassword() ) );
            iReq.setProfile(Constants.Profile.PDF);
            iReq.setInputPdfBase64Data(dataSigned);

            SmartVerifyResponse iResp = iReq.send();
            
            VerifyResponse verifyResponse = new VerifyResponse();
            
            if (UtilesSWHelper.RESULTMAJOR_SUCCESS_FIRMA.equals(iResp.getResultMajor()) && 
                    (UtilesSWHelper.RESULTMINOR_SUCCESS_VERIFY.equals(iResp.getResultMinor()) ||
                    UtilesSWHelper.RESULTMINOR_SUCCESS_VERIFY_MULTISIGN_PDF.equals(iResp.getResultMinor()))){
            	System.out.println("FIRMAS VALIDAS");
            	verifyResponse.setSignatures( getSignatures(iResp));
            	verifyResponse.setValida(true);
            }
            else{
            	System.out.println("FIRMAS NO VALIDAS");
            	verifyResponse.setValida(false);
            	verifyResponse.setSignatures( getSignatures(iResp));
            }
            return verifyResponse;
        }  
        catch(IOException ex){
            Logger.getLogger(ServicioDSV.class.getName()).log(Level.ERROR, null, ex);
            throw new WService_TXException("Error accediendo a archivos de configuración.", ex.getMessage(), ex.getCause());	
        }
        catch(Exception ex){
            Logger.getLogger(ServicioDSV.class.getName()).log(Level.ERROR, null, ex);
            throw new WService_TXException(UtilesMsg.ERROR_VALIDAR_FIRMA, ex.getMessage(), ex.getCause());                         
        } 		
	}
	
	/**
	 * Servicio de verificación, con autenticación explícita.
	 * 
	 * */
    public VerifyResponse verifySignedPdf(String artifact, String dataSigned) throws WService_TXException {
        try{
            SmartVerifyRequest iReq = new SmartVerifyRequest( UtilesSWHelper.getURLTrustedX() );
            iReq.setHeader( UtilesSWHelper.crearSmartHeader( artifact ));
            iReq.setProfile(Constants.Profile.PDF);
            iReq.setInputPdfBase64Data(dataSigned);
            iReq.setAddCertificateValues(UtilesResources.getProperty(UtilesResources.PROP_WS_CERT_VALUES));
            iReq.setAddRevocationValues(UtilesResources.getProperty(UtilesResources.PROP_WS_CERT_VALUES));
            iReq.setAddChainCertificateValues(UtilesResources.getProperty(UtilesResources.PROP_WS_CERT_VALUES));
            iReq.setAddChainRevocationValues(UtilesResources.getProperty(UtilesResources.PROP_WS_CERT_VALUES));
            SmartVerifyResponse iResp = iReq.send();
            
            VerifyResponse verifyResponse = new VerifyResponse();
            
            if (UtilesSWHelper.RESULTMAJOR_SUCCESS_FIRMA.equals(iResp.getResultMajor()) && 
                    (UtilesSWHelper.RESULTMINOR_SUCCESS_VERIFY.equals(iResp.getResultMinor()) ||
                    UtilesSWHelper.RESULTMINOR_SUCCESS_VERIFY_MULTISIGN_PDF.equals(iResp.getResultMinor()))){
            	System.out.println("FIRMAS VALIDAS");
            	verifyResponse.setSignatures( getSignatures(iResp));
            	verifyResponse.setValida(true);
            }
            else{
            	System.out.println("FIRMAS NO VALIDAS");
            	verifyResponse.setValida(false);
            	verifyResponse.setSignatures( getSignatures(iResp));
            }
            return verifyResponse;
        }  
        catch(Exception ex){
            Logger.getLogger(ServicioDSV.class.getName()).log(Level.ERROR, null, ex);
            throw new WService_TXException(UtilesMsg.ERROR_VALIDAR_FIRMA, ex.getMessage(), ex.getCause());                         
        } 
    }
    
    
    private Signature[] getSignatures( SmartVerifyResponse iresp ) throws Exception{
    	
    	Signature[] arrsing = new Signature[iresp.getNumberSignatures()]; 
    	
    	for (int i = 0; i < iresp.getNumberSignatures(); i++){
    		SmartSignatureResult signatureResponse = iresp.getSignature(i);
    		Signature signature = new Signature();
    		if (signatureResponse.getSigningTime() != null) signature.setFecha( signatureResponse.getSigningTime() );
    		
    		if (signatureResponse.getSignerIdentity() != null) signature.setCn(UtilesSWHelper.getCN(signatureResponse.getSignerIdentity()));
    		
    		if (signatureResponse.getSignerRole() != null) signature.setRol(signatureResponse.getSignerRole());

    		if (signatureResponse.getProductionPlace() != null) signature.setUbicacion(signatureResponse.getProductionPlace());
    		
    		if (signatureResponse.getPdfSignatureInfo() != null) signature.setMotivo(signatureResponse.getPdfSignatureInfo());
    		
    		signature.setVerify(UtilesSWHelper.verifySmartSignature(signatureResponse));
    		
    		arrsing[i] = signature;
    	}
    	return arrsing;
    }
    
    
}
