<%-----------------------------------------------------------------------------
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
-----------------------------------------------------------------------------%>
<!--
To change this template, choose Tools | Templates
and open the template in the editor.
-->
<%@page contentType="text/html" pageEncoding="UTF-8" session="true"%>
<jsp:useBean id="bd" class="org.openbizview.util.Bd" scope="session"/>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=utf-8">
        <link rel="shortcut icon"  href="../img/favicon.ico"/>
        <LINK REL="stylesheet" HREF="../css/styles.css" TYPE="text/css">
        <title>&nbsp;<%=bd.getMessage("tituloAce")%></title>
    </head>
    <body>
        <center>
            <b>Versión:&nbsp;</b><b style="color:red" ><%=bd.getMessage("versionbiz")%></b><br>
            <b><%=bd.getMessage("os")%></b><br>
            <b><%=bd.getMessage("gnu")%></b>&nbsp;&nbsp;<A HREF="http://es.wikipedia.org/wiki/GNU_General_Public_License" TARGET="_new"><b>Gnu</b></A><br>
            <a><%=bd.getMessage("des")%></a>&nbsp;&nbsp;<A HREF="http://www.dvconsultores.com" TARGET="_new"><b>Web</b></A><br>
        </center>
    </body>
</html>
