/*
 *  Copyright (C) 2011 - 2016  DVCONSULTORES

    Este programa es software libre: usted puede redistribuirlo y/o modificarlo 
    bajo los términos de la Licencia Pública General GNU publicada 
    por la Fundación para el Software Libre, ya sea la versión 3 
    de la Licencia, o (a su elección) cualquier versión posterior.

    Este programa se distribuye con la esperanza de que sea útil, pero 
    SIN GARANTÍA ALGUNA; ni siquiera la garantía implícita 
    MERCANTIL o de APTITUD PARA UN PROPÓSITO DETERMINADO. 
    Consulte los detalles de la Licencia Pública General GNU para obtener 
    una información más detallada. 

    Debería haber recibido una copia de la Licencia Pública General GNU 
    junto a este programa. 
    En caso contrario, consulte <http://www.gnu.org/licenses/>.
 */

var milisec = 120000;


function limpiarInputs(){
	if(document.getElementById("formAutoext:mensaje").className == "exito"){
    document.getElementById("formAutoext:lista").value="1";
	}
}

function procesar(){
	if(document.getElementById("formAutoext:lista").value=="1"){
		document.getElementById("jsmsj").innerHTML= document.getElementById("msnProc").value;
        document.getElementById("jsmsj").className = 'alerta';
        document.getElementById("formAutoext:lista").focus();
        setTimeout("limpiarMensaje('jsmsj');", milisec);
        document.getElementById("formAutoext:mensaje").innerHTML= "";
        document.getElementById("formAutoext:mensaje").className = "";
    }else{
    	// Limpiamos valores   
    	document.getElementById("jsmsj").innerHTML= "";
        document.getElementById("jsmsj").className = "";
        
      //Creamos mensaje
        setTimeout("limpiarInputs();",500);
    } 
}




function limpiar()
{  //Llamado por el boton limpiar
	document.getElementById("formAutoext:lista").value="1";
    document.getElementById("jsmsj").innerHTML = "";
    document.getElementById("jsmsj").className = "";

}

