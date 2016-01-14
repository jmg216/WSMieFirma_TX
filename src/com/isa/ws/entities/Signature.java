package com.isa.ws.entities;

import java.util.Date;

public class Signature {

	private String cn;
	private Date fecha;
	private String rol;
	private String motivo;
	private String ubicacion;
	private boolean verify;
	
	public Signature() {
		super();
		this.cn = "";
		this.rol = "";
		this.motivo = "";
		this.ubicacion = "";
		this.verify = false;
	}
	
	public boolean isVerify() {
		return verify;
	}
	public void setVerify(boolean verify) {
		this.verify = verify;
	}
	public String getCn() {
		return cn;
	}
	public void setCn(String cn) {
		this.cn = cn;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public String getRol() {
		return rol;
	}
	public void setRol(String rol) {
		this.rol = rol;
	}
	public String getMotivo() {
		return motivo;
	}
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	public String getUbicacion() {
		return ubicacion;
	}
	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}
	
	
	
	
}
