/*
 *  Copyright (C) 2011 - 2016  DVCONSULTORES,

    Este programa es software libre: usted puede redistribuirlo y/o modificarlo 
    bajo los términos de la Licencia Pública General GNU publicada 
    por la Fundación para el Software Libre, ya sea la versión 3 
    de la Licencia, o (a su elección) cualquier versión posterior.

    Este programa se distribuye con la esperanza de que sea útil, pero 
    SIN GARANTÍA ALGUNA; ni siquiera la garantía implícita 
    MERCANTIL o de APTITUD PARA UN PROPÓSITO DETERMINADO. 
    Consulte los detaloles de la Licencia Pública General GNU para obtener 
    una información más detallada. 

    Debería haber recibido una copia de la Licencia Pública General GNU 
    junto a este programa. 
    En caso contrario, consulte <http://www.gnu.org/licenses/>.
 */

function enviar(vT0,vT1,vT2,vT3,vT4,vT5,vT6,vT7,vT8,vT9,vT10,vT11,vT12,vT13){

	
	  if(vT5=='0'){
		  vT5='2';
	  }
	  if(vT10=='0'){
		  vT10='1';
	  }	  
	  
	  if(vT12=='0'){
		  document.getElementById("formmailconfig:btnStartTask").style.display = "none";
		  document.getElementById("formmailconfig:btnStopTask").style.display = "";
	  } else {
		  document.getElementById("formmailconfig:btnStartTask").style.display = "";
		  document.getElementById("formmailconfig:btnStopTask").style.display = "none";
	  }
	 
	  listarBt2();
	  //Bloquea trigger, codigo de tarea y reporte
	  //Se bloquea reporte para no tener que crear operaciones nuevamente de leer parámtros
	  //Si desea cambiar reporte, eliminar tarea y crear una nueva
	  updateInput('formmailconfig:tabView:tarea', '#F5F6CE');
	  updateInput('formmailconfig:tabView:prg', '#F5F6CE');

	  //
	  //
	  //console.log(vT12);
	  document.getElementById("formmailconfig:tabView:tarea").value= rTrim(vT0);
	  document.getElementById("formmailconfig:tabView:prg").value= rTrim(vT1);
	  //document.getElementById("formmailconfig:tabView:hora").value= rTrim(vT2);
	  //document.getElementById("formmailconfig:tabView:minutos").value= rTrim(vT3);
	  document.getElementById("formmailconfig:tabView:frecuencia").value= rTrim(vT4);
	  document.getElementById("formmailconfig:tabView:dias").value= rTrim(vT5);
	  document.getElementById("formmailconfig:tabView:reporte_input").value= rTrim(vT6);
	  document.getElementById("formmailconfig:tabView:idgrupo_input").value= rTrim(vT7);
	  document.getElementById("formmailconfig:tabView:asunto").value= vT8;
	  document.getElementById("formmailconfig:tabView:contenido_input").innerHTML= vT9;
	  document.getElementById("formmailconfig:tabView:diames").value= rTrim(vT10);
	  document.getElementById("formmailconfig:tabView:ini_input").value= rTrim(vT11);	
	  document.getElementById("formmailconfig:tabView:horarep").value= rTrim(vT13);	
	  //document.getElementById("formmailconfig:ruta").value = "NA";

	}

    function listarBt1(){
    	document.getElementById("bt1").style.display = '';
    	document.getElementById("bt2").style.display = 'none';
    }
    
    function listarBt2(){
    	document.getElementById("bt1").style.display = 'none';
    	document.getElementById("bt2").style.display = '';  
    }


	
	function inputs(){
		var opt = document.getElementById("formmailconfig:tabView:opciones").value;
		//alert(opt);
        if(opt=='2'){
			document.getElementById("formmailconfig:tabView:asunto").value = "NA";
			document.getElementById("formmailconfig:tabView:contenido_input").value= "NA";
		} 
	}

	/*
	function loadT(){
		document.getElementById("formmailconfig:ruta").value = "NA";
	}*/
