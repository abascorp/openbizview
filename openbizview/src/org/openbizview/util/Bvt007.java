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
	public class Bvt007 extends Bd implements Serializable {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		
		private LazyDataModel<Bvt007> lazyModel;  
		
		
		/**
		 * @return the lazyModel
		 */
		public LazyDataModel<Bvt007> getLazyModel() {
			return lazyModel;
		}	
	
	@PostConstruct	
	public void init() {
		if (instancia == null){instancia = "99999";}
		
		//Si no tiene acceso al módulo no puede ingresar
		if (new SeguridadMenuBean().opcmnu("M16")=="false") {
			RequestContext.getCurrentInstance().execute("PF('idleDialogNP').show()");
		}
		
		lazyModel  = new LazyDataModel<Bvt007>(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 7217573531435419432L;
			
            @Override
			public List<Bvt007> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) { 
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
		selectRep();
	} catch (NamingException | SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	
	private String instancia = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("instancia"); //Usuario logeado
	private String b_codrol = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("segrol");
	private String b_codrep = "";
	private String desrol = "";
	private String desrep;
	private Object filterValue = "";
	private List<Bvt007> list = new ArrayList<Bvt007>();
	private Map<String,String> listRep = new HashMap<String, String>();   //Lista de compañías disponibles para acceso a seguridad 
	private List<String> selectedRep;   //Listado de compañias seleccionadas
	private Map<String, String> sorted;
	private String exito = "exito";
	
	
		

   

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
	 * @return the b_codrep
	 */
	public String getB_codrep() {
		return b_codrep;
	}

	/**
	 * @param b_codrep the b_codrep to set
	 */
	public void setB_codrep(String b_codrep) {
		this.b_codrep = b_codrep;
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
	 * @return the desrep
	 */
	public String getDesrep() {
		return desrep;
	}

	/**
	 * @param desrep the desrep to set
	 */
	public void setDesrep(String desrep) {
		this.desrep = desrep;
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
	public List<Bvt007> getList() {
		return list;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(List<Bvt007> list) {
		this.list = list;
	}

	/**
	 * @param rows the rows to set
	 */
	public void setRows(int rows) {
		this.rows = rows;
	}
	
	

		/**
	 * @return the listRep
	 */
	public Map<String, String> getListRep() {
		return listRep;
	}

	/**
	 * @param listRep the listRep to set
	 */
	public void setListRep(Map<String, String> listRep) {
		this.listRep = listRep;
	}

	/**
	 * @return the selectedRep
	 */
	public List<String> getSelectedRep() {
		return selectedRep;
	}

	/**
	 * @param selectedRep the selectedRep to set
	 */
	public void setSelectedRep(List<String> selectedRep) {
		this.selectedRep = selectedRep;
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
     * Inserta acceso reportes.
     * <p>
     * <b>Parametros del Metodo:<b> String rol, String codrep unidos como un solo string.<br>
     * String pool, String login.<br><br>
     **/
    public void insert(String pcodrep) throws  NamingException {
    	//Valida que los campos no sean nulos
    
        		String[] veccodrol = b_codrol.split("\\ - ", -1);
        try {
        	Context initContext = new InitialContext();     
     		DataSource ds = (DataSource) initContext.lookup(JNDI);
            con = ds.getConnection();
            
            String query = "INSERT INTO Bvt007 VALUES (?,?,?,'" + getFecha() + "',?,'" + getFecha() + "',?)";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, veccodrol[0].toUpperCase());
            pstmt.setString(2, pcodrep.toUpperCase());
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
        if (selectedRep.size()<=0){
    		msj = new FacesMessage(FacesMessage.SEVERITY_WARN, getMessage("bvt007IngRep"), "");
    		FacesContext.getCurrentInstance().addMessage(null, msj);
    	} else {  	
    	   for (int i = 0; i< selectedRep.size(); i++){
    		   ////System.out.println("Selected :" + selectedAcccat1.get(i));
    		insert(selectedRep.get(i));           
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
	
	        	String query = "DELETE from Bvt007 WHERE b_codrol||b_codrep in (" + param + ") and instancia = '" + instancia + "'";
	        		        	
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
 		//System.out.println("b_codrol = "+b_codrol);
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
        	   query += " (SELECT trim(a.b_codrol), trim(b.desrol), trim(a.b_codrep), trim(c.desrep)";
        	   query += " FROM Bvt007 a, bvt003 b, bvt001 c";
        	   query += " WHERE a.b_codrol=b.codrol";
        	   query += " and   a.b_codrep=c.codrep ";
        	   query += " and A.instancia=B.instancia";
        	   query += " and a.b_codrol||b.desrol||a.b_codrep||c.desrep like '%" + ((String) filterValue).toUpperCase() + "%'";
        	   query += " and  a.b_codrol like '" + veccodrol[0] + "%'";
        	   query += " AND   a.instancia = '" + instancia + "'";
	  		   query += " order by " + sortField + ") query";
	           query += " ) where rownum <= " + pageSize ;
	           query += " and rn > (" + first + ")";
             break;
        case "PostgreSQL":
        	   query += " SELECT trim(a.b_codrol), trim(b.desrol), trim(a.b_codrep), trim(c.desrep)";
        	   query += " FROM Bvt007 a, bvt003 b, bvt001 c";
        	   query += " WHERE a.b_codrol=b.codrol";
        	   query += " and   a.b_codrep=c.codrep ";
        	   query += " and A.instancia=B.instancia";
        	   query += " and a.b_codrol||b.desrol||a.b_codrep||c.desrep like '%" + ((String) filterValue).toUpperCase() + "%'";
        	   query += " and  a.b_codrol like '" + veccodrol[0] + "%'";
        	   query += " AND  a.instancia = '" + instancia + "'";
	  		   query += " order by " + sortField ;
	           query += " LIMIT " + pageSize;
	           query += " OFFSET " + first;
             break;
        case "Microsoft SQL Server":
      	   query += " SELECT TOP " + pageSize;
      	   query += " TOT.B_CODROL, ";
      	   query += " TOT.DESROL, ";
      	   query += " TOT.B_CODREP, ";
      	   query += " TOT.DESREP ";
      	   query += " FROM (SELECT ";
      	   query += " 	    ROW_NUMBER() OVER (ORDER BY A.B_CODROL ASC) AS ROW_NUM, ";
      	   query += " 	    A.B_CODROL, ";
      	   query += " 	    B.DESROL, ";
      	   query += " 	    A.B_CODREP, "; 
      	   query += " 	    C.DESREP ";
      	   query += " 	    FROM BVT007 A, BVT003 B, BVT001 C ";
      	   query += " 	    WHERE ";
      	   query += " 	    A.B_CODROL = B.CODROL ";
      	   query += " 	    AND A.B_CODREP=C.CODREP "; 
      	   query += " 	    AND A.B_CODROL + B.DESROL + A.B_CODREP + C.DESREP LIKE '%" + ((String) filterValue).toUpperCase() + "%'";
      	   query += " 	    AND A.B_CODROL LIKE '" + veccodrol[0] + "%') TOT ";
      	   query += "       AND   a.instancia = '" + instancia + "'";
      	   query += " WHERE ";
      	   query += " TOT.ROW_NUM <= " + pageSize;
      	   query += " AND TOT.ROW_NUM > " + first;
      	   query += " ORDER BY " + sortField ;
          break;
  		}
 		
        
        pstmt = con.prepareStatement(query);
        //System.out.println(query);
  		
        r =  pstmt.executeQuery();

        while (r.next()){
        	Bvt007 select = new Bvt007();
     	    select.setB_codrol(r.getString(1));
     	    select.setDesrol(r.getString(2));
     	    select.setB_codrep(r.getString(3));
     	    select.setDesrep(r.getString(4));
        	//Agrega la lista
        	list.add(select);
        }
        //Cierra las conecciones
        pstmt.close();
        con.close();
        
  	}
  	
  	
  	/**
     * Leer Datos de nominas para asignar a menucheck
     * @throws NamingException 
	 * @throws SQLException 
     * @throws IOException 
     **/ 	
  	private void selectRep() throws NamingException, SQLException  {
  		
  		Context initContext = new InitialContext();     
    	DataSource ds = (DataSource) initContext.lookup(JNDI);
        con = ds.getConnection();

        //Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
 		DatabaseMetaData databaseMetaData = con.getMetaData();
 		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección  		   		

 		String query = "";

  		switch ( productName ) {
        case "Oracle":
     		query = " Select codrep, codrep||' - '||desrep";
            query += " from bvt001";
            query += " where instancia = '" + instancia + "'";
            query += " order by codrep";
            
             break;
        case "PostgreSQL":
     		query = " Select codrep, codrep||' - '||desrep";
            query += " from bvt001";
            query += " where  instancia = '" + instancia + "'";
            query += " order by codrep";
             break;
        case "Microsoft SQL Server":
     		query = " Select codrep, codrep + ' - ' + desrep";
            query += " from bvt001";
            query += " where instancia = '" + instancia + "'";
            query += " order by codrep";
          break;
  		}

        ////System.out.println(query);

        
        pstmt = con.prepareStatement(query);
        ////System.out.println(query);
  		
        r =  pstmt.executeQuery();
        
  		
        
        while (r.next()){
        	String codrep = new String(r.getString(1));
        	String desrep = new String(r.getString(2));
        	
            listRep.put(desrep, codrep);
            sorted = sortByValues(listRep);
            
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
        	 query = "SELECT count_bvt007('" + ((String) filterValue).toUpperCase() + "','" + veccodrol[0] + "','" + instancia + "') from dual";
             break;
        case "PostgreSQL":
        	 query = "SELECT count_bvt007('" + ((String) filterValue).toUpperCase() + "','" + veccodrol[0] + "','" + instancia + "')";
             break;
        case "Microsoft SQL Server":
       	 query = "SELECT DBO.count_bvt007('" + ((String) filterValue).toUpperCase() + "','" + veccodrol[0] + "','" + instancia + "')";
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
	

	public void reset() {
  		b_codrol = null;    
    }
	
  	private void limpiarValores() {
		// TODO Auto-generated method stub
  		b_codrol = "";
  		b_codrep = "";
	}



}
