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
public class Indt05 extends Bd implements Serializable {

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private LazyDataModel<Indt05> lazyModel;  
	
	
	/**
	 * @return the lazyModel
	 */
	public LazyDataModel<Indt05> getLazyModel() {
		return lazyModel;
	}	

@PostConstruct
public void init() {
	//System.out.println("entre al metodo INIT");
	lazyModel  = new LazyDataModel<Indt05>(){
		/**
		 * 
		 */
		private static final long serialVersionUID = 7217573531435419432L;
		
        @Override
		public List<Indt05> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) { 
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
	private String codcia= (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("codcia"); //Usuario logeado
	private String codubi= (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("codubi"); //Usuario logeado
	private String coduser= (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("coduser"); //Usuario logeado
	private String user = "";
	private Object filterValue = "";
	private List<Indt05> list = new ArrayList<Indt05>();
	private int validarOperacion = 0;
	private String instancia = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("instancia"); //Usuario logeado
	private String zcodcia= "";
	private String zcodubi= "";
	private String zcoduser= "";
	private String zuser = "";
	private String zdesc1= "";
	private String zdesc2= "";
	private String zdesc3= "";
	private String zdesc4 = "";
	
	public String getCodcia() {
		return codcia;
	}

	public void setCodcia(String codcia) {
		this.codcia = codcia;
	}

	public String getCodubi() {
		return codubi;
	}

	public void setCodubi(String codubi) {
		this.codubi = codubi;
	}

	public String getCoduser() {
		return coduser;
	}

	public void setCoduser(String coduser) {
		this.coduser = coduser;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getZcodcia() {
		return zcodcia;
	}

	public void setZcodcia(String zcodcia) {
		this.zcodcia = zcodcia;
	}

	public String getZcodubi() {
		return zcodubi;
	}

	public void setZcodubi(String zcodubi) {
		this.zcodubi = zcodubi;
	}

	public String getZcoduser() {
		return zcoduser;
	}

	public void setZcoduser(String zcoduser) {
		this.zcoduser = zcoduser;
	}

	public String getZuser() {
		return zuser;
	}

	public void setZuser(String zuser) {
		this.zuser = zuser;
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
        
        if(codcia==null){
 			codcia = " - ";
 		}
 		if(codcia==""){
 			codcia = " - ";
 		}        
        if(codubi==null){
 			codubi = " - ";
 		}
 		if(codubi==""){
 			codubi = " - ";
 		}        
        if(coduser==null){
 			coduser = " - ";
 		}
 		if(coduser==""){
 			coduser = " - ";
 		}        

        String[] veccia = codcia.split("\\ - ", -1);
        String[] vecubi = codubi.split("\\ - ", -1);
        String[] vecuser = coduser.split("\\ - ", -1);
                    
        String query = "INSERT INTO INDT05 VALUES (?,?,?,?,'" + getFecha() + "',?,'" + getFecha() + "',?)";
        pstmt = con.prepareStatement(query);
        pstmt.setString(1, veccia[0].toUpperCase());
        pstmt.setString(2, vecubi[0].toUpperCase());
        pstmt.setString(3, vecuser[0].toUpperCase());
        pstmt.setString(4, login);
        pstmt.setString(5, login);            
        pstmt.setInt(6, Integer.parseInt(instancia));
        
        //System.out.println(query);
        //System.out.println(veccia[0]);
        //System.out.println(vecubi[0]);
        //System.out.println(vecuser[0]);
     
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

        	String query = "DELETE from INDT05 WHERE CODCIA || CODUBI || CODUSR in (" + param + ") and INSTANCIA = " + instancia + "";
        		        	
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
  		
  	  if(codcia==null){
			codcia = " - ";
		}
		if(codcia==""){
			codcia = " - ";
		}        
      if(codubi==null){
			codubi = " - ";
		}
		if(codubi==""){
			codubi = " - ";
		}        
      if(coduser==null){
			coduser = " - ";
		}
		if(coduser==""){
			coduser = " - ";
		}        

      String[] veccia = codcia.split("\\ - ", -1);
      String[] vecubi = codubi.split("\\ - ", -1);
      String[] vecuser = coduser.split("\\ - ", -1);
  		
        String query = "UPDATE INDT05";
         query += " SET CODUSR = ?, ";
         query += " USRACT = ?,";
         query += " FECACT = '" + getFecha() + "'";
         query += " WHERE CODCIA = ? AND CODUBI = ? AND INSTANCIA = " + instancia + "";

        pstmt = con.prepareStatement(query);
        pstmt.setString(1, vecuser[0].toUpperCase());
        pstmt.setString(2, login);
        pstmt.setString(3, veccia[0].toUpperCase());    
        pstmt.setString(4, vecubi[0].toUpperCase()); 

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
		
	 	  if(codcia==null){
				codcia = " - ";
			}
			if(codcia==""){
				codcia = " - ";
			}        
	      if(codubi==null){
				codubi = " - ";
			}
			if(codubi==""){
				codubi = " - ";
			}        
	      if(coduser==null){
				coduser = " - ";
			}
			if(coduser==""){
				coduser = " - ";
			}        

	      String[] veccia = codcia.split("\\ - ", -1);
	      String[] vecubi = codubi.split("\\ - ", -1);
	      String[] vecuser = coduser.split("\\ - ", -1);
		
		//Consulta paginada
     String query = "SELECT * FROM"; 
	    query += "(select query.*, rownum as rn from";
		query += "(SELECT A.CODCIA, A.CODUBI, A.CODUSR, B.DESCIA AS DESC1, C.DESUBI AS DESC2, D.DESUSER AS DESC3 ";
	    query += " FROM INDT05 A, INDT03 B, INDT04 C, BVT002 D";
	    query += " WHERE A.CODCIA = B.CODCIA";
	    query += " AND A.CODUBI = C.CODUBI";
	    query += " AND A.CODUSR = D.CODUSER";
	    query += " AND TRIM(A.CODCIA) LIKE TRIM('%" + veccia[0] + "%')";
	    query += " AND TRIM(A.CODUBI) LIKE TRIM('%" + vecubi[0] + "%')";
	    query += " AND TRIM(A.CODUSR) LIKE TRIM('%" + vecuser[0] + "%')";
	    query += " GROUP BY A.CODCIA, A.CODUBI, A.CODUSR, B.DESCIA, C.DESUBI, D.DESUSER";
	    query += ")query ) " ;
	    query += " WHERE ROWNUM <="+pageSize;
	    query += " AND rn > ("+ first +")";
	    query += " ORDER BY CODCIA, CODUBI, CODUSR" + sortField.replace("z", "");

    pstmt = con.prepareStatement(query);
    //System.out.println(query);
		
    r =  pstmt.executeQuery();
    
    while (r.next()){
 	Indt05 select = new Indt05();
 	select.setZcodcia(r.getString(1));
 	select.setZcodubi(r.getString(2));
 	select.setZcoduser(r.getString(3));
 	select.setZdesc1(r.getString(4));
 	select.setZdesc2(r.getString(5));
 	select.setZdesc3(r.getString(1)+ " - " + r.getString(4));
 	select.setZdesc4(r.getString(2)+ " - " + r.getString(5));
 	select.setZuser(r.getString(3)+ " - " + r.getString(6));
   	
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

       	  if(codcia==null){
  			codcia = " - ";
  		}
  		if(codcia==""){
  			codcia = " - ";
  		}        
        if(codubi==null){
  			codubi = " - ";
  		}
  		if(codubi==""){
  			codubi = " - ";
  		}        
        if(coduser==null){
  			coduser = " - ";
  		}
  		if(coduser==""){
  			coduser = " - ";
  		}        

        String[] veccia = codcia.split("\\ - ", -1);
        String[] vecubi = codubi.split("\\ - ", -1);
        String[] vecuser = coduser.split("\\ - ", -1);
      		
     		//Consulta paginada
     		String query = "SELECT COUNT_INDT05('" + ((String) filterValue).toUpperCase() + "','" + veccia[0] + "','" + vecubi[0] + "','" + vecuser[0] + "','" + instancia + "') FROM DUAL";

           
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
  		coduser = "";
  		codcia = "";
  		codubi = "";
  		validarOperacion = 0;
	}
  	
    public void reset() {
    	// TODO Auto-generated method stub
    	codcia = null;
    	codubi = null;
    	coduser = null;    		
    }    
       
}
