package com.isa.ws.entities;

public class Certificate {

    private String nombre;
    private String emisor;
    private String fechaDesde;
    private String fechaHasta;
    private String oSubject;
    private String oUSubject;
    private String oEmisor;
    private String oUEmisor;
    private String nroSerie;
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getEmisor() {
		return emisor;
	}
	public void setEmisor(String emisor) {
		this.emisor = emisor;
	}
	public String getFechaDesde() {
		return fechaDesde;
	}
	public void setFechaDesde(String fechaDesde) {
		this.fechaDesde = fechaDesde;
	}
	public String getFechaHasta() {
		return fechaHasta;
	}
	public void setFechaHasta(String fechaHasta) {
		this.fechaHasta = fechaHasta;
	}
	public String getoSubject() {
		return oSubject;
	}
	public void setoSubject(String oSubject) {
		this.oSubject = oSubject;
	}
	public String getoUSubject() {
		return oUSubject;
	}
	public void setoUSubject(String oUSubject) {
		this.oUSubject = oUSubject;
	}
	public String getoEmisor() {
		return oEmisor;
	}
	public void setoEmisor(String oEmisor) {
		this.oEmisor = oEmisor;
	}
	public String getoUEmisor() {
		return oUEmisor;
	}
	public void setoUEmisor(String oUEmisor) {
		this.oUEmisor = oUEmisor;
	}
	public String getNroSerie() {
		return nroSerie;
	}
	public void setNroSerie(String nroSerie) {
		this.nroSerie = nroSerie;
	}
    
	
}
