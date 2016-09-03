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
	    document.getElementById("formsgc012b:vop").value="0";
	    clearUpdateInput('formsgc012b:comp', 'white');
	    clearUpdateInput('formsgc012b:area', 'white');
	    clearUpdateInput('formsgc012b:codigo', 'white');
	    clearUpdateInput('formsgc012b:anocal', 'white');
	    clearUpdateInput('formsgc012b:mescal', 'white');
	}
	
	function enviar(vT0,vT1,vT2,vT3,vT4,vT5,vT6){
		  document.getElementById("formsgc012b:comp_input").value= rTrim(vT0);
		  document.getElementById("formsgc012b:area_input").value= rTrim(vT1);
		  document.getElementById("formsgc012b:codigo_input").value= rTrim(vT2);
		  document.getElementById("formsgc012b:anocal_input").value= rTrim(vT3);
		  document.getElementById("formsgc012b:mescal_input").value= rTrim(vT4);
		  document.getElementById("formsgc012b:regist").value= rTrim(vT5);
		  document.getElementById("formsgc012b:vop").value= rTrim(vT6);
		  updateInput('formsgc012b:comp', '#F2F2F2');
		  updateInput('formsgc012b:area', '#F2F2F2');
		  updateInput('formsgc012b:codigo', '#F2F2F2');
		  updateInput('formsgc012b:anocal', '#F2F2F2');
		  updateInput('formsgc012b:mescal', '#F2F2F2');
		}