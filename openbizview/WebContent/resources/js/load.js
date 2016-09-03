	/*
	 * Funciones estandart que pueden ser utilizadas en todas las páginas
	 * 
	 */
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
	
	//Crea el foco en el input usuario, al cargar la página
	function inicio(){
	    	document.getElementById("loginform:usuario").focus();
	}
	
	
	/******************************************************************************
	 ******************************************************************************/
	
	function lTrim(sStr){
	    while (sStr.charAt(0) == " ")
	        sStr = sStr.substr(1, sStr.length - 1);
	    return sStr;
	}
	
	/******************************************************************************
	  *********************Click para acceso directo en lista de valores************
	 ******************************************************************************/
	function fm_mlista(vtId)
	{
	    document.getElementById(vtId).value='%';
	    document.getElementById(vtId).focus();   
	        $('#'+vtId).keydown();
	
	}
	
	
	function info(){
	    // Si el mensaje que retorna es acceso
	    window.open('../ct/acercade.jsp','target_blank','height=190,width=550','top=100,resizable=false,scrollbars=no,toolbar=no,status=no');
	
	}
	/******************************************************************************
	  *********************Funciones genéricas************
	 ******************************************************************************/
	
	function rTrim(sStr){
	    while (sStr.charAt(sStr.length - 1) == " ")
	        sStr = sStr.substr(0, sStr.length - 1);
	    return sStr;
	}
	
	function fm_check(vTcheck){
	
		var chkBoxes = $('input[name='+vTcheck+']');
	    chkBoxes.prop("checked", !chkBoxes.prop("checked"));
	}
	
	function cursor(){ 
	    document.body.style.cursor = "pointer"; 
	} 
	
	function uncursor(){ 
	    document.body.style.cursor = ""; 
	} 
	
	
	function updateInput(vinputid, vcolor){
		document.getElementById(vinputid).style.backgroundColor = vcolor;
		document.getElementById(vinputid).readOnly = true;
	}
	
	function clearUpdateInput(vinputid, vcolor){
		document.getElementById(vinputid).style.backgroundColor = vcolor;
		document.getElementById(vinputid).readOnly = false;
	}
	
	
	function HideUpload(){
		jQuery('#upload').hide("linear"); //oculto mediante id
	}
	
	
	///Menus
	
	function mnuBas(){
		jQuery('.mnuBas').show();
		jQuery('.mnuAdmin').hide();
		jQuery('.mnuTask').hide();
		jQuery('.mnuSeg').hide();
		jQuery('.mnuRep').hide();
		//Protinal
		jQuery('.mnuGto').hide();
		jQuery('.mnuBudget').hide();
		jQuery('.mnuVta').hide();
		jQuery('.mnuSgc').hide();
		jQuery('.mnuPre').hide();
		jQuery('.mnuCha').hide();
		jQuery('.mnuCst').hide();
		jQuery('.mnuInd').hide();
	}
	
	function mnuAdmin(){
		jQuery('.mnuBas').hide();
		jQuery('.mnuAdmin').show();
		jQuery('.mnuTask').hide();
		jQuery('.mnuSeg').hide();
		jQuery('.mnuRep').hide();
		//Protinal
		jQuery('.mnuGto').hide();
		jQuery('.mnuBudget').hide();
		jQuery('.mnuVta').hide();
		jQuery('.mnuSgc').hide();
		jQuery('.mnuPre').hide();
		jQuery('.mnuCha').hide();
		jQuery('.mnuCst').hide();
		jQuery('.mnuInd').hide();
	}
	
	function mnuTask(){
		jQuery('.mnuBas').hide();
		jQuery('.mnuAdmin').hide();
		jQuery('.mnuTask').show();
		jQuery('.mnuSeg').hide();
		jQuery('.mnuRep').hide();
		//Protinal
		jQuery('.mnuGto').hide();
		jQuery('.mnuBudget').hide();
		jQuery('.mnuVta').hide();
		jQuery('.mnuSgc').hide();
		jQuery('.mnuPre').hide();
		jQuery('.mnuCha').hide();
		jQuery('.mnuCst').hide();
		jQuery('.mnuInd').hide();
	}
	
	function mnuRep(){
		jQuery('.mnuBas').hide();
		jQuery('.mnuAdmin').hide();
		jQuery('.mnuTask').hide();
		jQuery('.mnuSeg').hide();
		jQuery('.mnuRep').show();
		//Protinal
		jQuery('.mnuGto').hide();
		jQuery('.mnuBudget').hide();
		jQuery('.mnuVta').hide();
		jQuery('.mnuSgc').hide();
		jQuery('.mnuPre').hide();
		jQuery('.mnuCha').hide();
		jQuery('.mnuCst').hide();
		jQuery('.mnuInd').hide();
	}
	
	function mnuSeg(){
		jQuery('.mnuBas').hide();
		jQuery('.mnuAdmin').hide();
		jQuery('.mnuTask').hide();
		jQuery('.mnuSeg').show();
		jQuery('.mnuRep').hide();
		//Protinal
		jQuery('.mnuGto').hide();
		jQuery('.mnuBudget').hide();
		jQuery('.mnuVta').hide();
		jQuery('.mnuSgc').hide();
		jQuery('.mnuPre').hide();
		jQuery('.mnuCha').hide();
		jQuery('.mnuCst').hide();
		jQuery('.mnuInd').hide();
	}

	function mnuGto(){
		jQuery('.mnuBas').hide();
		jQuery('.mnuAdmin').hide();
		jQuery('.mnuTask').hide();
		jQuery('.mnuSeg').hide();
		jQuery('.mnuRep').hide();
		//Protinal
		jQuery('.mnuGto').show();
		jQuery('.mnuBudget').hide();
		jQuery('.mnuVta').hide();
		jQuery('.mnuSgc').hide();
		jQuery('.mnuPre').hide();
		jQuery('.mnuCha').hide();
		jQuery('.mnuCst').hide();
		jQuery('.mnuInd').hide();
	}
	

	function mnuBudget(){
		jQuery('.mnuBas').hide();
		jQuery('.mnuAdmin').hide();
		jQuery('.mnuTask').hide();
		jQuery('.mnuSeg').hide();
		jQuery('.mnuRep').hide();
		//Protinal
		jQuery('.mnuGto').hide();
		jQuery('.mnuBudget').show();
		jQuery('.mnuVta').hide();
		jQuery('.mnuSgc').hide();
		jQuery('.mnuPre').hide();
		jQuery('.mnuCha').hide();
		jQuery('.mnuCst').hide();
		jQuery('.mnuInd').hide();
	}


function mnuVta(){
	jQuery('.mnuBas').hide();
	jQuery('.mnuAdmin').hide();
	jQuery('.mnuTask').hide();
	jQuery('.mnuSeg').hide();
	jQuery('.mnuRep').hide();
	//Protinal
	jQuery('.mnuGto').hide();
	jQuery('.mnuBudget').hide();
	jQuery('.mnuVta').show();
	jQuery('.mnuSgc').hide();
	jQuery('.mnuPre').hide();
	jQuery('.mnuCha').hide();
	jQuery('.mnuCst').hide();
	jQuery('.mnuInd').hide();
}


function mnuSgc(){
	jQuery('.mnuBas').hide();
	jQuery('.mnuAdmin').hide();
	jQuery('.mnuTask').hide();
	jQuery('.mnuSeg').hide();
	jQuery('.mnuRep').hide();
	//Protinal
	jQuery('.mnuGto').hide();
	jQuery('.mnuBudget').hide();
	jQuery('.mnuVta').hide();
	jQuery('.mnuSgc').show();
	jQuery('.mnuPre').hide();
	jQuery('.mnuCha').hide();
	jQuery('.mnuCst').hide();
	jQuery('.mnuInd').hide();
}


function mnuPre(){
	jQuery('.mnuBas').hide();
	jQuery('.mnuAdmin').hide();
	jQuery('.mnuTask').hide();
	jQuery('.mnuSeg').hide();
	jQuery('.mnuRep').hide();
	//Protinal
	jQuery('.mnuGto').hide();
	jQuery('.mnuBudget').hide();
	jQuery('.mnuVta').hide();
	jQuery('.mnuSgc').hide();
	jQuery('.mnuPre').show();
	jQuery('.mnuCha').hide();
	jQuery('.mnuCst').hide();
	jQuery('.mnuInd').hide();
}

function mnuCha(){
	jQuery('.mnuBas').hide();
	jQuery('.mnuAdmin').hide();
	jQuery('.mnuTask').hide();
	jQuery('.mnuSeg').hide();
	jQuery('.mnuRep').hide();
	//Protinal
	jQuery('.mnuGto').hide();
	jQuery('.mnuBudget').hide();
	jQuery('.mnuVta').hide();
	jQuery('.mnuSgc').hide();
	jQuery('.mnuPre').hide();
	jQuery('.mnuCha').show();
	jQuery('.mnuCst').hide();
	jQuery('.mnuInd').hide();
}

function mnuCst(){
	jQuery('.mnuBas').hide();
	jQuery('.mnuAdmin').hide();
	jQuery('.mnuTask').hide();
	jQuery('.mnuSeg').hide();
	jQuery('.mnuRep').hide();
	//Protinal
	jQuery('.mnuGto').hide();
	jQuery('.mnuBudget').hide();
	jQuery('.mnuVta').hide();
	jQuery('.mnuSgc').hide();
	jQuery('.mnuPre').hide();
	jQuery('.mnuCha').hide();
	jQuery('.mnuCst').show();
	jQuery('.mnuInd').hide();
}

function mnuInd(){
	jQuery('.mnuBas').hide();
	jQuery('.mnuAdmin').hide();
	jQuery('.mnuTask').hide();
	jQuery('.mnuSeg').hide();
	jQuery('.mnuRep').hide();
	//Protinal
	jQuery('.mnuGto').hide();
	jQuery('.mnuBudget').hide();
	jQuery('.mnuVta').hide();
	jQuery('.mnuSgc').hide();
	jQuery('.mnuPre').hide();
	jQuery('.mnuCha').hide();
	jQuery('.mnuCst').hide();
	jQuery('.mnuInd').show();
}
	//Modal delete
	function modalDelete(){
		$("#myModalDelete").modal();
	}
	

	//Modal delete
	function modalHide(){
		$("#myModalDelete").modal('hide');
	}
	
	function chpassSuccess(vT0){
		alert(vT0);
		if(vT0=='1'){
			PF('idleDialog').show();
		}
	}
