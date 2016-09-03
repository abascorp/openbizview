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
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

	/**
	 *
	 * @author Andres
	 */
	@ManagedBean
	@ViewScoped
	public class Bvtcat2 extends Bd implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		
		private LazyDataModel<Bvtcat2> lazyModel;  
		
		/**
		 * @return the lazyModel
		 */
		public LazyDataModel<Bvtcat2> getLazyModel() {
			return lazyModel;
		}	
	
	@PostConstruct
	public void init() {
		if (instancia == null){instancia = "99999";}
		//Si no tiene acceso al módulo no puede ingresar
		if (new SeguridadMenuBean().opcmnu("M02")=="false") {
			RequestContext.getCurrentInstance().execute("PF('idleDialogNP').show()");
		}
		
		lazyModel  = new LazyDataModel<Bvtcat2>(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 7217573531435419432L;
			
            @Override
			public List<Bvtcat2> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) { 
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
	
	private String b_codcat1 = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cat1"); 
	private String bcodcatdescat1 = ""; 
	private String codcat2 = "";
	private String descat2 = "";
	private Object filterValue = "";
	private List<Bvtcat2> list = new ArrayList<Bvtcat2>();
	private int validarOperacion = 0;
	private String instancia = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("instancia"); //Usuario logeado


	
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

	/**
	 * @param rows the rows to set
	 */
	public void setRows(int rows) {
		this.rows = rows;
	}

		/**
	 * @return the b_codcat1
	 */
	public String getb_codcat1() {
		return b_codcat1;
	}
	
	/**
	 * @param b_codcat1 the b_codcat1 to set
	 */
	public void setb_codcat1(String b_codcat1) {
		this.b_codcat1 = b_codcat1;
	}
	
	/**
	 * @return the codcat2
	 */
	public String getCodcat2() {
		return codcat2;
	}
	
	/**
	 * @param codcat2 the codcat2 to set
	 */
	public void setCodcat2(String codcat2) {
		this.codcat2 = codcat2;
	}
	
	/**
	 * @return the descat2
	 */
	public String getDescat2() {
		return descat2;
	}
	
	/**
	 * @param descat2 the descat2 to set
	 */
	public void setDescat2(String descat2) {
		this.descat2 = descat2;
	}


	
	/**
	 * @return the bcodcatdescat1
	 */
	public String getBcodcatdescat1() {
		return bcodcatdescat1;
	}

	/**
	 * @param bcodcatdescat1 the bcodcatdescat1 to set
	 */
	public void setBcodcatdescat1(String bcodcatdescat1) {
		this.bcodcatdescat1 = bcodcatdescat1;
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
     * Inserta categoria2.
     * <p>
     * <b>Parametros del Metodo:<b> String b_codcat1, String codcat2, String descat1 unidos como un solo string.<br>
     * String pool, String login.<br><br>
     **/
    public void insert() throws  NamingException {
    	//Valida que los campos no sean nulos
    	String[] veccodcat1 = b_codcat1.split("\\ - ", -1);
    		
        try {
        	Context initContext = new InitialContext();     
     		DataSource ds = (DataSource) initContext.lookup(JNDI);
            con = ds.getConnection();
            
            String query = "INSERT INTO Bvtcat2 VALUES (?,?,?,?,'" + getFecha() + "',?,'" + getFecha() + "',?)";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, veccodcat1[0].toUpperCase());
            pstmt.setString(2, codcat2.toUpperCase());
            pstmt.setString(3, descat2.toUpperCase());
            pstmt.setString(4, login);
            pstmt.setString(5, login);
            pstmt.setInt(6, Integer.parseInt(instancia));
            ////System.out.println(query);
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
	
	        	String query = "DELETE from Bvtcat2 WHERE b_codcat1||codcat2 in (" + param + ") and instancia = '" + instancia + "'";
	        		        	
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
     * Actualiza categoria2
     * <b>Parametros del Metodo:<b> String b_codcat1, String codcat2,  String descat1 unidos como un solo string.<br>
     * String pool, String login.<br><br>
     **/
    public void update() throws  NamingException {
    
    		String[] veccodcat1 = b_codcat1.split("\\ - ", -1);
        try {
        	 Context initContext = new InitialContext();     
       		DataSource ds = (DataSource) initContext.lookup(JNDI);

       		con = ds.getConnection();		
       		
            String query = "UPDATE Bvtcat2";
             query += " SET descat2 = ?, usract = ?, fecact='" + getFecha() + "'";
             query += " WHERE b_codcat1 = ? and codcat2 = ? and instancia = '" + instancia + "'";
            ////System.out.println(query);
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, descat2.toUpperCase());
            pstmt.setString(2, login);
            pstmt.setString(3, veccodcat1[0].toUpperCase());
            pstmt.setString(4, codcat2.toUpperCase());
            try {
                //Avisando
                pstmt.executeUpdate();
                if(pstmt.getUpdateCount()==0){
                	msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("msnNoUpdate"), "");
                } else {
                	msj = new FacesMessage(FacesMessage.SEVERITY_INFO,  getMessage("msnUpdate"), "");
                }
                descat2 = "";
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
     * Leer Datos de categoria2
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
 		
 		if(b_codcat1 == null){
			b_codcat1 = " - ";
		}
		if(b_codcat1 == ""){
			b_codcat1 = " - ";
		}
 		
 		String[] veccodcat1 = b_codcat1.split("\\ - ", -1);
 		
 		String query = "";

  		switch ( productName ) {
        case "Oracle":
        	   query += "  select * from ";
        	   query += " ( select query.*, rownum as rn from";
        	   query += " (SELECT trim(A.codcat2), trim(A.descat2), trim(A.B_CODCAT1), trim(B.DESCAT1) ";
        	   query += " FROM BVTcat2 A, BVTCAT1 B";
               query += " WHERE A.B_CODCAT1=B.CODCAT1";
               query += " and A.instancia=B.instancia";
               query += " and  A.b_codcat1 like '" + veccodcat1[0].toUpperCase() + "%'";
               query += " and  A.codcat2 ||a.descat2 like  '%" + ((String) filterValue).toUpperCase() + "%'";
               query += " AND   a.instancia = '" + instancia + "'";
               query += " order by  a." + sortField + ") query";
	           query += " ) where rownum <= " + pageSize ;
	           query += " and rn > (" + first + ")";
             break;
        case "PostgreSQL":
        	   query += " SELECT trim(A.codcat2), trim(A.descat2), trim(A.B_CODCAT1), trim(B.DESCAT1) ";
        	   query += " FROM BVTcat2 A, BVTCAT1 B";
               query += " WHERE A.B_CODCAT1=B.CODCAT1";
               query += " and A.instancia=B.instancia";
               query += " and  A.b_codcat1 like '" + veccodcat1[0].toUpperCase() + "%'";
               query += " and  A.codcat2 ||a.descat2 like  '%" + ((String) filterValue).toUpperCase() + "%'";
               query += " AND   a.instancia = '" + instancia + "'";
               query += " order by a." + sortField ;
	           query += " LIMIT " + pageSize;
	           query += " OFFSET " + first;
             break;
        case "Microsoft SQL Server":
		        query += " SELECT * ";
		        query += " FROM (SELECT ";
		        query += " 	     ROW_NUMBER() OVER (ORDER BY A.CODCAT2 ASC) AS ROW_NUM,  ";
		        query += " 	     A.CODCAT2,  ";
		        query += " 	     A.DESCAT2,  ";
		        query += " 	     A.B_CODCAT1,  ";
		        query += " 	     B.DESCAT1  ";
		        query += " 	     FROM  ";
		        query += " 	     BVTCAT2 A, BVTCAT1 B ";
		        query += " 	     WHERE  ";
		        query += " 	     A.B_CODCAT1=B.CODCAT1) TOT ";
		        query += " WHERE ";
		        query += " TOT.B_CODCAT1 LIKE '" + veccodcat1[0].toUpperCase() + "%'";
		        query += " AND TOT.CODCAT2 + TOT.DESCAT2 LIKE  '%" + ((String) filterValue).toUpperCase() + "%'";
		        query += " AND   tot.instancia = '" + instancia + "'";
		        query += " AND TOT.ROW_NUM <= " + pageSize;
		        query += " AND TOT.ROW_NUM > " + first;
		        query += " ORDER BY " + sortField ;
          break;
          }
  		//System.out.println(query);
        
        pstmt = con.prepareStatement(query);
        
  		
        r =  pstmt.executeQuery();

        
        while (r.next()){
     	Bvtcat2 select = new Bvtcat2();
     	select.setCodcat2(r.getString(1));
     	select.setDescat2(r.getString(2));
     	select.setb_codcat1(r.getString(3));
     	select.setBcodcatdescat1(r.getString(3) + " - " + r.getString(4));
        	
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
 		
 		if(b_codcat1 == null){
			b_codcat1 = " - ";
		}
		if(b_codcat1 == ""){
			b_codcat1 = " - ";
		}
 		
 		String[] veccodcat1 = b_codcat1.split("\\ - ", -1);
  		
  		switch ( productName ) {
        case "Oracle":
        	 query = "SELECT count_bvtcat2('" + ((String) filterValue).toUpperCase() + "','" + veccodcat1[0] + "','" + instancia + "') from dual";
             break;
        case "PostgreSQL":
        	 query = "SELECT count_bvtcat2('" + ((String) filterValue).toUpperCase() + "','" + veccodcat1[0] + "','" + instancia + "')";
             break;
        case "Microsoft SQL Server":
       	 query = "SELECT DBO.count_bvtcat2('" + ((String) filterValue).toUpperCase() + "','" + veccodcat1[0] + "','" + instancia + "')";
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
  		codcat2 = "";
  		descat2 = "";
  		validarOperacion = 0;
	}
  	
  	public void reset() {
  		b_codcat1 = null;     
    }


}
