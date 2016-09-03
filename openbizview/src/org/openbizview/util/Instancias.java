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
	public class Instancias extends Bd implements Serializable {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		
		private LazyDataModel<Instancias> lazyModel;  
		
		
		/**
		 * @return the lazyModel
		 */
		public LazyDataModel<Instancias> getLazyModel() {
			return lazyModel;
		}	
	
		@PostConstruct
		public void init() {
			if (instancia == null){instancia = "99999";}
			
			//Si no tiene acceso al módulo no puede ingresar
			if (new SeguridadMenuBean().opcmnu("M11")=="false") {
				RequestContext.getCurrentInstance().execute("PF('idleDialogNP').show()");
			}
			
			lazyModel  = new LazyDataModel<Instancias>(){
				/**
				 * 
				 */
				private static final long serialVersionUID = 7217573531435419432L;
				
	            @Override
				public List<Instancias> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) { 
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
		
	
	private String instancia = "";
	private String descripcion = "";
	private Object filterValue = "";
	private List<Instancias> list = new ArrayList<Instancias>();
	private int validarOperacion = 0;
	
	
	   

	

	/**
	 * @return the instancia
	 */
	public String getInstancia() {
		return instancia;
	}

	/**
	 * @param instancia the instancia to set
	 */
	public void setInstancia(String instancia) {
		this.instancia = instancia;
	}

	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
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
     * <b>Parametros del Metodo:<b> String codigo, String descripcion unidos como un solo string.<br>
     * String pool, String login.<br><br>
     **/
    public void insert() throws  NamingException {   	
       		
        try {
        	Context initContext = new InitialContext();     
     		DataSource ds = (DataSource) initContext.lookup(JNDI);
            con = ds.getConnection();
            
                 
            String query = "INSERT INTO instancias VALUES (?,?,?,'" + getFecha() + "',?,'" + getFecha() + "')";
            pstmt = con.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(instancia));
            pstmt.setString(2, descripcion.toUpperCase());
            pstmt.setString(3, login);
            pstmt.setString(4, login);
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
	
	        	String query = "DELETE from instancias WHERE instancia in (" + param + ")";
	        		        	
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
     * <b>Parametros del Metodo:<b> String codigo, String descripcion unidos como un solo string.<br>
     * String pool, String login.<br><br>
     **/
    public void update() throws  NamingException {
         try {
        	 Context initContext = new InitialContext();     
      		DataSource ds = (DataSource) initContext.lookup(JNDI);

      		con = ds.getConnection();		
      		
            String query = "UPDATE instancias";
             query += " SET descripcion = ?, usract = ?, fecact='" + getFecha() + "'";
             query += " WHERE instancia = ?";
           // //System.out.println(query);
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, descripcion.toUpperCase());
            pstmt.setString(2, login);
            pstmt.setInt(3, Integer.parseInt(instancia));
            // Antes de ejecutar valida si existe el registro en la base de Datos.
            try {
                //Avisando
                pstmt.executeUpdate();
                if(pstmt.getUpdateCount()==0){
                	msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("msnNoUpdate"), "");
                } else {
                	msj = new FacesMessage(FacesMessage.SEVERITY_INFO,  getMessage("msnUpdate"), "");
                }
                descripcion = "";
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
        	   query += " (SELECT trim(instancia), trim(descripcion)";
        	   query += " FROM instancias";
        	   query += " WHERE instancia||descripcion like '%" + ((String) filterValue).toUpperCase() + "%'";
        	   query += " and instancia like '%" + instancia + "%'";
	  		   query += " order by " + sortField + ") query";
	           query += " ) where rownum <= " + pageSize ;
	           query += " and rn > (" + first + ")";
             break;
        case "PostgreSQL":
        	   query += " SELECT instancia, trim(descripcion) ";
        	   query += " FROM instancias";
        	   query += " WHERE cast(instancia as text)||descripcion like '%" + ((String) filterValue).toUpperCase() + "%'";
        	   query += " and cast(instancia as text) like '%" + instancia + "%'";
	  		   query += " order by " + sortField ;
	           query += " LIMIT " + pageSize;
	           query += " OFFSET " + first;
             break;
        case "Microsoft SQL Server":
	        	query += " SELECT * ";
	        	query += " FROM (SELECT  ";
	        	query += " 	  ROW_NUMBER() OVER (ORDER BY codigo ASC) AS ROW_NUM,  ";
	        	query += " 	  instancia,  ";
	        	query += " 	  descripcion  ";
	        	query += " 	  FROM instancias) TOT  ";
	        	query += " WHERE  ";
	        	query += " TOT.instancia + TOT.descripcion LIKE '%" + ((String) filterValue).toUpperCase() + "%'";
	        	query += " AND TOT.instancia LIKE '%" + instancia + "%'";
	        	query += " AND TOT.ROW_NUM <= " + pageSize;
	        	query += " AND TOT.ROW_NUM > " + first;
	        	query += " ORDER BY " + sortField ;
          break;        }

  		
       pstmt = con.prepareStatement(query);
       
 		
       r =  pstmt.executeQuery();
       
       while (r.next()){
    	Instancias select = new Instancias();
    	select.setInstancia(r.getString(1));
    	select.setDescripcion(r.getString(2));
       	
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
        	 query = "SELECT count_instancias('" + ((String) filterValue).toUpperCase() + "') from dual";
             break;
        case "PostgreSQL":
        	 query = "SELECT count_instancias('" + ((String) filterValue).toUpperCase() +  "')";
             break;
        case "Microsoft SQL Server":
       	 query = "SELECT DBO.count_instancias('" + ((String) filterValue).toUpperCase() +  "')";
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
  		instancia = "";
  		descripcion = "";
  		validarOperacion = 0;
	}

  	/*
	 * Listar instancias al momento del login
	 * si el usuario no tiene alguna instancia predefinida
	 * muestra el modal para seleccionar la instancia,
	 * lee de las instancias asociadas al usuario
	 */
     public List<String> selectInstancias() throws NamingException, SQLException   {
  		
  		Context initContext = new InitialContext();     
 		DataSource ds = (DataSource) initContext.lookup(JNDI);
 		con = ds.getConnection();
 		List<String> values = new ArrayList<String>();
 		
 		String query = "select a.instancia||' - '||b.descripcion";
	       query += " from instancias_usr a, instancias b";
	       query += " where a.instancia = b.instancia";
  		  		
  		pstmt = con.prepareStatement(query);
       // System.out.println(query);
  		
        r =  pstmt.executeQuery();
        		
        while (r.next()){
 
        values.add(r.getString(1));

        }
        //Cierra las conecciones
        pstmt.close();
        con.close();
        return values;
    }

}
