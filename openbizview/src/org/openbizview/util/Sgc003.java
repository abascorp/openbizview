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
public class Sgc003 extends Bd implements Serializable {

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private LazyDataModel<Sgc003> lazyModel;  
	
	
	/**
	 * @return the lazyModel
	 */
	public LazyDataModel<Sgc003> getLazyModel() {
		return lazyModel;
	}	

@PostConstruct
public void init() {
	//System.out.println("entre al metodo INIT");
	lazyModel  = new LazyDataModel<Sgc003>(){
		/**
		 * 
		 */
		private static final long serialVersionUID = 7217573531435419432L;
		
        @Override
		public List<Sgc003> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) { 
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
    //private String comp = "";
    //private String area = "";
    private String comp = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("comp"); 
    private String area = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("area"); 
	private String codigo = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("codigo"); //Usuario logeado
	private String anocal = "";
	private String mescal = "";
	private String valor = "";
	private Object filterValue = "";
	private List<Sgc003> list = new ArrayList<Sgc003>();
	private int validarOperacion = 0;
	private String instancia = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("instancia"); //Usuario logeado
	private String zcomp = "";
	private String exito = "exito";
    private String zarea = "";
	private String zcodigo = "";
	private String zanocal = "";
	private String zmescal = "";
	private String zvalor = "";
	private String zdesc1 = "";
	private String zdesc2 = "";
	private String zdesc3 = "";
	private String zcoddel = "";
	private String zorderby = "";
	private String zusrcre = "";
	private String zfeccre = "";
	String[][] tabla;

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

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
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

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
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

	public String getZcodigo() {
		return zcodigo;
	}

	public void setZcodigo(String zcodigo) {
		this.zcodigo = zcodigo;
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
	PntGenerica consulta = new PntGenerica();
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
        
        if(comp==null){
 			comp= " - ";
 		}
 		if(comp==""){
 			comp = " - ";
 		}        
        if(area==null){
 			area= " - ";
 		}
 		if(area==""){
 			area = " - ";
 		}    
        if(codigo==null){
 			codigo= " - ";
 		}
 		if(codigo==""){
 			codigo = " - ";
 		}  
        String[] veccomp = comp.split("\\ - ", -1);
        String[] vecarea = area.split("\\ - ", -1);
        String[] veccod = codigo.split("\\ - ", -1);
        
        String query = "INSERT INTO SGC003 VALUES (?,?,?,?,?,?,?,SYSDATE,?,SYSDATE,?)";
        pstmt = con.prepareStatement(query);
        pstmt.setString(1, veccomp[0].toUpperCase());
        pstmt.setString(2, vecarea[0].toUpperCase());
        pstmt.setString(3, veccod[0].toUpperCase());
        pstmt.setInt(4,Integer.parseInt(anocal));
        pstmt.setInt(5,Integer.parseInt(mescal));;
        pstmt.setFloat(6,Float.parseFloat(valor));
        pstmt.setString(7, login);
        pstmt.setString(8, login);            
        pstmt.setInt(9, Integer.parseInt(instancia));
        
        //System.out.println(query);
        //System.out.println(veccomp[0]);
        //System.out.println(vecarea[0]);
        //System.out.println(veccod[0]);
        //System.out.println(anocal);
        //System.out.println(mescal);
        //System.out.println(valor);


     
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

        	String query = "DELETE from SGC003 WHERE CODIGO in (" + param + ") and INSTANCIA = " + instancia + "";	        	
            
        	pstmt = con.prepareStatement(query);
            
        	////System.out.println(query2);
        	////System.out.println(query);

        	  try {
                  //Avisando
              	pstmt.executeUpdate();
                  
               } catch (SQLException e)  {
            	                                   

				        if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC001_FK1) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK1"), ""); }
				   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC001A_FK1) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK2"), ""); }
				   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC001B_FK1) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK3"), ""); }
				   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC001C_FK1) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK4"), ""); }
				   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC001D_FK1) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK5"), ""); }
				   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC003_FK1) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK6"), ""); }
				   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC006_FK1) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK7"), ""); }
				   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC007_FK1) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK8"), ""); }
				   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC008_FK1) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK7"), ""); }
				   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC009_FK1) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK9"), ""); }
				   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC012_FK1) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK10"), ""); }
				   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC012A_FK1) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK11"), ""); }
				   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC012B_FK1) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK12"), ""); }
				   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC012C_FK1) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK13"), ""); }
				   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC001_FK2) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK1"), ""); }
				   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC001A_FK2) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK2"), ""); }
				   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC001B_FK2) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK3"), ""); }
				   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC001C_FK2) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK4"), ""); }
				   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC001D_FK2) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK5"), ""); }
				   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC003_FK2) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK6"), ""); }
				   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC008_FK2) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK14"), ""); }
				   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC009_FK2) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK9"), ""); }
				   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC012_FK2) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK10"), ""); }
				   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC012A_FK2) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK11"), ""); }
				   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC012B_FK2) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK12"), ""); }
				   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC012C_FK2) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK13"), ""); }
				   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC001_FK3) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK1"), ""); }
				   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC001_FK4) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK1"), ""); }
				   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC001_FK5) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK1"), ""); }
				   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC001A_FK3) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK2"), ""); }
				   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC001B_FK3) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK3"), ""); }
				   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC001C_FK3) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK4"), ""); }
				   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC001_FK6) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK1"), ""); }
				   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC001_FK7) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK1"), ""); }
				   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC008_FK3) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK15"), ""); }
				   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC009_FK3) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK15"), ""); }
				   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC009_FK4) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK16"), ""); }
				   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC001A_FK4) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK2"), ""); }
				   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC001B_FK4) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK17"), ""); }
				   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC001C_FK4) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK4"), ""); }
				   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC003_FK3) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK6"), ""); }
				   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC012_FK3) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK10"), ""); }
				   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC012A_FK3) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK11"), ""); }
				   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC012B_FK3) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK12"), ""); }
				   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC012C_FK3) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK13"), ""); }
				   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC012_FK4) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK10"), ""); }
				   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC012A_FK4) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK11"), ""); }
				   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC012B_FK4) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK12"), ""); }
				   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC012C_FK4) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK13"), ""); }
				   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC012A_FK5) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK11"), ""); }
				   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC012B_FK5) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK12"), ""); }
				   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC012C_FK5) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK13"), ""); }
				   else {msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");}
                   
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

/*
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

        	String query = "DELETE from SGC003 WHERE CODIGO in (" + param + ") and INSTANCIA = " + instancia + "";
        		        	
            pstmt = con.prepareStatement(query);
            //System.out.println(query);

            try {
                //Avisando
                pstmt.executeUpdate();
                //msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnDelete"), "");
                //limpiarValores();	
            } catch (SQLException e) {

		        if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC001_FK1) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK1"), ""); }
		   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC001A_FK1) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK2"), ""); }
		   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC001B_FK1) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK3"), ""); }
		   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC001C_FK1) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK4"), ""); }
		   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC001D_FK1) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK5"), ""); }
		   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC003_FK1) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK6"), ""); }
		   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC006_FK1) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK7"), ""); }
		   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC007_FK1) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK8"), ""); }
		   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC008_FK1) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK7"), ""); }
		   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC009_FK1) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK9"), ""); }
		   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC012_FK1) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK10"), ""); }
		   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC012A_FK1) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK11"), ""); }
		   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC012B_FK1) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK12"), ""); }
		   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC012C_FK1) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK13"), ""); }
		   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC001_FK2) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK1"), ""); }
		   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC001A_FK2) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK2"), ""); }
		   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC001B_FK2) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK3"), ""); }
		   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC001C_FK2) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK4"), ""); }
		   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC001D_FK2) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK5"), ""); }
		   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC003_FK2) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK6"), ""); }
		   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC008_FK2) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK14"), ""); }
		   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC009_FK2) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK9"), ""); }
		   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC012_FK2) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK10"), ""); }
		   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC012A_FK2) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK11"), ""); }
		   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC012B_FK2) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK12"), ""); }
		   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC012C_FK2) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK13"), ""); }
		   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC001_FK3) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK1"), ""); }
		   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC001_FK4) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK1"), ""); }
		   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC001_FK5) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK1"), ""); }
		   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC001A_FK3) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK2"), ""); }
		   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC001B_FK3) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK3"), ""); }
		   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC001C_FK3) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK4"), ""); }
		   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC001_FK6) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK1"), ""); }
		   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC001_FK7) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK1"), ""); }
		   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC008_FK3) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK15"), ""); }
		   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC009_FK3) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK15"), ""); }
		   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC009_FK4) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK16"), ""); }
		   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC001A_FK4) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK2"), ""); }
		   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC001B_FK4) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK17"), ""); }
		   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC001C_FK4) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK4"), ""); }
		   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC003_FK3) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK6"), ""); }
		   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC012_FK3) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK10"), ""); }
		   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC012A_FK3) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK11"), ""); }
		   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC012B_FK3) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK12"), ""); }
		   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC012C_FK3) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK13"), ""); }
		   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC012_FK4) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK10"), ""); }
		   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC012A_FK4) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK11"), ""); }
		   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC012B_FK4) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK12"), ""); }
		   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC012C_FK4) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK13"), ""); }
		   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC012A_FK5) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK11"), ""); }
		   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC012B_FK5) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK12"), ""); }
		   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC012C_FK5) violated - child record found")){msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("SGC_FK13"), ""); }
		   else {msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");}
            

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

*/
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
  		
        if(comp==null){
 			comp= " - ";
 		}
 		if(comp==""){
 			comp = " - ";
 		}        
        if(area==null){
 			area= " - ";
 		}
 		if(area==""){
 			area = " - ";
 		}    
        if(codigo==null){
 			codigo= " - ";
 		}
 		if(codigo==""){
 			codigo = " - ";
 		}  
        String[] veccomp = comp.split("\\ - ", -1);
        String[] vecarea = area.split("\\ - ", -1);
        String[] veccod = codigo.split("\\ - ", -1);
  		
        String query = "UPDATE SGC003";
         query += " SET VALOR = ?, ";
         query += " USRACT = ?,";
         query += " FECACT = '" + getFecha() + "'";
         query += " WHERE COMP = ? ";
         query += " AND AREA = ? ";
         query += " AND CODIGO = ? ";
         query += " AND ANOCAL = ? ";
         query += " AND MESCAL = ? ";
         query += " AND INSTANCIA = " + instancia + "";

        pstmt = con.prepareStatement(query);
        pstmt.setFloat(1,Float.parseFloat(valor));
        pstmt.setString(2, login);
        pstmt.setString(3, veccomp[0].toUpperCase());
        pstmt.setString(4, vecarea[0].toUpperCase());
        pstmt.setString(5, veccod[0].toUpperCase());
        pstmt.setInt(6,Integer.parseInt(anocal));
        pstmt.setInt(7,Integer.parseInt(mescal));;
        
        System.out.println(query);
        //System.out.println(valor);
        //System.out.println(veccomp[0]);
        //System.out.println(vecarea[0]);
        //System.out.println(veccod[0]);
        //System.out.println(anocal);
        //System.out.println(mescal);
        
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
  		
		Context initContext = new InitialContext();     
		DataSource ds = (DataSource) initContext.lookup(JNDI);
		con = ds.getConnection();		 
        
 		String validar = "1";
		String querycon = "SELECT BI_SGC014('" + login.toUpperCase() + "') AS VALIDAR FROM DUAL";
		
		//System.out.println(querycon);
		//System.out.println(JNDI);
		
		consulta.selectPntGenerica(querycon, JNDI);
		
		rows = consulta.getRows();
		tabla = consulta.getArray();
		//System.out.println(tabla[0][0]);

		if (tabla[0][0].equals(validar)) {

			
            if(comp==null){
     			comp= " - ";
     		}
     		if(comp==""){
     			comp = " - ";
     		}        
            if(area==null){
     			area= " - ";
     		}
     		if(area==""){
     			area = " - ";
     		}    
            if(codigo==null){
     			codigo= " - ";
     		}
     		if(codigo==""){
     			codigo = " - ";
     		}  
            String[] veccomp = comp.split("\\ - ", -1);
            String[] vecarea = area.split("\\ - ", -1);
            String[] veccod = codigo.split("\\ - ", -1);		
			
		//Consulta paginada
        String query = "SELECT * FROM"; 
	    query += "(select query.*, rownum as rn from";
		query += "(SELECT A.COMP, A.AREA, A.CODIGO, A.ANOCAL, A.MESCAL, A.VALOR, B.NOMIND AS DESC1, C.DESCR AS DESC2, D.DESCR AS DESC3, A.USRCRE, TO_CHAR(A.FECCRE,'DD/MM/YYYY') AS FECCRE ";
	    query += " FROM SGC003 A, SGC001 B, SGC005 C, SGC006 D ";
	    query += " WHERE A.CODIGO = B.CODIGO ";
	    query += " AND A.COMP = C.CODIGO ";
	    query += " AND A.AREA = D.CODIGO ";
	    query += " AND C.CODIGO = D.COMP ";
	    query += " AND A.COMP like '%" + veccomp[0] + "%'";
	    query += " AND A.AREA like '%" + vecarea[0] + "%'";
	    query += " AND A.CODIGO like '%" + veccod[0] + "%'";
	    query += " AND A.ANOCAL || A.MESCAL || A.VALOR || B.NOMIND || C.DESCR || D.DESCR || A.USRCRE LIKE trim('%" + ((String) filterValue).toUpperCase() +  "%') ";
	    query += " GROUP BY A.COMP, A.AREA, A.CODIGO, A.ANOCAL, A.MESCAL, A.VALOR, B.NOMIND, C.DESCR, D.DESCR, A.USRCRE, A.FECCRE";
	    query += ")query ) " ;
	    query += " WHERE ROWNUM <="+pageSize;
	    query += " AND rn > ("+ first +")";
	    query += " ORDER BY COMP, AREA, CODIGO, ANOCAL, MESCAL";

    pstmt = con.prepareStatement(query);
    //System.out.println(query);
    //System.out.println("Usuario ADMINISTRADOR...");
		
    r =  pstmt.executeQuery();
    
    while (r.next()){
 	Sgc003 select = new Sgc003();
 	select.setZcomp(r.getString(1) + " - " + r.getString(8));
 	select.setZarea(r.getString(2) + " - " + r.getString(9));
 	select.setZcodigo(r.getString(3) + " - " + r.getString(7));
 	select.setZanocal(r.getString(4));
 	select.setZmescal(r.getString(5));
 	select.setZvalor(r.getString(6));
 	select.setZcoddel(r.getString(3));
 	select.setZorderby(r.getString(1) + ", " + r.getString(2) + ", " + r.getString(3) + ", " + r.getString(4) + ", " + r.getString(5));
 	select.setZusrcre(r.getString(10));
 	select.setZfeccre(r.getString(11));
   	
    	//Agrega la lista
    	list.add(select);
    }
    //Cierra las conecciones
    pstmt.close();
    con.close();

	} else {
		
        if(comp==null){
 			comp= " - ";
 		}
 		if(comp==""){
 			comp = " - ";
 		}        
        if(area==null){
 			area= " - ";
 		}
 		if(area==""){
 			area = " - ";
 		}    
        if(codigo==null){
 			codigo= " - ";
 		}
 		if(codigo==""){
 			codigo = " - ";
 		}  
        String[] veccomp = comp.split("\\ - ", -1);
        String[] vecarea = area.split("\\ - ", -1);
        String[] veccod = codigo.split("\\ - ", -1);		
		
        String query = " SELECT  * ";
        query += " FROM (select  ";
        query += "      query.*,  ";
        query += "      rownum as rn  ";
        query += "      from (SELECT  ";
        query += "            A.COMP, A.AREA, A.CODIGO, A.ANOCAL, A.MESCAL, A.VALOR, B.NOMIND AS DESC1, C.DESCR AS DESC2, D.DESCR AS DESC3, A.USRCRE, TO_CHAR(A.FECCRE,'DD/MM/YYYY') AS FECCRE   ";
        query += "            FROM  ";
        query += "            SGC003 A, ";
        query += "            SGC001 B, ";
        query += "            SGC005 C, ";
        query += "            SGC006 D, ";
        query += "            SGC009 E  ";
        query += "            WHERE  ";
        query += "            A.COMP = B.COMP  ";
        query += "            AND A.AREA = B.AREA ";
        query += "            AND A.CODIGO = B.CODIGO ";
        query += "            AND A.COMP = C.CODIGO   ";
        query += "            AND A.COMP = D.COMP ";
        query += "            AND A.AREA = D.CODIGO   ";
        query += "            AND A.COMP = E.COMP  ";
        query += "            AND A.AREA = E.AREA ";
        query += "            AND A.CODIGO = E.INDICA ";
        query += "            AND E.CODUSER = '" + login.toUpperCase() + "'";
        query += "            UNION ALL  ";
        query += "            SELECT  ";
        query += "            A.COMP, A.AREA, A.CODIGO, A.ANOCAL, A.MESCAL, A.VALOR, B.NOMIND AS DESC1, C.DESCR AS DESC2, D.DESCR AS DESC3, A.USRCRE, TO_CHAR(A.FECCRE,'DD/MM/YYYY') AS FECCRE   ";
        query += "            FROM  ";
        query += "            SGC003 A, ";
        query += "            SGC001 B, ";
        query += "            SGC005 C, ";
        query += "            SGC006 D ";
        query += "            WHERE  ";
        query += "            A.COMP = B.COMP  ";
        query += "            AND A.AREA = B.AREA ";
        query += "            AND A.CODIGO = B.CODIGO ";
        query += "            AND A.COMP = C.CODIGO   ";
        query += "            AND A.COMP = D.COMP ";
        query += "            AND A.AREA = D.CODIGO   ";
        query += "            AND A.COMP||A.AREA IN (SELECT COMP||AREA FROM SGC008 WHERE CODUSER = '" + login.toUpperCase() + "' AND DUEPRO = '1')";
        query += "            ) query ";
        query += "            WHERE ";
        query += "            query.COMP like '%" + veccomp[0] + "%'";
        query += "            AND query.AREA like '%" + vecarea[0] + "%'";
        query += "            AND query.CODIGO like '%" + veccod[0] + "%'";
        query += "            AND query.ANOCAL || query.MESCAL || query.VALOR || query.DESC1 || query.DESC2 || query.DESC3 || query.USRCRE LIKE trim('%" + ((String) filterValue).toUpperCase() +  "%')) ";
        query += " WHERE  ";
	    query += " ROWNUM <="+pageSize;
	    query += " AND rn > ("+ first +")";
	    query += " ORDER BY COMP, AREA, CODIGO, ANOCAL, MESCAL";


    pstmt = con.prepareStatement(query);
    //System.out.println(query);
    //System.out.println("Usuario *** NO *** ADMINISTRADOR...");
		
    r =  pstmt.executeQuery();
    
    while (r.next()){
 	Sgc003 select = new Sgc003();
 	select.setZcomp(r.getString(1) + " - " + r.getString(8));
 	select.setZarea(r.getString(2) + " - " + r.getString(9));
 	select.setZcodigo(r.getString(3) + " - " + r.getString(7));
 	select.setZanocal(r.getString(4));
 	select.setZmescal(r.getString(5));
 	select.setZvalor(r.getString(6));
 	select.setZcoddel(r.getString(3));
 	select.setZorderby(r.getString(1) + ", " + r.getString(2) + ", " + r.getString(3) + ", " + r.getString(4) + ", " + r.getString(5));
 	select.setZusrcre(r.getString(10));
 	select.setZfeccre(r.getString(11));
   	
    	//Agrega la lista
    	list.add(select);
    }
    //Cierra las conecciones
    pstmt.close();
    con.close();		
	}
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
     			comp= " - ";
     		}
     		if(comp==""){
     			comp = " - ";
     		}        
            if(area==null){
     			area= " - ";
     		}
     		if(area==""){
     			area = " - ";
     		}    
            if(codigo==null){
     			codigo= " - ";
     		}
     		if(codigo==""){
     			codigo = " - ";
     		}  
            String[] veccomp = comp.split("\\ - ", -1);
            String[] vecarea = area.split("\\ - ", -1);
            String[] veccod = codigo.split("\\ - ", -1);
            

     		//Consulta paginada
     		String query = "SELECT COUNT_SGC003('" + veccomp[0] + "','" 
     		                                       + vecarea[0] + "','" 
     		                                       + veccod[0] + "','" 
    				                               + ((String) filterValue).toUpperCase() + "','"
    				                               + login.toUpperCase() 
    				                               + "') FROM DUAL";

           
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
  		area = "";
  		codigo = "";
  		anocal = "";
  		mescal = "";
  		valor = "";
  		validarOperacion = 0;
	}
  	
	public void reset() {
		// TODO Auto-generated method stub
  		comp = null;
  		area = null;
  		codigo = null;
	}
       
}
