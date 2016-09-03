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
	public class Acccat1 extends Bd implements Serializable {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		
		private LazyDataModel<Acccat1> lazyModel;  
		
		
	
	/**
		 * @return the lazyModel
		 */
		public LazyDataModel<Acccat1> getLazyModel() {
			return lazyModel;
		}


	@PostConstruct	
	public void init() {
		if (instancia == null){instancia = "99999";}
		
		//Si no tiene acceso al módulo no puede ingresar
		if (new SeguridadMenuBean().opcmnu("M18")=="false") {
			RequestContext.getCurrentInstance().execute("PF('idleDialogNP').show()");
		}
		
		lazyModel  = new LazyDataModel<Acccat1>(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 7217573531435419432L;
			
            @Override
			public List<Acccat1> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) { 
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
		selectAcccat1();
	} catch (NamingException | SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}

	
	private String b_codrol = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("segrol");
	private String desrol = "";
	private String b_codcat1 = "";
	private String descat1 = "";
	private Object filterValue = "";
	private List<Acccat1> list = new ArrayList<Acccat1>();
	private Map<String,String> listAcccat1 = new HashMap<String, String>();   //Lista de compañías disponibles para acceso a seguridad 
    private List<String> selectedAcccat1;   //Listado de compañias seleccionadas
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
	 * @return the desrol
	 */
	public String getDesrol() {
		return desrol;
	}


	/**
	 * @param desrol the desrol to set
	 */
	public void setDesrol(String desrol) {
		this.desrol = desrol;
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
	public List<Acccat1> getList() {
		return list;
	}


	/**
	 * @param list the list to set
	 */
	public void setList(List<Acccat1> list) {
		this.list = list;
	}


	/**
	 * @param rows the rows to set
	 */
	public void setRows(int rows) {
		this.rows = rows;
	}
	
    	

	/**
	 * @return the listAcccat1
	 */
	public Map<String, String> getListAcccat1() {
		return listAcccat1;
	}

	/**
	 * @param listAcccat1 the listAcccat1 to set
	 */
	public void setListAcccat1(Map<String, String> listAcccat1) {
		this.listAcccat1 = listAcccat1;
	}

	/**
	 * @return the selectedAcccat1
	 */
	public List<String> getSelectedAcccat1() {
		return selectedAcccat1;
	}

	/**
	 * @param selectedAcccat1 the selectedAcccat1 to set
	 */
	public void setSelectedAcccat1(List<String> selectedAcccat1) {
		this.selectedAcccat1 = selectedAcccat1;
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
    private void insert(String pcat1) throws  NamingException {
    		String[] veccodrol = b_codrol.split("\\ - ", -1);
        try {
        	Context initContext = new InitialContext();     
     		DataSource ds = (DataSource) initContext.lookup(JNDI);
            con = ds.getConnection();
            
            String query = "INSERT INTO acccat1 VALUES (?,?,?,'" + getFecha() + "',?,'" + getFecha() + "',?)";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, veccodrol[0].toUpperCase());
            pstmt.setString(2, pcat1.toUpperCase());
            pstmt.setString(3, login);
            pstmt.setString(4, login);
            pstmt.setInt(5, Integer.parseInt(instancia));
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
        if (selectedAcccat1.size()<=0){
    		msj = new FacesMessage(FacesMessage.SEVERITY_WARN, getMessage("acccat1IngCat1"), "");
    		FacesContext.getCurrentInstance().addMessage(null, msj);
    	} else {  	
    	   for (int i = 0; i< selectedAcccat1.size(); i++){
    		   ////System.out.println("Selected :" + selectedAcccat1.get(i));
    		insert(selectedAcccat1.get(i));           
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
	
	        	String query = "DELETE from acccat1 WHERE b_codrol||b_codcat1 in (" + param + ") and instancia = '" + instancia + "'";
	        		        	
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
 		String[] veccodrol = b_codrol.split("\\ - ", -1);

  		switch ( productName ) {
        case "Oracle":
        	   query += "  select * from ";
        	   query += " ( select query.*, rownum as rn from";
        	   query += " (SELECT trim(a.b_codrol), trim(b.desrol), trim(a.b_codcat1), trim(c.descat1)";
        	   query += " FROM acccat1 a, bvt003 b, bvtcat1 c";
  		       query += " WHERE a.b_codrol=b.codrol ";
  		       query += " and   a.b_codcat1=c.codcat1 ";
  		       query += " and A.instancia=B.instancia";
  		       query += " and A.instancia=c.instancia";
  		       query += " and   a.b_codrol like '" + veccodrol[0] + "%'";
        	   query += " AND   a.b_codcat1||c.descat1 like '%" + ((String) filterValue).toUpperCase() + "%'";
        	   query += " AND   a.instancia = '" + instancia + "'";
	  		   query += " order by " + sortField + ") query";
	           query += " ) where rownum <= " + pageSize ;
	           query += " and rn > (" + first + ")";
             break;
        case "PostgreSQL":
        	   query += " SELECT trim(a.b_codrol), trim(b.desrol), trim(a.b_codcat1), trim(c.descat1)";
      	       query += " FROM acccat1 a, bvt003 b, bvtcat1 c";
		       query += " WHERE a.b_codrol=b.codrol ";
		       query += " and   a.b_codcat1=c.codcat1 ";
		       query += " and A.instancia=B.instancia";
  		       query += " and A.instancia=c.instancia";
		       query += " and   a.b_codrol like '" + veccodrol[0] + "%'";
      	       query += " AND   a.b_codcat1||c.descat1 like '%" + ((String) filterValue).toUpperCase() + "%'";
      	       query += " AND   a.instancia = '" + instancia + "'";
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
				query += " TOT.B_CODROL = '" + veccodrol[0] + "'";
				query += " AND TOT.B_CODCAT1 + TOT.DESCAT1 LIKE '%" + ((String) filterValue).toUpperCase() + "%'";
				query += " AND   tot.instancia = '" + instancia + "'";
				query += " AND TOT.ROW_NUM > " + first;
				query += " ORDER BY " + sortField ;
          break;
          }
  		
        
        pstmt = con.prepareStatement(query);
        //System.out.println(query);
  		
        r =  pstmt.executeQuery();

        
        while (r.next()){
        	Acccat1 select = new Acccat1();
     	    select.setB_codrol(r.getString(1));
     	    select.setDesrol(r.getString(1) + " - " + r.getString(2));
        	select.setB_codcat1(r.getString(3));
        	select.setDescat1(r.getString(4));
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
 		String[] veccodrol = b_codrol.split("\\ - ", -1);
  		
  		switch ( productName ) {
        case "Oracle":
        	 query = "SELECT count_acccat1('" + ((String) filterValue).toUpperCase() + "','" + veccodrol[0] + "','" + instancia + "') from dual";
             break;
        case "PostgreSQL":
        	 query = "SELECT count_acccat1('" + ((String) filterValue).toUpperCase() + "','" + veccodrol[0] + "','" + instancia + "')";
             break;
        case "Microsoft SQL Server":
       	 query = "SELECT DBO.count_acccat1('" + ((String) filterValue).toUpperCase() + "','" + veccodrol[0] + "','" + instancia + "')";
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
  	private void selectAcccat1() throws NamingException, SQLException  {
  		
  		Context initContext = new InitialContext();     
    	DataSource ds = (DataSource) initContext.lookup(JNDI);
        con = ds.getConnection();
  		   		
    	//Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
  		DatabaseMetaData databaseMetaData = con.getMetaData();
  		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
    	      		
  		String query = "";

  		switch ( productName ) {
  		case "Oracle":
	    	query = "Select codcat1, codcat1||' - '||descat1";
	        query += " from bvtcat1";
	        query += " where   instancia = '" + instancia + "'";
	        query += " order by codcat1";
	        break;
  		case "PostgreSQL":
	    	query = "Select codcat1, codcat1||' - '||descat1";
	        query += " from bvtcat1";
	        query += " where   instancia = '" + instancia + "'";
	        query += " order by codcat1";
	        break;
  		case "Microsoft SQL Server":
	    	query = "Select codcat1, codcat1 + ' - ' + descat1";
	        query += " from bvtcat1";
	        query += " where   instancia = '" + instancia + "'";
	        query += " order by codcat1";
	        break;
	        }


        ////System.out.println(query);

        
        pstmt = con.prepareStatement(query);
        ////System.out.println(query);
  		
        r =  pstmt.executeQuery();
        
  		
        
        while (r.next()){
        	String cat1 = new String(r.getString(1));
        	String descat1 = new String(r.getString(2));
        	
            listAcccat1.put(descat1, cat1);
            sorted = sortByValues(listAcccat1);
            
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
  		b_codcat1 = "";
 	}

  	public void reset() {
  		b_codrol = null;     
    }

}
