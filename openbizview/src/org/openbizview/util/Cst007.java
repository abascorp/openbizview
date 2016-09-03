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
public class Cst007 extends Bd implements Serializable {

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private LazyDataModel<Cst007> lazyModel;  
	
	
	/**
	 * @return the lazyModel
	 */
	public LazyDataModel<Cst007> getLazyModel() {
		return lazyModel;
	}	

@PostConstruct
public void init() {
	//System.out.println("entre al metodo INIT");
	lazyModel  = new LazyDataModel<Cst007>(){
		/**
		 * 
		 */
		private static final long serialVersionUID = 7217573531435419432L;
		
        @Override
		public List<Cst007> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) { 
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
	private String codigo = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("codigo"); //Usuario logeado
	private String comp = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("comp"); //Usuario logeado
	private String suc = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("suc"); //Usuario logeado
	private String mescompra = "";
	private String mesproye = "";
	private String txt_det_1 = "";
	private Object filterValue = "";
	private String anocal = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("anocal"); //Usuario logeado
	private String mescal = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("mescal"); //Usuario logeado
	private String ceno = "";
	private List<Cst007> list = new ArrayList<Cst007>();
	private int validarOperacion = 0;
	//private String instancia = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("instancia"); //Usuario logeado
	private String zcodigo = "";
	private String zcomp = "";
	private String zsuc = "";
	private String zdesc = "";
	private String zdesc1 = "";
	private String zdesc2= "";
	private String zdesc3 = "";
	private String zconv = "";
	private String zdescomp = "";
	private String zdessuc = "";
	private String zdestino = "";
	private String zdelete = "";
	private String zmescompra = "";
	private String zmesproye = "";
	private String zfecini = "";
	private String zfecfin = "";
	private String zid = "";
	private String zestatu = "";
	private String zanocal = "";
	private String zmescal = "";
	private String zceno = "";

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

	public String getCeno() {
		return ceno;
	}

	public void setCeno(String ceno) {
		this.ceno = ceno;
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

	public String getZceno() {
		return zceno;
	}

	public void setZceno(String zceno) {
		this.zceno = zceno;
	}

	public String getTxt_det_1() {
		return txt_det_1;
	}

	public void setTxt_det_1(String txt_det_1) {
		this.txt_det_1 = txt_det_1;
	}

	public String getZfecini() {
		return zfecini;
	}

	public void setZfecini(String zfecini) {
		this.zfecini = zfecini;
	}

	public String getZfecfin() {
		return zfecfin;
	}

	public void setZfecfin(String zfecfin) {
		this.zfecfin = zfecfin;
	}

	public String getZid() {
		return zid;
	}

	public void setZid(String zid) {
		this.zid = zid;
	}

	public String getZestatu() {
		return zestatu;
	}

	public void setZestatu(String zestatu) {
		this.zestatu = zestatu;
	}

	public String getMescompra() {
		return mescompra;
	}

	public void setMescompra(String mescompra) {
		this.mescompra = mescompra;
	}

	public String getMesproye() {
		return mesproye;
	}

	public void setMesproye(String mesproye) {
		this.mesproye = mesproye;
	}

	public String getZmescompra() {
		return zmescompra;
	}

	public void setZmescompra(String zmescompra) {
		this.zmescompra = zmescompra;
	}

	public String getZmesproye() {
		return zmesproye;
	}

	public void setZmesproye(String zmesproye) {
		this.zmesproye = zmesproye;
	}

	public String getZconv() {
		return zconv;
	}

	public void setZconv(String zconv) {
		this.zconv = zconv;
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

	public String getZdelete() {
		return zdelete;
	}

	public void setZdelete(String zdelete) {
		this.zdelete = zdelete;
	}

	public String getComp() {
		return comp;
	}

	public void setComp(String comp) {
		this.comp = comp;
	}

	public String getSuc() {
		return suc;
	}

	public void setSuc(String suc) {
		this.suc = suc;
	}

	public String getZcomp() {
		return zcomp;
	}

	public void setZcomp(String zcomp) {
		this.zcomp = zcomp;
	}

	public String getZsuc() {
		return zsuc;
	}

	public void setZsuc(String zsuc) {
		this.zsuc = zsuc;
	}

	public String getZdescomp() {
		return zdescomp;
	}

	public void setZdescomp(String zdescomp) {
		this.zdescomp = zdescomp;
	}

	public String getZdessuc() {
		return zdessuc;
	}

	public void setZdessuc(String zdessuc) {
		this.zdessuc = zdessuc;
	}

	public String getZdestino() {
		return zdestino;
	}

	public void setZdestino(String zdestino) {
		this.zdestino = zdestino;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getZcodigo() {
		return zcodigo;
	}

	public void setZcodigo(String zcodigo) {
		this.zcodigo = zcodigo;
	}

	public String getZdesc() {
		return zdesc;
	}

	public void setZdesc(String zdesc) {
		this.zdesc = zdesc;
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
		public void actproyeccion() throws  NamingException {   	
			//System.out.println("entre al metodo ACT");	
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
		        if(suc==null){
		 			suc = " - ";
		 		}
		 		if(suc==""){
		 			suc = " - ";
		 		}           
		        if(codigo==null){
		 			codigo = " - ";
		 		}
		 		if(codigo==""){
		 			codigo = " - ";
		 		}  

		        String[] veccomp = comp.split("\\ - ", -1);
		        String[] vecsuc = suc.split("\\ - ", -1);
		        String[] veccod = codigo.split("\\ - ", -1);
		                	
		        String query = "BEGIN ACT_PROYECCION_DOTACION(?,?,?,?,?,?);  END;";
		        pstmt = con.prepareStatement(query);
		        pstmt.setString(1, veccomp[0].toUpperCase());
		        pstmt.setString(2, vecsuc[0].toUpperCase());
		        pstmt.setString(3, veccod[0].toUpperCase());
		        pstmt.setInt(4, Integer.parseInt(mescompra));
		        pstmt.setInt(5, Integer.parseInt(mesproye));
		        pstmt.setString(6, login);            
		        
		        //System.out.println(query);
		        //System.out.println("compa;ia: " + veccomp[0]);
		        //System.out.println("sucursal: " + vecsuc[0]);
		        //System.out.println("convenio: " + veccod[0]);
		        //System.out.println("mescompra: " + mescompra);
		        //System.out.println("mesproyec: " + mesproye);

		        try {
		            //Avisando
		        	pstmt.executeUpdate();
		        	msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnActpro"), "");
		            limpiarValores();                
		         } catch (SQLException e)  {
		        	msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
		        }
		        
		        pstmt.close();
		        con.close();
		    } catch (Exception e) {
		    	e.printStackTrace();
		    }	
		    FacesContext.getCurrentInstance().addMessage(null, msj);
		    
		}
		
		public void runcostpgm() throws  NamingException {   	
			//System.out.println("entre al metodo runcostpgm");	
		    try {
		    	Context initContext = new InitialContext();     
		 		DataSource ds = (DataSource) initContext.lookup(JNDI);
		        con = ds.getConnection();
		        
		        if(ceno==null){
		 			ceno = " - ";
		 		}

		        String[] vecceno = ceno.split("\\ - ", -1);
		                	
		        String query = "BEGIN SP_ZFI_CST100A(?,?,?,?);  END;";
		        pstmt = con.prepareStatement(query);
		        pstmt.setString(1, vecceno[0].toUpperCase());
		        pstmt.setString(2, anocal.toUpperCase());
		        pstmt.setString(3, mescal.toUpperCase());
		        pstmt.setString(4, login);            

		        //System.out.println(query);
		        //System.out.println("compa;ia: " + vecceno[0]);
		        //System.out.println("sucursal: " + anocal);
		        //System.out.println("convenio: " + mescal);
		        //System.out.println("mescompra: " + login);

		        try {
		            //Avisando
		        	pstmt.executeUpdate();
		        	msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("cst007Run"), "");
		            limpiarValores();                
		         } catch (SQLException e)  {
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
 * Leer Datos de paises
 * @throws NamingException 
* @throws IOException 
 **/ 	
	public void select(int first, int pageSize, String sortField, Object filterValue) throws SQLException, ClassNotFoundException, NamingException {
  		
		//System.out.println("entre al metodo SELECT");	
		Context initContext = new InitialContext();     
		DataSource ds = (DataSource) initContext.lookup(JNDI);
		con = ds.getConnection();		
       
		//Consulta paginada
     String query = "SELECT * FROM"; 
	    query += "(select query.*, rownum as rn from";
		query += "(SELECT SYSDATE";
	    query += " FROM DUAL";
	    query += " GROUP BY SYSDATE";
	    query += ")query ) " ;
	    query += " WHERE ROWNUM <="+pageSize;
	    query += " AND rn > ("+ first +")";
	    query += " ORDER BY  1" + sortField.replace("z", "");

    pstmt = con.prepareStatement(query);
    //System.out.println(query);
		
    r =  pstmt.executeQuery();
    
    while (r.next()){
 	Cst007 select = new Cst007();
 	select.setZid(r.getString(1)); 	 	  	
   	
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
      		
            if(comp==null){
     			comp = " - ";
     		}
     		if(comp==""){
     			comp = " - ";
     		}        
            if(suc==null){
     			suc = " - ";
     		}
     		if(suc==""){
     			suc = " - ";
     		}             
            if(codigo==null){
     			codigo = " - ";
     		}
     		if(codigo==""){
     			codigo = " - ";
     		}  

            String[] veccomp = comp.split("\\ - ", -1);
            String[] vecsuc = suc.split("\\ - ", -1);
            String[] veccod = codigo.split("\\ - ", -1);

     		//Consulta paginada
     		String query = "SELECT COUNT_DOT_PROYECCION('" + ((String) filterValue).toUpperCase() + "','" + veccomp[0] + "','" + vecsuc[0] + "','" + veccod[0] + "') FROM DUAL";

           
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
  		codigo = "";
  		comp = "";
  		suc = "";	
  		mescompra = "";
  		mesproye = "";
  		validarOperacion = 0;
	}
  	
}
