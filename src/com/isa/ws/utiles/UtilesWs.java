package com.isa.ws.utiles;

import java.io.IOException;

import uy.isa.docman.client.DocmanClient;
import uy.isa.docman.schemas.DocumentType;
import uy.isa.docman.schemas.GetDocumentRequest;
import uy.isa.docman.schemas.GetDocumentResponse;
import uy.isa.docman.schemas.UploadDocumentRequest;
import uy.isa.docman.schemas.UploadDocumentResponse;

import com.isa.ws.exceptions.WService_TXException;


public class UtilesWs {

	
	private static DocmanClient docmanClient = null;
	
	private static UtilesWs instance;
	
    public static int CODIGO_RESPUESTA_ERROR = -1; 
    public static int CODIGO_RESPUESTA_OK = 1; 
    
    public UtilesWs() throws WService_TXException  {
        
        String endpoint;
		try {
			endpoint = UtilesResources.getProperty(UtilesResources.PROP_WS_ENDPOINT);
			docmanClient = new DocmanClient();
			docmanClient.setDefaultUri( endpoint );
			docmanClient.setMarshallerAndBound();
        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new WService_TXException("Error accediendo a archivo de configuración de servicios.");
		}        
    }
    
    public static UtilesWs getInstanceWS() throws WService_TXException {
        if (instance == null){
            instance = new UtilesWs();
        }
        return instance;
    }
    
    
    public  GetDocumentResponse getDocumento( String documento ){
        
        GetDocumentRequest getDocRequest = docmanClient.CreateGetDocumentRequest( documento );
        return (GetDocumentResponse) docmanClient.GetDocument(getDocRequest);
    }
    
    public  UploadDocumentResponse uploadDocumento( DocumentType document ) {
        UploadDocumentRequest uploadDocRequest = new UploadDocumentRequest();
        uploadDocRequest.setDocument(document);
        return (UploadDocumentResponse) docmanClient.UploadDocument( uploadDocRequest );
    }	
	
}
