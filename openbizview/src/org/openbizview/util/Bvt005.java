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

import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

	/**
	 *
	 * @author Andres
	 */
	@ManagedBean
	@ViewScoped
	public class Bvt005 extends Bd implements Serializable {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		
		private LazyDataModel<Bvt005> lazyModel;  
		
		
		/**
		 * @return the lazyModel
		 */
		public LazyDataModel<Bvt005> getLazyModel() {
			return lazyModel;
		}	
	
	@PostConstruct	
	public void init() {
		if (instancia == null){instancia = "99999";}
		
		//Si no tiene acceso al módulo no puede ingresar
		if (new SeguridadMenuBean().opcmnu("M17")=="false") {
		  RequestContext.getCurrentInstance().execute("PF('idleDialogNP').show()");
		}
		
		lazyModel  = new LazyDataModel<Bvt005>(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 7217573531435419432L;
			
            @Override
			public List<Bvt005> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) { 
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

	
	private String codopc = "";
	private String desopc = "";
	private String codvis = "";
	private String b_codrol = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("segrol");
	private Object filterValue = "";
	private List<Bvt005> list = new ArrayList<Bvt005>();
	String exito = "exito";
	int rows = 0;
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
	 * @return the codopc
	 */
	public String getCodopc() {
		return codopc;
	}
	
	/**
	 * @param codopc the codopc to set
	 */
	public void setCodopc(String codopc) {
		this.codopc = codopc;
	}
	
	/**
	 * @return the desopc
	 */
	public String getDesopc() {
		return desopc;
	}
	
	/**
	 * @param desopc the desopc to set
	 */
	public void setDesopc(String desopc) {
		this.desopc = desopc;
	}
	
	/**
	 * @return the codvis
	 */
	public String getCodvis() {
		return codvis;
	}
	
	/**
	 * @param codvis the codvis to set
	 */
	public void setCodvis(String codvis) {
		this.codvis = codvis;
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
	public List<Bvt005> getList() {
		return list;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(List<Bvt005> list) {
		this.list = list;
	}

	/**
	 * @param lazyModel the lazyModel to set
	 */
	public void setLazyModel(LazyDataModel<Bvt005> lazyModel) {
		this.lazyModel = lazyModel;
	}


	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Variables seran utilizadas para capturar mensajes de errores de Oracle y parametros de metodos
	PntGenerica consulta = new PntGenerica();
	boolean vGacc; //Validador de opciones del menú
	private String login = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario"); //Usuario logeado
	FacesMessage msj =  null;
	
	
	//Coneccion a base de datos
	//Pool de conecciones JNDI
	//Coneccion a base de datos
	//Pool de conecciones JNDIFARM
	Connection con;
	PreparedStatement pstmt = null;
    ResultSet r;

     /**
     * Inserta acceso botones.
     * <p>
     * <b>Parametros del Metodo:<b> String b_codrol, String codopc, String desopc, String codvis.<br>
     * String pool, String login.<br><br>
     **/
    public void insertBvt005(String rol, String strValores, String pool, String login) throws  NamingException {
    	 String[] vecValores = strValores.split("\\|", -1);
         String[] veccodrol = rol.split("\\ - ", -1);
        try {
        	Context initContext = new InitialContext();     
     		DataSource ds = (DataSource) initContext.lookup(JNDI);

     		con = ds.getConnection();		
     		
            String query = "INSERT INTO Bvt005 VALUES (?,?,?,?,?,'" + getFecha() + "',?,'" + getFecha() + "',?)";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, veccodrol[0].toUpperCase());
            pstmt.setString(2, vecValores[0].toUpperCase());
            pstmt.setString(3, vecValores[1].toUpperCase());
            pstmt.setString(4, vecValores[2].toUpperCase());
            pstmt.setString(5, login);
            pstmt.setString(6, login);
            pstmt.setInt(7, Integer.parseInt(instancia));
            ////System.out.println(query);
          ////System.out.println(query);
            try {
                //Avisando
                pstmt.executeUpdate();
                      
             } catch (SQLException e)  {
            	 exito = "error";
            	msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
            	FacesContext.getCurrentInstance().addMessage(null, msj);
            }
            pstmt.close();
            con.close();
            
        } catch (Exception e) {
        }
    	        
    }
    
    /**
    * Inserta acceso botones.
    * <p>
    * <b>Parametros del Metodo:<b> String b_codrol, String codopc, String desopc, String codvis.<br>
    * String pool, String login.<br><br>
     * @throws SQLException 
     * @throws ClassNotFoundException 
     * @throws IOException 
    **/
   public void insertBvt005Bot() throws  NamingException, ClassNotFoundException, SQLException {
	   String[] veccodrol = b_codrol.split("\\ - ", -1);
	   
	   consulta.selectPntGenerica("select * from bvt005 where b_codrol = '" + veccodrol[0].toUpperCase() + "'" , JNDI);
	   int registros = consulta.getRows();
	   
	   ////System.out.println("Registros: " + registros);
	   if (registros==3){
		   msj = new FacesMessage(FacesMessage.SEVERITY_WARN, getMessage("bvtmenumsj"), ""); 
		   FacesContext.getCurrentInstance().addMessage(null, msj);
		   exito="error";
	   } else {
       insertBvt005(veccodrol[0], "1|BOTON GUARDAR|0", JNDI, login);
       insertBvt005(veccodrol[0], "2|BOTON ELIMINAR|0", JNDI, login);
	   }
		if (exito.equals("exito")) {
			msj = new FacesMessage(FacesMessage.SEVERITY_INFO,
					getMessage("msnInsert"), "");
			FacesContext.getCurrentInstance().addMessage(null, msj);
		}
		limpiarValores();
   }
	   
   
   /**
    * Borra Paises
    * <p>
    * Parametros del metodo: String codpai. Pool de conecciones
    **/
   public void delete() throws  NamingException {  
	        try {
   		Context initContext = new InitialContext();     
    		DataSource ds = (DataSource) initContext.lookup(JNDI);

    		con = ds.getConnection();
    		
    		String[] veccodrol = b_codrol.split("\\ - ", -1);
    		
    		String query = "DELETE from Bvt005  WHERE b_codrol ='" + veccodrol[0].toUpperCase() + "' and instancia = '" + instancia + "'";
           pstmt = con.prepareStatement(query);
           //System.out.println(query);
           //Antes de insertar verifica si el rol del usuario tiene permisos para insertar
           
           try {
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
			
			FacesContext.getCurrentInstance().addMessage(null, msj); 
		}

       
    /**
     * Actualiza bvtmenu
     * <p> Parámetros del método: String codvis, String p_codrol, String codopc
     **/
    public void updateBvt005(String param, String codvis) throws  NamingException {
        String[] veccodrol = b_codrol.split("\\ - ", -1);
        ////System.out.println(cadStr);
        try {
        	Context initContext = new InitialContext();     
     		DataSource ds = (DataSource) initContext.lookup(JNDI);

     		con = ds.getConnection();
     		
            String query = "UPDATE BVT005 SET CODVIS = '" + codvis + "'"
                    + " WHERE B_CODROL = ? AND CODOPC IN ('" + param + "') and instancia = '" + instancia + "'";
            ////System.out.println(query);
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, veccodrol[0].toUpperCase());
            try {
                //Avisando
                pstmt.executeUpdate();
                
            } catch (SQLException e) {
            	msj = new FacesMessage(FacesMessage.SEVERITY_INFO, e.getMessage(), "");
            	FacesContext.getCurrentInstance().addMessage(null, msj);
            }
            pstmt.close();
            con.close();
        } catch (Exception e) {
        }
        
    }
    
    public void update() throws  NamingException, ClassNotFoundException, SQLException { 
    	String vista;
    	HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
    	String[] paramAcc = request.getParameterValues("toAcc");
    	String[] paramDacc = request.getParameterValues("toDacc");
    	if(paramAcc!=null && paramDacc!=null){
    		msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("bvtmenuUp1"), "");
    		FacesContext.getCurrentInstance().addMessage(null, msj);
    		exito = "error";
    	} else 	if(paramAcc!=null && paramDacc==null){
    	for(int i=0;i<paramAcc.length;i++){
    		vista = "0";
    		updateBvt005(paramAcc[i], vista);
    		  }
    	} else if (paramAcc==null && paramDacc!=null){
    	for(int i=0;i<paramDacc.length;i++){
          vista = "1";
        	updateBvt005(paramDacc[i], vista);
          }	
    	}
		if (exito.equals("exito")) {
			 msj = new FacesMessage(FacesMessage.SEVERITY_INFO,  getMessage("msnUpdate"), "");
			 FacesContext.getCurrentInstance().addMessage(null, msj);
		}
		limpiarValores();
    }

    
    public void guardar() throws NamingException, SQLException, ClassNotFoundException{   	
    	HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
    	String[] paramAcc = request.getParameterValues("toAcc");
    	String[] paramDacc = request.getParameterValues("toDacc");
    	////System.out.println(paramAcc);
    	////System.out.println(paramDacc);
    	if(paramAcc==null && paramDacc==null){
    		////System.out.println("Inserta");
    		insertBvt005Bot();
    	} else {
    		////System.out.println("Update");
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
        	   query += " (SELECT  trim(codopc), trim(desopc), decode(codvis,'0','ACCESO','SIN ACCESO'), trim(b_codrol)";
        	   query += " FROM Bvt005";
        	   query += " WHERE b_codrol||codopc||desopc like '%" + ((String) filterValue).toUpperCase() + "%'";
        	   query += " and  b_codrol like '" + veccodrol[0] + "%'";
        	   query += " AND   instancia = '" + instancia + "'";
	  		   query += " order by " + sortField + ") query";
	           query += " ) where rownum <= " + pageSize ;
	           query += " and rn > (" + first + ")";
             break;
        case "PostgreSQL":
        	   query += "  SELECT  trim(codopc), trim(desopc), case when codvis = '0' then 'ACCESO' else 'SIN ACCESO' end, trim(b_codrol)";
       	       query += " FROM Bvt005";
       	       query += " WHERE b_codrol||codopc||desopc like '%" + ((String) filterValue).toUpperCase() + "%'";
       	       query += " and  b_codrol like '" + veccodrol[0] + "%'";
       	       query += " AND   instancia = '" + instancia + "'";
	  		   query += " order by " + sortField ;
	           query += " LIMIT " + pageSize;
	           query += " OFFSET " + first;
             break;
        case "Microsoft SQL Server":
	        	query += " SELECT * ";
	        	query += " FROM (SELECT "; 
	        	query += " 		 ROW_NUMBER() OVER (ORDER BY A.CODOPC ASC) AS ROW_NUM, "; 
	        	query += "		 A.CODOPC, ";
	        	query += "		 A.DESOPC, ";
	        	query += "		 CASE ";
	        	query += "	 		WHEN A.CODVIS = '0' THEN 'ACCESO' ";
	        	query += " 			ELSE 'SIN ACCESO' ";
	        	query += "		 END COND, ";
	        	query += "		 A.B_CODROL ";
	        	query += "		 FROM BVT005 A) TOT ";
	        	query += " WHERE ";
	        	query += " TOT.B_CODROL + TOT.CODOPC + TOT.DESOPC LIKE '%" + ((String) filterValue).toUpperCase() + "%'";
	        	query += " AND  TOT.B_CODROL = '" + veccodrol[0] + "'";
	        	query += " AND   tot.instancia = '" + instancia + "'";
	        	query += " AND TOT.ROW_NUM <= " + pageSize;
	        	query += " AND TOT.ROW_NUM > " + first;
	        	query += " ORDER BY " + sortField ;
        	break;
  		}
        
        pstmt = con.prepareStatement(query);
        //System.out.println(query);
  		
        r =  pstmt.executeQuery();
        
        while (r.next()){
        	Bvt005 select = new Bvt005();
     		select.setCodopc(r.getString(1));
     		select.setDesopc(r.getString(2));
     		select.setCodvis(r.getString(3));
     		select.setB_codrol(r.getString(4));
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
        	 query = "SELECT count_bvt005('" + ((String) filterValue).toUpperCase() + "','" + veccodrol[0] + "','" + instancia + "') from dual";
             break;
        case "PostgreSQL":
        	 query = "SELECT count_bvt005('" + ((String) filterValue).toUpperCase() + "','" + veccodrol[0] + "','" + instancia + "')";
             break;
        case "Microsoft SQL Server":
       	 query = "SELECT DBO.count_bvt005('" + ((String) filterValue).toUpperCase() + "','" + veccodrol[0] + "','" + instancia + "')";
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
  	
  	public void limpiarValores(){
		codopc = "";
		desopc = "";
		codvis = "";
	}
  	

  	
   /**
 	 * @return the rows
 	 */
 	public int getRows() {
 		return rows;
 	}


 	public void reset(){
 		b_codrol="";
 	}

}
