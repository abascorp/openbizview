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
public class Cst006 extends Bd implements Serializable {

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private LazyDataModel<Cst006> lazyModel;  
	
	
	/**
	 * @return the lazyModel
	 */
	public LazyDataModel<Cst006> getLazyModel() {
		return lazyModel;
	}	

@PostConstruct
public void init() {
	//System.out.println("entre al metodo INIT");
	lazyModel  = new LazyDataModel<Cst006>(){
		/**
		 * 
		 */
		private static final long serialVersionUID = 7217573531435419432L;
		
        @Override
		public List<Cst006> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) { 
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
	private String ordf = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("ordf"); //Usuario logeado
	private String anocal = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("anocal"); //Usuario logeado
	private String mescal = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("mescal"); //Usuario logeado
	private String ordini = "";
	private String ordfin = "";
	private Object filterValue = "";
	private List<Cst006> list = new ArrayList<Cst006>();
	private int validarOperacion = 0;
	private String instancia = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("instancia"); //Usuario logeado
	private String zordf = "";
	private String zanocal = "";
	private String zmescal = "";
	private String zordini = "";
	private String zordfin = "";
	private String zdesc1 = "";
	private String zdesc2 = "";
	private String zdesc3 = "";
	private String zdesc4 = "";

	public String getAnocal() {
		return anocal;
	}

	public void setAnocal(String anocal) {
		this.anocal = anocal;
	}

	public String getMescal() {
		return mescal;
	}

	public void setMescal(String mescal) {
		this.mescal = mescal;
	}

	public String getOrdini() {
		return ordini;
	}

	public void setOrdini(String ordini) {
		this.ordini = ordini;
	}

	public String getOrdfin() {
		return ordfin;
	}

	public void setOrdfin(String ordfin) {
		this.ordfin = ordfin;
	}

	public String getZanocal() {
		return zanocal;
	}

	public void setZanocal(String zanocal) {
		this.zanocal = zanocal;
	}

	public String getZmescal() {
		return zmescal;
	}

	public void setZmescal(String zmescal) {
		this.zmescal = zmescal;
	}

	public String getZordini() {
		return zordini;
	}

	public void setZordini(String zordini) {
		this.zordini = zordini;
	}

	public String getZordfin() {
		return zordfin;
	}

	public void setZordfin(String zordfin) {
		this.zordfin = zordfin;
	}

	public String getZdesc3() {
		return zdesc3;
	}

	public void setZdesc3(String zdesc3) {
		this.zdesc3 = zdesc3;
	}

	public String getZdesc4() {
		return zdesc4;
	}

	public void setZdesc4(String zdesc4) {
		this.zdesc4 = zdesc4;
	}

	public String getOrdf() {
		return ordf;
	}

	public void setOrdf(String ordf) {
		this.ordf = ordf;
	}

	public String getZordf() {
		return zordf;
	}

	public void setZordf(String zordf) {
		this.zordf = zordf;
	}

	public String getZdesc1() {
		return zdesc1;
	}

	public void setZdesc1(String zdesc1) {
		this.zdesc1 = zdesc1;
	}

	public String getZdesc2() {
		return zdesc2;
	}

	public void setZdesc2(String zdesc2) {
		this.zdesc2 = zdesc2;
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
        
        if(ordf==null){
 			ordf = " - ";
 		}
 		if(ordf==""){
 			ordf = " - ";
 		}     

        String[] vecordf = ordf.split("\\ - ", -1);
        
        String query = "INSERT INTO CST006 VALUES (?,?,?,?,?,?,'" + getFecha() + "',?,'" + getFecha() + "',?)";
        pstmt = con.prepareStatement(query);
        pstmt.setString(1, vecordf[0].toUpperCase());
        pstmt.setString(2, anocal.toUpperCase());
        pstmt.setString(3, mescal.toUpperCase());
        pstmt.setString(4, ordini.toUpperCase());
        pstmt.setString(5, ordfin.toUpperCase());
        pstmt.setString(6, login);
        pstmt.setString(7, login);            
        pstmt.setInt(8, Integer.parseInt(instancia));
        
        //System.out.println(query);
     
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

        	String query = "DELETE from CST006 WHERE ORDEN || ANOCAL || MESCAL in (" + param + ") and INSTANCIA = " + instancia + "";
        		        	
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
  		
        if(ordf==null){
 			ordf = " - ";
 		}
 		if(ordf==""){
 			ordf = " - ";
 		}  
      

        String[] vecordf = ordf.split("\\ - ", -1);
  		
        String query = "UPDATE CST006";
         query += " SET ORDINI = ?, ";
         query += " ORDFIN = ?,";
         query += " USRACT = ?,";
         query += " FECACT = '" + getFecha() + "'";
         query += " WHERE ORDEN = ? AND ANOCAL = ? AND MESCAL = ? AND INSTANCIA = " + instancia + "";

        pstmt = con.prepareStatement(query);
        pstmt.setString(1, ordini.toUpperCase());  
        pstmt.setString(2, ordfin.toUpperCase());  
        pstmt.setString(3, login);
        pstmt.setString(4, vecordf[0].toUpperCase());         
        pstmt.setString(5, anocal.toUpperCase());  
        pstmt.setString(6, mescal.toUpperCase());  

        //System.out.println(query);
        
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
		
        
        if(ordf==null){
 			ordf = " - ";
 		}
 		if(ordf==""){
 			ordf = " - ";
 		}     
 		
 		if(anocal==null){
 			anocal = " - ";
 		}
 		if(anocal==""){
 			anocal = " - ";
 		} 
 		
 		if(mescal==null){
 			mescal = " - ";
 		}
 		if(mescal==""){
 			mescal = " - ";
 		} 

        String[] vecordf = ordf.split("\\ - ", -1);
        String[] vecanocal = anocal.split("\\ - ", -1);
        String[] vecmescal = mescal.split("\\ - ", -1);
		
		//Consulta paginada
     String query = "SELECT * FROM"; 
	    query += "(select query.*, rownum as rn from";
		query += "(SELECT A.ORDEN, A.ANOCAL, A.MESCAL, A.ORDINI, A.ORDFIN, B.DESCR AS DESC2 ";
	    query += " FROM CST006 A, CST002 B";
	    query += " WHERE A.ORDEN = B.CODIGO";
	    query += " AND TRIM(A.ORDEN)  LIKE TRIM('" + vecordf[0]   + "%')";
	    query += " AND TRIM(A.ANOCAL) LIKE TRIM('" + vecanocal[0] + "%')"; 
	    query += " AND TRIM(A.MESCAL) LIKE TRIM('" + vecmescal[0] + "%')"; 
	    query += " GROUP BY A.ORDEN, A.ANOCAL, A.MESCAL, A.ORDINI, A.ORDFIN, B.DESCR";
	    query += ")query ) " ;
	    //query += " WHERE ROWNUM <="+pageSize;
	    //query += " AND rn > ("+ first +")";
	    query += " ORDER BY ORDEN, ANOCAL, MESCAL, ORDINI, ORDFIN " + sortField.replace("z", "");

    pstmt = con.prepareStatement(query);
    //System.out.println(query);
		
    r =  pstmt.executeQuery();
    
    while (r.next()){
 	Cst006 select = new Cst006();
 	select.setZordf(r.getString(1));
 	select.setZdesc1(r.getString(1)+ " - " + r.getString(6));
 	select.setZanocal(r.getString(2));
 	select.setZmescal(r.getString(3));
 	select.setZordini(r.getString(4));
 	select.setZordfin(r.getString(5));
 	select.setZdesc2(r.getString(1));
 	select.setZdesc3(r.getString(6));
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
      		
            if(ordf==null){
     			ordf = " - ";
     		}
     		if(ordf==""){
     			ordf = " - ";
     		} 
     		
     		if(anocal==null){
     			anocal = " - ";
     		}
     		if(anocal==""){
     			anocal = " - ";
     		} 
     		
     		if(mescal==null){
     			mescal = " - ";
     		}
     		if(mescal==""){
     			mescal = " - ";
     		} 

            String[] vecordf = ordf.split("\\ - ", -1);
            String[] vecanocal = anocal.split("\\ - ", -1);
            String[] vecmescal = mescal.split("\\ - ", -1);

     		//Consulta paginada
     		String query = "SELECT COUNT_CST006('" + ((String) filterValue).toUpperCase() + "','" + vecordf[0] + "','" + vecanocal[0] + "','" + vecmescal[0] + "','" + instancia + "') FROM DUAL";

           
           pstmt = con.prepareStatement(query);
           //System.out.println(query);
           //System.out.println(veccodven[0]);

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
  		anocal = "";
  		mescal = "";
  		ordini = "";
  		ordfin = "";
  		ordf = "";
  		validarOperacion = 0;
	}
  	
    public void reset() {
    	// TODO Auto-generated method stub
    	anocal = null;
    	mescal = null;
    	ordf = null;
    		
    }    
       
}
