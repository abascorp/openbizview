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

import java.sql.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * @author Andres Dominguez 08/02/2009
 */
/**
 * datos de cualquier tabla pasada por parametro
 *  */
public class PntGenerica extends Bd {
//Variables serán utilizadas para capturar mensajes de errores de Oracle

//Variables para select
private int columns;
private String[][] arr;
private int rows;

	 /**
	 * Leer datos de cualquier tabla pasada por parametro
	 **/

    public void  selectPntGenerica(String strCadena, String pool) throws  NamingException {
        //Pool de conecciones JNDI
        Context initContext = new InitialContext();
        DataSource ds = (DataSource) initContext.lookup(pool);
        try {
            Statement stmt;
            ResultSet rs = null;
            Connection con = ds.getConnection();

            //Class.forName(getDriver());
            //con = DriverManager.getConnection(
            //        getUrl(), getUsuario(), getClave());
            stmt = con.createStatement(
               		ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

			////System.out.println(strCadena);
            try{
            rs = stmt.executeQuery(strCadena);
            rows = 1;
		    rs.last();
		    rows = rs.getRow();
            ////System.out.println(rows);

            ResultSetMetaData rsmd = rs.getMetaData();
        	columns = rsmd.getColumnCount();
		    ////System.out.println(columns);
        	arr = new String[rows][columns];

            int i = 0;
		    rs.beforeFirst();
            while (rs.next()){
                for (int j = 0; j < columns; j++)
				arr [i][j] = rs.getString(j+1);
				i++;
               }
                } catch (SQLException e) {
                }
            stmt.close();
            con.close(); 
            rs.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
   	 * Leer datos de cualquier tabla pasada por parametro
   	 * @param strCadena query para oracle
   	 * @param strCadena1 query para postgres
   	 * @param strCadena2 query para sqlserver
   	 **/

       public void  selectPntGenericaMDB(String strCadena, String strCadena1, String strCadena2, String pool) throws  NamingException {
           //Pool de conecciones JNDI
           Context initContext = new InitialContext();
           DataSource ds = (DataSource) initContext.lookup(pool);
           try {
               Statement stmt;
               ResultSet rs = null;
               Connection con = ds.getConnection();
             //Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
        		DatabaseMetaData databaseMetaData = con.getMetaData();
        		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
               //Class.forName(getDriver());
               //con = DriverManager.getConnection(
               //        getUrl(), getUsuario(), getClave());
               stmt = con.createStatement(
                  		ResultSet.TYPE_SCROLL_INSENSITIVE,
                       ResultSet.CONCUR_READ_ONLY);

   			 //System.out.println(strCadena);
               try{
               	switch ( productName ) {
                   case "Oracle":
                   	rs = stmt.executeQuery(strCadena);
                        break;
                   case "PostgreSQL":
                   	rs = stmt.executeQuery(strCadena1);
                        break;
                   case "Microsoft SQL Server":
                   	rs = stmt.executeQuery(strCadena2);
                        break;     
                   }
               
               rows = 1;
   		    rs.last();
   		    rows = rs.getRow();
               ////System.out.println(rows);

               ResultSetMetaData rsmd = rs.getMetaData();
           	columns = rsmd.getColumnCount();
   		    ////System.out.println(columns);
           	arr = new String[rows][columns];

               int i = 0;
   		    rs.beforeFirst();
               while (rs.next()){
                   for (int j = 0; j < columns; j++)
   				arr [i][j] = rs.getString(j+1);
   				i++;
                  }
                   } catch (SQLException e) {
                   }
               stmt.close();
               con.close(); 
               rs.close();

           } catch (Exception e) {
               e.printStackTrace();
           }
       }
       
       
       



 /**
 * @return Retorna el arreglo
 **/
public String[][] getArray(){
	return arr;
}

/**
 * @return Retorna número de filas
 **/
public int getRows(){
	return rows;
}
/**
 * @return Retorna número de columnas
 **/
public int getColumns(){
	return columns;
}



}
