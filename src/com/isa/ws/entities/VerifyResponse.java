package com.isa.ws.entities;


public class VerifyResponse {
	
	private Signature[] signatures;
	private boolean valida;
	private boolean errorServer;
	private String errorMsj;
	
	public VerifyResponse(){
		this.signatures = new Signature[0];
		this.valida = false;
		this.errorServer = false;
		this.errorMsj = "";
	}

	public boolean isValida() {
		return valida;
	}

	public void setValida(boolean valida) {
		this.valida = valida;
	}

	public Signature[] getSignatures() {
		return signatures;
	}

	public void setSignatures(Signature[] signatures) {
		this.signatures = signatures;
	}

	public boolean isErrorServer() {
		return errorServer;
	}

	public void setErrorServer(boolean errorServer) {
		this.errorServer = errorServer;
	}

	public String isErrorMsj() {
		return errorMsj;
	}

	public void setErrorMsj(String errorMsj) {
		this.errorMsj = errorMsj;
	}
	
	
	

}
