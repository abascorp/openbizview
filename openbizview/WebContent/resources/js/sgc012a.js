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
	    document.getElementById("formsgc012a:vop").value="0";
	    clearUpdateInput('formsgc012a:comp_input', 'white');
	    clearUpdateInput('formsgc012a:area_input', 'white');
	    clearUpdateInput('formsgc012a:codigo_input', 'white');
	    clearUpdateInput('formsgc012a:anocal_input', 'white');
	    clearUpdateInput('formsgc012a:mescal_input', 'white');
	    clearUpdateInput('formsgc012a:accion', 'white');
	}
	
	function enviar(vT0,vT1,vT2,vT3,vT4,vT5,vT6,vT7,vT8,vT9){
		  document.getElementById("formsgc012a:comp_input").value= rTrim(vT0);
		  document.getElementById("formsgc012a:area_input").value= rTrim(vT1);
		  document.getElementById("formsgc012a:codigo_input").value= rTrim(vT2);
		  document.getElementById("formsgc012a:anocal_input").value= rTrim(vT3);
		  document.getElementById("formsgc012a:mescal_input").value= rTrim(vT4);
		  document.getElementById("formsgc012a:accion").value= rTrim(vT5);
		  document.getElementById("formsgc012a:respon").value= rTrim(vT6);
		  document.getElementById("formsgc012a:fecacc_input").value= rTrim(vT7);
		  document.getElementById("formsgc012a:regist").value= rTrim(vT8);
		  document.getElementById("formsgc012a:vop").value= rTrim(vT9);
		  updateInput('formsgc012a:comp_input', '#F2F2F2');
		  updateInput('formsgc012a:area_input', '#F2F2F2');
		  updateInput('formsgc012a:codigo_input', '#F2F2F2');
		  updateInput('formsgc012a:anocal_input', '#F2F2F2');
		  updateInput('formsgc012a:mescal_input', '#F2F2F2');
		  updateInput('formsgc012a:accion', '#F2F2F2');
		}