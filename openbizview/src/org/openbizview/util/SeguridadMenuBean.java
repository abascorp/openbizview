package org.openbizview.util;

/*
 *  Copyright (C) 2011 - 2016  DVCONSULTORES

 Este programa es software libre: usted puede redistribuirlo y/o modificarlo 
 bajo los terminos de la Licencia Pública General GNU publicada 
 por la Fundacion para el Software Libre, ya sea la version 3 
 de la Licencia, o (a su eleccion) cualquier version posterior.

 Este programa se distribuye con la esperanza de que sea útil, pero 
 SIN GARANTiA ALGUNA; ni siquiera la garantia implicita 
 MERCANTIL o de APTITUD PARA UN PROPoSITO DETERMINADO. 
 Consulte los detalles de la Licencia Pública General GNU para obtener 
 una informacion mas detallada. 

 Deberia haber recibido una copia de la Licencia Pública General GNU 
 junto a este programa. 
 En caso contrario, consulte <http://www.gnu.org/licenses/>.
 */

import java.io.IOException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.context.RequestContext;

@ManagedBean(name = "smnubean")
@ViewScoped
public class SeguridadMenuBean extends Bd {
	public SeguridadMenuBean() {

	}

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ///////////////////////////////////////OPCIONES DE SEGURIDAD DEL MENU
	// BASICO/////////////////////////////////////////////
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	PntGenerica consulta = new PntGenerica();
	private String login = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario"); //Usuario logeado
	HttpServletRequest rq = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();


	/**
	 * Lee las opciones del menú para acceso
	 * 
	 * @return true o false
	 * @throws IOException 
	 */
	public String opcmnu(String opc)  {
		String rendered = "true";
		if (!rq.isRequestedSessionIdValid()) {
			RequestContext.getCurrentInstance().execute("PF('idleDialog').show()");
			rendered = "false";
		} else {
		
		if(login==null){
			login = "";
		} 		
		if(login==""){
			login = "";
		}
		String vlquery = "select codvis " + "from bvtmenu "
				+ " where codopc ='" + opc.toUpperCase()
				+ "' and b_codrol in (select b_codrol " + " from bvt002  where coduser = '" + login.toUpperCase() + "')";
		try {
			consulta.selectPntGenerica(vlquery, JNDI);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(vlquery);
		String[][] tabla = consulta.getArray();
		int rows = consulta.getRows();
		String vista;
		if (rows > 0) {
			vista = tabla[0][0].toString();
			if (vista.equals("1")) {
				rendered = "false";
			} else {
				rendered = "true";
			}
		
		}
		}
		
		return rendered;
	}

	/**
	 * Lee las opciones del menú para acceso totones
	 * 
	 * @return true o false
	 * @throws IOException 
	 */
	public String opcbot(String opc) throws NamingException {
		String rendered = "true";
		if (!rq.isRequestedSessionIdValid()) {
			RequestContext.getCurrentInstance().execute("PF('idleDialog').show()");
			rendered = "false";
		} else {
		consulta.selectPntGenerica("select codvis " + "from bvt005 "
				+ " where codopc ='" + opc.toUpperCase()
				+ "' and b_codrol = (select b_codrol " + " from bvt002 "
				+ " where coduser = '" + login.toUpperCase() + "')", JNDI);
		String[][] tabla = consulta.getArray();
		int rows = consulta.getRows();
		String vista;
		if (rows > 0) {
			vista = tabla[0][0].toString();
			if (vista.equals("1")) {
				rendered = "false";
			} else {
				rendered = "true";
			}
		}
		}
		return rendered;
		
	}

		

}
