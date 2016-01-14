/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isa.ws.exceptions;

/**
 *
 * @author JMiraballes
 */
public class WService_TXException extends Exception {
    
	private static final long serialVersionUID = 1L;
    
    private String mensaje;
    
    
    public WService_TXException() {
		super();
	}


	public WService_TXException(String customMsg, String message, Throwable cause) {
		super(message, cause);
		mensaje = customMsg;
	}

	public WService_TXException(String message) {
		super(message);
	}

	public WService_TXException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    
    
}
