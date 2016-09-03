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

import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.openbizview.util.PntGenerica;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;


@ManagedBean
@SessionScoped
public class Mailgrupos  extends Bd implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private LazyDataModel<Mailgrupos> lazyModel;  
	
	
	/**
	 * @return the lazyModel
	 */
	public LazyDataModel<Mailgrupos> getLazyModel() {
		return lazyModel;
	}	
	
	@PostConstruct
	public void init() {
		if (instancia == null){instancia = "99999";}
		//Si no tiene acceso al módulo no puede ingresar
		if (new SeguridadMenuBean().opcmnu("M23")=="false") {
			RequestContext.getCurrentInstance().execute("PF('idleDialogNP').show()");
		}
		
		lazyModel  = new LazyDataModel<Mailgrupos>(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 7217573531435419432L;
			
            @Override
			public List<Mailgrupos> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) { 
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
				} catch (SQLException | NamingException e) {	
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

	private String idgrupo = "";
	private String desgrupo = "";
	private int validarOperacion = 0;//Param guardar para validar si guarda o actualiza
	private Object filterValue = "";
	private List<Mailgrupos> list = new ArrayList<Mailgrupos>();
	private String instancia = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("instancia"); //Usuario logeado
	
	   
	
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
	 * @param lazyModel the lazyModel to set
	 */
	public void setLazyModel(LazyDataModel<Mailgrupos> lazyModel) {
		this.lazyModel = lazyModel;
	}

	
	
   
     /**
	 * @return the idgrupo
	 */
	public String getIdgrupo() {
		return idgrupo;
	}

	/**
	 * @param idgrupo the idgrupo to set
	 */
	public void setIdgrupo(String idgrupo) {
		this.idgrupo = idgrupo;
	}

	/**
	 * @return the desgrupo
	 */
	public String getDesgrupo() {
		return desgrupo;
	}

	/**
	 * @param desgrupo the desgrupo to set
	 */
	public void setDesgrupo(String desgrupo) {
		this.desgrupo = desgrupo;
	}

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
	 * @return the list
	 */
	public List<Mailgrupos> getList() {
		return list;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(List<Mailgrupos> list) {
		this.list = list;
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//Variables seran utilizadas para capturar mensajes de errores de Oracle y parametros de metodos
FacesMessage msj = null; 
PntGenerica consulta = new PntGenerica();
private int rows; //Registros de tabla


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
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

		//Coneccion a base de datos
		//Pool de conecciones JNDIFARM
		Connection con;
		PreparedStatement pstmt = null;
		ResultSet r;
	
	/**
     * Inserta Configuración.
     * Parametros del Metodo: String codpai, String despai. Pool de conecciones y login
	 * @throws NamingException 
     **/
    private void insert() throws NamingException {
        try {
        	 Context initContext = new InitialContext();     
     		DataSource ds = (DataSource) initContext.lookup(JNDI);

     		con = ds.getConnection();
    		

            String query = "INSERT INTO MAILGRUPOS VALUES (?,?,?)";
            pstmt = con.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(idgrupo));
            pstmt.setString(2, desgrupo.toUpperCase());
            pstmt.setInt(3, Integer.parseInt(instancia));

            try {
                pstmt.executeUpdate();
                msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnInsert"), "");
                limpiarValores();
            } catch (SQLException e)  {
                 //e.printStackTrace();
                 msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
            }
            
            pstmt.close();
            con.close();           
        } catch (Exception e) {
            //e.printStackTrace();
        }
        FacesContext.getCurrentInstance().addMessage(null, msj);
    }
    
    /**
     * Borra mailconfig
     * <p>
     * @throws NamingException 
     **/  
    public void delete() throws NamingException  {  

    	HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
    	String[] chkbox = request.getParameterValues("toDelete");
    	
    	if (chkbox==null){
    		msj = new FacesMessage(FacesMessage.SEVERITY_WARN, getMessage("del"), "");
    		FacesContext.getCurrentInstance().addMessage(null, msj); 
    	} else {
        try {

        	 Context initContext = new InitialContext();     
     		DataSource ds = (DataSource) initContext.lookup(JNDI);

     		con = ds.getConnection();
    		
        	
        	String param = "'" + StringUtils.join(chkbox, "','") + "'";

        	String query = "DELETE from MAILGRUPOS WHERE IDGRUPO in (" + param + ") and instancia = '" + instancia + "'";
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
    	
    	FacesContext.getCurrentInstance().addMessage(null, msj); 
    	}
    	
    }
    
    /**
     * Actualiza mailconfig
     **/
    private void update()  {
        try {

        	 Context initContext = new InitialContext();     
     		DataSource ds = (DataSource) initContext.lookup(JNDI);

     		con = ds.getConnection();
    		

            String query = "UPDATE MAILGRUPOS";
            	   query += " SET DESGRUPO = ?";
            	   query += " where IDGRUPO = ? and instancia = '" + instancia + "'";


            //System.out.println(query);
            	   pstmt = con.prepareStatement(query);
                   pstmt.setString(1, desgrupo.toUpperCase());
                   pstmt.setInt(2, Integer.parseInt(idgrupo));
            try {
                //Avisando
                pstmt.executeUpdate();
                if(pstmt.getUpdateCount()==0){
                	msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("msnNoUpdate"), "");
                } else {
                msj = new FacesMessage(FacesMessage.SEVERITY_INFO,  getMessage("msnUpdate"), "");
                }
                desgrupo = "";
                list.clear();
            } catch (SQLException e) {
                e.printStackTrace();
                msj = new FacesMessage(FacesMessage.SEVERITY_FATAL,  e.getMessage(), "");
            }

            pstmt.close();
            con.close();
              

        } catch (Exception e) {
            e.printStackTrace();
        }
        FacesContext.getCurrentInstance().addMessage(null, msj); 
    }
    
    
    /**
     * Genera las operaciones de guardar o modificar
     * @throws NamingException 
     * @throws SQLException 
     **/ 
    public void guardar() throws NamingException, SQLException{   	
    	if(validarOperacion==0){
    		insert();
    	} else {
    		update();
    	}
    }
    
    /**
     * Leer Datos de mailconfig
     * @throws NamingException 
     * @throws IOException 
     **/ 	
  	public void select(int first, int pageSize, String sortField, Object filterValue) throws SQLException, NamingException {
     try {	

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
        	   query += " (SELECT trim(IDGRUPO), trim(DESGRUPO)";
        	   query += " FROM MAILGRUPOS";
        	   query += " WHERE idgrupo||desgrupo like '%" + ((String) filterValue).toUpperCase() + "%'";
        	   query += " and  idgrupo like '" + idgrupo + "%'";
        	   query += " AND   instancia = '" + instancia + "'";
	  		   query += " order by " + sortField + ") query";
	           query += " ) where rownum <= " + pageSize ;
	           query += " and rn > (" + first + ")";
             break;
        case "PostgreSQL":
       	       query += " SELECT IDGRUPO, trim(DESGRUPO)";
    	       query += " FROM MAILGRUPOS";
    	       query += " WHERE idgrupo||desgrupo like '%" + ((String) filterValue).toUpperCase() + "%'";
    	       query += " and  cast(idgrupo as text) like '" + idgrupo + "%'";
    	       query += " AND   instancia = '" + instancia + "'";
	  		   query += " order by " + sortField ;
	           query += " LIMIT " + pageSize;
	           query += " OFFSET " + first;
             break;
        case "Microsoft SQL Server":
        	   query += " SELECT TOP " + pageSize;
        	   query += " TOT.IDGRUPO, ";
        	   query += " TOT.DESGRUPO  ";
        	   query += " FROM (SELECT  "; 
        	   query += " 	     ROW_NUMBER() OVER (ORDER BY IDGRUPO ASC) AS ROW_NUM, ";
        	   query += " 	     IDGRUPO, "; 
        	   query += " 	     LTRIM(RTRIM(DESGRUPO)) DESGRUPO ";
        	   query += " 	     FROM  ";
        	   query += " 	     MAILGRUPOS) TOT  ";
        	   query += " WHERE ";
        	   query += " LTRIM(RTRIM(CAST(TOT.IDGRUPO AS CHAR))) + TOT.DESGRUPO LIKE '%" + ((String) filterValue).toUpperCase() + "%' ";
        	   query += " AND LTRIM(RTRIM(CAST(TOT.IDGRUPO AS CHAR))) LIKE '" + idgrupo + "%' ";
        	   query += " AND   tot.instancia = '" + instancia + "'";
        	   query += " AND TOT.ROW_NUM > " + first;
        	   query += " ORDER BY TOT." + sortField ;
          break;
          }
   		
        
        pstmt = con.prepareStatement(query);
        //System.out.println(query);

         r =  pstmt.executeQuery();
        
        
        while (r.next()){
        	Mailgrupos select = new Mailgrupos();
            select.setIdgrupo(r.getString(1));
            select.setDesgrupo(r.getString(2));
        	//Agrega la lista
        	list.add(select);
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
 		
 		
 		switch ( productName ) {
        case "Oracle":
        	 query = "SELECT count_mailgrupo('" + ((String) filterValue).toUpperCase() + "','" + instancia + "') from dual";
             break;
        case "PostgreSQL":
        	 query = "SELECT count_mailgrupo('" + ((String) filterValue).toUpperCase() + "','" + instancia + "')";
             break;
        case "Microsoft SQL Server":
       	 query = "SELECT DBO.count_mailgrupo('" + ((String) filterValue).toUpperCase() + "','" + instancia + "')";
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
     * Limpia los valores
     **/
	public void limpiarValores(){
		idgrupo = "";
		desgrupo = "";
		validarOperacion=0;
	}
}
