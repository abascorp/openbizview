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
import javax.sql.DataSource;

import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
	

	
	
	/**
	 *
	 * @author Andres
	 */
	@ManagedBean
	@ViewScoped
	public class BiAudit extends Bd implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		
		private LazyDataModel<BiAudit> lazyModel; 
		
		/**
		 * @return the lazyModel
		 */
		public LazyDataModel<BiAudit> getLazyModel() {
			return lazyModel;
		}	
	
		
	@PostConstruct	
	public void init() {
		if (instancia == null){instancia = "99999";}
		
		//Si no tiene acceso al módulo no puede ingresar
		if (new SeguridadMenuBean().opcmnu("M09")=="false") {
			RequestContext.getCurrentInstance().execute("PF('idleDialogNP').show()");
		}
		
		lazyModel  = new LazyDataModel<BiAudit>(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 7217573531435419432L;
			
            @Override
			public List<BiAudit> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) { 
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

		private String fechadia = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fecha"); 
		private String vfecaacc = "";
		private String detfaz = "";
        private String hora = "";
        private String descripcion = "";
        private String estatus = "";
        private String negocio = "";
		private Object filterValue = "";
		private List<BiAudit> list = new ArrayList<BiAudit>();
		private String instancia = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("instancia"); //Usuario logeado

		
       
		/**
		 * @return the fechadia
		 */
		public String getFechadia() {
			return fechadia;
		}


		/**
		 * @param fechadia the fechadia to set
		 */
		public void setFechadia(String fechadia) {
			this.fechadia = fechadia;
		}


		/**
		 * @return the hora
		 */
		public String getHora() {
			return hora;
		}


		/**
		 * @param hora the hora to set
		 */
		public void setHora(String hora) {
			this.hora = hora;
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
		 * @return the estatus
		 */
		public String getEstatus() {
			return estatus;
		}


		/**
		 * @param estatus the estatus to set
		 */
		public void setEstatus(String estatus) {
			this.estatus = estatus;
		}


		/**
		 * @return the negocio
		 */
		public String getNegocio() {
			return negocio;
		}


		/**
		 * @param negocio the negocio to set
		 */
		public void setNegocio(String negocio) {
			this.negocio = negocio;
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
	 * @return the rows
	 */
	public int getRows() {
		return rows;
	}

	/**
	 * @param rows the rows to set
	 */
	public void setRows(int rows) {
		this.rows = rows;
	}
	
	
	

		/**
	 * @return the list
	 */
	public List<BiAudit> getList() {
		return list;
	}


	/**
	 * @param list the list to set
	 */
	public void setList(List<BiAudit> list) {
		this.list = list;
	}
	
	

		/**
	 * @return the vfecaacc
	 */
	public String getVfecaacc() {
		return vfecaacc;
	}


	/**
	 * @param vfecaacc the vfecaacc to set
	 */
	public void setVfecaacc(String vfecaacc) {
		this.vfecaacc = vfecaacc;
	}
	
	

		/**
	 * @return the detfaz
	 */
	public String getDetfaz() {
		return detfaz;
	}


	/**
	 * @param detfaz the detfaz to set
	 */
	public void setDetfaz(String detfaz) {
		this.detfaz = detfaz;
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
            	   query += " (select to_char(fechadia,'dd/mm/yyyy'), substr(fecacc,12,22), detfaz, result, negocio";
            	   query += " FROM biaudit";
            	   query += " WHERE substr(fecacc,12,22)||detfaz||negocio like '%" + ((String) filterValue).toUpperCase() + "%'";
            	   query += " AND   instancia = '" + instancia + "'";
            	   if(fechadia != null && !fechadia.equals("0") && !fechadia.equals(getMessage("biauditFecD"))){
                	   query += " and fechadia = '" + fechadia + "'";
                	   }
    	  		   query += " order by fechadia desc) query";
    	           query += " ) where rownum <= " + pageSize ;
    	           query += " and rn > (" + first + ")";
                 break;
            case "PostgreSQL":
            	   query += " select to_char(fechadia,'dd/mm/yyyy'), substr(fecacc,12,22), detfaz, result, negocio ";
            	   query += " FROM biaudit";
            	   query += " WHERE substr(fecacc,12,22)||detfaz||negocio like '%" + ((String) filterValue).toUpperCase() + "%'";
            	   query += " AND   instancia = '" + instancia + "'";
            	   if(fechadia != null && !fechadia.equals("0") && !fechadia.equals(getMessage("biauditFecD"))){
            	   query += " and fechadia = '" + fechadia + "'";
            	   }
            	   query += " order by fechadia desc";
    	           query += " LIMIT " + pageSize;
    	           query += " OFFSET " + first;
                 break;
            }
	        
	        pstmt = con.prepareStatement(query);
	        //System.out.println(query);
	  		
	        r =  pstmt.executeQuery();

	        while (r.next()){
	        BiAudit select = new BiAudit();
	     	select.setFechadia(r.getString(1));
	        select.setVfecaacc(r.getString(2));
	        select.setDetfaz(r.getString(3));
	        select.setEstatus(r.getString(4).toUpperCase());
	        select.setNegocio(r.getString(5));

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
	 		String busqueda = "1";
	 		
	 		if (fechadia==null && productName.equals("Oracle")){
	 			fechadia = "0";
	 		} else	if (fechadia==null || fechadia.equals("0") && productName.equals("PostgreSQL") ){	 		
	 			busqueda = "0";
	 			fechadia = "01/01/1900";
	 		}
	 		//System.out.println("Fechadia:" + fechadia);
	  		switch ( productName ) {
	        case "Oracle":
	        	 query = "SELECT count_biaudit('" + ((String) filterValue).toUpperCase() + "','" + fechadia + "','" + instancia + "') from dual";
	             break;
	        case "PostgreSQL":
	        	 query = "SELECT count_biaudit('" + ((String) filterValue).toUpperCase() + "','" + fechadia + "','" + busqueda  + "','" + instancia + "')";
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
	  		fechadia = null;     
	    }

}
