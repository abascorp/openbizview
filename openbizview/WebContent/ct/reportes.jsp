<!-- 
     Copyright (C) 2011 - 2016  DVCONSULTORES

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
 -->
<%@ page session="true" language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/birt.tld" prefix="birt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-----------------------------------------------------------------------------
    Expected java beans
-----------------------------------------------------------------------------%>

<%-------------------------------------------------------------------------------------------
  Define variables de session.
--------------------------------------------------------------------------------------------%>
<%
boolean sesion = false;
HttpSession sesionOk = request.getSession();
if (sesionOk.getAttribute("usuario") == null) {
	sesion = false;
response.sendRedirect("/openbizview/login.xhtml");

}//Fin valida que usuario no sea nulo
%>
<html>
    <head>
        <META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=utf-8">
        <jsp:useBean id="log" class="org.openbizview.util.LogReportesImpresos" scope="session"/>
        <title></title>
    </head>
    <body>

       <%   
       //El viejo jsp me ayudó a insertar el log, ya que por la página xhtml, cuando el datatable es lazy nada que ver
       //Ni idea porqué no deja. 01/09/2014      
   
        // Recoge parámetros para pasar a reportes BIRT
       String rep= null;
       String codcia = request.getParameter("codcia");
       String area = request.getParameter("area");
       String indicador = request.getParameter("indicador");
       String captura = request.getParameter("reporte");
       String replog = request.getParameter("replog");
       String usuario = request.getParameter("usuario");
       String descripcion = request.getParameter("descripcion");
       String hora = request.getParameter("hora");
       String rol = request.getParameter("rol");
       String instancia = request.getParameter("instancia");
        if (captura!=null){
         rep =  captura;
        }
      
       log.insertBvt006(replog, descripcion,  usuario, hora, instancia);
        %>
        <birt:viewer id='birtViewer' reportDesign= "<%=rep%>"  isHostPage="true">
            <birt:param  name="CODCIA" value="<%=codcia%>"  ></birt:param>
            <birt:param  name="AREA" value="<%=area%>"  ></birt:param>
            <birt:param  name="INDICADOR" value="<%=indicador%>"  ></birt:param>
            <birt:param  name="ROL" value="<%=rol%>"  ></birt:param>
            <birt:param  name="USUARIO" value="<%=usuario%>"  ></birt:param>
            <birt:param  name="INSTANCIA" value="<%=instancia%>"  ></birt:param>
        </birt:viewer>
    </body>
</html>
