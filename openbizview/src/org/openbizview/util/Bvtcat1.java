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
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.openbizview.util.PntGenerica;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

	/**
	 *
	 * @author Andres
	 */
	@ManagedBean
	@ViewScoped
	public class Bvtcat1 extends Bd implements Serializable {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		
		private LazyDataModel<Bvtcat1> lazyModel;  
		
		
		/**
		 * @return the lazyModel
		 */
		public LazyDataModel<Bvtcat1> getLazyModel() {
			return lazyModel;
		}	
	
		@PostConstruct
		public void init() {
			if (instancia == null){instancia = "99999";}
			//Si no tiene acceso al módulo no puede ingresar
			if (new SeguridadMenuBean().opcmnu("M02")=="false") {
				RequestContext.getCurrentInstance().execute("PF('idleDialogNP').show()");
			}
			
			lazyModel  = new LazyDataModel<Bvtcat1>(){
				/**
				 * 
				 */
				private static final long serialVersionUID = 7217573531435419432L;
				
	            @Override
				public List<Bvtcat1> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) { 
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
		
	
	private String codcat1 = "";
	private String descat1 = "";
	private Object filterValue = "";
	private List<Bvtcat1> list = new ArrayList<Bvtcat1>();
	private int validarOperacion = 0;
	private String instancia = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("instancia"); //Usuario logeado

	
	
	/**
	 * @return the codcat1
	 */
	public String getcodcat1() {
		return codcat1;
	}
	/**
	 * @param codcat1 the codcat to set
	 */
	public void setcodcat1(String codcat1) {
		this.codcat1 = codcat1;
	}
	/**
	 * @return the descat1
	 */
	public String getDescat1() {
		return descat1;
	}
	/**
	 * @param descat1 the descat1 to set
	 */
	public void setDescat1(String descat1) {
		this.descat1 = descat1;
	}
	
   

	/**
	 * @return the validarOperacion
	 */
	public int getValidarOperacion() {
		return validarOperacion;
	}
	/**
	 * @param validarOperacion the validarOperacion to set
	 */
	public void setValidarOperacion(int validarOperacion) {
		this.validarOperacion = validarOperacion;
	}


	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//Variables seran utilizadas para capturar mensajes de errores de Oracle y parametros de metodos
	FacesMessage msj = null;
	PntGenerica consulta = new PntGenerica();
	boolean vGacc; //Validador de opciones del menú
	private int rows; //Registros de tabla
	private String login = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario"); //Usuario logeado
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


	//Coneccion a base de datos
	//Pool de conecciones JNDI
		Connection con;
		PreparedStatement pstmt = null;
		ResultSet r;



     /**
     * Inserta categoria1.
     * <p>
     * <b>Parametros del Metodo:<b> String codcat1, String descat1 unidos como un solo string.<br>
     * String pool, String login.<br><br>
     **/
    public void insert() throws  NamingException {   	
       		
        try {
        	Context initContext = new InitialContext();     
     		DataSource ds = (DataSource) initContext.lookup(JNDI);
            con = ds.getConnection();
            
                 
            String query = "INSERT INTO Bvtcat1 VALUES (?,?,?,'" + getFecha() + "',?,'" + getFecha() + "',?)";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, codcat1.toUpperCase());
            pstmt.setString(2, descat1.toUpperCase());
            pstmt.setString(3, login);
            pstmt.setString(4, login);            
            pstmt.setInt(5, Integer.parseInt(instancia));
            try {
                //Avisando
            	pstmt.executeUpdate();
            	msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnInsert"), "");
                limpiarValores();                
             } catch (SQLException e)  {
            	msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
            }
            
            pstmt.close();
            con.close();
        } catch (Exception e) {
        	e.printStackTrace();
        }	
        FacesContext.getCurrentInstance().addMessage(null, msj);
    }
    
    
    public void delete() throws NamingException  {  
    	HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
    	String[] chkbox = request.getParameterValues("toDelete");
    	
    	if (chkbox==null){
    		msj = new FacesMessage(FacesMessage.SEVERITY_WARN, getMessage("del"), "");
    	} else {
	        try {
	       	
	        	Context initContext = new InitialContext();     
	     		DataSource ds = (DataSource) initContext.lookup(JNDI);

	     		con = ds.getConnection();		
	        	
	        	String param = "'" + StringUtils.join(chkbox, "','") + "'";
	
	        	String query = "DELETE from Bvtcat1 WHERE codcat1 in (" + param + ") and instancia = '" + instancia + "'";
	        		        	
	            pstmt = con.prepareStatement(query);
	            ////System.out.println(query);
	
	            try {
	                //Avisando
	                pstmt.executeUpdate();
	                msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnDelete"), "");
	                limpiarValores();	
	            } catch (SQLException e) {
	                e.printStackTrace();
	                msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
	            }
	
	            pstmt.close();
	            con.close();
	
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
    	}
    	FacesContext.getCurrentInstance().addMessage(null, msj); 
    }
    
  
    /**
     * Actualiza categoria1
     * <b>Parametros del Metodo:<b> String codcat1, String descat1 unidos como un solo string.<br>
     * String pool, String login.<br><br>
     **/
    public void update() throws  NamingException {
         try {
        	 Context initContext = new InitialContext();     
      		DataSource ds = (DataSource) initContext.lookup(JNDI);

      		con = ds.getConnection();		
      		
            String query = "UPDATE Bvtcat1";
             query += " SET descat1 = ?, usract = ?, fecact='" + getFecha() + "'";
             query += " WHERE codcat1 = ? and instancia = '" + instancia + "'";
           // //System.out.println(query);
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, descat1.toUpperCase());
            pstmt.setString(2, login);
            pstmt.setString(3, codcat1.toUpperCase());
            // Antes de ejecutar valida si existe el registro en la base de Datos.
            try {
                //Avisando
                pstmt.executeUpdate();
                if(pstmt.getUpdateCount()==0){
                	msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("msnNoUpdate"), "");
                } else {
                	msj = new FacesMessage(FacesMessage.SEVERITY_INFO,  getMessage("msnUpdate"), "");
                }
                descat1 = "";
            	validarOperacion = 0;
            } catch (SQLException e) {
            	msj = new FacesMessage(FacesMessage.SEVERITY_INFO, e.getMessage(), "");
            }
            pstmt.close();
            con.close();
        } catch (Exception e) {
        }
        FacesContext.getCurrentInstance().addMessage(null, msj);
    }
    
    
    public void guardar() throws NamingException, SQLException{   	
    	if(validarOperacion==0){
    		insert();
    	} else {
    		update();
    	}
    }
   
   /**
    * Leer Datos de paises
    * @throws NamingException 
 * @throws IOException 
    **/ 	
 	public void select(int first, int pageSize, String sortField, Object filterValue) throws SQLException, ClassNotFoundException, NamingException {
 				
 		Context initContext = new InitialContext();     
 		DataSource ds = (DataSource) initContext.lookup(JNDI);
 		con = ds.getConnection();		
 		//Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
 		DatabaseMetaData databaseMetaData = con.getMetaData();
 		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
 		
 		
 		String query = "";

  		switch ( productName ) {
        case "Oracle":
        	   query += "  select * from ";
        	   query += " ( select query.*, rownum as rn from";
        	   query += " (SELECT trim(codcat1), trim(descat1)";
        	   query += " FROM BVTcat1";
        	   query += " WHERE codcat1||descat1 like '%" + ((String) filterValue).toUpperCase() + "%'";
        	   query += " and codcat1 like '%" + codcat1.toUpperCase() + "%'";
        	   query += " AND   instancia = '" + instancia + "'";
	  		   query += " order by " + sortField + ") query";
	           query += " ) where rownum <= " + pageSize ;
	           query += " and rn > (" + first + ")";
             break;
        case "PostgreSQL":
        	   query += " SELECT trim(codcat1), trim(descat1) ";
        	   query += " FROM BVTcat1";
        	   query += " WHERE codcat1||descat1 like '%" + ((String) filterValue).toUpperCase() + "%'";
        	   query += " and codcat1 like '%" + codcat1.toUpperCase() + "%'";
        	   query += " AND   instancia = '" + instancia + "'";
	  		   query += " order by " + sortField ;
	           query += " LIMIT " + pageSize;
	           query += " OFFSET " + first;
             break;
        case "Microsoft SQL Server":
	        	query += " SELECT * ";
	        	query += " FROM (SELECT  ";
	        	query += " 	  ROW_NUMBER() OVER (ORDER BY CODCAT1 ASC) AS ROW_NUM,  ";
	        	query += " 	  CODCAT1,  ";
	        	query += " 	  DESCAT1  ";
	        	query += " 	  FROM BVTCAT1) TOT  ";
	        	query += " WHERE  ";
	        	query += " TOT.CODCAT1 + TOT.DESCAT1 LIKE '%" + ((String) filterValue).toUpperCase() + "%'";
	        	query += " AND TOT.CODCAT1 LIKE '%" + codcat1.toUpperCase() + "%'";
	        	query += " AND   tot.instancia = '" + instancia + "'";
	        	query += " AND TOT.ROW_NUM <= " + pageSize;
	        	query += " AND TOT.ROW_NUM > " + first;
	        	query += " ORDER BY " + sortField ;
          break;        }

       
       pstmt = con.prepareStatement(query);
       //System.out.println(query);
 		
       r =  pstmt.executeQuery();
       
       while (r.next()){
    	Bvtcat1 select = new Bvtcat1();
    	select.setcodcat1(r.getString(1));
    	select.setDescat1(r.getString(2));
       	
       	//Agrega la lista
       	list.add(select);
       }
       //Cierra las conecciones
       pstmt.close();
       con.close();

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
   	      		
 		String query = "";
  		
  		switch ( productName ) {
        case "Oracle":
        	 query = "SELECT count_bvtcat1('" + ((String) filterValue).toUpperCase() + "','" + instancia + "') from dual";
             break;
        case "PostgreSQL":
        	 query = "SELECT count_bvtcat1('" + ((String) filterValue).toUpperCase() + "','" + instancia + "')";
             break;
        case "Microsoft SQL Server":
       	 query = "SELECT DBO.count_bvtcat1('" + ((String) filterValue).toUpperCase() + "','" + instancia + "')";
            break;
         }

        
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

  	private void limpiarValores() {
		// TODO Auto-generated method stub
  		codcat1 = "";
  		descat1 = "";
  		validarOperacion = 0;
	}



}
