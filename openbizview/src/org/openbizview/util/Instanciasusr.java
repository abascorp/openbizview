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
	public class Instanciasusr extends Bd implements Serializable {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		
		private LazyDataModel<Instanciasusr> lazyModel;  
		
		
	
	/**
		 * @return the lazyModel
		 */
		public LazyDataModel<Instanciasusr> getLazyModel() {
			return lazyModel;
		}


	@PostConstruct	
	public void init() {
		
	
		//Si no tiene acceso al módulo no puede ingresar
		if (new SeguridadMenuBean().opcmnu("M12")=="false") {
			RequestContext.getCurrentInstance().execute("PF('idleDialogNP').show()");
		}
		
		lazyModel  = new LazyDataModel<Instanciasusr>(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 7217573531435419432L;
			
            @Override
			public List<Instanciasusr> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) { 
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
	  //
	  try {
		  selectInstanciasUsr();
	} catch (NamingException | SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}

	
	private String coduser = "";
	private String instancias = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("bcoduser");
	private String descripcion = "";
	private Object filterValue = "";
	private List<Instanciasusr> list = new ArrayList<Instanciasusr>();
	private Map<String,String> listUsr = new HashMap<String, String>();   //Lista de compañías disponibles para acceso a seguridad 
    private List<String> selectedUsr;   //Listado de compañias seleccionadas
    private Map<String, String> sorted;
    private String exito = "exito";


	/**
	 * @return the coduser
	 */
	public String getCoduser() {
		return coduser;
	}


	/**
	 * @param coduser the coduser to set
	 */
	public void setCoduser(String coduser) {
		this.coduser = coduser;
	}


	/**
	 * @return the instancias
	 */
	public String getInstancias() {
		return instancias;
	}


	/**
	 * @param instancias the instancias to set
	 */
	public void setInstancias(String instancias) {
		this.instancias = instancias;
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
	public List<Instanciasusr> getList() {
		return list;
	}


	/**
	 * @param list the list to set
	 */
	public void setList(List<Instanciasusr> list) {
		this.list = list;
	}


	/**
	 * @param rows the rows to set
	 */
	public void setRows(int rows) {
		this.rows = rows;
	}
	
	

	/**
	 * @return the listUsr
	 */
	public Map<String, String> getListUsr() {
		return listUsr;
	}


	/**
	 * @param listUsr the listUsr to set
	 */
	public void setListUsr(Map<String, String> listUsr) {
		this.listUsr = listUsr;
	}


	/**
	 * @return the selectedUsr
	 */
	public List<String> getSelectedUsr() {
		return selectedUsr;
	}


	/**
	 * @param selectedUsr the selectedUsr to set
	 */
	public void setSelectedUsr(List<String> selectedUsr) {
		this.selectedUsr = selectedUsr;
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
     * Inserta acceso categoria1.
     * <p>
     * <b>Parametros del Metodo:<b> String rol, String cat1 unidos como un solo string.<br>
     * String pool, String login.<br><br>
     **/
    private void insert(String pusuarios) throws  NamingException {
    		String[] veccat1 = pusuarios.split("\\ - ", -1);
        try {
        	Context initContext = new InitialContext();     
     		DataSource ds = (DataSource) initContext.lookup(JNDI);
            con = ds.getConnection();
            
            String query = "INSERT INTO instancias_usr VALUES (?,?,?,'" + getFecha() + "',?,'" + getFecha() + "')";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, veccat1[0].toUpperCase());
            pstmt.setInt(2, Integer.parseInt(instancias.split(" - ")[0]));
            pstmt.setString(3, login);
            pstmt.setString(4, login);
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
        if (selectedUsr.size()<=0){
    		msj = new FacesMessage(FacesMessage.SEVERITY_WARN, getMessage("InstanciasUsrIngUsr"), "");
    		FacesContext.getCurrentInstance().addMessage(null, msj);
    	} else {  	
    	   for (int i = 0; i< selectedUsr.size(); i++){
    		  //System.out.println("Selected :" + selectedUsr.get(i));
    		insert(selectedUsr.get(i));           
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
	
	        	String query = "DELETE from instancias_usr WHERE coduser||instancia in (" + param + ")";
	        		        	
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
 		if(instancias==null){
 			instancias = " - ";
 		}
 		if(instancias==""){
 			instancias = " - ";
 		}
 		String[] veccodrol = instancias.split("\\ - ", -1);

  		switch ( productName ) {
        case "Oracle":
        	   query += "  select * from ";
        	   query += " ( select query.*, rownum as rn from";
        	   query += " (SELECT trim(a.coduser), trim(a.instancia), trim(b.descripcion)";
        	   query += " FROM instancias_usr a, instancias b";
  		       query += " where a.instancia=b.instancia";
  		       query += " and   a.instancia like '" + veccodrol[0] + "%'";
        	   query += " AND   a.coduser||a.instancia||b.descripcion like '%" + ((String) filterValue).toUpperCase() + "%'";
	  		   query += " order by " + sortField + ") query";
	           query += " ) where rownum <= " + pageSize ;
	           query += " and rn > (" + first + ")";
             break;
        case "PostgreSQL":
      	       query += " SELECT trim(a.coduser), a.instancia, trim(b.descripcion)";
    	       query += " FROM instancias_usr a, instancias b";
		       query += " where a.instancia=b.instancia";
		       query += " and   cast(a.instancia as text) like '" + veccodrol[0] + "%'";
    	       query += " AND   a.coduser||cast(a.instancia as text)||b.descripcion like '%" + ((String) filterValue).toUpperCase() + "%'";
	  		   query += " order by " + sortField ;
	           query += " LIMIT " + pageSize;
	           query += " OFFSET " + first;
             break;
        case "Microsoft SQL Server":
				query += " SELECT TOP " + pageSize;
				query += " TOT.B_CODROL, "; 
				query += " TOT.DESROL, "; 
				query += " TOT.B_CODCAT1, "; 
				query += " TOT.DESCAT1 "; 
				query += " FROM (SELECT "; 
				query += "       ROW_NUMBER() OVER (ORDER BY A.B_CODROL ASC) AS ROW_NUM, ";
				query += " 	     A.B_CODROL, "; 
				query += " 	     B.DESROL, "; 
				query += " 	     A.B_CODCAT1, "; 
				query += " 	     C.DESCAT1 ";
				query += " 	     FROM ACCCAT1 A, BVT003 B, BVTCAT1 C ";
				query += " 	     WHERE "; 
				query += " 	     A.B_CODROL=B.CODROL "; 
				query += " 	     AND   A.B_CODCAT1=C.CODCAT1) TOT "; 
				query += " WHERE ";
				query += " TOT.B_CODROL LIKE '" + veccodrol[0] + "%'";
				query += " AND TOT.B_CODCAT1 + TOT.DESCAT1 LIKE '%" + ((String) filterValue).toUpperCase() + "%'";
				query += " AND TOT.ROW_NUM > " + first;
				query += " ORDER BY " + sortField ;
          break;
          }
  		
        
        pstmt = con.prepareStatement(query);
        //System.out.println(query);
  		
        r =  pstmt.executeQuery();

        
        while (r.next()){
        	Instanciasusr select = new Instanciasusr();
     	    select.setCoduser(r.getString(1));
     	    select.setInstancias(r.getString(2));
            select.setDescripcion(r.getString(3));
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
 		
 		if(instancias==null){
 			instancias = " - ";
 		}
 		if(instancias==""){
 			instancias = " - ";
 		}
 		String[] veccodrol = instancias.split("\\ - ", -1);
  		
  		switch ( productName ) {
        case "Oracle":
        	 query = "SELECT count_instanciasusr('" + ((String) filterValue).toUpperCase() + "','" + veccodrol[0] + "') from dual";
             break;
        case "PostgreSQL":
        	 query = "SELECT count_instanciasusr('" + ((String) filterValue).toUpperCase() + "','" + veccodrol[0] +  "')";
             break;
        case "Microsoft SQL Server":
       	 query = "SELECT DBO.count_instanciasusr('" + ((String) filterValue).toUpperCase() + "','" + veccodrol[0] +  "')";
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
  	private void selectInstanciasUsr() throws NamingException, SQLException  {
  		
  		Context initContext = new InitialContext();     
    	DataSource ds = (DataSource) initContext.lookup(JNDI);
        con = ds.getConnection();
  		   		
    	//Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
  		DatabaseMetaData databaseMetaData = con.getMetaData();
  		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
    	      		
  		String query = "";

  		switch ( productName ) {
  		case "Oracle":
	    	query = "Select trim(coduser), trim(coduser)||' - '||trim(desuser)";
	        query += " from bvt002";
	        query += " order by coduser";
	        break;
  		case "PostgreSQL":
  			query = "Select trim(coduser), trim(coduser)||' - '||trim(desuser)";
	        query += " from bvt002";
	        query += " order by coduser";
	        break;
  		case "Microsoft SQL Server":
  			query = "Select coduser, coduser||' - '||desuser";
	        query += " from bvt002";
	        query += " order by coduser";
	        break;
	        }


        //System.out.println(query);

        
        pstmt = con.prepareStatement(query);
        ////System.out.println(query);
  		
        r =  pstmt.executeQuery();
        
  		
        
        while (r.next()){
        	String cat1 = new String(r.getString(1));
        	String descat1 = new String(r.getString(2));
        	
            listUsr.put(descat1, cat1);
            sorted = sortByValues(listUsr);
            
        }
        //Cierra las conecciones
        pstmt.close();
        con.close();
        
  	}
   
   /**
  	 * @return the rows
  	 */
  	public int getRows() {
  		return rows;
  	}

  	private void limpiarValores() {
 		// TODO Auto-generated method stub
  		instancias = "";
 	}

  	public void reset() {
  		instancias = null;     
    }

}
