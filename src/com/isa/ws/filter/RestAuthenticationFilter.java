package com.isa.ws.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.isa.ws.entities.VerifyResponse;
import com.isa.ws.exceptions.WService_TXException;
import com.isa.ws.services.AuthenticationService;
import com.isa.ws.utiles.GsonHelper;
import com.isa.ws.utiles.UtilesResources;

public class RestAuthenticationFilter implements Filter{

	public static final String AUTHENTICATION_HEADER = "Authorization";
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filter) throws IOException, ServletException {
		// TODO Auto-generated method stub
		
		if (request instanceof HttpServletRequest) {
			try{
				HttpServletRequest httpServletRequest = (HttpServletRequest) request;
				String authCredentials = httpServletRequest.getHeader(AUTHENTICATION_HEADER);
	
				// better injected
				AuthenticationService authenticationService = new AuthenticationService();
	
				boolean authenticationStatus = authenticationService.authenticate(authCredentials);
	
				if (authenticationStatus) {
					filter.doFilter(request, response);
				} 
				else {
					if (response instanceof HttpServletResponse) {
						HttpServletResponse httpServletResponse = (HttpServletResponse) response;
						httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
						VerifyResponse verifyResponse = new VerifyResponse();
						verifyResponse.setErrorServer(true);
						verifyResponse.setErrorMsj(UtilesResources.getProperty("swHelperConfig.AuthMsj"));
						
						String resp = GsonHelper.getInstance().getGson().toJson(verifyResponse);
						httpServletResponse.getOutputStream().write( resp.getBytes() );
					}
				}
				
			}
			catch(WService_TXException e){
				if (response instanceof HttpServletResponse) {
					
					HttpServletResponse httpServletResponse = (HttpServletResponse) response;
					VerifyResponse verifyResponse = new VerifyResponse();
					verifyResponse.setErrorServer(true);
					verifyResponse.setErrorMsj(e.getMensaje());		
					String resp = GsonHelper.getInstance().getGson().toJson(verifyResponse);
					httpServletResponse.getOutputStream().write( resp.getBytes() );
					
				}
			}				
		}		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
