/*
o *  Copyright (C) 2011  ANDRES DOMINGUEZ

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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
	/**
	 *
	 * @author Andres
	 */

	@ManagedBean
	@ViewScoped
	public class Sgc013 extends Bd implements Serializable {
	
	/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		private LazyDataModel<Sgc013> lazyModel;  
		
		
		/**
		 * @return the lazyModel
		 */
		public LazyDataModel<Sgc013> getLazyModel() {
			return lazyModel;
		}	
	
	@PostConstruct
	public void init() {
		
		lazyModel  = new LazyDataModel<Sgc013>(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 7217573531435419432L;
			
            @Override
			public List<Sgc013> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) { 
            	//Filtro
            	if (filters != null) {
               	 for(Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
               		 String filterProperty = it.next(); // table column name = field name
                     filterValue = filters.get(filterProperty);
               	 }
                }
            	try { 
            		//Consulta
					select(first, pageSize,sortField, filterValue);
					//Counter
					counter(filterValue);
					//Contador lazy
					lazyModel.setRowCount(rows);  //Necesario para crear la paginación
				} catch (SQLException | NamingException | ClassNotFoundException e) {	
					e.printStackTrace();
				}             
				return list;  
            } 
            
            
            //Arregla bug de primefaces
            @Override
            /**
			 * Arregla el Issue 1544 de primefaces lazy loading porge generaba un error
			 * de divisor equal a cero, es solamente un override
			 */
            public void setRowIndex(int rowIndex) {
                /*
                 * The following is in ancestor (LazyDataModel):
                 * this.rowIndex = rowIndex == -1 ? rowIndex : (rowIndex % pageSize);
                 */
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                }
                else
                    super.setRowIndex(rowIndex % getPageSize());
            }
            
		};
	}
	
	
	private String codcia = "";
	private String descia = "";
	
	private String codarea = "";
	private String desarea = "";
	
	private String codind = "";
	private String desind = "";
	
	private String vlRol = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("rol"); //Usuario logeado
	private int rows;
	private Object filterValue = "";
	private List<Sgc013> list = new ArrayList<Sgc013>();
	private String zinstancia = "";
	
	private String zcodcia = "";
	private String zdescia = "";
	
	private String zcodarea = "";
	private String zdesarea = "";

	private String zcodind = "";
	private String zdesind = "";
	String[][] tabla;

	public String getZinstancia() {
		return zinstancia;
	}

	public void setZinstancia(String zinstancia) {
		this.zinstancia = zinstancia;
	}

	public String getVlRol() {
		return vlRol;
	}

	public void setVlRol(String vlRol) {
		this.vlRol = vlRol;
	}

	public String getCodcia() {
		return codcia;
	}

	public void setCodcia(String codcia) {
		this.codcia = codcia;
	}

	public String getDescia() {
		return descia;
	}

	public void setDescia(String descia) {
		this.descia = descia;
	}
	
	public String getCodarea() {
		return codarea;
	}

	public void setCodarea(String codarea) {
		this.codarea = codarea;
	}

	public String getDesarea() {
		return desarea;
	}

	public String getCodind() {
		return codind;
	}

	public void setCodind(String codind) {
		this.codind = codind;
	}

	public String getDesind() {
		return desind;
	}

	public void setDesind(String desind) {
		this.desind = desind;
	}	
	
	public void setDesarea(String desarea) {
		this.desarea = desarea;
	}

	public String getZcodcia() {
		return zcodcia;
	}

	public void setZcodcia (String zcodcia) {
		this.zcodcia = zcodcia;
	}

	public String getZdescia() {
		return zdescia;
	}

	public void setZdescia (String zdescia) {
		this.zdescia = zdescia;
	}
	
	public String getZcodarea() {
		return zcodarea;
	}

	public void setZcodarea(String zcodarea) {
		this.zcodarea = zcodarea;
	}

	public String getZdesarea() {
		return zdesarea;
	}

	public void setZdesarea(String zdesarea) {
		this.zdesarea = zdesarea;
	}

	public String getZcodind() {
		return zcodind;
	}

	public void setZcodind(String zcodind) {
		this.zcodind = zcodind;
	}

	public String getZdesind() {
		return zdesind;
	}

	public void setZdesind (String zdesind) {
		this.zdesind = zdesind;
	}	
	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * @return the list
	 */
	public List<Sgc013> getList() {
		return list;
	}
	
	/**
	 * @param list the list to set
	 */
	public void setList(List<Sgc013> list) {
		this.list = list;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Variables seran utilizadas para capturar mensajes de errores de Oracle y parametros de metodos
	PntGenerica consulta = new PntGenerica();
	boolean vGacc; //Validador de opciones del menú
	private String login = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario"); //Usuario logeado
	//private String rol = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("rol"); //Usuario logeado
	FacesMessage msj =  null;
	
	
	
	//Coneccion a base de datos
	//Pool de conecciones JNDISB_SB
	//Coneccion a base de datos
	//Pool de conecciones JNDISB_SBFARM
	Connection con;
	PreparedStatement pstmt = null;
	ResultSet r;

	
	private int validarOperacion=0;
	
	
	public int getValidarOperacion() {
		return validarOperacion;
	}
	
	
	public void setValidarOperacion(int validarOperacion) {
		this.validarOperacion = validarOperacion;
	}

    /**
     * Leer Datos de paises
     * @throws SQLException 
     * @throws ClassNotFoundException 
     * @throws NamingException
     * @throws IOException 
     **/ 	
  	public void select(int first, int pageSize, String sortField, Object filterValue) throws SQLException, ClassNotFoundException, NamingException  {
  		
  		Context initContext = new InitialContext();     
 		DataSource ds = (DataSource) initContext.lookup(JNDI);
 		con = ds.getConnection();
 		
 		String validar = "1";
		String querycon = "SELECT BI_SGC014('" + login.toUpperCase() + "') AS VALIDAR FROM DUAL";
		
		//System.out.println(querycon);
		//System.out.println(JNDI);
		
		consulta.selectPntGenerica(querycon, JNDI);
		
		rows = consulta.getRows();
		tabla = consulta.getArray();
		//System.out.println(tabla[0][0]);
		
		if (tabla[0][0].equals(validar)) {
			
			String query = " SELECT ";
			query += " SQ2.*   ";
			query += " FROM (select "; 
			query += "       SQ1.*, "; 
			query += "       SQ1.NUMREG as rn "; 
			query += "       from (SELECT  ";
			query += "             A.COMP CODCIA, B.DESCR DESCIA, A.AREA CODAREA, C.DESCR DESAREA, A.CODIGO CODIND, D.NOMIND DESIND, A.USRCRE CODUSER, COUNT(A.CODIGO) NUMREG, A.INSTANCIA ";
			query += "             FROM SGC012 A LEFT OUTER JOIN SGC005 B ON A.COMP = B.CODIGO ";
			query += "                           LEFT OUTER JOIN SGC006 C ON A.COMP = C.COMP AND ";                                                                       
			query += "                                                       A.AREA = C.CODIGO ";
			query += "                           LEFT OUTER JOIN SGC001 D ON A.COMP = D.COMP AND ";                                                                       
			query += "                                                       A.AREA   = D.AREA AND ";                                                                       
			query += "                                                       A.CODIGO = D.CODIGO "; 
			query += "             GROUP BY ";
			query += "             A.COMP, B.DESCR, A.AREA, C.DESCR, A.CODIGO, D.NOMIND, A.USRCRE, A.INSTANCIA ";
			query += "             ) SQ1 ";
			query += "      ) SQ2 "; 
			query += " WHERE "; 
			query += " SQ2.NUMREG <= " + pageSize;
			query += " AND SQ2.CODCIA||SQ2.DESCIA||SQ2.CODAREA||SQ2.DESAREA||SQ2.CODIND||SQ2.DESIND LIKE trim('%" + ((String) filterValue).toUpperCase() +  "%') ";
			query += " AND SQ2.NUMREG >( "+ first +")";  
			query += " ORDER BY  SQ2.CODCIA, SQ2.CODAREA, SQ2.CODIND";
		
  		pstmt = con.prepareStatement(query);
        //System.out.println(query);
        //System.out.println("ADMINISTRADOR");
        
        r =  pstmt.executeQuery();
        		
        while (r.next()){
        Sgc013 select = new Sgc013();
     	select.setZcodcia(r.getString(1));
     	select.setZdescia(r.getString(2));
     	select.setZcodarea(r.getString(3));
     	select.setZdesarea(r.getString(4));
     	select.setZcodind(r.getString(5));
     	select.setZdesind(r.getString(6));
     	select.setZinstancia(r.getString(7));

        	//Agrega la lista
        	list.add(select);
        }
        //Cierra las conecciones
        pstmt.close();
        con.close();
    } else {
    	


		String query = "SELECT SQ2.* ";
		query += " FROM (select SQ1.*, SQ1.NUMREG as rn"; 
		query += " from (SELECT"; 
		query += " A.COMP CODCIA, B.DESCR DESCIA, A.AREA CODAREA, C.DESCR DESAREA, A.INDICA CODIND, D.NOMIND DESIND, A.CODUSER, COUNT(A.INDICA) NUMREG, A.INSTANCIA";                
		query += " FROM SGC009 A LEFT OUTER JOIN SGC005 B ON A.COMP = B.CODIGO";                             
		query += "                                      LEFT OUTER JOIN SGC006 C ON A.COMP = C.COMP AND";                                                             
		query += "                                                                       A.AREA = C.CODIGO"; 
		query += "                                      LEFT OUTER JOIN SGC001 D ON A.COMP   = D.COMP AND";                                                              
		query += "                                                                       A.AREA   = D.AREA AND";                                                             
		query += "                                                                       A.INDICA = D.CODIGO";               
		query += "                    WHERE";                 
		query += "                    TRIM(A.CODUSER) LIKE TRIM('%" + login.toUpperCase() + "%')";              
		query += "                    AND A.COMP||A.AREA||A.INDICA IN (SELECT DISTINCT COMP||AREA||CODIGO"; 
		query += "                                                    FROM SGC012)";                 
		query += "                    GROUP BY";                
		query += "                    A.COMP, B.DESCR, A.AREA, C.DESCR, A.INDICA, D.NOMIND, A.CODUSER, A.INSTANCIA) SQ1) SQ2"; 
		query += " WHERE SQ2.NUMREG <= " + pageSize;
		query += " AND SQ2.CODCIA||SQ2.DESCIA||SQ2.CODAREA||SQ2.DESAREA||SQ2.CODIND||SQ2.DESIND LIKE trim('%" + ((String) filterValue).toUpperCase() +  "%') ";
		query += " AND SQ2.NUMREG >("+ first +")"; 
		query += " ORDER BY  SQ2.CODCIA, SQ2.CODAREA, SQ2.CODIND";
	
  		pstmt = con.prepareStatement(query);
        //System.out.println(query);
        //System.out.println("*****NO ADMINISTRADOR ****");
        
        r =  pstmt.executeQuery();
        		
        while (r.next()){
        Sgc013 select = new Sgc013();
     	select.setZcodcia(r.getString(1));
     	select.setZdescia(r.getString(2));
     	select.setZcodarea(r.getString(3));
     	select.setZdesarea(r.getString(4));
     	select.setZcodind(r.getString(5));
     	select.setZdesind(r.getString(6));
     	select.setZinstancia(r.getString(7));

        	//Agrega la lista
        	list.add(select);
        }
        //Cierra las conecciones
        pstmt.close();
        con.close();
    	
    }
		
  	}		
  	
  	/**
     * Leer registros en la tabla
     * @throws NamingException 
     * @throws IOException 
     **/ 	
  	public void counter(Object filterValue ) throws SQLException, NamingException {
     try {	
    	Context initContext = new InitialContext();     
   		DataSource ds = (DataSource) initContext.lookup(JNDI);
   		con = ds.getConnection();
   	   //Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
 		DatabaseMetaData databaseMetaData = con.getMetaData();
 		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
 		
  	      		
 		//Consulta paginada
 		String query = "SELECT COUNT_SGC013('" + ((String) filterValue).toUpperCase() + "','" + login.toUpperCase() + "') FROM DUAL";

        
        pstmt = con.prepareStatement(query);
        //System.out.println(query);

         r =  pstmt.executeQuery();
        
        
        while (r.next()){
        	rows = r.getInt(1);
        }
     } catch (SQLException e){
         e.printStackTrace();    
     }
        //Cierra las conecciones
        pstmt.close();
        con.close();
        r.close();

  	}
  	
    /**
  	 * @return the rows
  	 */
  	public int getRows() {
  		return rows;
  	}
  	
  	
 
}
