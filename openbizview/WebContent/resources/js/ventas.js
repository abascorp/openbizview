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
	    document.getElementById("formventas:vop").value="0";
	    clearUpdateInput('formventas:codven_input', 'white');
	}
	
	function enviar(vT0,vT1,vT2,vT3,vT4){
		  document.getElementById("formventas:codven_input").value= rTrim(vT0);
		  document.getElementById("formventas:porcvis").value= rTrim(vT1);
		  document.getElementById("formventas:anocal").value= rTrim(vT2);
		  document.getElementById("formventas:mescal").value= rTrim(vT3);
		  document.getElementById("formventas:vop").value= rTrim(vT4);
		  updateInput('formventas:codven_input', '#F2F2F2');
		}