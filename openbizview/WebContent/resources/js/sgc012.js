	/*
	 *  Copyright (C) 2011  ANDRES DOMINGUEZ, CHRISTIAN DÃ�AZ
	
	    Este programa es software libre: usted puede redistribuirlo y/o modificarlo 
	    bajo los tÃ©rminos de la Licencia PÃºblica General GNU publicada 
	    por la FundaciÃ³n para el Software Libre, ya sea la versiÃ³n 3 
	    de la Licencia, o (a su elecciÃ³n) cualquier versiÃ³n posterior.
	
	    Este programa se distribuye con la esperanza de que sea Ãºtil, pero 
	    SIN GARANTÃ�A ALGUNA; ni siquiera la garantÃ­a implÃ­cita 
	    MERCANTIL o de APTITUD PARA UN PROPÃ“SITO DETERMINADO. 
	    Consulte los detalles de la Licencia PÃºblica General GNU para obtener 
	    una informaciÃ³n mÃ¡s detallada. 
	
	    DeberÃ­a haber recibido una copia de la Licencia PÃºblica General GNU 
	    junto a este programa. 
	    En caso contrario, consulte <http://www.gnu.org/licenses/>.
	 */
	
	function limpiar()
	{  //Llamado por el boton limpiar
	    document.getElementById("formsgc012:vop").value="0"; 
	    clearUpdateInput('formsgc012:comp_input', 'white');
	    clearUpdateInput('formsgc012:area_input', 'white');
	    clearUpdateInput('formsgc012:codigo_input', 'white');
	    clearUpdateInput('formsgc012:anocal', 'white');
	    clearUpdateInput('formsgc012:mescal', 'white');
	}
	
	function enviar(vT0,vT1,vT2,vT3,vT4,vT5,vT6,vT7,vT8,vT9,vT10,vT11,vT12,vT13){
		  document.getElementById("formsgc012:comp_input").value= rTrim(vT0);
		  document.getElementById("formsgc012:area_input").value= rTrim(vT1);
		  document.getElementById("formsgc012:codigo_input").value= rTrim(vT2);
		  document.getElementById("formsgc012:anocal").value= rTrim(vT3);
		  document.getElementById("formsgc012:mescal").value= rTrim(vT4);
		  document.getElementById("formsgc012:invana").value= rTrim(vT5);
		  document.getElementById("formsgc012:rrhh").value= rTrim(vT6);
		  document.getElementById("formsgc012:superv").value= rTrim(vT7);
		  document.getElementById("formsgc012:medamb").value= rTrim(vT8);
		  document.getElementById("formsgc012:equipo").value= rTrim(vT9);
		  document.getElementById("formsgc012:infstr").value= rTrim(vT10);
		  document.getElementById("formsgc012:proced").value= rTrim(vT11);
		  document.getElementById("formsgc012:otros").value= rTrim(vT12);
		  document.getElementById("formsgc012:vop").value= rTrim(vT13);
		  updateInput('formsgc012:comp_input', '#F2F2F2');
		  updateInput('formsgc012:area_input', '#F2F2F2');
		  updateInput('formsgc012:codigo_input', '#F2F2F2');
		  updateInput('formsgc012:anocal', '#F2F2F2');
		  updateInput('formsgc012:mescal', '#F2F2F2');
		}
	/*
	 function alertFilename()
	    {
	        var thefile = document.getElementById('foto');
	        document.getElementById("formsgc012:archivo").value= thefile.value; 
	        //alert(thefile.value);
	    }
	*/