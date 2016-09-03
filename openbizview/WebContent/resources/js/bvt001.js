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
	
	function limpiar()
	{  //Llamado por el boton limpiar
	    document.getElementById("formBvt001:vop").value="0";
	    clearUpdateInput('formBvt001:codrep', 'white');
	}
	
	function enviar(vT0,vT1,vT2,vT3,vT4){
		  document.getElementById("formBvt001:codrep").value= rTrim(vT0);
		  document.getElementById("formBvt001:desrep").value= rTrim(vT1);
		  document.getElementById("formBvt001:comrep").value= rTrim(vT2);
		  document.getElementById("formBvt001:vop").value= rTrim(vT3);
		  document.getElementById("formBvt001:grupo_input").value= rTrim(vT4);
		  updateInput('formBvt001:codrep', '#F5F6CE');
		}
	
	function imprimir(rep, usuario, rol, descripcion, instancia, locale){
		//alert(locale);
	  // Si el mensaje que retorna es acceso
	    window.open('../ct/reportes.jsp?reporte='+rep+'.rptdesign'
				+ "&usuario=" + usuario 
				+ "&rol=" + rol
				+ "&replog=" + rep
				+ "&descripcion=" + descripcion
				+ "&hora=" + displayTime(locale)
				+ "&instancia=" + instancia);
	   //alert(displayTime());
	}
	
	

	//Muestra la hora del lado del cliente
	function displayTime(vT0)
	   {
	       var localTime = new Date();
	       var month= localTime.getMonth()+1; 
	       var cadena = month.toString();
	       var hora = localTime.getHours();
	       var minutos = localTime.getMinutes();
		   //var monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun",  "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
		   var monthNames = ["01", "02", "03", "04", "05", "06",  "07", "08", "09", "10", "11", "12"];
		   if(vT0=='es'){
			   monthNames = ["01", "02", "03", "04", "05", "06",  "07", "08", "09", "10", "11", "12"];
		   } else {
			   monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun",  "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
		   }
		   var mes = monthNames[localTime.getMonth()];
	      
	       if(cadena.length==1){
	       	month = '0'+month;
	       }
	       if(hora.length==1){
	    	   hora = '0'+hora;
	          }
	       if(minutos.length==1){
	    	   minutos = '0'+minutos;
	          } 
	       
	       var fechaHora = localTime.getDate()+"/"+mes+"/"+localTime.getFullYear()+" "+formatAMPM(localTime);
	     
		   //alert(month + ' ' + mes);
		 
		   return fechaHora;
	   
	   } 
	
	function formatAMPM(date) {
		  var hours = date.getHours();
		  var minutes = date.getMinutes();
		  var segundos = date.getSeconds();
		  var ampm = hours >= 12 ? 'PM' : 'AM';
		  hours = hours % 12;
		  hours = hours ? hours : 12; // the hour '0' should be '12'
		  minutes = minutes < 10 ? '0'+minutes : minutes;
		  var strTime = hours + ':' + minutes + ':' + segundos + " " + ampm;
		  return strTime;
		}
	
	function log(){	
		
		document.getElementById("formBvt001:codrep").value= "1";
		document.getElementById("formBvt001:desrep").value= "1";
		document.getElementById("formBvt001:comrep").value= "1";
	
	    setTimeout("document.getElementById('formBvt001:codrep').value=''",300);
	   	setTimeout("document.getElementById('formBvt001:desrep').value=''",300);  
	   	setTimeout("document.getElementById('formBvt001:comrep').value=''",300);  
	   	
	}
	
	
	function detalle(vT0,vT1,vT2,vT3){
		$("#txt_det_1").text(vT0);
		$("#txt_det_2").text(vT1);
		$("#txt_det_3").text(vT2);
		$("#txt_det_4").text(vT3);
	}
	
	function modal(vT0){
		if(vT0=="1"){
		$( document ).ready( function() {
		    $( '#myModal' ).modal( 'toggle' );
		});
	}
	}
	
	
	function dismissModal(){
		$( document ).ready( function() {
		    $( '#myModal' ).modal( 'hide' );
		});
	}
	
	
		//Morris charts snippet - js
		function chart(vT0,vT1,vT2,vT3,vT4,vT5,vT6,vT7,vT8,vT9){	
			
		$.getScript('../resources/js/raphael.min.js',function(){
		$.getScript('../resources/js/morris.min.js',function(){     
			Morris.Bar({
		        element: 'morris-bar-chart',
		        data: [{
		            report: vT5,
		            geekbench: vT0
		        }, {
		        	report: vT6,
		            geekbench: vT1
		        }, {
		        	report: vT7,
		            geekbench: vT2
		        }, {
		        	report: vT8,
		            geekbench: vT3
		        }, {
		        	report: vT9,
		            geekbench: vT4
		        }],
		        xkey: 'report',
		        ykeys: ['geekbench'],
		        labels: ['Total'],
		        barRatio: 0.4,
		        xLabelAngle: 35,
		        hideHover: 'auto',
		        resize: true
		    });
		
		});
		});}
