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
public class Acccat3 extends Bd implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private LazyDataModel<Acccat3> lazyModel;  
	
	

/**
	 * @return the lazyModel
	 */
	public LazyDataModel<Acccat3> getLazyModel() {
		return lazyModel;
	}
	
	@PostConstruct
	public void init() {
		if (instancia == null){instancia = "99999";}
		
		//Si no tiene acceso al módulo no puede ingresar
		if (new SeguridadMenuBean().opcmnu("M20")=="false") {
			RequestContext.getCurrentInstance().execute("PF('idleDialogNP').show()");
		}
		
		lazyModel  = new LazyDataModel<Acccat3>(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 7217573531435419432L;
			
            @Override
			public List<Acccat3> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) { 
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
		selectAcccat3();
	}
	
	
	 private String b_codrol = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("segrol");
	 private String b_codcat1 = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cat1"); 
	 private String b_codcat2 = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cat2"); 
	 private String b_codcat3 = "";
	 private String descat1 = "";
	 private String descat2 = "";
	 private String descat3 = "";
	 private Object filterValue = "";
	 private List<Acccat3> list = new ArrayList<Acccat3>();
	 private Map<String,String> listAcccat3 = new HashMap<String, String>();   //Lista de compañías disponibles para acceso a seguridad 
	 private List<String> selectedAcccat3;   //Listado de compañias seleccionadas
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
	 * @return the b_codcat3
	 */
	public String getB_codcat3() {
		return b_codcat3;
	}
	/**
	 * @param b_codcat3 the b_codcat3 to set
	 */
	public void setB_codcat3(String b_codcat3) {
		this.b_codcat3 = b_codcat3;
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
	 * @return the descat3
	 */
	public String getDescat3() {
		return descat3;
	}
	/**
	 * @param descat3 the descat3 to set
	 */
	public void setDescat3(String descat3) {
		this.descat3 = descat3;
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
	public List<Acccat3> getList() {
		return list;
	}
	/**
	 * @param list the list to set
	 */
	public void setList(List<Acccat3> list) {
		this.list = list;
	}
	/**
	 * @return the listAcccat3
	 */
	public Map<String, String> getListAcccat3() {
		return listAcccat3;
	}

	/**
	 * @param listAcccat3 the listAcccat3 to set
	 */
	public void setListAcccat3(Map<String, String> listAcccat3) {
		this.listAcccat3 = listAcccat3;
	}

	/**
	 * @return the selectedAcccat3
	 */
	public List<String> getSelectedAcccat3() {
		return selectedAcccat3;
	}

	/**
	 * @param selectedAcccat3 the selectedAcccat3 to set
	 */
	public void setSelectedAcccat3(List<String> selectedAcccat3) {
		this.selectedAcccat3 = selectedAcccat3;
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
     * Inserta acceso categoria3.
     * <p>
     * <b>Parametros del Metodo:<b> String codrol, String cat1, String cat2, String cat3 unidos como un solo string.<br>
     * String pool, String login.<br><br>
     **/
    public void insert(String codcat3) throws  NamingException {
    	//Valida que los campos no sean nulos
        try {
        	Context initContext = new InitialContext();     
     		DataSource ds = (DataSource) initContext.lookup(JNDI);
            con = ds.getConnection();
            
            String query = "INSERT INTO acccat3 VALUES (?,?,?,?,?,'" + getFecha() + "',?,'" + getFecha() + "',?)";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, b_codrol.split(" - ")[0].toUpperCase());
            pstmt.setString(2, b_codcat1.split(" - ")[0].toUpperCase());
            pstmt.setString(3, b_codcat2.split(" - ")[0].toUpperCase());
            pstmt.setString(4, codcat3.toUpperCase());
            pstmt.setString(5, login);
            pstmt.setString(6, login);
            pstmt.setInt(7, Integer.parseInt(instancia));
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
        if (selectedAcccat3.size()<=0){
    		msj = new FacesMessage(FacesMessage.SEVERITY_WARN, getMessage("acccat3IngCat3"), "");
    		FacesContext.getCurrentInstance().addMessage(null, msj);
    	} else {  	
    	   for (int i = 0; i< selectedAcccat3.size(); i++){
    		   ////System.out.println("Selected :" + selectedAcccat1.get(i));
    		insert(selectedAcccat3.get(i));           
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
	
	        	String query = "DELETE from acccat3 WHERE b_codrol||b_codcat1||b_codcat2||b_codcat3 in (" + param + ") and instancia = '" + instancia + "'";
	        		        	
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
 		if(b_codcat2==null){
 			b_codcat2 = " - ";
 		}
 		if(b_codcat2==""){
 			b_codcat2 = " - ";
 		}
 		String[] veccodrol = b_codrol.split("\\ - ", -1);
 		String[] veccodcat1 = b_codcat1.split("\\ - ", -1);
 		String[] veccodcat2 = b_codcat2.split("\\ - ", -1);
 		
 		switch ( productName ) {
        case "Oracle":
        	   query += "  select * from ";
        	   query += " ( select query.*, rownum as rn from";
        	   query += "  ( SELECT trim(a.b_codrol), trim(a.b_codcat1), trim(b.descat1), trim(a.b_codcat2), trim(c.descat2), trim(a.b_codcat3), trim(d.descat3)";
        	   query += " FROM acccat3 a, bvtcat1 b, bvtcat2 c, bvtcat3 d";
    		   query += " WHERE a.b_codcat1=b.codcat1 ";
    		   query += " and   a.b_codcat1=c.b_codcat1";
    		   query += " and   a.b_codcat2=c.codcat2";
    		   query += " and   a.b_codcat1=d.b_codcat1";
    		   query += " and   a.b_codcat2=d.b_codcat2";
    		   query += " and   a.b_codcat3=d.codcat3";
    		   query += " and A.instancia=B.instancia";
  		       query += " and A.instancia=c.instancia";
  		       query += " and A.instancia=d.instancia";
    		   query += " and  a.b_codrol like '" + veccodrol[0] + "%'";
    		   query += " and  A.b_codcat1 like '" + veccodcat1[0].toUpperCase() + "%'";
               query += " and  A.b_codcat2 like '" + veccodcat2[0].toUpperCase() + "%'";
        	   query += " AND   a.b_codcat1||b.descat1||a.b_codcat2||c.descat2||a.b_codcat3||d.descat3 like '%" + ((String) filterValue).toUpperCase() + "%'";
        	   query += " AND   a.instancia = '" + instancia + "'";
        	   query += " order by " + sortField + ") query";
	           query += " ) where rownum <= " + pageSize ;
	           query += " and rn > (" + first + ")";
             break;
        case "PostgreSQL":
        	   query += " SELECT trim(a.b_codrol), trim(a.b_codcat1), trim(b.descat1), trim(a.b_codcat2), trim(c.descat2), trim(a.b_codcat3), trim(d.descat3)";
        	   query += " FROM acccat3 a, bvtcat1 b, bvtcat2 c, bvtcat3 d";
    		   query += " WHERE a.b_codcat1=b.codcat1 ";
    		   query += " and   a.b_codcat1=c.b_codcat1";
    		   query += " and   a.b_codcat2=c.codcat2";
    		   query += " and   a.b_codcat1=d.b_codcat1";
    		   query += " and   a.b_codcat2=d.b_codcat2";
    		   query += " and   a.b_codcat3=d.codcat3";
    		   query += " and A.instancia=B.instancia";
  		       query += " and A.instancia=c.instancia";
  		       query += " and A.instancia=d.instancia";
    		   query += " and  a.b_codrol like '" + veccodrol[0] + "%'";
    		   query += " and  A.b_codcat1 like '" + veccodcat1[0].toUpperCase() + "%'";
               query += " and  A.b_codcat2 like '" + veccodcat2[0].toUpperCase() + "%'";
        	   query += " AND   a.b_codcat1||b.descat1||a.b_codcat2||c.descat2||a.b_codcat3||d.descat3 like '%" + ((String) filterValue).toUpperCase() + "%'";
        	   query += " AND   a.instancia = '" + instancia + "'";
	  		   query += " order by " + sortField ;
	           query += " LIMIT " + pageSize;
	           query += " OFFSET " + first;
             break;
        case "Microsoft SQL Server":
        	query += " SELECT TOP " + pageSize;
        	query += " TOT.ROW_NUM,  ";
        	query += " TOT.B_CODROL, ";
        	query += " TOT.B_CODCAT1, ";
        	query += " TOT.DESCAT1, ";
        	query += " TOT.B_CODCAT2, "; 
        	query += " TOT.DESCAT2, ";
        	query += " TOT.B_CODCAT3,  ";
        	query += " TOT.DESCAT3 ";
        	query += " FROM (SELECT  ";
        	query += "		ROW_NUMBER() OVER (ORDER BY A.B_CODROL ASC) AS ROW_NUM,  ";
        	query += "		A.B_CODROL,  ";
        	query += "		A.B_CODCAT1, "; 
        	query += "		B.DESCAT1,  ";
        	query += "		A.B_CODCAT2, ";
        	query += "		C.DESCAT2,  ";
        	query += "		A.B_CODCAT3,  ";
        	query += "		D.DESCAT3 ";
        	query += "		FROM ACCCAT3 A, BVTCAT1 B, BVTCAT2 C, BVTCAT3 D ";
        	query += "		WHERE  ";
        	query += "		A.B_CODCAT1=B.CODCAT1  ";
        	query += "		AND A.B_CODCAT1=C.B_CODCAT1 ";
        	query += "		AND A.B_CODCAT2=C.CODCAT2 ";
        	query += "		AND A.B_CODCAT1=D.B_CODCAT1 ";
        	query += "		AND A.B_CODCAT2=D.B_CODCAT2 ";
        	query += "		AND A.B_CODCAT3=D.CODCAT3) TOT ";
        	query += " WHERE ";
        	query += " TOT.B_CODROL = '" + veccodrol[0] + "'";
        	query += " AND TOT.B_CODCAT1 LIKE '" + veccodcat1[0].toUpperCase() + "%'";
        	query += " AND TOT.B_CODCAT2 LIKE '" + veccodcat2[0].toUpperCase() + "%'";
        	query += " AND TOT.B_CODCAT1+TOT.DESCAT1+TOT.B_CODCAT2+TOT.DESCAT2+TOT.B_CODCAT3+TOT.DESCAT3 LIKE '%" + ((String) filterValue).toUpperCase() + "%'";
        	query += " AND   tot.instancia = '" + instancia + "'";
        	query += " AND TOT.ROW_NUM > " + first;
        	query += " ORDER BY " + sortField ;
          break;
          }

 		//System.out.println(query);
        pstmt = con.prepareStatement(query);
        
  		
        r =  pstmt.executeQuery();
        
        while (r.next()){
        	Acccat3 select = new Acccat3();
     	    select.setB_codrol(r.getString(1));
        	select.setB_codcat1(r.getString(2));
            select.setDescat1(r.getString(3));
            select.setB_codcat2(r.getString(4));
            select.setDescat2(r.getString(5));
            select.setB_codcat3(r.getString(6));
            select.setDescat3(r.getString(7));
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
  	private void selectAcccat3() {
  		try {
  		Context initContext = new InitialContext();     
    	DataSource ds = (DataSource) initContext.lookup(JNDI);
        con = ds.getConnection();

    	//Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
  		DatabaseMetaData databaseMetaData = con.getMetaData();
  		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección        
        
  		String query = "";

  		String cat1 = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cat1"); //Usuario logeado
        String cat2 = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cat2"); //Usuario logeado
         
        if(cat1==null){
        	cat1 = " - ";
        }
        if(cat1==""){
        	cat1 = " - ";
        }
        if(cat2==null){
        	cat2 = " - ";
        }
        if(cat2==""){
        	cat2 = " - ";
        }
        String[] veccat1 = cat1.split("\\ - ", -1);
        String[] veccat2 = cat2.split("\\ - ", -1);

        switch ( productName ) {
        case "Oracle": 
			query = "Select codcat3, codcat3||' - '||descat3";
			query += " from bvtcat3";
			query += " where B_CODCAT1 = '" + veccat1[0].toUpperCase() + "'";
			query += " and B_CODCAT2 = '" + veccat2[0].toUpperCase() + "'";
			query += " and   instancia = '" + instancia + "'";
			query += " order by codcat3";
			break;
        case "PostgreSQL": 
			query = "Select codcat3, codcat3||' - '||descat3";
			query += " from bvtcat3";
			query += " where B_CODCAT1 = '" + veccat1[0].toUpperCase() + "'";
			query += " and B_CODCAT2 = '" + veccat2[0].toUpperCase() + "'";
			query += " and   instancia = '" + instancia + "'";
			query += " order by codcat3";
			break;
        case "Microsoft SQL Server": 
			query = "Select codcat3, codcat3+' - '+descat3";
			query += " from bvtcat3";
			query += " where B_CODCAT1 = '" + veccat1[0].toUpperCase() + "'";
			query += " and B_CODCAT2 = '" + veccat2[0].toUpperCase() + "'";
			query += " and   instancia = '" + instancia + "'";
			query += " order by codcat3";
			break;
			}
            

        ////System.out.println(query);

        
        pstmt = con.prepareStatement(query);
        ////System.out.println(query);
  		
        r =  pstmt.executeQuery();
        
  		
        
        while (r.next()){
        	String cat3 = new String(r.getString(1));
        	String descat3 = new String(r.getString(2));
        	
            listAcccat3.put(descat3, cat3);
            sorted = sortByValues(listAcccat3);
            
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
 		if(b_codcat2==null){
 			b_codcat2 = " - ";
 		}
 		if(b_codcat2==""){
 			b_codcat2 = " - ";
 		}
 		String[] veccodrol = b_codrol.split("\\ - ", -1);
 		String[] veccodcat1 = b_codcat1.split("\\ - ", -1);
 		String[] veccodcat2 = b_codcat2.split("\\ - ", -1);
  		
  		switch ( productName ) {
        case "Oracle":
        	 query = "SELECT count_acccat3('" + ((String) filterValue).toUpperCase() + "','" + veccodrol[0] + "','" + veccodcat1[0] + "','" + veccodcat2[0] + "','" + instancia + "') from dual";
             break;
        case "PostgreSQL":
        	 query = "SELECT count_acccat3('" + ((String) filterValue).toUpperCase() + "','" + veccodrol[0] + "','" + veccodcat1[0] + "','" + veccodcat2[0] + "','" + instancia + "')";
             break;
        case "Microsoft SQL Server":
       	 query = "SELECT DBO.count_acccat3('" + ((String) filterValue).toUpperCase() + "','" + veccodrol[0] + "','" + veccodcat1[0] + "','" + veccodcat2[0] + "','" + instancia + "')";
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
  		 b_codcat2 = "";
  		 b_codcat3 = "";
 	}
  	
  	public void reset() {
  		b_codrol = null;    
  		b_codcat1 = null;
  		b_codcat2 = null;
    }



}
