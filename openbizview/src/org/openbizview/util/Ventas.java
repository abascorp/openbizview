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
public class Ventas extends Bd implements Serializable {

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private LazyDataModel<Ventas> lazyModel;  
	
	
	/**
	 * @return the lazyModel
	 */
	public LazyDataModel<Ventas> getLazyModel() {
		return lazyModel;
	}	

@PostConstruct
public void init() {
	//System.out.println("entre al metodo INIT");
	lazyModel  = new LazyDataModel<Ventas>(){
		/**
		 * 
		 */
		private static final long serialVersionUID = 7217573531435419432L;
		
        @Override
		public List<Ventas> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) { 
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
	private String codven = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("codven"); //Usuario logeado
	private String porcvis = "";
	private String anocal = ""; 
	private String mescal = ""; 
	private Object filterValue = "";
	private List<Ventas> list = new ArrayList<Ventas>();
	private int validarOperacion = 0;
	private String instancia = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("instancia"); //Usuario logeado
	private String zcodven = "";
	private String zporcvis = "";
	private String zanocal = ""; 
	private String zmescal = ""; 
	private String zcoddesc = ""; 
	
	public String getZcoddesc() {
		return zcoddesc;
	}

	public void setZcoddesc(String zcoddesc) {
		this.zcoddesc = zcoddesc;
	}

	public String getCodven() {
		return codven;
	}

	public void setCodven(String codven) {
		this.codven = codven;
	}

	public String getPorcvis() {
		return porcvis;
	}

	public void setPorcvis(String porcvis) {
		this.porcvis = porcvis;
	}

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

	public String getZcodven() {
		return zcodven;
	}

	public void setZcodven(String vcodven) {
		this.zcodven = vcodven;
	}

	public String getZporcvis() {
		return zporcvis;
	}

	public void setZporcvis(String vporcvis) {
		this.zporcvis = vporcvis;
	}

	public String getZanocal() {
		return zanocal;
	}

	public void setZanocal(String vanocal) {
		this.zanocal = vanocal;
	}

	public String getZmescal() {
		return zmescal;
	}

	public void setZmescal(String vmescal) {
		this.zmescal = vmescal;
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
	//Pool de conecciones JNDISB
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
 		DataSource ds = (DataSource) initContext.lookup(JNDISB);
        con = ds.getConnection();
        if(codven==null){
 			codven = " - ";
 		}
 		if(codven==""){
 			codven = " - ";
 		}        
        String[] veccodven = codven.split("\\ - ", -1);
             
        String query = "INSERT INTO R3P.dbo.TUBDER06 VALUES (?,ROUND(?,1),?,?,?,CONVERT(VARCHAR,CONVERT(DATE,GETDATE(),101),111),?,CONVERT(VARCHAR,CONVERT(DATE,GETDATE(),101),111),?)";
        pstmt = con.prepareStatement(query);
        pstmt.setString(1, veccodven[0].toUpperCase());
        pstmt.setFloat(2,Float.parseFloat(porcvis));
        pstmt.setInt(3, Integer.parseInt(anocal));
        pstmt.setInt(4, Integer.parseInt(mescal));
        pstmt.setString(5, login);
        pstmt.setString(6, login);            
        pstmt.setInt(7, Integer.parseInt(instancia));
        
        //System.out.println(query);
        //System.out.println(veccodven[0]);
        //System.out.println(porcvis);
        //System.out.println(anocal);
        //System.out.println(mescal);
     
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
     		DataSource ds = (DataSource) initContext.lookup(JNDISB);

     		con = ds.getConnection();		
        	
        	String param = "'" + StringUtils.join(chkbox, "','") + "'";

        	String query = "DELETE from R3P.dbo.TUBDER06 WHERE CODIGO_VENDEDOR in (" + param + ") and INSTANCIA = " + instancia + "";
        		        	
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
  		DataSource ds = (DataSource) initContext.lookup(JNDISB);

    	if(codven==null){
  			codven = " - ";
  		}
  		if(codven==""){
  			codven = " - ";
  		}
  		String[] veccodven = codven.split("\\ - ", -1);
  		
  		con = ds.getConnection();		
  		
        String query = "UPDATE R3P.dbo.TUBDER06";
         query += " SET PORC_VISITA = ROUND(?,1), ";
         query += " ANOCAL = ?,";
         query += " MESCAL = ?,";
         query += " USRACT = ?,";
         query += " FECACT = CONVERT(VARCHAR,CONVERT(DATE,GETDATE(),101),111)";
         query += " WHERE CODIGO_VENDEDOR = ? AND INSTANCIA = " + instancia + "";
                
        pstmt = con.prepareStatement(query);
        pstmt.setFloat(1,Float.parseFloat(porcvis));
        pstmt.setInt(2, Integer.parseInt(anocal));
        pstmt.setInt(3, Integer.parseInt(mescal));
        pstmt.setString(4, login);
        pstmt.setString(5, veccodven[0].toUpperCase());
        //System.out.println(query);
        //System.out.println(porcvis);
        //System.out.println(anocal);
        //System.out.println(mescal);
        //System.out.println(veccodven[0]);
        
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
		if(codven==null){
 			codven = " - ";
 		}
 		if(codven==""){
 			codven = " - ";
 		}
 		String[] veccodven = codven.split("\\ - ", -1);
		//System.out.println("entre al metodo SELECT");	
		Context initContext = new InitialContext();     
		DataSource ds = (DataSource) initContext.lookup(JNDISB);
		con = ds.getConnection();		
		//Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
		DatabaseMetaData databaseMetaData = con.getMetaData();
		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
		
		//Consulta paginada
     String query = "SELECT * FROM"; 
	    query += "(select query.*, query.ROWNUM as rn from";
		query += "(SELECT A.CODIGO_VENDEDOR, A.PORC_VISITA, A.ANOCAL, A.MESCAL, count(A.CODIGO_VENDEDOR) AS ROWNUM, B.PERNR + ' - ' + B.NCHMC + ' ' + B.VNAMC CLAVE ";
	    query += " FROM R3P.dbo.TUBDER06 A, R3P.SAPSR3.M_PREMN B";
	    query += " WHERE LTRIM(RTRIM(A.CODIGO_VENDEDOR)) = LTRIM(RTRIM(B.PERNR))";
	    query += " AND LTRIM(RTRIM(A.CODIGO_VENDEDOR)) LIKE LTRIM(RTRIM('%" + veccodven[0] + "%'))";
	    query += " GROUP BY A.CODIGO_VENDEDOR, A.PORC_VISITA, A.ANOCAL, A.MESCAL, B.PERNR, B.NCHMC, B.VNAMC";
	    query += ")query ) sq1" ;
	    query += " WHERE ROWNUM <="+pageSize;
	    query += " AND rn > ("+ first +")";
	    query += " ORDER BY  " + sortField.replace("z", "");

    pstmt = con.prepareStatement(query);
    //System.out.println(query);
		
    r =  pstmt.executeQuery();
    
    while (r.next()){
 	Ventas select = new Ventas();
 	select.setZcodven(r.getString(1));
 	select.setZporcvis(r.getString(2));
 	select.setZanocal(r.getString(3));
 	select.setZmescal(r.getString(4));
 	select.setZcoddesc(r.getString(6));
    	
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
        	if(codven==null){
     			codven = " - ";
     		}
     		if(codven==""){
     			codven = " - ";
     		}
     		String[] veccodven = codven.split("\\ - ", -1);
       	    Context initContext = new InitialContext();     
      		DataSource ds = (DataSource) initContext.lookup(JNDISB);

      		con = ds.getConnection();

     		//Consulta paginada
     		String query = "SELECT R3P.dbo.COUNT_VENTAS('" + ((String) filterValue).toUpperCase() + "','" + veccodven[0] + "') FROM R3P.dbo.TUBDER06";

           
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
  		codven = "";
  		porcvis = "";
  		anocal = "";
  		mescal = "";
  		validarOperacion = 0;
	}

    
    public void reset() {
    	// TODO Auto-generated method stub

    	codven = null;
    		
    }    
    
}
