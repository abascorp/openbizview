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
    document.getElementById("formmailgrupos:vop").value='0';
    clearUpdateInput('formmailgrupos:codigo', 'white');
}



function borrar(){
	document.getElementById("formmailgrupos:codigo").value= "1";
	document.getElementById("formmailgrupos:descripcion").value= " ";
    
    setTimeout("document.getElementById('formmailgrupos:codigo').value=''",100);
   	setTimeout("document.getElementById('formmailgrupos:descripcion').value=''",100);  
}


function enviar(vT0,vT1,vT2){
	  document.getElementById("formmailgrupos:codigo").value= rTrim(vT0);
	  document.getElementById("formmailgrupos:descripcion").value= rTrim(vT1);
	  document.getElementById("formmailgrupos:vop").value= rTrim(vT2);
	  updateInput('formmailgrupos:codigo', '#F2F2F2');
	}



