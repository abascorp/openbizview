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
public class Pre001 extends Bd implements Serializable {

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private LazyDataModel<Pre001> lazyModel;  
	
	
	/**
	 * @return the lazyModel
	 */
	public LazyDataModel<Pre001> getLazyModel() {
		return lazyModel;
	}	

@PostConstruct
public void init() {
	//System.out.println("entre al metodo INIT");
	lazyModel  = new LazyDataModel<Pre001>(){
		/**
		 * 
		 */
		private static final long serialVersionUID = 7217573531435419432L;
		
        @Override
		public List<Pre001> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) { 
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
    private String reporte = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("reporte"); //Usuario logeado
	private String nombre = "";
    private String codigo = "";
	private String desc = "";
	private String tipval = "";
	private String valor = "";
	private String exito = "exito";
	private Object filterValue = "";
	private List<Pre001> list = new ArrayList<Pre001>();
	private int validarOperacion = 0;
	private String instancia = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("instancia"); //Usuario logeado
	private String zreporte = "";
	private String znombre = "";
	private String zcodigo = "";
	private String zdesc = "";
	private String ztipval = "";
	private String zvalor = "";
	private String zdesc1 = "";
	private String zdesc2 = "";
	private String zdesc3 = "";
	private String zcoddel = "";
	private String zorderby = "";
	private String zusrcre = "";
	private String zfeccre = "";

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getZnombre() {
		return znombre;
	}

	public void setZnombre(String znombre) {
		this.znombre = znombre;
	}

	public String getReporte() {
		return reporte;
	}

	public void setReporte(String reporte) {
		this.reporte = reporte;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getTipval() {
		return tipval;
	}

	public void setTipval(String tipval) {
		this.tipval = tipval;
	}

	public String getZreporte() {
		return zreporte;
	}

	public void setZreporte(String zreporte) {
		this.zreporte = zreporte;
	}

	public String getZdesc() {
		return zdesc;
	}

	public void setZdesc(String zdesc) {
		this.zdesc = zdesc;
	}

	public String getZtipval() {
		return ztipval;
	}

	public void setZtipval(String ztipval) {
		this.ztipval = ztipval;
	}

	public String getZusrcre() {
		return zusrcre;
	}

	public void setZusrcre(String zusrcre) {
		this.zusrcre = zusrcre;
	}

	public String getZfeccre() {
		return zfeccre;
	}

	public void setZfeccre(String zfeccre) {
		this.zfeccre = zfeccre;
	}

	public String getZorderby() {
		return zorderby;
	}

	public void setZorderby(String zorderby) {
		this.zorderby = zorderby;
	}

	public String getZcoddel() {
		return zcoddel;
	}

	public void setZcoddel(String zcoddel) {
		this.zcoddel = zcoddel;
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

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getZcodigo() {
		return zcodigo;
	}

	public void setZcodigo(String zcodigo) {
		this.zcodigo = zcodigo;
	}

	public String getZvalor() {
		return zvalor;
	}

	public void setZvalor(String zvalor) {
		this.zvalor = zvalor;
	}

	public String getZdesc1() {
		return zdesc1;
	}

	public void setZdesc1(String zdesc1) {
		this.zdesc1 = zdesc1;
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
 		DataSource ds = (DataSource) initContext.lookup(JNDISB);
        con = ds.getConnection();
        
        if(reporte==null){
 			reporte= " - ";
 		}
 		if(reporte==""){
 			reporte = " - ";
 		}    
        if(tipval==null){
 			tipval= " - ";
 		}
 		if(tipval==""){
 			tipval = " - ";
 		}  
        String[] vectipv = tipval.split("\\ - ", -1);
        String[] vecrepor = reporte.split("\\ - ", -1);
        
        String query = "INSERT INTO R3P.dbo.PRE001 VALUES (?,?,?,?,?,?,?,CONVERT(VARCHAR,CONVERT(DATE,GETDATE(),101),112),?,CONVERT(VARCHAR,CONVERT(DATE,GETDATE(),101),112),?)";
        pstmt = con.prepareStatement(query);
        pstmt.setString(1, reporte.toUpperCase());
        pstmt.setString(2, codigo.toUpperCase());
        pstmt.setString(3, nombre.toUpperCase());
        pstmt.setString(4, desc.toUpperCase());
        pstmt.setString(5, vectipv[0].toUpperCase());
        pstmt.setString(6, valor.toUpperCase());
        pstmt.setString(7, login);
        pstmt.setString(8, login);            
        pstmt.setInt(9, Integer.parseInt(instancia));
        
        //System.out.println(query);
     
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
 * Inserta historico PRE001A
 * <p>
 * <b>Parametros del Metodo:<b> String codcat1, String descat1 unidos como un solo string.<br>
 * String pool, String login.<br><br>
 **/
public void inserta() throws  NamingException {   	
	//System.out.println("entre al metodo INSERT");	
    try {
    	Context initContext = new InitialContext();     
 		DataSource ds = (DataSource) initContext.lookup(JNDISB);
        con = ds.getConnection();
        
        if(reporte==null){
 			reporte= " - ";
 		}
 		if(reporte==""){
 			reporte = " - ";
 		}    
        if(tipval==null){
 			tipval= " - ";
 		}
 		if(tipval==""){
 			tipval = " - ";
 		}  
        String[] vectipv = tipval.split("\\ - ", -1);
        String[] vecrepor = reporte.split("\\ - ", -1);
        
        String query = "INSERT INTO R3P.dbo.PRE001A VALUES (?,?,?,?,?,?,?,CONVERT(VARCHAR,CONVERT(DATE,GETDATE(),101),112),?,CONVERT(VARCHAR,CONVERT(DATE,GETDATE(),101),112),?)";
        pstmt = con.prepareStatement(query);
        pstmt.setString(1, reporte.toUpperCase());
        pstmt.setString(2, codigo.toUpperCase());
        pstmt.setString(3, nombre.toUpperCase());
        pstmt.setString(4, desc.toUpperCase());
        pstmt.setString(5, vectipv[0].toUpperCase());
        pstmt.setString(6, valor.toUpperCase());
        pstmt.setString(7, login);
        pstmt.setString(8, login);            
        pstmt.setInt(9, Integer.parseInt(instancia));
        
        //System.out.println(query);
     
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

        	String query = "DELETE from R3P.dbo.PRE001 WHERE CODIGO in (" + param + ") and INSTANCIA = " + instancia + "";
        		        	
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
  		
  		con = ds.getConnection();	
  		   
        if(reporte==null){
 			reporte= " - ";
 		}
 		if(reporte==""){
 			reporte = " - ";
 		}    
        if(tipval==null){
 			tipval= " - ";
 		}
 		if(tipval==""){
 			tipval = " - ";
 		}  

        String[] vecrepor = reporte.split("\\ - ", -1);
        String[] vectipv = tipval.split("\\ - ", -1);
  		
        String query = "UPDATE R3P.dbo.PRE001";
         query += " SET NOMBRE = ?, "; 
         query += " DESCR = ?, ";
         query += " TIPVAL = ?, ";
         query += " VALOR = ?, ";
         query += " USRACT = ?,";
         query += " FECACT = CONVERT(VARCHAR,CONVERT(DATE,GETDATE(),101),112)";
         query += " WHERE CODIGO = ? ";
         query += " AND INSTANCIA = " + instancia + "";

        pstmt = con.prepareStatement(query);
        pstmt.setString(1, nombre.toUpperCase());
        pstmt.setString(2, desc.toUpperCase());
        pstmt.setString(3, vectipv[0].toUpperCase());
        pstmt.setString(4, valor.toUpperCase());
        pstmt.setString(5, login);
        pstmt.setString(6, codigo.toUpperCase());

        
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
            inserta();
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
		inserta();
		limpiarValores();	
        if(exito=="exito"){
        	msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnInsert"), "");
        	FacesContext.getCurrentInstance().addMessage(null, msj);
        }
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
		DataSource ds = (DataSource) initContext.lookup(JNDISB);
		con = ds.getConnection();		
		//Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
		DatabaseMetaData databaseMetaData = con.getMetaData();
		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
		
		if(reporte==null){
 			reporte= " - ";
 		}
 		if(reporte==""){
 			reporte = " - ";
 		}  
		if(tipval==null){
 			tipval= " - ";
 		}
 		if(tipval==""){
 			tipval = " - ";
 		}        
        String[] vecrepor = reporte.split("\\ - ", -1);
        String[] vectipv = tipval.split("\\ - ", -1);
		
		//Consulta paginada
     String query = "SELECT * FROM"; 
	    query += "(select query.*, query.ROWNUM as rn from";
		query += "(SELECT A.REPORTE, A.CODIGO, A.DESCR, A.TIPVAL, A.VALOR, COUNT(A.CODIGO) AS ROWNUM, B.DESCR AS DESC1, A.NOMBRE";
	    query += " FROM R3P.dbo.PRE001 A, R3P.dbo.TUBDER12 B ";
	    query += " WHERE A.TIPVAL = B.CODIGO";
	    query += " AND LTRIM(RTRIM(A.REPORTE)) LIKE LTRIM(RTRIM('%" + vecrepor[0] + "%'))";
	    query += " AND LTRIM(RTRIM(A.TIPVAL)) LIKE LTRIM(RTRIM('%" + vectipv[0] + "%'))";
	    query += " GROUP BY A.REPORTE, A.CODIGO, A.NOMBRE, A.DESCR, A.TIPVAL, A.VALOR, B.DESCR";
	    query += ")query ) sq1" ;
	    query += " WHERE ROWNUM <="+pageSize;
	    query += " AND rn > ("+ first +")";
	    query += " ORDER BY REPORTE, CODIGO" + sortField.replace("z", "");

    pstmt = con.prepareStatement(query);
    //System.out.println(query);
		
    r =  pstmt.executeQuery();
    
    while (r.next()){
 	Pre001 select = new Pre001();
 	select.setZreporte(r.getString(1));
 	select.setZcodigo(r.getString(2));
 	select.setZnombre(r.getString(8));
 	select.setZdesc(r.getString(3));
 	select.setZtipval(r.getString(4)+ " - " + r.getString(7));
 	select.setZvalor(r.getString(5));
 	select.setZcoddel(r.getString(2));
 	select.setZorderby(r.getString(1) + ", " + r.getString(2));
 	select.setZdesc1(r.getString(7));
   	
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
      		DataSource ds = (DataSource) initContext.lookup(JNDISB);

      		con = ds.getConnection();
      		
      		if(reporte==null){
     			reporte= " - ";
     		}
     		if(reporte==""){
     			reporte = " - ";
     		}     
      		if(tipval==null){
     			tipval= " - ";
     		}
     		if(tipval==""){
     			tipval = " - ";
     		}        
            String[] vecrepor = reporte.split("\\ - ", -1);
            String[] vectipv = tipval.split("\\ - ", -1);

     		//Consulta paginada
     		String query = "SELECT R3P.dbo.COUNT_PRE001('" + ((String) filterValue).toUpperCase() + "','" + vecrepor[0] + "','" + vectipv[0] + "','" + codigo + "') FROM R3P.dbo.PRE001";

           
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
  		reporte = "";
  		codigo = "";
  		nombre = "";
  		desc = "";
  		tipval = "";
  		valor = "";
  		validarOperacion = 0;
	}
  	
	public void reset() {
		// TODO Auto-generated method stub
  		reporte = null;
  		tipval = null;
	}
       
}
