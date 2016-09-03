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
	    document.getElementById("formcst004:vop").value="0";
	    clearUpdateInput('formcst004:anocal', 'white');
	    clearUpdateInput('formcst004:mescal', 'white');
	    clearUpdateInput('formcst004:semana', 'white');
	}
	
	function enviar(vT0,vT1,vT2,vT3,vT4,vT5){
		//window.alert("llame al metodo");
		//window.alert(vT0);
		//window.alert(vT1);
		//window.alert(vT2);
		//window.alert(vT3);
		//window.alert(vT4);
		  document.getElementById("formcst004:anocal").value= rTrim(vT0);
		  document.getElementById("formcst004:mescal").value= rTrim(vT1);
		  document.getElementById("formcst004:semana").value= rTrim(vT2);
		  document.getElementById("formcst004:fecini_input").value= rTrim(vT3);
		  document.getElementById("formcst004:fecfin_input").value= rTrim(vT4);
		  document.getElementById("formcst004:vop").value= rTrim(vT5);
		  updateInput('formcst004:anocal', '#F2F2F2');
		  updateInput('formcst004:mescal', '#F2F2F2');
		  updateInput('formcst004:semana', '#F2F2F2');
		}
