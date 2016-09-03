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

import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author Andres
 */
public class BirtResources  {
//Declaración de variables para manejo de mensajes multi idioma y país
    private String lenguaje = "es";
    private String pais = "VEN";
    private Locale  localidad = new Locale(lenguaje, pais);
    ResourceBundle BirtResources =  ResourceBundle.getBundle("org.openbizview.util.MessagesBirt",localidad);

    private String Message = "";


      /**
     * Recursos de lenguaje. Archivos Properties. Integración con birt
     **/
    public  String getMessage(String mensaje) {
        Message = BirtResources.getString(mensaje);
        return Message;
    }

    public  String getHtmlMessage(String mensaje) {
        Message = BirtResources.getString(mensaje);
        return Message;
    }

     public  String getJavaScriptMessage(String mensaje) {
        Message = BirtResources.getString(mensaje);
        return Message;
    }

}
