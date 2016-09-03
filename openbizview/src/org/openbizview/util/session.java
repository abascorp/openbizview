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


/**
 *
 * @author Andres
 */
public class session {


 public session(){

 }

/*****************************************************************************
 * Métodos y definición de variables para las JSP personalizadas del Birt
 * Andrés Dominguez 07/04/2009.
 ****************************************************************************/
//Definición de variables
private String param = "Parametros";
private String param1 = "parameterDialog";
private String expDatos = "Exportar Datos";
private String expDatos1 = "simpleExportDataDialog";
private String expRep = "Exportar Reporte";
private String expRep1 = "exportReportDialog";
private String impRep = "Imprimir Reporte";
private String impRep1 = "printReportDialog";

    public String getExpDatos() {
        return expDatos;
    }

    public String getExpRep() {
        return expRep;
    }

    public String getImpRep() {
        return impRep;
    }

    public  String getParam() {
        return param;
    }

    public String getExpDatos1() {
        return expDatos1;
    }

    public String getExpRep1() {
        return expRep1;
    }

    public String getImpRep1() {
        return impRep1;
    }

    public String getParam1() {
        return param1;
    }

}
