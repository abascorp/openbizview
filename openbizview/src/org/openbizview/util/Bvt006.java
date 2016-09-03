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
	public class Bvt006 extends Bd implements Serializable {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		
		private LazyDataModel<Bvt006> lazyModel;  
		
		/**
		 * @return the lazyModel
		 */
		public LazyDataModel<Bvt006> getLazyModel() {
			return lazyModel;
		}	
	
	@PostConstruct
	public void init() {	
		if (instancia == null){instancia = "99999";}
		//Si no tiene acceso al módulo no puede ingresar
				if (new SeguridadMenuBean().opcmnu("M08")=="false") {
					RequestContext.getCurrentInstance().execute("PF('idleDialogNP').show()");
				}
		
		lazyModel  = new LazyDataModel<Bvt006>(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 7217573531435419432L;
			
            @Override
			public List<Bvt006> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) { 
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
	
	
	private String bcodrep = "";
	private String bdesrep = "";
	private String fecacc = "";
	private String bcoduser = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("bcoduser");
	private Object filterValue = "";
	private List<Bvt006> list = new ArrayList<Bvt006>();
	private String instancia = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("instancia"); //Usuario logeado


		/**
	 * @return the bcodrep
	 */
	public String getBcodrep() {
		return bcodrep;
	}
	
	
	/**
	 * @param bcodrep the bcodrep to set
	 */
	public void setBcodrep(String bcodrep) {
		this.bcodrep = bcodrep;
	}
	
	
	/**
	 * @return the bdesrep
	 */
	public String getBdesrep() {
		return bdesrep;
	}
	
	
	/**
	 * @param bdesrep the bdesrep to set
	 */
	public void setBdesrep(String bdesrep) {
		this.bdesrep = bdesrep;
	}
	
	
	/**
	 * @return the bcoduser
	 */
	public String getBcoduser() {
		return bcoduser;
	}
	
	
	/**
	 * @param bcoduser the bcoduser to set
	 */
	public void setBcoduser(String bcoduser) {
		this.bcoduser = bcoduser;
	}
	
		
		
	/**
	 * @return the list
	 */
	public List<Bvt006> getList() {
		return list;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(List<Bvt006> list) {
		this.list = list;
	}

	/**
	 * @return the fecacc
	 */
	public String getFecacc() {
		return fecacc;
	}

	/**
	 * @param fecacc the fecacc to set
	 */
	public void setFecacc(String fecacc) {
		this.fecacc = fecacc;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Variables seran utilizadas para capturar mensajes de errores de Oracle y parametros de metodos
	FacesMessage msj = null;
	PntGenerica consulta = new PntGenerica();
	private int rows; //Registros de tabla
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    //Coneccion a base de datos
    //Pool de conecciones JNDI
	Connection con;
	PreparedStatement pstmt = null;
	ResultSet r;



	
	
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
	
	        	String query = "DELETE from  Bvt006 WHERE b_codrep||b_coduser||to_char(fecacc,'dd/mm/yyyy hh:mi:ss') in (" + param + ") and instancia = '" + instancia + "'";
	        		        	
	            pstmt = con.prepareStatement(query);
	            //System.out.println(query);
	
	            try {
	                //Avisando
	                pstmt.executeUpdate();
	                msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnDelete"), "");
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
     * Leer Datos de auditoria
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
 		
 		if(bcoduser==null){
			bcoduser = " - ";
		}
		if(bcoduser==""){
			bcoduser = " - ";
		}
		
		String[] vlcoduser = bcoduser.split("\\ - ", -1); 

  		switch ( productName ) {
        case "Oracle":
        	   query += "  select * from ";
        	   query += " ( select query.*, rownum as rn from";
        	   query += " (SELECT trim(B_CODREP), trim(B_DESREP), trim(B_CODUSER), to_char(FECACC, 'dd/mm/yyyy hh:mi:ss')";
        	   query += " FROM bvt006";
        	   query += " WHERE B_CODUSER LIKE '" + vlcoduser[0] + "%'";
        	   query += " and b_codrep||b_desrep like '%" + ((String) filterValue).toUpperCase() + "%'";
        	   query += " AND   instancia = '" + instancia + "'";
	  		   query += " order by  B_CODUSER, FECACC desc) query";
	  		   query += " ) where rownum <= " + pageSize ;
	           query += " and rn > (" + first + ")";
             break;
        case "PostgreSQL":
        	   query += " SELECT trim(B_CODREP), trim(B_DESREP), trim(B_CODUSER), to_char(FECACC, 'dd/mm/yyyy hh:mi:ss') ";
        	   query += " FROM BVT006";
        	   query += " WHERE B_CODUSER LIKE '" + vlcoduser[0] + "%'";
        	   query += " and b_codrep||b_desrep like '%" + ((String) filterValue).toUpperCase() + "%'";
        	   query += " AND   instancia = '" + instancia + "'";
        	   query += " order by  B_CODUSER, FECACC desc";
	           query += " LIMIT " + pageSize;
	           query += " OFFSET " + first;
             break;
        case "Microsoft SQL Server":
	        	query += " SELECT * "; 
	        	query += " FROM (SELECT ";
	        	query += "	     ROW_NUMBER() OVER (ORDER BY B_CODREP ASC) AS ROW_NUM, "; 
	        	query += "	     B_CODREP, ";
	        	query += "	     B_DESREP, ";
	        	query += "	     B_CODUSER, ";
	        	query += "	     CONVERT(VARCHAR(10), FECACC, 103) + ' ' + CONVERT(VARCHAR(8), FECACC, 14) FECACC ";
	        	query += "	     FROM BVT006) TOT ";
	        	query += " WHERE "; 
	        	query += " TOT.B_CODUSER LIKE '" + vlcoduser[0] + "%'";
	        	query += " AND TOT.B_CODREP + TOT.B_DESREP LIKE '%" + ((String) filterValue).toUpperCase() + "%'";
	        	query += " AND  tot.instancia = '" + instancia + "'";
	        	query += " AND TOT.ROW_NUM <= " + pageSize;
	        	query += " AND TOT.ROW_NUM > " + first;
	        	query += " ORDER BY  ";
	        	query += " TOT.B_CODUSER,  ";
	        	query += " TOT.FECACC DESC ";
          break;
          }

 		 		

        
        pstmt = con.prepareStatement(query);
        ////System.out.println(query);
  		
        r =  pstmt.executeQuery();

        
        while (r.next()){
        Bvt006 select = new Bvt006();
        select.setBcodrep(r.getString(1));
        select.setBdesrep(r.getString(2));
        select.setBcoduser(r.getString(3));
        select.setFecacc(r.getString(4));
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
 		
 		if(bcoduser==null){
			bcoduser = " - ";
		}
		if(bcoduser==""){
			bcoduser = " - ";
		}
		
		String[] vlcoduser = bcoduser.split("\\ - ", -1); 
  		
  		switch ( productName ) {
        case "Oracle":
        	 query = "SELECT count_bvt006('" + ((String) filterValue).toUpperCase() + "','" + vlcoduser[0]  + "','" + instancia + "') from dual";
             break;
        case "PostgreSQL":
        	 query = "SELECT count_bvt006('" + ((String) filterValue).toUpperCase() + "','" + vlcoduser[0]  + "','" + instancia + "')";
             break;
        case "Microsoft SQL Server":
       	 query = "SELECT DBO.count_bvt006('" + ((String) filterValue).toUpperCase() + "','" + vlcoduser[0]  + "','" + instancia + "')";
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

  	public void reset() {
  		bcoduser = null;     
    }
 	
	
	/**
	 * @return Retorna número de filas
	 **/
	public int getRows(){
		return rows;
	}


}
