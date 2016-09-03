/*
 *  Copyright (C) 2011 - 2016  DVCONSULTORES

    Este programa es software libre: usted puede redistribuirlo y/o modificarlo 
    bajo los terminos de la Licencia P�blica General GNU publicada 
    por la Fundacion para el Software Libre, ya sea la version 3 
    de la Licencia, o (a su eleccion) cualquier version posterior.

    Este programa se distribuye con la esperanza de que sea �til, pero 
    SIN GARANTiA ALGUNA; ni siquiera la garantia implicita 
    MERCANTIL o de APTITUD PARA UN PROPoSITO DETERMINADO. 
    Consulte los detalles de la Licencia P�blica General GNU para obtener 
    una informacion mas detallada. 

    Deberia haber recibido una copia de la Licencia P�blica General GNU 
    junto a este programa. 
    En caso contrario, consulte <http://www.gnu.org/licenses/>.
 */

package org.openbizview.util;


import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.birt.report.engine.api.*;



public class RunReport implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//Constructor
	public RunReport(){

	}

	/**
     * Este m�todo utiliza el BIRT engine para generar reportes
	 * desde el API utilizando POJO.
     * @param reporte: Nombre del reporte que es� leyendo, debe ser .rptdesing
	 * @parama format: Formato de salida: html, pdf, doc
	 * @param ubicacionrep: Ubicaci�n del reporte
	 * @param rutasalida: Salida del reporte ubicaci�n en disco
	 * @param nbrreporte: nombre del reporte al momento de la salida
	 * @param nbrreporte: nombre del reporte al momento de la salida
	 * @param feccon
     **/ 
	public void outReporteRecibo(String reporte, String format, String ubicacionrep
			, String rutasalida, String nbrreporte, Date feccon, String job
			, String paramnames, String paramvalues){
		
		 
	  //Variables used to control BIRT Engine instance
  	  //Now, setup the BIRT engine configuration. The Engine Home is hardcoded
  	  EngineConfig config = new EngineConfig( );
  	  //Create new Report engine based off of the configuration
      ReportEngine engine = new ReportEngine( config );
      IReportRunnable report = null;
      IRunAndRenderTask task;
      IRenderOption options = new RenderOption();     
      //PDFRenderOption pdfOptions = new PDFRenderOption();
      final HashMap<String, Date> PARAMAMFECCON = new HashMap<String, Date>();
      
      PARAMAMFECCON.put("FECCON", feccon);
      
      //Lectura de par�meros din�micos desde la tabla t_programaci�n
      //Funci�n agregada para versi�n 5 de bizview 11/10/2014
      //Por los momentos solo se trabajar� con Strings
      final HashMap<String, String> PARAMAMARRAY = new HashMap<String, String>();

      //Arreglo para longitud, se recibe como par�metro de longitud paramvalues
      //Con la libreria String utils se define el separador y con ello se conoce la longitud de los parametros
      //Por ejemplo recibe como par�metro P1|P2|P3, con separador '|', al recorrerlo devuelve longitud tres
      //Se va pasando al reporte cada parametro
      //DVConsultores Andr�s Dominguez 18/08/2015
      String[] arraySplitSrtings = StringUtils.splitByWholeSeparator(paramvalues, "|");
     
      //Si la longitud de los valores de par�metros es diferente de cero
      //Procede a leer y pasar par�metros
      if(arraySplitSrtings.length>0 ){
    	  String[] arr1 = paramnames.split("\\|", -1);//Toma la lista de parametros de la tabla y hace un split, trabaja el arreglo y lo recorre
    	  String[] arr2 = paramvalues.split("\\|", -1);//Toma la lista de parametros de la tabla y hace un split, trabaja el arreglo y lo recorre
    	  for(int i = 0; i < arraySplitSrtings.length; i++){
    		  PARAMAMARRAY.put(arr1[i], arr2[i]);
    	  }
    	  
       }//Valida longitud 

      //
 
        
        //With our new engine, lets try to open the report design
        try
        {
      	  report = engine.openReportDesign( ubicacionrep + File.separator + reporte + ".rptdesign"); 
        }
        catch (Exception e)
        {
        	 //msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), "");
//			 FacesContext.getCurrentInstance().addMessage(null, msj);
        	 e.printStackTrace();
            // System.exit(-1);
        }
        
        //With the file open, create the Run and Render task to run the report
        task = engine.createRunAndRenderTask(report);
          

        //This will set the output file location, the format to render to, and
        //apply to the task
        //System.out.println(format);
            options.setOutputFormat(format);
            options.setOutputFileName( rutasalida + File.separator + nbrreporte + "." + format);
            task.setRenderOption(options);  
        
        try
        {
        	 
        	 task.setParameterValues(PARAMAMFECCON);
        	 task.setParameterValues(PARAMAMARRAY);
        	 task.validateParameters();
             task.run();
        }
        catch (Exception e)
        {
        	 //msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), "");
			 //FacesContext.getCurrentInstance().addMessage(null, msj);
             e.printStackTrace();
             //System.exit(-1);
        }
        
        //Yeah, we finished. Now destroy the engine and let the garbage collector
        //do its thing
        //System.out.println("Reporte ejecutado : " + nbrreporte);
        engine.destroy();
        task.close();
   }


}
