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
    document.getElementById("formmaillista:vop").value='0';
}



function borrar(){
	document.getElementById("formmaillista:idgrupo_input").value= " ";
	document.getElementById("formmaillista:idmail").value= "1";
	document.getElementById("formmaillista:mail").value= "a@a.com";
    
	setTimeout("document.getElementById('formmaillista:idgrupo_input').value=''",100);
    setTimeout("document.getElementById('formmaillista:idmail').value=''",100);
   	setTimeout("document.getElementById('formmaillista:mail').value=''",100);  
   	updateInput('formmaillista:idgrupo_input', 'white');
    updateInput('formmaillista:idmail', 'white');
}


function enviar(vT0,vT1,vT2,vT3){
	  document.getElementById("formmaillista:idgrupo_input").value= rTrim(vT0);
	  document.getElementById("formmaillista:idmail").value= rTrim(vT1);
	  document.getElementById("formmaillista:mail").value= rTrim(vT2);
	  document.getElementById("formmaillista:vop").value= rTrim(vT3);
	  updateInput('formmaillista:idgrupo_input', '#F2F2F2');
	  updateInput('formmaillista:idmail', '#F2F2F2');
	}


