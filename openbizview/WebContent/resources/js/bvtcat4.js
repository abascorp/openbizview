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

function enviar(vT0,vT1,vT2,vT3,vT4,vT5,vT6,vT7){
	  document.getElementById("formbvtcat4:bcodcat1_input").value= rTrim(vT0);
	  document.getElementById("formbvtcat4:bcodcat2_input").value= rTrim(vT1);
	  document.getElementById("formbvtcat4:bcodcat3_input").value= rTrim(vT2);
	  document.getElementById("formbvtcat4:codcat4").value= rTrim(vT3);
	  document.getElementById("formbvtcat4:descat4").value= rTrim(vT4);
	  document.getElementById("formbvtcat4:equicat4").value= rTrim(vT5);
	  document.getElementById("formbvtcat4:tippro").value= rTrim(vT6);	  
	  document.getElementById("formbvtcat4:vop").value= rTrim(vT7);	 
	  updateInput('formbvtcat4:bcodcat1_input', '#F5F6CE');
	  updateInput('formbvtcat4:bcodcat2_input', '#F5F6CE');
	  updateInput('formbvtcat4:bcodcat3_input', '#F5F6CE');
	  updateInput('formbvtcat4:codcat4', '#F5F6CE');
	}

