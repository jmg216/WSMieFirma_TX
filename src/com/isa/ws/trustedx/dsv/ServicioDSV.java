package com.isa.ws.trustedx.dsv;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.isa.ws.entities.Certificate;
import com.isa.ws.entities.Signature;
import com.isa.ws.entities.VerifyResponse;
import com.isa.ws.exceptions.WService_TXException;
import com.isa.ws.utiles.UtilesMsg;
import com.isa.ws.utiles.UtilesResources;
import com.isa.ws.utiles.UtilesSWHelper;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfString;
import com.safelayer.trustedx.client.smartwrapper.Constants;
import com.safelayer.trustedx.client.smartwrapper.SmartSignatureResult;
import com.safelayer.trustedx.client.smartwrapper.SmartVerifyRequest;
import com.safelayer.trustedx.client.smartwrapper.SmartVerifyResponse;


public class ServicioDSV {

	

	/**
	 * Servicio de verificación, con autenticación implícita.
	 * */
	public VerifyResponse verifySignedPdf (String dataSigned) throws WService_TXException{
		VerifyResponse verifyResponse = new VerifyResponse();
		
        try{
        	boolean IS_TEST = UtilesResources.getProperty("swHelperConfig.DummyServices").equals("true");     	
        	
        	if (!IS_TEST){
	            SmartVerifyRequest iReq = new SmartVerifyRequest( UtilesSWHelper.getURLTrustedX() );
	            iReq.setHeader( UtilesSWHelper.crearSmartHeader( UtilesSWHelper.getAdminUsuario(), UtilesSWHelper.getAdminPassword() ) );
	            iReq.setProfile(Constants.Profile.PDF);
	            iReq.setAddSignatureForm(true);
	            iReq.setAddAdditionalInfoValues(true);
	            iReq.setAddCertificateValues(UtilesResources.getProperty(UtilesResources.PROP_WS_CERT_VALUES));
	            iReq.setAddChainCertificateValues(UtilesResources.getProperty(UtilesResources.PROP_WS_CERT_VALUES));          
	            
	            iReq.setInputPdfBase64Data(dataSigned);
	
	            SmartVerifyResponse iResp = iReq.send();
	            
	            byte[] pdf = UtilesSWHelper.convertBase64ToBytes(dataSigned); 
	        	PdfReader reader = new PdfReader(pdf);
	        	AcroFields fields = reader.getAcroFields();             
	            
	            if (UtilesSWHelper.RESULTMAJOR_SUCCESS_FIRMA.equals(iResp.getResultMajor()) && 
	                    (UtilesSWHelper.RESULTMINOR_SUCCESS_VERIFY.equals(iResp.getResultMinor()) ||
	                    UtilesSWHelper.RESULTMINOR_SUCCESS_VERIFY_MULTISIGN_PDF.equals(iResp.getResultMinor()))){
	            	System.out.println("FIRMAS VALIDAS");
	            	verifyResponse.setSignatures( getSignatures(iResp, fields));
	            	verifyResponse.setValida(true);
	            }
	            else{
	            	System.out.println("FIRMAS NO VALIDAS");
	            	verifyResponse.setValida(false);
	            	verifyResponse.setSignatures( getSignatures(iResp, fields));
	            }
	            return verifyResponse;
        	}
        	else{
        		
        		verifyResponse.setSignatures(getSignatureDummy());
        		verifyResponse.setValida(true);
        		
        		return verifyResponse;	
        	}
            
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
    	
    	VerifyResponse verifyResponse = new VerifyResponse();
    	
        try{
        	boolean IS_TEST = UtilesResources.getProperty("swHelperConfig.DummyServices").equals("true");
        	
        	if (!IS_TEST){
	            SmartVerifyRequest iReq = new SmartVerifyRequest( UtilesSWHelper.getURLTrustedX() );
	            iReq.setHeader( UtilesSWHelper.crearSmartHeader( artifact ));
	            iReq.setProfile(Constants.Profile.PDF);
	            iReq.setInputPdfBase64Data(dataSigned);
	            iReq.setAddAdditionalInfoValues(true);
	            iReq.setAddSignatureForm(true);
	            iReq.setAddSignatureType(true);
	            iReq.setAddSignedAttributes(true);
	            iReq.setAddCertificateValues(UtilesResources.getProperty(UtilesResources.PROP_WS_CERT_VALUES));
	            iReq.setAddRevocationValues(UtilesResources.getProperty(UtilesResources.PROP_WS_CERT_VALUES));
	            iReq.setAddChainCertificateValues(UtilesResources.getProperty(UtilesResources.PROP_WS_CERT_VALUES));
	            iReq.setAddChainRevocationValues(UtilesResources.getProperty(UtilesResources.PROP_WS_CERT_VALUES));
	            SmartVerifyResponse iResp = iReq.send();
	            
	            byte[] pdf = UtilesSWHelper.convertBase64ToBytes(dataSigned); 
	        	PdfReader reader = new PdfReader(pdf);
	        	AcroFields fields = reader.getAcroFields();  	            
	            
	            if (UtilesSWHelper.RESULTMAJOR_SUCCESS_FIRMA.equals(iResp.getResultMajor()) && 
	                    (UtilesSWHelper.RESULTMINOR_SUCCESS_VERIFY.equals(iResp.getResultMinor()) ||
	                    UtilesSWHelper.RESULTMINOR_SUCCESS_VERIFY_MULTISIGN_PDF.equals(iResp.getResultMinor()))){
	            	System.out.println("FIRMAS VALIDAS");
	            	verifyResponse.setSignatures( getSignatures(iResp, fields));
	            	verifyResponse.setValida(true);
	            }
	            else{
	            	System.out.println("FIRMAS NO VALIDAS");
	            	verifyResponse.setValida(false);
	            	verifyResponse.setSignatures( getSignatures(iResp, fields));
	            }
	            return verifyResponse;
        	}
        	else{
        		
        		verifyResponse.setSignatures(getSignatureDummy());
        		verifyResponse.setValida(true);
        		
        		return verifyResponse;
        	}
        }  
        catch(Exception ex){
            Logger.getLogger(ServicioDSV.class.getName()).log(Level.ERROR, null, ex);
            throw new WService_TXException(UtilesMsg.ERROR_VALIDAR_FIRMA, ex.getMessage(), ex.getCause());                         
        } 
    }
    
    private Signature[] getSignatureDummy(){
    	Signature[] arrsing = new Signature[1];
    	Signature signature = new Signature();
    	
    	signature.setFecha( new Date() );
    	signature.setCn("CN_TEST");
    	signature.setMotivo("Motivo 1");
    	signature.setUbicacion("Ubicación 1");
    	signature.setVerify(true);
    	
    	Certificate cert = new Certificate();
    	cert.setNombre("TESTCERT");
    	cert.setEmisor("Emisor test");
    	cert.setFechaDesde("27-03-2015");
    	cert.setFechaHasta("27-03-2016");
    	cert.setoEmisor("Organizacion test");
    	cert.setNroSerie("5D9E90564C437581551542C20FFDB816");
    	signature.setCertificado(cert);
    	
    	arrsing[0] = signature;
    	return arrsing;
    }
    
    private Signature[] getSignatures( SmartVerifyResponse iresp, AcroFields fields ) throws Exception{
    	
    	Signature[] arrsing = new Signature[iresp.getNumberSignatures()]; 
    	
	    	
    	
    	for (int i = 0; i < iresp.getNumberSignatures(); i++){
    		SmartSignatureResult signatureResponse = iresp.getSignature(i);
    		
    		PdfString ubicacion = null;
    		PdfString motivo = null;
    		
    		Signature signature = new Signature();
        	for (String signame : fields.getSignatureNames()){
        		System.out.println("Signame: " + signame);
        		if (signame.equals(signatureResponse.getPdfFieldLabel())){
        			PdfDictionary sig = fields.getSignatureDictionary(signame);
        			motivo = sig.getAsString(PdfName.REASON);
        			ubicacion = sig.getAsString(PdfName.LOCATION);
		    		if (signatureResponse.getSigningTime() != null) signature.setFecha( signatureResponse.getSigningTime() );
		    		if (signatureResponse.getSignerIdentity() != null) signature.setCn(UtilesSWHelper.getCN(signatureResponse.getSignerIdentity()));
		
		    		signature.setUbicacion((ubicacion == null || ubicacion.isNull()) ? "" : ubicacion.toString());
		    		signature.setMotivo((motivo == null || motivo.isNull()) ? "" : motivo.toString());
		    		signature.setVerify(UtilesSWHelper.verifySmartSignature(signatureResponse));
		    		
		    		signature.setCertificado(getCertificado( signatureResponse.getSignerCertificateXml() ));
		    		
		    		arrsing[i] = signature;
    		
        		}
        	} 
    	}
    	return arrsing;
    }
    
    private Certificate getCertificado( String xmlCert ){
    	
    	
    	Certificate cert = new Certificate();
    	if (!UtilesSWHelper.isNullOrEmpty(xmlCert)){
    		String cnSubject = UtilesSWHelper.getCN( UtilesSWHelper.getNodeValue(xmlCert, "css:subject") ) ;
    		String oSubject = UtilesSWHelper.getO( UtilesSWHelper.getNodeValue(xmlCert, "css:subject") ) ;;
    		String oUSubject = UtilesSWHelper.getOU( UtilesSWHelper.getNodeValue(xmlCert, "css:subject") ) ;;
    		
    		String issuer = UtilesSWHelper.getNodeValue(xmlCert, "css:issuer");
    		String emisor = UtilesSWHelper.getCN(issuer);
    		String oEmisor = UtilesSWHelper.getO(issuer);
    		String oUEmisor = UtilesSWHelper.getOU(issuer);;
    		
    		String serialNumberBase64 = UtilesSWHelper.getNodeValue(xmlCert, "css:serialNumber");;
    		String serialNumberHexa = UtilesSWHelper.convertBase64ToHexa(serialNumberBase64);
    		
    		String validity = UtilesSWHelper.getNodeValue(xmlCert, "css:validity");
    		String fechaDesde = validity.split(",")[0];
    		String fechaHasta = validity.split(",")[1]; 	
    		try {
    			Date fdesde = UtilesSWHelper.DATE_TRUSTEDX.parse(fechaDesde);
				Date fHasta = UtilesSWHelper.DATE_TRUSTEDX.parse(fechaHasta);
				cert.setFechaDesde(UtilesSWHelper.DATE_SIMPLE.format(fdesde));
				cert.setFechaHasta(UtilesSWHelper.DATE_SIMPLE.format(fHasta));
			} 
    		catch (ParseException e) {
    			cert.setFechaDesde("");
    			cert.setFechaHasta("");
				e.printStackTrace();
			}
    		
    		cert.setEmisor(emisor);
    		cert.setNombre(cnSubject);
    		cert.setNroSerie(serialNumberHexa);
    		cert.setoEmisor(oEmisor);
    		cert.setoSubject(oSubject);
    		cert.setoUEmisor(oUEmisor);
    		cert.setoUSubject(oUSubject);
    		
    	}
    	return cert;
    }
    
    
}
