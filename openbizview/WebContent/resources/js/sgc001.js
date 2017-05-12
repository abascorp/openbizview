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
	    document.getElementById("formsgc001:vop").value="0";
	    clearUpdateInput('formsgc001:comp_input', 'white');
	    clearUpdateInput('formsgc001:area_input', 'white');
	    clearUpdateInput('formsgc001:codigo', 'white');
	}
	
	//function enviar(vT0,vT1,vT2,vT3,vT4,vT5,vT6,vT7,vT8,vT9,vT10,vT11,vT12,vT13,vT14,vT15,vT16,vT17,vT18,vT19,vT20,vT21,vT22,vT23,vT24,vT25)
	function enviar(vT0,vT1,vT2,vT3,vT4,vT5,vT6,vT7,vT8,vT9,vT10,vT11,vT12,vT13,vT14,vT15,vT16,vT17,vT18,vT19,vT20,vT21,vT22,vT23)
	{
		   //alert(vT5);
		  document.getElementById("formsgc001:tabView:comp_input").value= rTrim(vT0);
		  document.getElementById("formsgc001:tabView:area_input").value= rTrim(vT1);
		  document.getElementById("formsgc001:tabView:codigo").value= rTrim(vT2);
		  document.getElementById("formsgc001:tabView:desc").value  = rTrim(vT3);
		  document.getElementById("formsgc001:tabView:respon_input").innerHTML= rTrim(vT4);
		  document.getElementById("formsgc001:tabView:calculo_input").innerHTML = vT5;
		  document.getElementById("formsgc001:tabView:fuente_input").innerHTML= rTrim(vT6);
		  document.getElementById("formsgc001:tabView:proces_input").innerHTML= rTrim(vT7);
		  document.getElementById("formsgc001:tabView:period_input").value= rTrim(vT8);
		  document.getElementById("formsgc001:tabView:compor").value= rTrim(vT9);
		  document.getElementById("formsgc001:tabView:nivapp_input").value= rTrim(vT10);
		  document.getElementById("formsgc001:tabView:estatu_input").value= rTrim(vT11);
		  document.getElementById("formsgc001:tabView:vigenc_input").value= rTrim(vT12);
		  document.getElementById("formsgc001:vop").value= rTrim(vT13);
		  document.getElementById("formsgc001:tabView:tvalm_input").value= rTrim(vT14);
		  document.getElementById("formsgc001:tabView:feccam_input").value= rTrim(vT15);
		  document.getElementById("formsgc001:tabView:meta").value= rTrim(vT16);
		  document.getElementById("formsgc001:tabView:resmet").value= rTrim(vT17);
		  document.getElementById("formsgc001:tabView:tvalti_input").value= rTrim(vT18);
		  document.getElementById("formsgc001:tabView:feccai_input").value= rTrim(vT19);
		  document.getElementById("formsgc001:tabView:tolinf").value= rTrim(vT20);
		  //document.getElementById("formsgc001:tabView:obvtoli_input").value= rTrim(vT21);
		  document.getElementById("formsgc001:tabView:tvalts_input").value= rTrim(vT21);
		  document.getElementById("formsgc001:tabView:feccas_input").value= rTrim(vT22);
		  document.getElementById("formsgc001:tabView:tolsup").value= rTrim(vT23);
		  //document.getElementById("formsgc001:tabView:obvtols_input").value= rTrim(vT25);
		  
		  
		  updateInput('formsgc001:tabView:comp_input', '#F2F2F2');
		  updateInput('formsgc001:tabView:area_input', '#F2F2F2');
		  updateInput('formsgc001:tabView:codigo', '#F2F2F2');
		  
		  //alert(document.getElementById("formsgc001:tabView:calculo_input").innerHTML);
		}
	