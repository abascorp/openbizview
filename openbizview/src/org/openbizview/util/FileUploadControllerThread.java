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
package org.openbizview.util;

/*
 *  Esta clase controla todos los archivos de subida y procesamiento de txt al servidos
 */
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;

public class FileUploadControllerThread extends Thread {
	// Constructor
	public FileUploadControllerThread(FileUploadEvent event) {
      this.event = event;
	}
	
	private volatile boolean bExit = false;
	  
    public void setStop(boolean bExit){
        this.bExit = bExit;
    }
  

	// Primitives
	private static final int BUFFER_SIZE = 6124;
	private static final String RUTA_REPORTE = File.separator + "reportes";
	FileUploadEvent event;
	ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext(); //Toma ruta real de la aplicación
	private String retorno;
	private Boolean exito = false;

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Variables seran utilizadas para capturar mensajes de errores de Oracle y
	// parametros de metodos
	FacesMessage msj = null;
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////UPLOAD DE ARCHIVOS PARA AUTOSERVICIO/////////////////////////////////////////////////  

	    
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// /////////////////////////////////////UPLOAD DE ARCHIVOS PARA AUTOSERVICIO//////////////////////////////////////////////////

	public void run()  {		
		 while(!bExit){
	     //System.out.println("Thread is running");
		 File ruta = new File(extContext.getRealPath(RUTA_REPORTE)+ File.separator +  event.getFile().getFileName());
			try {
				FileOutputStream fileOutputStream = new FileOutputStream(ruta);
				byte[] buffer = new byte[BUFFER_SIZE];

				int bulk;
				InputStream inputStream = event.getFile().getInputstream();
				while (true) {
					bulk = inputStream.read(buffer);
					if (bulk < 0) {
						break;
					}
					fileOutputStream.write(buffer, 0, bulk);
					fileOutputStream.flush();
				}
				fileOutputStream.close();
				inputStream.close();
        				
			} catch (IOException  e) {
				e.printStackTrace();
				retorno = e.getMessage();
				exito = true;
			}
		 }
	}
	
	
	
	public String getRetornoThread(){
		return retorno;
	}

	/**
	 * @return the exito
	 */
	public Boolean getExito() {
		return exito;
	}

	/**
	 * @param exito the exito to set
	 */
	public void setExito(Boolean exito) {
		this.exito = exito;
	}

    
	
	


}
