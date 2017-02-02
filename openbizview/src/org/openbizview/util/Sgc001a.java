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
import java.util.Date;
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
public class Sgc001a extends Bd implements Serializable {

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private LazyDataModel<Sgc001a> lazyModel;  
	
	
	/**
	 * @return the lazyModel
	 */
	public LazyDataModel<Sgc001a> getLazyModel() {
		return lazyModel;
	}	

@PostConstruct
public void init() {
	//System.out.println("entre al metodo INIT");
	lazyModel  = new LazyDataModel<Sgc001a>(){
		/**
		 * 
		 */
		private static final long serialVersionUID = 7217573531435419432L;
		
		@Override
		public List<Sgc001a> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) { 
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
    private String comp = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("comp"); //Usuario logeado
    private String area = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("area"); //Usuario logeado
	private String codigo =  (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("codigo"); //Usuario logeado
	private String meta = "";
	private String resmet = "";
	private String tvalm = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("tvalm"); //Usuario logeado
	private Date feccam = new Date();
    private String regist = "";	
	private Object filterValue = "";
	private List<Sgc001a> list = new ArrayList<Sgc001a>();
	private int validarOperacion = 0;
	private String instancia = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("instancia"); //Usuario logeado
	private String exito = "exito";
	private String zcomp = "";
	private String zarea = "";
	private String zcodigo = "";
	private String zmeta = "";
	private String zdesc1 = "";
	private String zdesc2 = "";
	private String zdesc3 = "";
	private String zdesc4 = "";
	private String ztvalm = "";
	private String zfeccam = "";	
	private String zdelete = "";
	private String zusername = "";
	private String zregist = "";
	private String zresmet = "";
	
	public String getResmet() {
		return resmet;
	}

	public void setResmet(String resmet) {
		this.resmet = resmet;
	}

	public String getZresmet() {
		return zresmet;
	}

	public void setZresmet(String zresmet) {
		this.zresmet = zresmet;
	}

	public String getZregist() {
		return zregist;
	}

	public void setZregist(String zregist) {
		this.zregist = zregist;
	}

	public String getRegist() {
		return regist;
	}

	public void setRegist(String regist) {
		this.regist = regist;
	}

	public String getZusername() {
		return zusername;
	}

	public void setZusername(String zusername) {
		this.zusername = zusername;
	}

	public String getZdelete() {
		return zdelete;
	}

	public void setZdelete(String zdelete) {
		this.zdelete = zdelete;
	}

	public String getZdesc4() {
		return zdesc4;
	}

	public void setZdesc4(String zdesc4) {
		this.zdesc4 = zdesc4;
	}

	public Date getFeccam() {
		return feccam;
	}

	public void setFeccam(Date feccam) {
		this.feccam = feccam;
	}

	public String getZfeccam() {
		return zfeccam;
	}

	public void setZfeccam(String zfeccam) {
		this.zfeccam = zfeccam;
	}

	public String getTvalm() {
		return tvalm;
	}

	public void setTvalm(String tvalm) {
		this.tvalm = tvalm;
	}

	public String getZtvalm() {
		return ztvalm;
	}

	public void setZtvalm(String ztvalm) {
		this.ztvalm = ztvalm;
	}

	public String getComp() {
		return comp;
	}

	public void setComp(String comp) {
		this.comp = comp;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getZcomp() {
		return zcomp;
	}

	public void setZcomp(String zcomp) {
		this.zcomp = zcomp;
	}

	public String getZarea() {
		return zarea;
	}

	public void setZarea(String zarea) {
		this.zarea = zarea;
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

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getMeta() {
		return meta;
	}

	public void setMeta(String meta) {
		this.meta = meta;
	}

	public String getZcodigo() {
		return zcodigo;
	}

	public void setZcodigo(String zcodigo) {
		this.zcodigo = zcodigo;
	}

	public String getZmeta() {
		return zmeta;
	}

	public void setZmeta(String zmeta) {
		this.zmeta = zmeta;
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
 * Inserta Sgc001A.
 * <p>
 * <b>Parametros del Metodo:<b> String codcat1, String descat1 unidos como un solo string.<br>
 * String pool, String login.<br><br>
 **/
public void insertm() throws  NamingException {   	
	//System.out.println("entre al metodo INSERT");	
    try {
    	Context initContext = new InitialContext();     
 		DataSource ds = (DataSource) initContext.lookup(JNDI);
        con = ds.getConnection();
    
        if(comp==null){
 			comp = " - ";
 		}
 		if(comp==""){
 			comp = " - ";
 		}        
        if(area==null){
 			area = " - ";
 		}
 		if(area==""){
 			area = " - ";
 		}   		
        if(codigo==null){
 			codigo = " - ";
 		}
 		if(codigo==""){
 			codigo = " - ";
 		}   
        if(tvalm==null){
 			tvalm = " - ";
 		}
 		if(tvalm==""){
 			tvalm = " - ";
 		}  
        String[] veccomp = comp.split("\\ - ", -1);
        String[] vecarea = area.split("\\ - ", -1);
        String[] veccodigo = codigo.split("\\ - ", -1);
        String[] vectvalm = tvalm.split("\\ - ", -1);
                    
        String query = "INSERT INTO SGC001A VALUES (?,?,?,?,to_date('" + sdfecha.format(feccam) + "','dd/mm/yyyy'),?,?,?,'" + getFecha() + "',?,'" + getFecha() + "',?,null)";
        pstmt = con.prepareStatement(query);
        pstmt.setString(1, veccomp[0].toUpperCase());
        pstmt.setString(2, vecarea[0].toUpperCase());
        pstmt.setString(3, veccodigo[0].toUpperCase());
        pstmt.setString(4, vectvalm[0].toUpperCase());
        pstmt.setFloat(5, Float.parseFloat(meta));
        pstmt.setString(6, resmet.toUpperCase());
        pstmt.setString(7, login);
        pstmt.setString(8, login);            
        pstmt.setInt(9, Integer.parseInt(instancia));
   
        //System.out.println(query);
        //System.out.println(veccomp[0]);
        //System.out.println(vecarea[0]);
        //System.out.println(veccodigo[0]);
        //System.out.println(vectvalm[0]);
        //System.out.println(sdfecha2.format(feccam));
        //System.out.println(meta);
        //System.out.println(resmet);
     
        try {
            //Avisando
        	pstmt.executeUpdate();
            
         } catch (SQLException e)  {
        	msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("msnPrimaryk"), "");
        	FacesContext.getCurrentInstance().addMessage(null, msj);
        	exito = "error";
        }
        
        pstmt.close();
        con.close();
    } catch (Exception e) {
    	e.printStackTrace();
    }	       
}

public void deletem() throws NamingException  {  
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

        	String query = "DELETE from SGC001A WHERE COMP || AREA || CODIGO || REGIST in (" + param + ") and INSTANCIA = " + instancia + "";        	
            
        	pstmt = con.prepareStatement(query);
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
}
   
/**
 * Actualiza sgc001A
 * <b>Parametros del Metodo:<b> String codcat1, String descat1 unidos como un solo string.<br>
 * String pool, String login.<br><br>
 **/
public void updatem() throws  NamingException {
	//System.out.println("entre al metodo UPDATE");
     try { 	 
    	 Context initContext = new InitialContext();     
  		DataSource ds = (DataSource) initContext.lookup(JNDI);
    
  		if(comp==null){
 			comp = " - ";
 		}
 		if(comp==""){
 			comp = " - ";
 		}        
        if(area==null){
 			area = " - ";
 		}
 		if(area==""){
 			area = " - ";
 		} 
        if(codigo==null){
 			codigo = " - ";
 		}
 		if(codigo==""){
 			codigo = " - ";
 		} 
 		if(tvalm==null){
 			tvalm = " - ";
 		}
 		if(tvalm==""){
 			tvalm = " - ";
 		} 

         String[] veccomp = comp.split("\\ - ", -1);
         String[] vecarea = area.split("\\ - ", -1);
         String[] veccodigo = codigo.split("\\ - ", -1);
         String[] vectvalm = tvalm.split("\\ - ", -1);
  		
  		con = ds.getConnection();		
  		
        String query = "UPDATE SGC001A";
         query += " SET TIPVAL = ?, ";
         query += " FECCAM = to_date('" + sdfecha.format(feccam) + "','dd/mm/yyyy'),";
         query += " META = ?,";
         query += " RESMET = ?,";
         query += " USRACT = ?,";
         query += " FECACT = '" + getFecha() + "'";
         query += " WHERE CODIGO = ? AND COMP = ? AND AREA = ? AND INSTANCIA = " + instancia + " AND REGIST = ?";

        pstmt = con.prepareStatement(query);
        pstmt.setString(1, vectvalm[0].toUpperCase()); 
        pstmt.setFloat(2, Float.parseFloat(meta));
        pstmt.setString(3, resmet.toUpperCase()); 
        pstmt.setString(4, login);
        pstmt.setString(5, veccodigo[0].toUpperCase());       
        pstmt.setString(6, veccomp[0].toUpperCase());    
        pstmt.setString(7, vecarea[0].toUpperCase());    
        pstmt.setInt(8,Integer.parseInt(regist));

        //System.out.println(query);
        //System.out.println(vectvalm[0]);
        //System.out.println(meta);
        //System.out.println(sdfecha2.format(feccam));
        
        // Antes de ejecutar valida si existe el registro en la base de Datos.
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

public void guardar() throws NamingException, SQLException{   	
	if(validarOperacion==0){
		insertm();
		limpiarValores();   
        if(exito=="exito"){
        	msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnInsert"), "");
        	FacesContext.getCurrentInstance().addMessage(null, msj);
        }
	} else {
		updatem();
		limpiarValores(); 
		if(exito=="exito"){
        	msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnUpdate"), "");
        	FacesContext.getCurrentInstance().addMessage(null, msj);
	}
}
}

public void borrar() throws NamingException, SQLException{   	
		deletem();
		limpiarValores();   
        if(exito=="exito"){
        	msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnDelete"), "");
        	FacesContext.getCurrentInstance().addMessage(null, msj);
        }
}

/**
 * Leer Datos de paises
 * @throws NamingException 
* @throws IOException 
 **/ 	
	public void select(int first, int pageSize, String sortField, Object filterValue) throws SQLException, ClassNotFoundException, NamingException {
  		if(comp==null){
  			comp = " - ";
  		}
  		if(comp==""){
  			comp = " - ";
  		}        
         if(area==null){
  			area = " - ";
  		}
  		if(area==""){
  			area = " - ";
  		}        
         if(codigo==null){
  			codigo = " - ";
  		}
  		if(codigo==""){
  			codigo = " - ";
  		}        
         String[] veccomp = comp.split("\\ - ", -1);
         String[] vecarea = area.split("\\ - ", -1);
         String[] veccodigo = codigo.split("\\ - ", -1);
		//System.out.println("entre al metodo SELECT");	
		Context initContext = new InitialContext();     
		DataSource ds = (DataSource) initContext.lookup(JNDI);
		con = ds.getConnection();		
		//Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
		DatabaseMetaData databaseMetaData = con.getMetaData();
		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
		
		//Consulta paginada
     String query = "SELECT * FROM"; 
	    query += "(select query.*, rownum as rn from";
		query += "(SELECT A.COMP, A.AREA, A.CODIGO, A.TIPVAL, TO_CHAR(A.FECCAM,'DD/MM/YYYY') AS FECCAM, A.META, B.DESCR AS DESC1, C.DESCR AS DESC2, D.NOMIND AS DESC3, E.DESCR AS DESC4, A.REGIST, A.RESMET ";
		query += "FROM SGC001A A, SGC005 B, SGC006 C, SGC001 D, TUBDER11 E ";
		query += "WHERE A.COMP = B.CODIGO ";
		query += "AND A.COMP = C.COMP ";
		query += "AND A.AREA = C.CODIGO ";
		query += "AND A.COMP = D.COMP ";
		query += "AND A.AREA = D.AREA ";
		query += "AND A.CODIGO = D.CODIGO ";
		query += "AND A.TIPVAL = E.CODIGO ";
	    query += "AND TRIM(A.COMP) LIKE TRIM('%" + veccomp[0] + "%') ";
	    query += "AND TRIM(A.AREA) LIKE TRIM('%" + vecarea[0] + "%') ";
	    query += "AND TRIM(A.CODIGO) LIKE TRIM('%" + veccodigo[0] + "%') ";
	    query += "GROUP BY A.COMP, A.AREA, A.CODIGO, A.TIPVAL, A.FECCAM, A.META, B.DESCR, C.DESCR, D.NOMIND, E.DESCR, A.REGIST, A.RESMET ";
	    query += "ORDER BY A.COMP, A.AREA, A.CODIGO, A.FECCAM, A.REGIST ";
	    query += ")query ) " ;
	    query += " WHERE ROWNUM <="+pageSize;
	    query += " AND rn > ("+ first +")";
	    //query += " ORDER BY  " + sortField.replace("z", "");

    pstmt = con.prepareStatement(query);
    //System.out.println(query);
		
    r =  pstmt.executeQuery();
    
    while (r.next()){
 	Sgc001a select = new Sgc001a();
 	select.setZcomp(r.getString(1)+ " - " + r.getString(7));
 	select.setZarea(r.getString(2)+ " - " + r.getString(8));
 	select.setZcodigo(r.getString(3)+ " - " + r.getString(9));
 	select.setZtvalm(r.getString(4)+ " - " + r.getString(10));
	select.setZfeccam(r.getString(5));
 	select.setZmeta(r.getString(6));
 	select.setZdesc1(r.getString(7));
 	select.setZdesc2(r.getString(8));
 	select.setZdesc3(r.getString(9));
 	select.setZdesc4(r.getString(10));
 	select.setZdelete(r.getString(1)+ "" + r.getString(2)+ "" + r.getString(3)+ "" + r.getString(11));
 	select.setZregist(r.getString(11));
 	select.setZresmet(r.getString(12));
    	
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
      		if(comp==null){
      			comp = " - ";
      		}
      		if(comp==""){
      			comp= " - ";
      		}        
             if(area==null){
      			area = " - ";
      		}
      		if(area==""){
      			area = " - ";
      		}        
             if(codigo==null){
      			codigo = " - ";
      		}
      		if(codigo==""){
      			codigo = " - ";
      		}        
             String[] veccomp = comp.split("\\ - ", -1);
             String[] vecarea = area.split("\\ - ", -1);
             String[] veccodigo = codigo.split("\\ - ", -1);
       	    Context initContext = new InitialContext();     
      		DataSource ds = (DataSource) initContext.lookup(JNDI);

      		con = ds.getConnection();

     		//Consulta paginada
     		String query = "SELECT COUNT_SGC001A('" + ((String) filterValue).toUpperCase() + "','" + veccomp[0] + "','" + vecarea[0] + "','" + veccodigo[0] + "', '" + instancia + "') FROM DUAL";

           
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
  		comp = "";
  		area = "";
  		codigo = "";
  		meta = "";
  		tvalm = "";
  		feccam = new Date();
  		resmet = "";
  		validarOperacion = 0;
	}
    
    public void reset() {
    	// TODO Auto-generated method stub
    	comp = null;
    	area = null;
  		tvalm = null;
    		
    }    
    
}
