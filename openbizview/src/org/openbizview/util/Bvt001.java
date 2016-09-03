/*
o *  Copyright (C) 2011 - 2016  DVCONSULTORES

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
	public class Bvt001 extends Bd implements Serializable {
	
	/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		private LazyDataModel<Bvt001> lazyModel;  
		
		
		/**
		 * @return the lazyModel
		 */
		public LazyDataModel<Bvt001> getLazyModel() {
			return lazyModel;
		}	
	
	@PostConstruct
	public void init() {
		if (instancia == null){instancia = "99999";}
		
		//Si no tiene acceso al módulo no puede ingresar
		if (new SeguridadMenuBean().opcmnu("R01")=="false") {
		 RequestContext.getCurrentInstance().execute("PF('idleDialogNP').show()");
		}
		
		lazyModel  = new LazyDataModel<Bvt001>(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 7217573531435419432L;
			
            @Override
			public List<Bvt001> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) { 
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
	
	
	private String codrep = "";
	private String desrep = "";
	private String comrep = "";
	private String anio = "";
	private String vgrupo;
	private String vgrupodesgrupo;
	private String codgrup = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("codgrup"); 
	private String vlRol = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("rol"); //Usuario logeado
	private String instancia = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("instancia"); //Usuario logeado
	private int rows;
	private Object filterValue = "";
	private List<Bvt001> list = new ArrayList<Bvt001>();
	private String vinstancia = ""; //Istancia para el log

	
	/**
	 * @return the vlRol
	 */
	public String getVlRol() {
		return vlRol;
	}
	
	


	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}
	
	
	


	/**
	 * @return the rol
	 */
	public String getRol() {
		return rol;
	}


	/**
	 * @return the list
	 */
	public List<Bvt001> getList() {
		return list;
	}
	
	
	/**
	 * @param list the list to set
	 */
	public void setList(List<Bvt001> list) {
		this.list = list;
	}
	
	
	public String getCodrep() {
		return codrep;
	}
	
	
	public void setCodrep(String codrep) {
		this.codrep = codrep;
	}
	
	
	public String getDesrep() {
		return desrep;
	}
	
	
	public void setDesrep(String desrep) {
		this.desrep = desrep;
	}
	
	
	public String getComrep() {
		return comrep;
	}
	
	
	public void setComrep(String comrep) {
		this.comrep = comrep;
	}
	
	
	public String getAnio() {
		return anio;
	}
	
	
	public void setAnio(String anio) {
		this.anio = anio;
	}
	
	
	
	/**
	 * @return the codgrup
	 */
	public String getCodgrup() {
		return codgrup;
	}
	
	
	/**
	 * @param codgrup the codgrup to set
	 */
	public void setCodgrup(String codgrup) {
		this.codgrup = codgrup;
	}
	
	
	
	
	/**
	 * @return the vgrupo
	 */
	public String getVgrupo() {
		return vgrupo;
	}


	/**
	 * @param vgrupo the vgrupo to set
	 */
	public void setVgrupo(String vgrupo) {
		this.vgrupo = vgrupo;
	}


	/**
	 * @return the vgrupodesgrupo
	 */
	public String getVgrupodesgrupo() {
		return vgrupodesgrupo;
	}


	/**
	 * @param vgrupodesgrupo the vgrupodesgrupo to set
	 */
	public void setVgrupodesgrupo(String vgrupodesgrupo) {
		this.vgrupodesgrupo = vgrupodesgrupo;
	}
	
	
	


	/**
	 * @return the vinstancia
	 */
	public String getVinstancia() {
		return vinstancia;
	}

	/**
	 * @param vinstancia the vinstancia to set
	 */
	public void setVinstancia(String vinstancia) {
		this.vinstancia = vinstancia;
	}


	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Variables seran utilizadas para capturar mensajes de errores de Oracle y parametros de metodos
	PntGenerica consulta = new PntGenerica();
	boolean vGacc; //Validador de opciones del menú
	private String login = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario"); //Usuario logeado
	private String rol = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("rol"); //Usuario logeado
	FacesMessage msj =  null;
	
	
	
	//Coneccion a base de datos
	//Pool de conecciones JNDI
	//Coneccion a base de datos
	//Pool de conecciones JNDIFARM
	Connection con;
	PreparedStatement pstmt = null;
	PreparedStatement pstmt1 = null;
	ResultSet r;

	
	private int validarOperacion=0;
	
	
	public int getValidarOperacion() {
		return validarOperacion;
	}
	
	
	public void setValidarOperacion(int validarOperacion) {
		this.validarOperacion = validarOperacion;
	}


	/**
     * Inserta reportes.
     * <p>
     * <b>Parametros del Metodo:<b> String codrep, String desrep, Strim comrep unidos como un solo string.<br>
     * String pool, String login.<br><br>
     * <b>Ejemplo:</b>insertBvt003("01|EJEMPLO|EJEMPLO","jdbc/opennomina","admin").
     **/
    public void insert() throws  NamingException {
    	//Valida que los campos no sean nulos
        try {
        	Context initContext = new InitialContext();     
     		DataSource ds = (DataSource) initContext.lookup(JNDI);
     		
     		String[] veccodgrup = codgrup.split("\\ - ", -1);
     		
     		if(codgrup==null){
     			codgrup = " - ";
     		}
     		if(codgrup==""){
     			codgrup = " - ";
     		}
     		//System.out.println("Grupo: " + veccodgrup[0].toUpperCase());
     		con = ds.getConnection();		
            //Class.forName(getDriver());
            //con = DriverManager.getConnection(
            //      getUrl(), getUsuario(), getClave());
            String query = "INSERT INTO Bvt001 VALUES (?,?,?,?,'" + getFecha() + "',?,'" + getFecha() + "',?,?)";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, codrep.toUpperCase());
            pstmt.setString(2, desrep.toUpperCase());
            pstmt.setString(3, comrep.toUpperCase());
            pstmt.setString(4, login);
            pstmt.setString(5, login);
            pstmt.setString(6, veccodgrup[0].toUpperCase());
            pstmt.setInt(7, Integer.parseInt(instancia));
            ////System.out.println(query);
            try {
                //Avisando
                pstmt.executeUpdate();
                msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnInsert"), "");
                FacesContext.getCurrentInstance().addMessage(null, msj);
                desrep = "";
                codgrup = "";
                comrep = "";
                //Acceso automático a reportes
                insertAccesoReporte();
            } catch (SQLException e)  {
            	msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
            	FacesContext.getCurrentInstance().addMessage(null, msj);
            }
            
            pstmt.close();
            con.close();
    	} catch (Exception e){
    		msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
        	FacesContext.getCurrentInstance().addMessage(null, msj);
    		
    	}
    }
    
    
    /**
     * Inserta el acceso al reporte según el usuario logueado reportes.
     **/
    private void insertAccesoReporte() throws  NamingException {
    	//Valida que los campos no sean nulos
        try {
        	Context initContext = new InitialContext();     
     		DataSource ds = (DataSource) initContext.lookup(JNDI);
     		//System.out.println("Grupo: " + veccodgrup[0].toUpperCase());
     		con = ds.getConnection();		
            //Class.forName(getDriver());
            //con = DriverManager.getConnection(
            //      getUrl(), getUsuario(), getClave());
            String query = "INSERT INTO Bvt007 VALUES (?,?,?,'" + getFecha() + "',?,'" + getFecha() + "',?)";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, vlRol.toUpperCase());
            pstmt.setString(2, codrep.toUpperCase());
            pstmt.setString(3, login);
            pstmt.setString(4, login);
            pstmt.setInt(5, Integer.parseInt(instancia));
            ////System.out.println(query);
            try {
                //Avisando
                pstmt.executeUpdate();
                codrep = "";
            } catch (SQLException e)  {
            	e.printStackTrace();
            }
            pstmt.close();
            con.close();
    	} catch (Exception e){
    		e.printStackTrace();  		
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
	
	        	//String query = "DELETE from Bvt007 WHERE b_codrep in (" + param + ") and instancia = '" + instancia + "'";
	        	String query1 = "DELETE from Bvt001 WHERE codrep in (" + param + ") and instancia = '" + instancia + "'";
	        		        	
	           // pstmt = con.prepareStatement(query);
	            pstmt1 = con.prepareStatement(query1);
	            //System.out.println(query);
	
	            try {
	                //Avisando
	               // pstmt.executeUpdate();
	                pstmt1.executeUpdate();
	                msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnDelete"), "");
	                limpiarValores();	
	            } catch (SQLException e) {
	                e.printStackTrace();
	                msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
	            }
	
	            pstmt.close();
	            pstmt1.close();
	            con.close();
	
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
    	}
    	FacesContext.getCurrentInstance().addMessage(null, msj); 
    }
    
    
    
    private void limpiarValores() {
		// TODO Auto-generated method stub
    	codrep = "";
    	desrep = "";
    	comrep = "";
    	anio = "";
    	validarOperacion = 0; 
	}

        
    /**
     * Actualiza reportes
     * <b>Parametros del Metodo:<b> String codrep, String desrep, Strin comrep unidos como un solo string.<br>
     * String pool, String login.<br><br>
     * <b>Ejemplo:</b>updateBvt003("01|EJEMPLO|EJEMPLO","jdbc/opennomina","admin").
     **/
    public void update() throws  NamingException {
        try {
        	Context initContext = new InitialContext();     
     		DataSource ds = (DataSource) initContext.lookup(JNDI);
     		
           
     		
     		if(codgrup==null){
     			codgrup = " - ";
     		}
     		if(codgrup==""){
     			codgrup = " - ";
     		}
     		
     		String[] veccodgrup = codgrup.split("\\ - ", -1);

     		con = ds.getConnection();		
            //Class.forName(getDriver());
            //con = DriverManager.getConnection(
            //      getUrl(), getUsuario(), getClave());
            String query = "UPDATE Bvt001";
             query += " SET desrep = ?, comrep= ?, usract = ?, fecact='" + getFecha() + "', codgrup = '" + veccodgrup[0].toUpperCase() + "'";
             query += " WHERE codrep = ? and instancia = '" + instancia + "'";
            ////System.out.println(query);
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, desrep.toUpperCase());
            pstmt.setString(2, comrep.toUpperCase());
            pstmt.setString(3, login);
            pstmt.setString(4, codrep.toUpperCase());
            // Antes de ejecutar valida si existe el registro en la base de Datos.
            try {
                //Avisando
                pstmt.executeUpdate();
                if(pstmt.getUpdateCount()==0){
                	msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("msnNoUpdate"), "");
                } else {
                	msj = new FacesMessage(FacesMessage.SEVERITY_INFO,  getMessage("msnUpdate"), "");
                }
                validarOperacion = 0;
                desrep = "";
            	comrep = "";
            	codgrup = "";
            	comrep = "";
            } catch (SQLException e) {
            	msj = new FacesMessage(FacesMessage.SEVERITY_INFO, e.getMessage(), "");
            }
            pstmt.close();
            con.close();
        } catch (Exception e) {
        }
        FacesContext.getCurrentInstance().addMessage(null, msj);
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
 		
 		//Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
 		DatabaseMetaData databaseMetaData = con.getMetaData();
 		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección

		
		if(codgrup==null){
 			codgrup = " - ";
 		}
 		if(codgrup==""){
 			codgrup = " - ";
 		}
 		
 		String[] veccodgrup = codgrup.split("\\ - ", -1);

  		String query = "";

  		switch ( productName ) {
        case "Oracle":
        	   query += "  select * from ";
        	   query += " ( select query.*, rownum as rn from";
        	   query += " (SELECT trim(A.CODREP), trim(A.DESREP), trim(A.COMREP), trim(A.CODGRUP), trim(B.DESGRUP), a.instancia";
		       query += " FROM BVT001 A, BVT001A B";
		       query += " WHERE A.CODGRUP=B.CODGRUP(+)";
		       query += " AND A.CODREP||A.DESREP LIKE trim('%" + ((String) filterValue).toUpperCase() +  "%') ";
		       query += " AND   A.CODREP  IN (SELECT B_CODREP FROM BVT007 WHERE B_CODROL IN (SELECT B_CODROL FROM BVT002 WHERE CODUSER = '" + login + "' and instancia ='" + instancia + "' UNION ALL SELECT B_CODrol FROM BVT008 WHERE CODUSER = '" + login + "' and instancia = '" +  instancia + "'))";
		       query += " AND   A.instancia = '" + instancia + "'";
		       query += " AND   A.codrep like '" + codrep + "%'";
	  		   if(!veccodgrup[0].equals("")){
		  	   query += " AND   A.CODGRUP  LIKE trim('" + veccodgrup[0].toUpperCase() +  "%')";
		  	 	}
	  		   query += " order by " + sortField.replace("v", "") + ") query";
	           query += " ) where rownum <= " + pageSize ;
	           query += " and rn > (" + first + ")";
             break;
        case "PostgreSQL":
        	   query += " SELECT trim(A.CODREP), trim(A.DESREP), trim(A.COMREP), trim(A.CODGRUP), trim(fn_desgrup(A.CODGRUP)), a.instancia ";
		       query += " FROM BVT001 A ";
		       query += " where A.CODREP||A.DESREP LIKE trim('%" + ((String) filterValue).toUpperCase() +  "%') ";
		       query += " AND   A.CODREP  IN (SELECT B_CODREP FROM BVT007 WHERE B_CODROL IN (SELECT B_CODROL FROM BVT002 WHERE CODUSER = '" + login + "' and instancia ='" + instancia + "' UNION ALL SELECT B_CODrol FROM BVT008 WHERE CODUSER = '" + login + "' and instancia = '" +  instancia + "'))";
		       query += " AND   A.instancia = '" + instancia + "'";
		       query += " AND   A.codrep like '" + codrep + "%'";
	  		   if(!veccodgrup[0].equals("")){
		  	   query += " AND   A.CODGRUP  LIKE trim('" + veccodgrup[0].toUpperCase() +  "%')";
		  	 	}
	  		   query += " order by " + sortField.replace("v", "") ;
	           query += " LIMIT " + pageSize;
	           query += " OFFSET " + first;
             break;
        case "Microsoft SQL Server":
        	query += " SELECT TOP " + pageSize;
        	query += " TOT.CODREP, "; 
        	query += " TOT.DESREP, "; 
        	query += " TOT.COMREP, "; 
        	query += " TOT.CODGRUP, "; 
        	query += " TOT.DESGRUP ";
        	query += " TOT.instancia ";
        	query += " FROM (SELECT "; 
        	query += " 	  ROW_NUMBER() OVER (ORDER BY A.CODREP ASC) AS ROW_NUM ";
        	query += " 	  ,A.CODREP AS CODREP ";
        	query += " 	  ,A.DESREP AS DESREP ";
        	query += " 	  ,A.COMREP AS COMREP ";
        	query += " 	  ,A.CODGRUP AS CODGRUP ";
        	query += " 	  ,B.DESGRUP AS DESGRUP ";
        	query += " 	  FROM BVT001 A LEFT OUTER JOIN BVT001A B ON A.CODGRUP = B.CODGRUP "; 
        	query += " 	  WHERE "; 
        	query += " 	  A.CODREP + A.DESREP LIKE ('" + ((String) filterValue).toUpperCase() +  "%') ";
        	query += " AND   A.instancia = '" + instancia + "'";
        	query += " AND   A.codrep like '" + codrep + "%'";
	  		   if(!veccodgrup[0].equals("")){
		  	   query += " AND A.CODGRUP LIKE '" + veccodgrup[0].toUpperCase() +  "%'";
		  	 	}        	
        	query += " 	  AND A.CODREP IN (SELECT ";
        	query += " 					   B_CODREP "; 
        	query += " 					   FROM  ";
        	query += " 					   BVT007 ";  
        	query += " 					   WHERE "; 
        	query += " 					   B_CODROL = '" + vlRol + "')) TOT "; 
        	query += " WHERE "; 
        	query += " TOT.ROW_NUM > " + first;
        	query += " ORDER BY " + sortField.replace("v", "");
	         break;            		   
  		}
  		//System.out.println(query);
  		pstmt = con.prepareStatement(query);
       
  		
        r =  pstmt.executeQuery();
        		
        while (r.next()){
        Bvt001 select = new Bvt001();
     	select.setCodrep(r.getString(1));
     	select.setDesrep(r.getString(2));
     	select.setComrep(r.getString(3));
     	select.setVgrupo(r.getString(4));
        select.setVgrupodesgrupo(r.getString(5));	
        select.setVinstancia(r.getString(6));
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
 		
  	      		
 		if(codgrup==null){
 			codgrup = " - ";
 		}
 		if(codgrup==""){
 			codgrup = " - ";
 		}
 		
 		String[] veccodgrup = codgrup.split("\\ - ", -1);
 		String query = "";
  		
  		switch ( productName ) {
        case "Oracle":
        	 query = "SELECT count_bvt001('" + ((String) filterValue).toUpperCase() + "','" +  vlRol +   "','" + veccodgrup[0] + "','" + instancia + "') from dual";
             break;
        case "PostgreSQL":
        	 query = "SELECT count_bvt001('" + ((String) filterValue).toUpperCase() + "','" +  vlRol +   "','" + veccodgrup[0] + "','" + instancia + "')";
             break;
        case "Microsoft SQL Server":
        	 query = "SELECT DBO.COUNT_BVT001('" + ((String) filterValue).toUpperCase() + "','" +  vlRol +   "','" + veccodgrup[0] + "','" + instancia + "')"; 
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
  	
    	
    public void guardar() throws NamingException, SQLException{   	
    	if(validarOperacion==0){
    		insert();
    	} else {
    		update();
    	}
    }
      	
  	
    public void reset(){
    	codgrup = null;
    }
   
   /**
  	 * @return the rows
  	 */
  	public int getRows() {
  		return rows;
  	}
  	
  	
 
}
