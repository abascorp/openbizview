/*
 *  Copyright (C) 2011 - 2016  DVCONSULTORES,

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


function limpiar(){
    limpiarInput('formmailconfig:usuario');
    limpiarInput('formmailconfig:clave');
    limpiarInput('formmailconfig:starttlsenable');
    limpiarInput('formmailconfig:auth');
    limpiarInput('formmailconfig:host');
    limpiarInput('formmailconfig:puerto');
    document.getElementById("formmailconfig:vop").value='0';
}



function borrar(){
	document.getElementById("formmailconfig:usuario").value= " ";
	document.getElementById("formmailconfig:clave").value= " ";
	document.getElementById("formmailconfig:starttlsenable").value= " ";
	document.getElementById("formmailconfig:auth").value= " ";
	document.getElementById("formmailconfig:host").value= " ";
	document.getElementById("formmailconfig:puerto").value= " ";
    
    setTimeout("document.getElementById('formmailconfig:usuario').value=''",100);
   	setTimeout("document.getElementById('formmailconfig:clave').value=''",100);  
   	setTimeout("document.getElementById('formmailconfig:starttlsenable').value=''",100);
   	setTimeout("document.getElementById('formmailconfig:auth').value=''",100);  
   	setTimeout("document.getElementById('formmailconfig:host').value=''",100);
   	setTimeout("document.getElementById('formmailconfig:puerto').value=''",100);  
}


function enviar(vT0,vT1,vT2,vT3,vT4,vT5,vT6){
	  document.getElementById("formmailconfig:usuario").value= rTrim(vT0);
	  document.getElementById("formmailconfig:clave").value= rTrim(vT1);
	  document.getElementById("formmailconfig:starttlsenable").value= rTrim(vT2);
	  document.getElementById("formmailconfig:auth").value= rTrim(vT3);
	  document.getElementById("formmailconfig:host").value= rTrim(vT4);
	  document.getElementById("formmailconfig:puerto").value= rTrim(vT5);
	  document.getElementById("formmailconfig:vop").value= rTrim(vT6);
	}



