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
import java.util.HashMap;
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
	public class Acccat2 extends Bd implements Serializable {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		
		private LazyDataModel<Acccat2> lazyModel;  
		
		
	
	/**
		 * @return the lazyModel
		 */
		public LazyDataModel<Acccat2> getLazyModel() {
			return lazyModel;
		}
		
		
		@PostConstruct
		public void init() {	
			if (instancia == null){instancia = "99999";}
			
			//Si no tiene acceso al módulo no puede ingresar
			if (new SeguridadMenuBean().opcmnu("M19")=="false") {
				RequestContext.getCurrentInstance().execute("PF('idleDialogNP').show()");
			}
			
			lazyModel  = new LazyDataModel<Acccat2>(){
				/**
				 * 
				 */
				private static final long serialVersionUID = 7217573531435419432L;
				
	            @Override
				public List<Acccat2> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) { 
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
			sorted = new HashMap<String,String>();
			selectAcccat2();	
		}
		
	
	 private String b_codrol = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("segrol");
	 private String b_codcat1 = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cat1"); 
	 private String b_codcat2 = "";
	 private String descat1 = "";
	 private String descat2 = "";
	 private Object filterValue = "";
	 private List<Acccat2> list = new ArrayList<Acccat2>();
	 private Map<String,String> listAcccat2 = new HashMap<String, String>();   //Lista de compañías disponibles para acceso a seguridad 
	 private List<String> selectedAcccat2;   //Listado de compañias seleccionadas
	 private Map<String, String> sorted;
	 private String exito = "exito";
	 private String instancia = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("instancia"); //Usuario logeado

	
	 

	/**
	 * @return the b_codrol
	 */
	public String getB_codrol() {
		return b_codrol;
	}


	/**
	 * @param b_codrol the b_codrol to set
	 */
	public void setB_codrol(String b_codrol) {
		this.b_codrol = b_codrol;
	}


	/**
	 * @return the b_codcat1
	 */
	public String getB_codcat1() {
		return b_codcat1;
	}


	/**
	 * @param b_codcat1 the b_codcat1 to set
	 */
	public void setB_codcat1(String b_codcat1) {
		this.b_codcat1 = b_codcat1;
	}


	/**
	 * @return the b_codcat2
	 */
	public String getB_codcat2() {
		return b_codcat2;
	}


	/**
	 * @param b_codcat2 the b_codcat2 to set
	 */
	public void setB_codcat2(String b_codcat2) {
		this.b_codcat2 = b_codcat2;
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
	 * @return the filterValue
	 */
	public Object getFilterValue() {
		return filterValue;
	}


	/**
	 * @param filterValue the filterValue to set
	 */
	public void setFilterValue(Object filterValue) {
		this.filterValue = filterValue;
	}


	/**
	 * @return the list
	 */
	public List<Acccat2> getList() {
		return list;
	}


	/**
	 * @param list the list to set
	 */
	public void setList(List<Acccat2> list) {
		this.list = list;
	}


	/**
	 * @return the listAcccat2
	 */
	public Map<String, String> getListAcccat2() {
		return listAcccat2;
	}

	/**
	 * @param listAcccat2 the listAcccat2 to set
	 */
	public void setListAcccat2(Map<String, String> listAcccat2) {
		this.listAcccat2 = listAcccat2;
	}

	/**
	 * @return the selectedAcccat2
	 */
	public List<String> getSelectedAcccat2() {
		return selectedAcccat2;
	}

	/**
	 * @param selectedAcccat2 the selectedAcccat2 to set
	 */
	public void setSelectedAcccat2(List<String> selectedAcccat2) {
		this.selectedAcccat2 = selectedAcccat2;
	}

	/**
	 * @return the sorted
	 */
	public Map<String, String> getSorted() {
		return sorted;
	}

	/**
	 * @param sorted the sorted to set
	 */
	public void setSorted(Map<String, String> sorted) {
		this.sorted = sorted;
	}

	/**
	 * @param rows the rows to set
	 */
	public void setRows(int rows) {
		this.rows = rows;
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
     * Inserta acceso categoria2.
     * <p>
     * <b>Parametros del Metodo:<b> String codrol, String cat1, String cat2 unidos como un solo string.<br>
     * String pool, String login.<br><br>
     **/
    public void insert(String pcodcat2) throws  NamingException {        
        try {
        	Context initContext = new InitialContext();     
     		DataSource ds = (DataSource) initContext.lookup(JNDI);
            con = ds.getConnection();
            
            String query = "INSERT INTO acccat2 VALUES (?,?,?,?,'" + getFecha() + "',?,'" + getFecha() + "',?)";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, b_codrol.split(" - ")[0].toUpperCase());
            pstmt.setString(2, b_codcat1.split(" - ")[0].toUpperCase());
            pstmt.setString(3, pcodcat2.toUpperCase());
            pstmt.setString(4, login);
            pstmt.setString(5, login);
            pstmt.setInt(6, Integer.parseInt(instancia));
            ////System.out.println(query);
            try {
                //Avisando
            	pstmt.executeUpdate();
                
             } catch (SQLException e)  {
            	msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
            	FacesContext.getCurrentInstance().addMessage(null, msj);
            	exito = "error";
            }
            
            pstmt.close();
            con.close();
        } catch (Exception e) {
        	e.printStackTrace();
        }	       
    }
    
    /**
     * Genera las operaciones de guardar o modificar
     * @throws NamingException 
     * @throws SQLException 
     * @throws IOException 
     * @throws ClassNotFoundException 
     **/ 
    public void guardar() throws NamingException, SQLException, ClassNotFoundException{  
    	//System.out.println("Selected :" + selectedAcccat2.size());
        if (selectedAcccat2.size()<=0){
    		msj = new FacesMessage(FacesMessage.SEVERITY_WARN, getMessage("acccat2IngCat2"), "");
    		FacesContext.getCurrentInstance().addMessage(null, msj);
    	} else {  	
    	   for (int i = 0; i< selectedAcccat2.size(); i++){
    		  //System.out.println("Selected :" + selectedAcccat2.get(i));
    		insert(selectedAcccat2.get(i));           
    	   }
   		limpiarValores();   
        if(exito=="exito"){
        	msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnInsert"), "");
        	FacesContext.getCurrentInstance().addMessage(null, msj);
        }
    	}
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
	
	        	String query = "DELETE from acccat2 WHERE b_codrol||b_codcat1||b_codcat2 in (" + param + ") and instancia = '" + instancia + "'";
	        		        	
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
 		
 		
 		String query = "";
 		if(b_codrol==null){
 			b_codrol = " - ";
 		}
 		if(b_codrol.equals("")){
 			b_codrol = " - ";
 		}
 		if(b_codcat1==null){
 			b_codcat1 = " - ";
 		}
 		if(b_codcat1==""){
 			b_codcat1 = " - ";
 		}
 		String[] veccodrol = b_codrol.split("\\ - ", -1);
 		String[] veccodcat1 = b_codcat1.split("\\ - ", -1);
 		
 		switch ( productName ) {
        case "Oracle":
        	   query += "  select * from ";
        	   query += " ( select query.*, rownum as rn from";
        	   query += "  ( SELECT trim(a.b_codrol), trim(a.b_codcat1), trim(b.descat1), trim(a.b_codcat2), trim(c.descat2)";
  		       query += " FROM acccat2 a, bvtcat1 b, bvtcat2 c";
  		       query += " WHERE a.b_codcat1=b.codcat1 ";
  		       query += " and   a.b_codcat1=c.b_codcat1";
  		       query += " and   a.b_codcat2=c.codcat2 ";
  		       query += " and A.instancia=B.instancia";
		       query += " and A.instancia=c.instancia";
  		       query += " and  a.b_codrol like '" + veccodrol[0] + "%'";
  		       query += " and  A.b_codcat1 like '" + veccodcat1[0].toUpperCase() + "%'";
        	   query += " AND   a.b_codcat1||b.descat1||a.b_codcat2||c.descat2 like '%" + ((String) filterValue).toUpperCase() + "%'";
        	   query += " AND   a.instancia = '" + instancia + "'";
        	   query += " order by " + sortField + ") query";
	           query += " ) where rownum <= " + pageSize ;
	           query += " and rn > (" + first + ")";
             break;
        case "PostgreSQL":
        	   query +=  " SELECT trim(a.b_codrol), trim(a.b_codcat1), trim(b.descat1), trim(a.b_codcat2), trim(c.descat2)";
		       query += " FROM acccat2 a, bvtcat1 b, bvtcat2 c";
		       query += " WHERE a.b_codcat1=b.codcat1 ";
		       query += " and   a.b_codcat1=c.b_codcat1";
		       query += " and   a.b_codcat2=c.codcat2 ";
		       query += " and A.instancia=B.instancia";
  		       query += " and A.instancia=c.instancia";
		       query += " and  a.b_codrol like '" + veccodrol[0] + "%'";
		       query += " and  A.b_codcat1 like '" + veccodcat1[0].toUpperCase() + "%'";
     	       query += " AND  a.b_codcat1||b.descat1||a.b_codcat2||c.descat2 like '%" + ((String) filterValue).toUpperCase() + "%'";
     	       query += " AND   a.instancia = '" + instancia + "'";
	  		   query += " order by " + sortField ;
	           query += " LIMIT " + pageSize;
	           query += " OFFSET " + first;
             break;
        }
 		 		
        
        pstmt = con.prepareStatement(query);
        //System.out.println(query);
  		
        r =  pstmt.executeQuery();

        
        while (r.next()){
        	Acccat2 select = new Acccat2();
     	    select.setB_codrol(r.getString(1));
        	select.setB_codcat1(r.getString(2));
        	select.setDescat1(r.getString(3));
        	select.setB_codcat2(r.getString(4));
        	select.setDescat2(r.getString(5));
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
 		if(b_codrol==null){
 			b_codrol = " - ";
 		}
 		if(b_codrol==""){
 			b_codrol = " - ";
 		}
 		if(b_codcat1==null){
 			b_codcat1 = " - ";
 		}
 		if(b_codcat1==""){
 			b_codcat1 = " - ";
 		}
 		String[] veccodrol = b_codrol.split("\\ - ", -1);
 		String[] veccodcat1 = b_codcat1.split("\\ - ", -1);
  		
  		switch ( productName ) {
        case "Oracle":
        	 query = "SELECT count_acccat2('" + ((String) filterValue).toUpperCase() + "','" + veccodrol[0] + "','" + veccodcat1[0] + "','" + instancia + "') from dual";
             break;
        case "PostgreSQL":
        	 query = "SELECT count_acccat2('" + ((String) filterValue).toUpperCase() + "','" + veccodrol[0] + "','" + veccodcat1[0] + "','" + instancia + "')";
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
     * Leer Datos de nominas para asignar a menucheck
     * @throws NamingException 
	 * @throws SQLException 
     * @throws IOException 
     **/ 	
  	private void selectAcccat2()   {
  		Context initContext;
		try {
		initContext = new InitialContext();
		   
    	DataSource ds = (DataSource) initContext.lookup(JNDI);
        con = ds.getConnection();
		
  		   		
  		String query = "";
        String cat1 = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cat1"); //Usuario logeado
         
        if(cat1==null){
        	cat1 = " - ";
        }
        if(cat1==""){
        	cat1 = " - ";
        }
        String[] veccat1 = cat1.split("\\ - ", -1);

        	query = "Select codcat2, codcat2||' - '||descat2";
            query += " from bvtcat2";
            query += " where B_CODCAT1 = '" + veccat1[0].toUpperCase() + "'";
            query += " and   instancia = '" + instancia + "'";
            query += " order by codcat2";


        
        pstmt = con.prepareStatement(query);
        //System.out.println(query);
  		
        r =  pstmt.executeQuery();
        
  		
        
        while (r.next()){
        	String cat2 = new String(r.getString(1));
        	String descat2 = new String(r.getString(2));
        	
            listAcccat2.put(descat2, cat2);
            sorted = sortByValues(listAcccat2);
            
        }
        
        //Cierra las conecciones
        pstmt.close();
        con.close();
        
        
		} catch (NamingException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
  	}

      
   /**
  	 * @return the rows
  	 */
  	public int getRows() {
  		return rows;
  	}

  	private void limpiarValores() {
 		// TODO Auto-generated method stub
  		b_codcat1 = "";
  		b_codcat2 = "";
 	}

  	public void reset() {
  		b_codrol = null;    
  		b_codcat1 = null;
    }

}
