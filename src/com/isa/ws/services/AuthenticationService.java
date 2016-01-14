package com.isa.ws.services;

import java.io.IOException;
import java.util.Base64;
import java.util.StringTokenizer;

import com.isa.ws.exceptions.WService_TXException;
import com.isa.ws.utiles.UtilesResources;

public class AuthenticationService {


	public boolean authenticate(String authCredentials) throws WService_TXException {
		
		try {
			if (null == authCredentials)
				return false;
			// header value format will be "Basic encodedstring" for Basic
			// authentication. Example "Basic YWRtaW46YWRtaW4="
			final String encodedUserPassword = authCredentials.replaceFirst("Basic" + " ", "");
			String usernameAndPassword = null;
			
			byte[] decodedBytes = Base64.getDecoder().decode(encodedUserPassword);
			usernameAndPassword = new String(decodedBytes, "UTF-8");
	
			final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
			final String username = tokenizer.nextToken();
			final String password = tokenizer.nextToken();
	
			// we have fixed the userid and password as admin
			// call some UserService/LDAP here
			boolean authenticationStatus = UtilesResources.getProperty("swHelperConfig.RestServiceUser").equals(username) && 
											UtilesResources.getProperty("swHelperConfig.RestServicePass").equals(password);
			return authenticationStatus;
		}	
		catch (IOException e) {
			e.printStackTrace();
			WService_TXException ex = new WService_TXException("No se puedo leer archivo de configuración.", e.getMessage(), e.getCause());
			throw ex;
		}
		
	}
}
