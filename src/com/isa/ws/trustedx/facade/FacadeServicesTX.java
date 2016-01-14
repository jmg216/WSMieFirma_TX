package com.isa.ws.trustedx.facade;

import com.isa.ws.trustedx.aa.ServicioAA;
import com.isa.ws.trustedx.dsv.ServicioDSV;

public class FacadeServicesTX {
	
	private static ServicioAA servicioAA;
	private static ServicioDSV servicioDSV;
	
	
	
	public static ServicioAA getServicioAA(){
		if (servicioAA == null){
			servicioAA = new ServicioAA();
		}
		return servicioAA;
	}
	
	public static ServicioDSV getServicioDSV(){
		if (servicioDSV == null){
			servicioDSV = new ServicioDSV();
		}
		return servicioDSV;
	}
	
}
