/*
 *  Copyright (C) 2011 - 2016  DVCONSULTORES

    Este programa es software libre: usted puede redistribuirlo y/o modificarlo 
    bajo los terminos de la Licencia Pública General GNU publicada 
    por la Fundacion para el Software Libre, ya sea la version 3 
    de la Licencia, o (a su eleccion) cualquier version posterior.

    Este programa se distribuye con la esperanza de que sea útil, pero 
    SIN GARANTiA ALGUNA; ni siquiera la garantia implicita 
    MERCANTIL o de APTITUD PARA UN PROPoSITO DETERMINADO. 
    Consulte los detalles de la Licencia Pública General GNU para obtener 
    una informacion mas detallada. 

    Deberia haber recibido una copia de la Licencia Pública General GNU 
    junto a este programa. 
    En caso contrario, consulte <http://www.gnu.org/licenses/>.
 */

package org.openbizview.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/*
 * Clase para imprimir log de reportes
 */
public class LogReportesImpresos {
	
	Connection con;
	PreparedStatement pstmt = null;
	String productName; //Manejador de base de datos
	
	/*
	 * Imprime log de reportes en la tabla bvt006
	 * Parámetros: codrepm desrep, jni, login
	 */
	public void insertBvt006(String codrep, String desrep, String login, String hora, String instancia)  {
        //Pool de conecciones JNDI(). Cambio de metodologia de conexion a Bd. Julio 2010
        try {
        	Context initContext = new InitialContext();     
     		DataSource ds = (DataSource) initContext.lookup(Bd.JNDI);
            con = ds.getConnection();
     		String query = "";
            	query = "INSERT INTO Bvt006 VALUES (trim('" + codrep.toUpperCase() + "'),'" + desrep.toUpperCase() + "','" + login + "','" + hora + "'," + instancia + ")";
           
            pstmt = con.prepareStatement(query);
            //pstmt.setString(1, codrep.toUpperCase());
            //pstmt.setString(2, desrep.toUpperCase());
            //pstmt.setString(3, login);
            //pstmt.setInt(4, Integer.parseInt(instancia));
            //System.out.println(query);
            try {
                //Avisando
            	pstmt.executeUpdate();
             } catch (SQLException e)  {
            	e.printStackTrace();
            }
            
            pstmt.close();
            con.close();
        } catch (Exception e) {
        	e.printStackTrace();
        }	
    }

}
