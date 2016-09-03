/*
 *  Copyright (C) 2011 - 2016  DVCONSULTORES, CHRISTIAN DÍAZ

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
    clearUpdateInput('formBvt002:coduser', 'white');
}

function enviar(vT0,vT1,vT2,vT3,vT4,vT5,vT6){
	  document.getElementById("formBvt002:tabView:coduser").value= rTrim(vT0);
	  document.getElementById("formBvt002:tabView:desuser").value= rTrim(vT1);
	  document.getElementById("formBvt002:tabView:cluser").value= rTrim(vT2);
	  document.getElementById("formBvt002:tabView:rol_input").value= rTrim(vT3);
	  document.getElementById("formBvt002:vop").value= rTrim(vT4);
	  document.getElementById("formBvt002:tabView:mail").value= rTrim(vT5);
	  document.getElementById("formBvt002:tabView:instancia_input").value= rTrim(vT6);
	  updateInput('formBvt002:tabView:coduser', '#F2F2F2');
	  updateInput('formBvt002:tabView:cluser', '#F2F2F2');
	}

