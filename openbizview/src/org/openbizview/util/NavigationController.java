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
import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name="navigateBean")
@SessionScoped
public class NavigationController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//private String targetUrl;
	   private String pageToDisplay = "bvt001";

	   public String getPageToDisplay(){
	      return this.pageToDisplay;
	   }
	   
	   public void setPageToDisplay(String pageToDisplay){
	      this.pageToDisplay = pageToDisplay;
	   }
	   
	   public void href(String page) throws IOException{
		   FacesContext.getCurrentInstance().getExternalContext().redirect(page + ".xhtml"); 
	   }
}
