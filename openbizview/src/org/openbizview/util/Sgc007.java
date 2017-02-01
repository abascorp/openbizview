/*
 *  Copyright (C) 2011  ANDRES DOMINGUEZ

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
import java.util.ArrayList;
import java.util.List;

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
import org.openbizview.util.PntGenerica;
import org.openbizview.util.PntGenericasb;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
/**
 *
 * @author Mauricio
 */
@ManagedBean
@ViewScoped
public class Sgc007 extends Bd implements Serializable {

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private LazyDataModel<Sgc007> lazyModel;  
	
	
	/**
	 * @return the lazyModel
	 */
	public LazyDataModel<Sgc007> getLazyModel() {
		return lazyModel;
	}	

@PostConstruct
public void init() {
	//System.out.println("entre al metodo INIT");
	lazyModel  = new LazyDataModel<Sgc007>(){
		/**
		 * 
		 */
		private static final long serialVersionUID = 7217573531435419432L;
		
        @Override
		public List<Sgc007> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) { 
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


	/**
	 * 
	 */
    private String coduser = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("coduser"); //Usuario logeado
    private String comp = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("comp"); //Usuario logeado
	private Object filterValue = "";
	private List<Sgc007> list = new ArrayList<Sgc007>();
	private int validarOperacion = 0;
	private String instancia = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("instancia"); //Usuario logeado
	private String zcoduser = "";
	private String zcomp = "";
	private String zvaldel = "";

	public String getCoduser() {
		return coduser;
	}

	public void setCoduser(String coduser) {
		this.coduser = coduser;
	}

	public String getZcoduser() {
		return zcoduser;
	}

	public void setZcoduser(String zcoduser) {
		this.zcoduser = zcoduser;
	}

	public String getZvaldel() {
		return zvaldel;
	}

	public void setZvaldel(String zvaldel) {
		this.zvaldel = zvaldel;
	}

	public String getComp() {
		return comp;
	}

	public void setComp(String comp) {
		this.comp = comp;
	}

	public String getZcomp() {
		return zcomp;
	}

	public void setZcomp(String zcomp) {
		this.zcomp = zcomp;
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
	
	//Formateador de la fecha sdfecha
    //java.text.SimpleDateFormat sdfecha = new java.text.SimpleDateFormat("dd/MM/yyyy", locale);
    //String fecha = sdfecha.format(fecact); //Fecha formateada para insertar en tablas


	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//Variables seran utilizadas para capturar mensajes de errores de Oracle y parametros de metodos
	FacesMessage msj = null;
	PntGenericasb consulta = new PntGenericasb();
	PntGenerica consulta1 = new PntGenerica();
	boolean vGacc; //Validador de opciones del menú
	private int rows; //Registros de tabla Sybase
	//private int rows1; //Registros de tabla oracle
	private String login = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario"); //Usuario logeado
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


	//Coneccion a base de datos
	//Pool de conecciones JNDI
		Connection con;
		PreparedStatement pstmt = null;
		ResultSet r;


/**
 * Inserta categoria1.
 * <p>
 * <b>Parametros del Metodo:<b> String codcat1, String descat1 unidos como un solo string.<br>
 * String pool, String login.<br><br>
 **/
public void insert() throws  NamingException {   	
	//System.out.println("entre al metodo INSERT");	
    try {
    	Context initContext = new InitialContext();     
 		DataSource ds = (DataSource) initContext.lookup(JNDI);
        con = ds.getConnection();
        
        if(coduser==null){
 			coduser= " - ";
 		}
 		if(coduser==""){
 			coduser = " - ";
 		}    
        if(comp==null){
 			comp= " - ";
 		}
 		if(comp==""){
 			comp = " - ";
 		}        

        String[] veccodu = coduser.split("\\ - ", -1);
        String[] veccomp = comp.split("\\ - ", -1);
        
        String query = "INSERT INTO SGC007 VALUES (?,?,?,SYSDATE,?,SYSDATE,?)";
        pstmt = con.prepareStatement(query);
        pstmt.setString(1, veccodu[0].toUpperCase());
        pstmt.setString(2, veccomp[0].toUpperCase());
        pstmt.setString(3, login);
        pstmt.setString(4, login);            
        pstmt.setInt(5, Integer.parseInt(instancia));
        
        //System.out.println(query);
        //System.out.println(veccodu[0]);
        //System.out.println(veccomp[0]);

     
        try {
            //Avisando
        	pstmt.executeUpdate();
        	msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnInsert"), "");
            limpiarValores();                
         } catch (SQLException e)  {
        	msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("msnPrimaryk"), "");
        }
        
        pstmt.close();
        con.close();
    } catch (Exception e) {
    	e.printStackTrace();
    }	
    FacesContext.getCurrentInstance().addMessage(null, msj);
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

        	String query = "DELETE from SGC007 WHERE CODUSER || COMP in (" + param + ") and INSTANCIA = " + instancia + "";
        		        	
            pstmt = con.prepareStatement(query);
            //System.out.println(query);

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
 * Actualiza categoria1
 * <b>Parametros del Metodo:<b> String codcat1, String descat1 unidos como un solo string.<br>
 * String pool, String login.<br><br>
 **/
public void update() throws  NamingException {
	//System.out.println("entre al metodo UPDATE");
     try { 	 
    	 Context initContext = new InitialContext();     
  		DataSource ds = (DataSource) initContext.lookup(JNDI);
  		
  		con = ds.getConnection();	
  		
        if(coduser==null){
 			coduser= " - ";
 		}
 		if(coduser==""){
 			coduser = " - ";
 		}  
        if(comp==null){
 			comp= " - ";
 		}
 		if(comp==""){
 			comp = " - ";
 		}        

        String[] veccomp = comp.split("\\ - ", -1);
        String[] veccodu = coduser.split("\\ - ", -1);
  		
        String query = "UPDATE SGC007";
         query += " SET COMP = ?, ";
         query += " USRACT = ?,";
         query += " FECACT = '" + getFecha() + "'";
         query += " WHERE CODUSER = ? ";
         query += " AND INSTANCIA = " + instancia + "";

        pstmt = con.prepareStatement(query);
        pstmt.setString(1, veccomp[0].toUpperCase());
        pstmt.setString(2, login);
        pstmt.setString(3, veccodu[0].toUpperCase());
        
        //System.out.println(query);
        //System.out.println(veccodu[0]);
        //System.out.println(veccomp[0]);

        
        // Antes de ejecutar valida si existe el registro en la base de Datos.
        try {
            //Avisando
            pstmt.executeUpdate();
            if(pstmt.getUpdateCount()==0){
            	msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("msnNoUpdate"), "");
            } else {
            	msj = new FacesMessage(FacesMessage.SEVERITY_INFO,  getMessage("msnUpdate"), "");
            }
            limpiarValores();
        } catch (SQLException e) {
        	msj = new FacesMessage(FacesMessage.SEVERITY_INFO, e.getMessage(), "");
        }
        pstmt.close();
        con.close();
    } catch (Exception e) {
    }
    FacesContext.getCurrentInstance().addMessage(null, msj);
}

public void guardar() throws NamingException, SQLException{   	
	if(validarOperacion==0){
		insert();
	} else {
		update();
	}
}


/**
 * Leer Datos de paises
 * @throws NamingException 
* @throws IOException 
 **/ 	
	public void select(int first, int pageSize, String sortField, Object filterValue) throws SQLException, ClassNotFoundException, NamingException {
  		
		//System.out.println("entre al metodo SELECT");	
		Context initContext = new InitialContext();     
		DataSource ds = (DataSource) initContext.lookup(JNDI);
		con = ds.getConnection();		
		//Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
		DatabaseMetaData databaseMetaData = con.getMetaData();
		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
		
	    if(coduser==null){
	 			coduser= " - ";
	 		}
	 		if(coduser==""){
	 			coduser = " - ";
	 		}  
	        if(comp==null){
	 			comp= " - ";
	 		}
	 		if(comp==""){
	 			comp = " - ";
	 		}        

	        String[] veccomp = comp.split("\\ - ", -1);
	        String[] veccodu = coduser.split("\\ - ", -1);
	        
		//Consulta paginada
     String query = "SELECT * FROM"; 
	    query += "(select query.*, rownum as rn from";
		query += "(SELECT TRIM(A.CODUSER) AS CODUSER, A.COMP, B.DESCR AS DESC1 ";
	    query += " FROM SGC007 A, SGC005 B ";
	    query += " WHERE A.COMP = B.CODIGO ";
	    query += " AND TRIM(A.CODUSER) LIKE TRIM('%" + veccodu[0] + "%')";
	    query += " AND TRIM(A.COMP) LIKE TRIM('%" + veccomp[0] + "%')";
	    query += " GROUP BY A.CODUSER, A.COMP, B.DESCR";
	    query += ")query ) " ;
	    query += " WHERE ROWNUM <="+pageSize;
	    query += " AND rn > ("+ first +")";
	    query += " ORDER BY  " + sortField.replace("z", "");

    pstmt = con.prepareStatement(query);
    //System.out.println(query);
		
    r =  pstmt.executeQuery();
    
    while (r.next()){
 	Sgc007 select = new Sgc007();
 	select.setZcomp(r.getString(2) + " - " + r.getString(3));
 	select.setZcoduser(r.getString(1));
 	select.setZvaldel(r.getString(1) + "" + r.getString(2));
   	
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

    public void counter(Object filterValue) throws SQLException, NamingException {
    	//System.out.println("entre al metodo COUNTER");
        try {	
    
       	    Context initContext = new InitialContext();     
      		DataSource ds = (DataSource) initContext.lookup(JNDI);

      		con = ds.getConnection();
      		
    	    if(coduser==null){
    	 			coduser= " - ";
    	 		}
    	 		if(coduser==""){
    	 			coduser = " - ";
    	 		}  
    	        if(comp==null){
    	 			comp= " - ";
    	 		}
    	 		if(comp==""){
    	 			comp = " - ";
    	 		}        

    	        String[] veccomp = comp.split("\\ - ", -1);
    	        String[] veccodu = coduser.split("\\ - ", -1);

     		//Consulta paginada
     		String query = "SELECT COUNT_SGC007('" + ((String) filterValue).toUpperCase() + "','" + veccodu[0] + "','" + veccomp[0] + "','" + instancia + "') FROM DUAL";

           
           pstmt = con.prepareStatement(query);
           //System.out.println(query);
           //System.out.println(veccomp[0]);

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
  		comp = "";
  		coduser = "";
  		validarOperacion = 0;
	}
  	
	public void reset() {
		// TODO Auto-generated method stub
  		comp = null;
  		coduser = null;
	}
       
}
