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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;

@ManagedBean
@ViewScoped
public class FileUploadController extends Bd {
	

	public void init() {
		//Si no tiene acceso al módulo no puede ingresar
		if (new SeguridadMenuBean().opcmnu("M07")=="false") {
			RequestContext.getCurrentInstance().execute("PF('idleDialogNP').show()");
		}
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Variables seran utilizadas para capturar mensajes de errores de Oracle y
	// parametros de metodos
	FacesMessage msj = null;
	// Primitives
	private static final int BUFFER_SIZE = 6124;
	//private static final String RUTA_REPORTE = File.separator + "reportes";
	FileUploadEvent event;
	ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext(); //Toma ruta real de la aplicación
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////UPLOAD DE ARCHIVOS /////////////////////////////////////////////////  
	    
	public void handleReportUpload(FileUploadEvent event)  {
		
		     //System.out.println("Thread is running");
			 File ruta = new File(BIRT_VIEWER_WORKING_FOLDER + File.separator +  event.getFile().getFileName());
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
					msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("autos02Uploaded"), "");
					FacesContext.getCurrentInstance().addMessage(null, msj);	
					fileOutputStream.close();
					inputStream.close();
	        				
				} catch (IOException  e) {
					e.printStackTrace();
					msj = new FacesMessage(FacesMessage.SEVERITY_FATAL,  e.getMessage(), "");
					FacesContext.getCurrentInstance().addMessage(null, msj);

				}	
	}
	
	/*
	public void handleReportUpload(FileUploadEvent event) throws InterruptedException {
		try {
		ExecutorService ex = null;
		
			ex = Executors.newFixedThreadPool(Integer.parseInt(THREADNUMBER));
			
			FileUploadControllerThread th = new FileUploadControllerThread(event);		
			
	        ex.execute(th);
	        //
	        Thread.sleep(100);
	        th.setStop(true);

	        ex.shutdown();
	        //Toma el mensaje que devuelva el thread
	        if(th.getExito()){
	        msj = new FacesMessage(FacesMessage.SEVERITY_FATAL,  th.getRetornoThread(), "");
	     	FacesContext.getCurrentInstance().addMessage(null, msj);
	        } else {
	        msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("autos02Uploaded"), "");
			FacesContext.getCurrentInstance().addMessage(null, msj);	
	        }
		} catch (NumberFormatException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msj = new FacesMessage(FacesMessage.SEVERITY_FATAL,  e.getMessage(), "");
			FacesContext.getCurrentInstance().addMessage(null, msj);
			
		}   //Número de hilos a usar para el insert
		
	}*/



}
