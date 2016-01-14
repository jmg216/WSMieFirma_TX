package com.isa.ws.services;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import uy.isa.docman.schemas.GetDocumentResponse;

import com.isa.ws.entities.VerifyResponse;
import com.isa.ws.exceptions.WService_TXException;
import com.isa.ws.trustedx.facade.FacadeServicesTX;
import com.isa.ws.utiles.GsonHelper;
import com.isa.ws.utiles.UtilesResources;
import com.isa.ws.utiles.UtilesWs;

@Path("/documento")
public class ValidarPDF {

	private static Logger logger = Logger.getLogger(ValidarPDF.class);
	
	@GET
	@Path("id/{id}")
	@Produces("application/json")
	public String validarDocumentoByParams ( @PathParam("id") String iddocumento ) throws WService_TXException{
		logger.info("VerificarDocumentoPDF::validarDocumentoByDoc");
		
		VerifyResponse verifyResponse = new VerifyResponse();
		
		try{
			GetDocumentResponse docResponse = UtilesWs.getInstanceWS().getDocumento(iddocumento);
			
			if (docResponse.getDocument() != null){
				byte[] doc = docResponse.getDocument().getDocument();			
				byte[] documento = Base64.encodeBase64( doc );
				String dataSigned = new String( documento );
				verifyResponse = FacadeServicesTX.getServicioDSV().verifySignedPdf( dataSigned );
				verifyResponse.setErrorServer(false);
			}
			else{
				verifyResponse.setErrorMsj( UtilesResources.getProperty("swHelperConfig.ErrorNoExisteDocumento") + " " + iddocumento );
			}
			
		}
		catch( Exception e ){
			e.printStackTrace();
			verifyResponse.setErrorMsj(e.getMessage());
			verifyResponse.setErrorServer(true);
		} 
		
		return GsonHelper.getInstance().getGson().toJson(verifyResponse);
	}
	
	
	@POST
	@Path("/base")
	@Produces("application/json")
	public String validarDocumentoByDoc( String dataSigned ) throws WService_TXException{
		logger.info("VerificarDocumentoPDF::validarDocumentoByDoc");
		VerifyResponse verifyResponse = new VerifyResponse();
		try{
			verifyResponse = FacadeServicesTX.getServicioDSV().verifySignedPdf( dataSigned );
			verifyResponse.setErrorServer(false);
		}
		catch( Exception e){
			e.printStackTrace();
			verifyResponse.setErrorMsj(e.getMessage());
			verifyResponse.setErrorServer(true);
		}
		return GsonHelper.getInstance().getGson().toJson(verifyResponse);
	}	
	
	
	
}
