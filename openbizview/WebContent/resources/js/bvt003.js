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


function limpiar()
{  //Llamado por el boton limpiar
    document.getElementById("formBvt003:vop").value="0";
    clearUpdateInput('formBvt003:codrol', 'white');
}

function enviar(vT0,vT1,vT2){
	  document.getElementById("formBvt003:codrol").value= rTrim(vT0);
	  document.getElementById("formBvt003:desrol").value= rTrim(vT1);
	  document.getElementById("formBvt003:vop").value= rTrim(vT2);
	  updateInput('formBvt003:codrol', '#F5F6CE');
	}

