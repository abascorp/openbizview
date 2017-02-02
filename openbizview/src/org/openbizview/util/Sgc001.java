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
public class Sgc001 extends Bd implements Serializable {

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private LazyDataModel<Sgc001> lazyModel;  
	
	
	/**
	 * @return the lazyModel
	 */
	public LazyDataModel<Sgc001> getLazyModel() {
		return lazyModel;
	}	

@PostConstruct
public void init() {
	//System.out.println("entre al metodo INIT");
	lazyModel  = new LazyDataModel<Sgc001>(){
		/**
		 * 
		 */
		private static final long serialVersionUID = 7217573531435419432L;
		
        @Override
		public List<Sgc001> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) { 
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
    private String comp = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("comp"); 
    private String area = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("area"); 
    private String codigo = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("codigo"); 
	private String desc = "";
	private String meta = "";
	private String tolsup = "";
	private String tolinf = "";
	private String calcul = "";
	private String fuente = "";
	private String proces = "";
	private String period = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("period"); //Usuario logeado
	private String nivapp = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("nivapp"); //Usuario logeado
	private String respon = ""; //Usuario logeado
	private String estatu = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("estatu"); //Usuario logeado
	private String tvalm = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("tvalm"); //Usuario logeado
	private String tvalti = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("tvalti"); //Usuario logeado
	private String tvalts = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("tvalts"); //Usuario logeado
	private Date vigenc = new Date();
	private Date feccam = new Date();
	private Date feccai = new Date();
	private Date feccas = new Date();
	private String compor = "";
	private String resmet = "";
	private Object filterValue = "";
	private List<Sgc001> list = new ArrayList<Sgc001>();
	private int validarOperacion = 0;
	private String instancia = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("instancia"); //Usuario logeado
	private String exito = "exito";
	private String zcomp = "";
	private String zarea = "";
	private String zcodigo = "";
	private String zdesc = "";
	private String zmeta = "";
	private String ztolsup = "";
	private String ztolinf = "";
	private String zcalcul = "";
	private String zfuente = "";
	private String zproces = "";
	private String zperiod = "";
	private String znivapp = "";
	private String zrespon = "";
	private String zestatu = "";
	private String zvigenc = "";
	private String zdesc1 = "";
	private String zdesc2 = "";
	private String zdesc3 = "";
	private String zdesc4 = "";
	private String ztvalm = "";
	private String ztvalti = "";
	private String ztvalts = "";
	private String zfeccam = "";	
	private String zfeccai = "";
	private String zfeccas = "";
	private String zdelete = "";
	private String zusername = "";
	private String zcompor = "";
	private String zresmet = "";
	String[][] tabla;
	
	public String getCompor() {
		return compor;
	}

	public void setCompor(String compor) {
		this.compor = compor;
	}

	public String getResmet() {
		return resmet;
	}

	public void setResmet(String resmet) {
		this.resmet = resmet;
	}

	public String getZcompor() {
		return zcompor;
	}

	public void setZcompor(String zcompor) {
		this.zcompor = zcompor;
	}

	public String getZresmet() {
		return zresmet;
	}

	public void setZresmet(String zresmet) {
		this.zresmet = zresmet;
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

	public Date getFeccai() {
		return feccai;
	}

	public void setFeccai(Date feccai) {
		this.feccai = feccai;
	}

	public Date getFeccas() {
		return feccas;
	}

	public void setFeccas(Date feccas) {
		this.feccas = feccas;
	}

	public String getZfeccai() {
		return zfeccai;
	}

	public void setZfeccai(String zfeccai) {
		this.zfeccai = zfeccai;
	}

	public String getZfeccas() {
		return zfeccas;
	}

	public void setZfeccas(String zfeccas) {
		this.zfeccas = zfeccas;
	}

	public String getTvalti() {
		return tvalti;
	}

	public void setTvalti(String tvalti) {
		this.tvalti = tvalti;
	}

	public String getTvalts() {
		return tvalts;
	}

	public void setTvalts(String tvalts) {
		this.tvalts = tvalts;
	}

	public String getZtvalti() {
		return ztvalti;
	}

	public void setZtvalti(String ztvalti) {
		this.ztvalti = ztvalti;
	}

	public String getZtvalts() {
		return ztvalts;
	}

	public void setZtvalts(String ztvalts) {
		this.ztvalts = ztvalts;
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

	public String getTolsup() {
		return tolsup;
	}

	public void setTolsup(String tolsup) {
		this.tolsup = tolsup;
	}

	public String getTolinf() {
		return tolinf;
	}

	public void setTolinf(String tolinf) {
		this.tolinf = tolinf;
	}

	public String getZtolsup() {
		return ztolsup;
	}

	public void setZtolsup(String ztolsup) {
		this.ztolsup = ztolsup;
	}

	public String getZtolinf() {
		return ztolinf;
	}

	public void setZtolinf(String ztolinf) {
		this.ztolinf = ztolinf;
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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getMeta() {
		return meta;
	}

	public void setMeta(String meta) {
		this.meta = meta;
	}

	public String getCalcul() {
		return calcul;
	}

	public void setCalcul(String calcul) {
		this.calcul = calcul;
	}

	public String getFuente() {
		return fuente;
	}

	public void setFuente(String fuente) {
		this.fuente = fuente;
	}

	public String getProces() {
		return proces;
	}

	public void setProces(String proces) {
		this.proces = proces;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getNivapp() {
		return nivapp;
	}

	public void setNivapp(String nivapp) {
		this.nivapp = nivapp;
	}

	public String getRespon() {
		return respon;
	}

	public void setRespon(String respon) {
		this.respon = respon;
	}

	public String getEstatu() {
		return estatu;
	}

	public void setEstatu(String estatu) {
		this.estatu = estatu;
	}

	public Date getVigenc() {
		return vigenc;
	}

	public void setVigenc(Date vigenc) {
		this.vigenc = vigenc;
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

	public String getZmeta() {
		return zmeta;
	}

	public void setZmeta(String zmeta) {
		this.zmeta = zmeta;
	}

	public String getZcalcul() {
		return zcalcul;
	}

	public void setZcalcul(String zcalcul) {
		this.zcalcul = zcalcul;
	}

	public String getZfuente() {
		return zfuente;
	}

	public void setZfuente(String zfuente) {
		this.zfuente = zfuente;
	}

	public String getZproces() {
		return zproces;
	}

	public void setZproces(String zproces) {
		this.zproces = zproces;
	}

	public String getZperiod() {
		return zperiod;
	}

	public void setZperiod(String zperiod) {
		this.zperiod = zperiod;
	}

	public String getZnivapp() {
		return znivapp;
	}

	public void setZnivapp(String znivapp) {
		this.znivapp = znivapp;
	}

	public String getZrespon() {
		return zrespon;
	}

	public void setZrespon(String zrespon) {
		this.zrespon = zrespon;
	}

	public String getZestatu() {
		return zestatu;
	}

	public void setZestatu(String zestatu) {
		this.zestatu = zestatu;
	}

	public String getZvigenc() {
		return zvigenc;
	}

	public void setZvigenc(String zvigenc) {
		this.zvigenc = zvigenc;
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


public void insert() throws  NamingException {   	
	//System.out.println("entre al metodo INSERT");	
    try {
    	Context initContext = new InitialContext();     
 		DataSource ds = (DataSource) initContext.lookup(JNDI);
        con = ds.getConnection();
        if(period==null){
 			period = " - ";
 		}
 		if(period==""){
 			period = " - ";
 		}        
        if(nivapp==null){
 			nivapp = " - ";
 		}
 		if(nivapp==""){
 			nivapp = " - ";
 		}        
        if(estatu==null){
 			estatu = " - ";
 		}
 		if(estatu==""){
 			estatu = " - ";
 		}        
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
 		if(tvalm==null){
  			tvalm = " - ";
  		}
  		if(tvalm==""){
  			tvalm = " - ";
  		}  
  		if(tvalti==null){
 			tvalti = " - ";
 		}
 		if(tvalti==""){
 			tvalti = " - ";
 		}  
 		if(tvalts==null){
 			tvalts = " - ";
 		}
 		if(tvalts==""){
 			tvalts = " - ";
 		}  
        String[] vectval = tvalts.split("\\ - ", -1);
        String[] vectvalt = tvalti.split("\\ - ", -1);
        String[] vecperiod = period.split("\\ - ", -1);
        String[] vecnivapp = nivapp.split("\\ - ", -1);
        String[] vecestatu = estatu.split("\\ - ", -1);
        String[] veccomp = comp.split("\\ - ", -1);
        String[] vecarea = area.split("\\ - ", -1);
        String[] vectvalm = tvalm.split("\\ - ", -1);
        //String[] vecrespon = respon.split("\\ - ", -1);
                    
        //String query = "INSERT INTO SGC001 VALUES (?,?,?,?,?,?,?,to_date('" + sdfecha.format(feccam) + "','dd/mm/yyyy'),?,?,to_date('" + sdfecha.format(feccai) + "','dd/mm/yyyy'),?,?,to_date('" + sdfecha.format(feccas) + "','dd/mm/yyyy'),?,?,?,?,?,?,?,to_date('" + sdfecha.format(vigenc) + "','dd/mm/yyyy'),?,?,'" + getFecha() + "',?,'" + getFecha() + "',?)";
        String query = "INSERT INTO SGC001 VALUES  (?, " +
				" ?, " +
				" ?, " +
				" ?, " +
				" ?, " +
				" ?, " +
				" ?, " +
				" to_date('" + sdfecha.format(feccam) + "','dd/mm/yyyy'), " +
				" ?, " +
				" ?, " +
				" to_date('" + sdfecha.format(feccai) + "','dd/mm/yyyy'), " +
				" ?, " +
				" ?, " +
				" to_date('" + sdfecha.format(feccas) + "','dd/mm/yyyy'), " +
				" TRIM(?), " +
				" TRIM(?), " +
				" TRIM(?), " +
				" TRIM(?), " +
				" TRIM(?), " +
				" TRIM(?), " +
				" TRIM(?), " +
				" to_date('" + sdfecha.format(vigenc) + "','dd/mm/yyyy'), " +
				" TRIM(?), " +
				" TRIM(?), " +
				" '" + getFecha() + "', " +
				" TRIM(?), " +
				" '" + getFecha() + "', " +
				" TRIM(?))";
        
        pstmt = con.prepareStatement(query);
        pstmt.setString(1, veccomp[0].toUpperCase());
        pstmt.setString(2, vecarea[0].toUpperCase());
        pstmt.setString(3, codigo.toUpperCase());
        pstmt.setString(4, desc.toUpperCase());
        pstmt.setString(5, vectvalm[0].toUpperCase());
        pstmt.setFloat(6, Float.parseFloat(meta));
        pstmt.setString(7, resmet.toUpperCase());
        pstmt.setString(8, vectvalt[0].toUpperCase());
        pstmt.setFloat(9, Float.parseFloat(tolinf));
        pstmt.setString(10, vectval[0].toUpperCase());
        pstmt.setFloat(11, Float.parseFloat(tolsup));
        pstmt.setString(12, calcul.toUpperCase());
        pstmt.setString(13, fuente.toUpperCase());
        pstmt.setString(14, proces.toUpperCase());
        pstmt.setString(15, vecperiod[0].toUpperCase());
        pstmt.setString(16, vecnivapp[0].toUpperCase());
        pstmt.setString(17, respon.toUpperCase());
        pstmt.setString(18, vecestatu[0].toUpperCase());
        pstmt.setString(19, compor.toUpperCase());
        pstmt.setString(20, login);
        pstmt.setString(21, login);            
        pstmt.setInt(22,Integer.parseInt(instancia));
   
        //System.out.println(query);
        //System.out.println(veccomp[0]);
        //System.out.println(vecarea[0]);
        //System.out.println(codigo);
        //System.out.println(desc);
        //System.out.println(vectvalm[0].toUpperCase());
        //System.out.println(meta);
        //System.out.println(resmet);
        //System.out.println(vectvalt[0].toUpperCase());
        //System.out.println(tolinf);
        //System.out.println(vectval[0].toUpperCase());
        //System.out.println(tolsup);
        //System.out.println(calcul);
        //System.out.println(fuente);
        //System.out.println(proces);
        //System.out.println(vecperiod[0]);
        //System.out.println(vecnivapp[0]);
        //System.out.println(respon.toUpperCase());
        //System.out.println(vecestatu[0]);
        //System.out.println(sdfecha.format(vigenc));
        //System.out.println(compor);
     
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

/**
 * Inserta Sgc001.
 * <p>
 * <b>Parametros del Metodo:<b> String codcat1, String descat1 unidos como un solo string.<br>
 * String pool, String login.<br><br>
 **/
public void inserth() throws  NamingException {   	
	//System.out.println("entre al metodo INSERTH");	
    try {
    	Context initContext = new InitialContext();     
 		DataSource ds = (DataSource) initContext.lookup(JNDI);
        con = ds.getConnection();
        if(period==null){
 			period = " - ";
 		}
 		if(period==""){
 			period = " - ";
 		}        
        if(nivapp==null){
 			nivapp = " - ";
 		}
 		if(nivapp==""){
 			nivapp = " - ";
 		}        
        if(estatu==null){
 			estatu = " - ";
 		}
 		if(estatu==""){
 			estatu = " - ";
 		}        
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

        String[] vecperiod = period.split("\\ - ", -1);
        String[] vecnivapp = nivapp.split("\\ - ", -1);
        String[] vecestatu = estatu.split("\\ - ", -1);
        String[] veccomp = comp.split("\\ - ", -1);
        String[] vecarea = area.split("\\ - ", -1);
        //String[] vecrespon = respon.split("\\ - ", -1);
                    
        String query = "INSERT INTO SGC001D VALUES (?,?,?,?,?,?,?,?,?,?,?,to_date('" + sdfecha.format(vigenc) + "','dd/mm/yyyy'),?,?,'" + getFecha() + "',?,null)";
        pstmt = con.prepareStatement(query);
        pstmt.setString(1, veccomp[0].toUpperCase());
        pstmt.setString(2, vecarea[0].toUpperCase());
        pstmt.setString(3, codigo.toUpperCase());
        pstmt.setString(4, desc.toUpperCase());
        pstmt.setString(5, calcul.toUpperCase());
        pstmt.setString(6, fuente.toUpperCase());
        pstmt.setString(7, proces.toUpperCase());
        pstmt.setString(8, vecperiod[0].toUpperCase());
        pstmt.setString(9, vecnivapp[0].toUpperCase());
        pstmt.setString(10, respon.toUpperCase());
        pstmt.setString(11, vecestatu[0].toUpperCase());
        pstmt.setString(12, compor.toUpperCase());
        pstmt.setString(13, login);         
        pstmt.setInt(14,Integer.parseInt(instancia));
   
        //System.out.println(query);
        //System.out.println(veccomp[0]);
        //System.out.println(vecarea[0]);
        //System.out.println(codigo);
        //System.out.println(desc);
        //System.out.println(calcul);
        //System.out.println(fuente);
        //System.out.println(proces);
        //System.out.println(vecperiod[0]);
        //System.out.println(vecnivapp[0]);
        //System.out.println(respon.toUpperCase());
        //System.out.println(vecestatu[0]);
        //System.out.println(sdfecha.format(vigenc));
        //System.out.println(compor);
     
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
        if(tvalm==null){
 			tvalm = " - ";
 		}
 		if(tvalm==""){
 			tvalm = " - ";
 		}  
        String[] veccomp = comp.split("\\ - ", -1);
        String[] vecarea = area.split("\\ - ", -1);
        String[] vectvalm = tvalm.split("\\ - ", -1);
                    
        String query = "INSERT INTO SGC001A VALUES (?,?,?,?,to_date('" + sdfecha.format(feccam) + "','dd/mm/yyyy'),?,?,?,'" + getFecha() + "',?,'" + getFecha() + "',?,null)";
        pstmt = con.prepareStatement(query);
        pstmt.setString(1, veccomp[0].toUpperCase());
        pstmt.setString(2, vecarea[0].toUpperCase());
        pstmt.setString(3, codigo.toUpperCase());
        pstmt.setString(4, vectvalm[0].toUpperCase());
        pstmt.setFloat(5, Float.parseFloat(meta));
        pstmt.setString(6, resmet.toUpperCase());
        pstmt.setString(7, login);
        pstmt.setString(8, login);            
        pstmt.setInt(9, Integer.parseInt(instancia));
   
        System.out.println(query);
        System.out.println(veccomp[0]);
        System.out.println(vecarea[0]);
        System.out.println(codigo);
        System.out.println(vectvalm[0]);
        System.out.println(sdfecha.format(feccam));
        System.out.println(meta);
        System.out.println(resmet);
     
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

/**
 * Inserta Sgc001B.
 * <p>
 * <b>Parametros del Metodo:<b> String codcat1, String descat1 unidos como un solo string.<br>
 * String pool, String login.<br><br>
 **/
public void insertti() throws  NamingException {   	
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
        if(tvalti==null){
 			tvalti = " - ";
 		}
 		if(tvalti==""){
 			tvalti = " - ";
 		}  
        String[] veccomp = comp.split("\\ - ", -1);
        String[] vecarea = area.split("\\ - ", -1);
        String[] vectvalt = tvalti.split("\\ - ", -1);
                    
        String query = "INSERT INTO SGC001B VALUES (?,?,?,?,to_date('" + sdfecha.format(feccai) + "','dd/mm/yyyy'),?,?,'" + getFecha() + "',?,'" + getFecha() + "',?,null)";
        pstmt = con.prepareStatement(query);
        pstmt.setString(1, veccomp[0].toUpperCase());
        pstmt.setString(2, vecarea[0].toUpperCase());
        pstmt.setString(3, codigo.toUpperCase());
        pstmt.setString(4, vectvalt[0].toUpperCase());
        pstmt.setFloat(5, Float.parseFloat(tolinf));
        pstmt.setString(6, login);
        pstmt.setString(7, login);            
        pstmt.setInt(8, Integer.parseInt(instancia));
   
        //System.out.println(query);
        //System.out.println(veccomp[0]);
        //System.out.println(vecarea[0]);
        //System.out.println(codigo);
        //System.out.println(vectvalt[0]);
        //System.out.println(sdfecha.format(feccai));
        //System.out.println(tolinf);
     
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

/**
 * Inserta Sgc001C.
 * <p>
 * <b>Parametros del Metodo:<b> String codcat1, String descat1 unidos como un solo string.<br>
 * String pool, String login.<br><br>
 **/
public void insertts() throws  NamingException {   	
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
        if(tvalts==null){
 			tvalts = " - ";
 		}
 		if(tvalts==""){
 			tvalts = " - ";
 		}  
        String[] veccomp = comp.split("\\ - ", -1);
        String[] vecarea = area.split("\\ - ", -1);
        String[] vectvalt = tvalts.split("\\ - ", -1);
                    
        String query = "INSERT INTO SGC001C VALUES (?,?,?,?,to_date('" + sdfecha.format(feccas) + "','dd/mm/yyyy'),?,?,'" + getFecha() + "',?,'" + getFecha() + "',?,null)";
        pstmt = con.prepareStatement(query);
        pstmt.setString(1, veccomp[0].toUpperCase());
        pstmt.setString(2, vecarea[0].toUpperCase());
        pstmt.setString(3, codigo.toUpperCase());
        pstmt.setString(4, vectvalt[0].toUpperCase());
        pstmt.setFloat(5, Float.parseFloat(tolsup));
        pstmt.setString(6, login);
        pstmt.setString(7, login);            
        pstmt.setInt(8, Integer.parseInt(instancia));
   
        //System.out.println(query);
        //System.out.println(veccomp[0]);
        //System.out.println(vecarea[0]);
        //System.out.println(codigo);
        //System.out.println(vectvalt[0]);
        //System.out.println(sdfecha.format(feccas));
        //System.out.println(tolsup);
     
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

        	String query2 = "DELETE from SGC001d WHERE COMP || AREA || CODIGO in (" + param + ") and INSTANCIA = " + instancia + "";	
        	String query = "DELETE from SGC001 WHERE COMP || AREA || CODIGO in (" + param + ") and INSTANCIA = " + instancia + "";	        	
            
        	pstmt = con.prepareStatement(query2);
        	pstmt = con.prepareStatement(query);
            
        	System.out.println(query2);
        	System.out.println(query);

        	  try {
                  //Avisando
              	pstmt.executeUpdate();
                  
               } catch (SQLException e)  {
            	                                   
                   if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC001D_FK3) violated - child record found")){
                   	
                   	//System.out.println("se cumple la condicion y muestro el msg");
                   	msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("sgc001dfk1"), "");
                   }
                   
                   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC009_FK4) violated - child record found")){
                      	
                      	//System.out.println("se cumple la condicion y muestro el msg");
                      	msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("sgc009fk4"), "");
                   }
                   
                   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC001A_FK4) violated - child record found")){
                      	
                      	//System.out.println("se cumple la condicion y muestro el msg");
                      	msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("sgc001afk4"), "");
                   }
                      
                   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC001B_FK4) violated - child record found")){
                         	
                         //System.out.println("se cumple la condicion y muestro el msg");
                        msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("sgc001bfk4"), "");
                   }
                      
                   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC001C_FK4) violated - child record found")){
                         	
                        //System.out.println("se cumple la condicion y muestro el msg");
                       	msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("sgc001cfk4"), "");
                   }
                         
                   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC003_FK3) violated - child record found")){
                            	
                     	//System.out.println("se cumple la condicion y muestro el msg");
                        msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("sgc003fk3"), "");
                   }
                   
                   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC012_FK3) violated - child record found")){
                     	
                     	//System.out.println("se cumple la condicion y muestro el msg");
                     	msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("sgc012fk3"), "");
                  }
                     
                   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC012A_FK3) violated - child record found")){
                        	
                        //System.out.println("se cumple la condicion y muestro el msg");
                       msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("sgc012afk3"), "");
                  }
                     
                   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC012B_FK3) violated - child record found")){
                        	
                       //System.out.println("se cumple la condicion y muestro el msg");
                      	msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("sgc012bfk3"), "");
                  }
                        
                   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC012C_FK3) violated - child record found")){
                           	
                    	//System.out.println("se cumple la condicion y muestro el msg");
                       msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("sgc012cfk3"), "");
                  }
                   else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC012C_FK2) violated - child record found")){
                     	
                  	//System.out.println("se cumple la condicion y muestro el msg");
                     msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("sgc012cfk3"), "");
                }                  
                   
                   else {
                   	
                   	//System.out.println("no se cumple la condicion y muestro otro msg");
                   	msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
                   }
                   
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

        	String query = "DELETE from SGC001A WHERE COMP || AREA || CODIGO in (" + param + ") and INSTANCIA = " + instancia + "";        	
            
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

public void deleteti() throws NamingException  {  
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

        	String query = "DELETE from SGC001B WHERE COMP || AREA || CODIGO in (" + param + ") and INSTANCIA = " + instancia + "";        	
            
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

public void deletets() throws NamingException  {  
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

        	String query = "DELETE from SGC001C WHERE COMP || AREA || CODIGO in (" + param + ") and INSTANCIA = " + instancia + "";
       	
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
 * Actualiza sgc001
 * <b>Parametros del Metodo:<b> String codcat1, String descat1 unidos como un solo string.<br>
 * String pool, String login.<br><br>
 **/
public void update() throws  NamingException {
	//System.out.println("entre al metodo UPDATE");
     try { 	 
    	 Context initContext = new InitialContext();     
  		DataSource ds = (DataSource) initContext.lookup(JNDI);

  		if(period==null){
  			period = " - ";
  		}
  		if(period==""){
  			period = " - ";
  		}        
         if(nivapp==null){
  			nivapp = " - ";
  		}
  		if(nivapp==""){
  			nivapp = " - ";
  		}        
         if(estatu==null){
  			estatu = " - ";
  		}
  		if(estatu==""){
  			estatu = " - ";
  		}        
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
 		if(tvalm==null){
 			tvalm = " - ";
 		}
 		if(tvalm==""){
 			tvalm = " - ";
 		} 
 		if(tvalti==null){
			tvalti = " - ";
		}
		if(tvalti==""){
			tvalti = " - ";
		} 
		if(tvalts==null){
			tvalts = " - ";
		}
		if(tvalts==""){
			tvalts = " - ";
		} 
         String[] vectvalts = tvalts.split("\\ - ", -1);
         String[] vectvalti = tvalti.split("\\ - ", -1);
         String[] vectvalm = tvalm.split("\\ - ", -1);
         String[] vecperiod = period.split("\\ - ", -1);
         String[] vecnivapp = nivapp.split("\\ - ", -1);
         String[] vecestatu = estatu.split("\\ - ", -1);
         String[] veccomp = comp.split("\\ - ", -1);
         String[] vecarea = area.split("\\ - ", -1);
        // String[] vecrespon = respon.split("\\ - ", -1);
  		
  		con = ds.getConnection();		
  		 
        String query = "UPDATE SGC001";
         query += " SET NOMIND = ?, ";   
         query += " TIPVALM = ?, ";
         query += " META = ?, ";
         query += " RESMET = ?, ";
         query += " FECCAM = to_date('" + sdfecha.format(feccam) + "','dd/mm/yyyy'),";
         query += " TIPVALTI = ?, ";
         query += " TOLINF = ?, ";
         query += " FECCATI = to_date('" + sdfecha.format(feccai) + "','dd/mm/yyyy'),";
         query += " TIPVALTS = ?, ";
         query += " TOLSUP = ?, ";
         query += " FECCATS = to_date('" + sdfecha.format(feccas) + "','dd/mm/yyyy'),";
         query += " CALCULO = TRIM(?), ";  
         query += " DATOS = TRIM(?),";
         query += " PROCES = TRIM(?),";
         query += " PERIOD = TRIM(?),";
         query += " NIVAPP = TRIM(?),";
         query += " DESCRI = TRIM(?),";
         query += " ESTATU = TRIM(?),";
         query += " VIGENC = to_date('" + sdfecha.format(vigenc) + "','dd/mm/yyyy'),";
         query += " COMPOR = TRIM(?),";
         query += " USRACT = TRIM(?),";
         query += " FECACT = '" + getFecha() + "'";
         query += " WHERE CODIGO = ? AND COMP = ? AND AREA = ? AND INSTANCIA = " + instancia + "";

        pstmt = con.prepareStatement(query);
        pstmt.setString(1, desc.toUpperCase());
        pstmt.setString(2, vectvalm[0].toUpperCase());
        pstmt.setFloat(3, Float.parseFloat(meta));
        pstmt.setString(4, resmet.toUpperCase());
        pstmt.setString(5, vectvalti[0].toUpperCase());
        pstmt.setFloat(6, Float.parseFloat(tolinf));
        pstmt.setString(7, vectvalts[0].toUpperCase());
        pstmt.setFloat(8, Float.parseFloat(tolsup));
        pstmt.setString(9, calcul.toUpperCase());
        pstmt.setString(10, fuente.toUpperCase());
        pstmt.setString(11, proces.toUpperCase());
        pstmt.setString(12, vecperiod[0].toUpperCase());
        pstmt.setString(13, vecnivapp[0].toUpperCase());
        pstmt.setString(14, respon.toUpperCase());
        pstmt.setString(15, vecestatu[0].toUpperCase());
        pstmt.setString(16, compor.toUpperCase());
        pstmt.setString(17, login);
        pstmt.setString(18, codigo.toUpperCase());       
        pstmt.setString(19, veccomp[0].toUpperCase());    
        pstmt.setString(20, vecarea[0].toUpperCase());    

        //System.out.println(query);
        //System.out.println(desc);
        //System.out.println(vectvalm[0]);
        //System.out.println(meta);
        //System.out.println(sdfecha.format(feccam));
        //System.out.println(vectvalti[0]);
        //System.out.println(tolinf);
        //System.out.println(sdfecha.format(feccai));
        //System.out.println(vectvalts[0]);
        //System.out.println(tolsup);       
        //System.out.println(sdfecha.format(feccas));
        //System.out.println(calcul);
        //System.out.println(fuente);
        //System.out.println(proces);
        //System.out.println(vecperiod[0]);
        //System.out.println(vecnivapp[0]);
        //System.out.println(respon);
        //System.out.println(vecestatu[0]);
        //System.out.println(sdfecha.format(vigenc));
        //System.out.println(codigo);
        //System.out.println(veccomp[0]);
        //System.out.println(vecarea[0]);
        
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
 		if(tvalm==null){
 			tvalm = " - ";
 		}
 		if(tvalm==""){
 			tvalm = " - ";
 		} 

         String[] veccomp = comp.split("\\ - ", -1);
         String[] vecarea = area.split("\\ - ", -1);
         String[] vectvalm = tvalm.split("\\ - ", -1);
  		
  		con = ds.getConnection();		
  		
        String query = "UPDATE SGC001A";
         query += " SET TIPVAL = ?, ";
         query += " FECCAM = to_date('" + sdfecha.format(feccam) + "','dd/mm/yyyy'),";
         query += " META = ?,";
         query += " RESMET = ?,";
         query += " USRACT = ?,";
         query += " FECACT = '" + getFecha() + "'";
         query += " WHERE CODIGO = ? AND COMP = ? AND AREA = ? AND INSTANCIA = " + instancia + "";

        pstmt = con.prepareStatement(query);
        pstmt.setString(1, vectvalm[0].toUpperCase()); 
        pstmt.setFloat(2, Float.parseFloat(meta));
        pstmt.setString(3, resmet.toUpperCase()); 
        pstmt.setString(4, login);
        pstmt.setString(5, codigo.toUpperCase());       
        pstmt.setString(6, veccomp[0].toUpperCase());    
        pstmt.setString(7, vecarea[0].toUpperCase());    

        //System.out.println(query);
        //System.out.println(vectvalm[0]);
        //System.out.println(meta);
        //System.out.println(sdfecha.format(feccam));
        
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

/**
 * Actualiza sgc001B
 * <b>Parametros del Metodo:<b> String codcat1, String descat1 unidos como un solo string.<br>
 * String pool, String login.<br><br>
 **/
public void updateti() throws  NamingException {
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
		if(tvalti==null){
			tvalti = " - ";
		}
		if(tvalti==""){
			tvalti = " - ";
		} 

        String[] veccomp = comp.split("\\ - ", -1);
        String[] vecarea = area.split("\\ - ", -1);
        String[] vectvalti = tvalti.split("\\ - ", -1);
 		
 		con = ds.getConnection();		
 		
       String query = "UPDATE SGC001B";
        query += " SET TIPVAL = ?, ";
        query += " FECCAM = to_date('" + sdfecha.format(feccai) + "','dd/mm/yyyy'),";
        query += " TOLINF = ?,";
        query += " USRACT = ?,";
        query += " FECACT = '" + getFecha() + "'";
        query += " WHERE CODIGO = ? AND COMP = ? AND AREA = ? AND INSTANCIA = " + instancia + "";

       pstmt = con.prepareStatement(query);
       pstmt.setString(1, vectvalti[0].toUpperCase()); 
       pstmt.setFloat(2, Float.parseFloat(tolinf));
       pstmt.setString(3, login);
       pstmt.setString(4, codigo.toUpperCase());       
       pstmt.setString(5, veccomp[0].toUpperCase());    
       pstmt.setString(6, vecarea[0].toUpperCase());    

       //System.out.println(query);
       //System.out.println(vectvalti[0]);
       //System.out.println(tolinf);
       //System.out.println(sdfecha.format(feccai));
       
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

/**
 * Actualiza sgc001B
 * <b>Parametros del Metodo:<b> String codcat1, String descat1 unidos como un solo string.<br>
 * String pool, String login.<br><br>
 **/
public void updatets() throws  NamingException {
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
		if(tvalts==null){
			tvalts = " - ";
		}
		if(tvalts==""){
			tvalts = " - ";
		} 

        String[] veccomp = comp.split("\\ - ", -1);
        String[] vecarea = area.split("\\ - ", -1);
        String[] vectvalts = tvalts.split("\\ - ", -1);
 		
 		con = ds.getConnection();		
 		
       String query = "UPDATE SGC001C";
        query += " SET TIPVAL = ?, ";
        query += " FECCAM = to_date('" + sdfecha.format(feccas) + "','dd/mm/yyyy'),";
        query += " TOLSUP = ?,";
        query += " USRACT = ?,";
        query += " FECACT = '" + getFecha() + "'";
        query += " WHERE CODIGO = ? AND COMP = ? AND AREA = ? AND INSTANCIA = " + instancia + "";

       pstmt = con.prepareStatement(query);
       pstmt.setString(1, vectvalts[0].toUpperCase()); 
       pstmt.setFloat(2, Float.parseFloat(tolsup));
       pstmt.setString(3, login);
       pstmt.setString(4, codigo.toUpperCase());       
       pstmt.setString(5, veccomp[0].toUpperCase());    
       pstmt.setString(6, vecarea[0].toUpperCase());    

       //System.out.println(query);
       //System.out.println(vectvalts[0]);
       //System.out.println(tolsup);
       //System.out.println(sdfecha.format(feccas));
       
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
		insert();
		//inserth();
		insertm();
		insertti();
		insertts();
		limpiarValores();   
        if(exito=="exito"){
        	msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnInsert"), "");
        	FacesContext.getCurrentInstance().addMessage(null, msj);
        }
	} else {
		update();
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
         String[] veccod = codigo.split("\\ - ", -1);
		//System.out.println("entre al metodo SELECT");	
		Context initContext = new InitialContext();     
		DataSource ds = (DataSource) initContext.lookup(JNDI);
		con = ds.getConnection();		
		//Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
		DatabaseMetaData databaseMetaData = con.getMetaData();
		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
		
		//Consulta paginada	
     String query = "SELECT A.TIPVAL, A.META, TRIM(TO_CHAR(A.FECCAM,'DD/MM/YYYY')) AS FECCAM, E.DESCR AS DESC4, F.DESCR AS DESC5, J.DESCR AS DESC6, TRIM(A.RESMET) AS RESMET, A.REGIST ";
	    query += " FROM SGC001A A, SGC005 E, SGC006 F, TUBDER11 J, (SELECT MAX(A.REGIST) AS REGIST ";
	    query += " FROM SGC001A A ";
	    query += " WHERE TRIM(A.COMP) LIKE TRIM('%" + veccomp[0] + "%') ";
	    query += " AND TRIM(A.AREA) LIKE TRIM('%" + vecarea[0] + "%') ";
	    query += " AND TRIM(A.CODIGO) LIKE TRIM('%" + veccod[0] + "%')) SQ1 ";
	    query += " WHERE A.COMP = E.CODIGO";
	    query += " AND A.AREA = F.CODIGO";
	    query += " AND A.TIPVAL = J.CODIGO";
	    query += " AND TRIM(A.COMP) LIKE TRIM('%" + veccomp[0] + "%')";
	    query += " AND TRIM(A.AREA) LIKE TRIM('%" + vecarea[0] + "%')";
	    query += " AND TRIM(A.CODIGO) LIKE TRIM('%" + veccod[0] + "%')";
	    query += " AND A.REGIST = SQ1.REGIST";
	    query += " GROUP BY A.TIPVAL, A.META, A.FECCAM, E.DESCR, F.DESCR, J.DESCR, A.RESMET, A.REGIST";
	    query += " ORDER BY  8";

    pstmt = con.prepareStatement(query);
    //System.out.println(query);
		
    r =  pstmt.executeQuery();
    while (r.next()){
 	Sgc001 select = new Sgc001();
 	select.setZtvalm(r.getString(1)+ " - " + r.getString(6));
 	select.setZmeta(r.getString(2));
 	select.setZfeccam(r.getString(3));
 	select.setZresmet(r.getString(7));
 	
	//System.out.println(r.getString(1));	
	if ((meta+tvalm+sdfecha3.format(feccam)+resmet).equals(r.getString(2)+r.getString(1)+ " - " + r.getString(6)+r.getString(3)+r.getString(7))){		
		//System.out.println(meta+tvalm+sdfecha3.format(feccam)+resmet);
		//System.out.println(r.getString(2)+r.getString(1)+ " - " + r.getString(6)+r.getString(3)+r.getString(7));
		System.out.println("no hago el insert meta, los valores son iguales");
	}
	else {
		//System.out.println(meta+tvalm+sdfecha3.format(feccam)+resmet);
		//System.out.println(r.getString(2)+r.getString(1)+ " - " + r.getString(6)+r.getString(3)+r.getString(7));
		System.out.println("hago el insert meta, los valores son diferentes");	
		insertm();
	}
	
    }
    //Cierra las conecciones
    pstmt.close();
    con.close();
    
    guardarh();
	}
}

public void guardarh() throws NamingException, SQLException{   	
	//System.out.println("entre al metodo guardarh");
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
     String[] veccod = codigo.split("\\ - ", -1);
     
	//System.out.println("entre al metodo SELECT");	
	Context initContext = new InitialContext();     
	DataSource ds = (DataSource) initContext.lookup(JNDI);
	con = ds.getConnection();		
	//Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
	DatabaseMetaData databaseMetaData = con.getMetaData();
	productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
	
	//Consulta paginada	
 String query = "SELECT A.CODIGO, A.NOMIND, A.CALCULO, A.DATOS, A.PROCES, A.PERIOD, A.NIVAPP, A.DESCRI, A.ESTATU, TO_CHAR(A.VIGENC,'DD/MM/YYYY') AS VIGENC, B.DESCR AS DESC1, C.DESCR AS DESC2, D.DESCR AS DESC3, A.COMP, A.AREA, E.DESCR AS DESC4, F.DESCR AS DESC5, A.COMPOR, A.REGIST ";
    query += " FROM SGC001D A, TUBDER08 B, TUBDER10 C, TUBDER09 D, SGC005 E, SGC006 F, (SELECT MAX(A.REGIST) AS REGIST ";
    query += " FROM SGC001D A ";
    query += " WHERE TRIM(A.COMP) LIKE TRIM('%" + veccomp[0] + "%') ";
    query += " AND TRIM(A.AREA) LIKE TRIM('%" + vecarea[0] + "%') ";
    query += " AND TRIM(A.CODIGO) LIKE TRIM('%" + veccod[0] + "%')) SQ1 ";
    query += " WHERE A.PERIOD = D.CODIGO";
    query += " AND A.NIVAPP = B.CODIGO";
    query += " AND A.ESTATU = C.CODIGO";
    query += " AND A.COMP = E.CODIGO";
    query += " AND A.AREA = F.CODIGO";
    query += " AND A.REGIST = SQ1.REGIST";
    query += " GROUP BY A.CODIGO, A.NOMIND, A.CALCULO, A.DATOS, A.PROCES, A.PERIOD, A.NIVAPP, A.DESCRI, A.ESTATU, A.VIGENC, B.DESCR, C.DESCR, D.DESCR, A.COMP, A.AREA, E.DESCR, F.DESCR, A.COMPOR, A.REGIST";
    query += " ORDER BY 19";
    
	pstmt = con.prepareStatement(query);
	//System.out.println(query);
	//System.out.println(veccomp[0]);
	//System.out.println(vecarea[0]);
	//System.out.println(codigo);
	//System.out.println(desc);
	//System.out.println(calcul);
	//System.out.println(fuente);
	//System.out.println(proces);
	//System.out.println(vecperiod[0]);
	//System.out.println(vecnivapp[0]);
	//System.out.println(respon.toUpperCase());
	//System.out.println(vecestatu[0]);
	//System.out.println(sdfecha.format(vigenc));
	//System.out.println(compor);
	
	r =  pstmt.executeQuery();
	while (r.next()){
	Sgc001 select = new Sgc001();
 	select.setZcodigo(r.getString(1));
 	select.setZdesc(r.getString(2));
 	select.setZcalcul(r.getString(3));
 	select.setZfuente(r.getString(4));
 	select.setZproces(r.getString(5));
 	select.setZperiod(r.getString(6) + " - " + r.getString(13));
 	select.setZnivapp(r.getString(7) + " - " + r.getString(11));
 	select.setZrespon(r.getString(8));
 	select.setZestatu(r.getString(9) + " - " + r.getString(12));
 	select.setZvigenc(r.getString(10));
 	select.setZdesc1(r.getString(11));
 	select.setZdesc2(r.getString(12));
 	select.setZdesc3(r.getString(13));
 	select.setZcomp(r.getString(14)+ " - " + r.getString(16));
 	select.setZarea(r.getString(15)+ " - " + r.getString(17));
 	select.setZcompor(r.getString(18));
	
	//System.out.println(r.getString(1));	
	if ((comp+area+codigo+desc+calcul+fuente+proces+period+nivapp+respon+estatu+sdfecha3.format(vigenc)+compor).equals(r.getString(14) + " - " + r.getString(16)+r.getString(15) + " - " + r.getString(17)+r.getString(1)+r.getString(2)+r.getString(3)+r.getString(4)+r.getString(5)+r.getString(6) + " - " + r.getString(13)+r.getString(7) + " - " + r.getString(11)+r.getString(8)+r.getString(9) + " - " + r.getString(12)+r.getString(10)+r.getString(18))){		
		//System.out.println(comp+area+codigo+desc+calcul+fuente+proces+period+nivapp+respon+estatu+sdfecha3.format(vigenc)+compor);
		//System.out.println(r.getString(14) + " - " + r.getString(16)+r.getString(15) + " - " + r.getString(17)+r.getString(1)+r.getString(2)+r.getString(3)+r.getString(4)+r.getString(5)+r.getString(6) + " - " + r.getString(13)+r.getString(7) + " - " + r.getString(11)+r.getString(8)+r.getString(9) + " - " + r.getString(12)+r.getString(10)+r.getString(18));
		System.out.println("no hago el insert historico, los valores son iguales");
	}
	else {
		//System.out.println(comp+area+codigo+desc+calcul+fuente+proces+period+nivapp+respon+estatu+sdfecha3.format(vigenc)+compor);
		//System.out.println(r.getString(14) + " - " + r.getString(16)+r.getString(15) + " - " + r.getString(17)+r.getString(1)+r.getString(2)+r.getString(3)+r.getString(4)+r.getString(5)+r.getString(6) + " - " + r.getString(13)+r.getString(7) + " - " + r.getString(11)+r.getString(8)+r.getString(9) + " - " + r.getString(12)+r.getString(10)+r.getString(18));
		System.out.println("hago el insert historico, los valores son diferentes");	
		inserth();
	}
				
}
	//Cierra las conecciones
	pstmt.close();
	con.close();
	
	guardarti();
	
}


public void guardarti() throws NamingException, SQLException{   	
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
         String[] veccod = codigo.split("\\ - ", -1);
		//System.out.println("entre al metodo SELECT");	
		Context initContext = new InitialContext();     
		DataSource ds = (DataSource) initContext.lookup(JNDI);
		con = ds.getConnection();		
		//Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
		DatabaseMetaData databaseMetaData = con.getMetaData();
		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
		
		//Consulta paginada	
     String query = "SELECT A.TIPVAL, A.TOLINF, TRIM(TO_CHAR(A.FECCAM,'DD/MM/YYYY')) AS FECCAM, E.DESCR AS DESC4, F.DESCR AS DESC5, J.DESCR AS DESC6, A.REGIST ";
	    query += " FROM SGC001B A, SGC005 E, SGC006 F, TUBDER11 J, (SELECT MAX(A.REGIST) AS REGIST ";
	    query += " FROM SGC001B A ";
	    query += " WHERE TRIM(A.COMP) LIKE TRIM('%" + veccomp[0] + "%') ";
	    query += " AND TRIM(A.AREA) LIKE TRIM('%" + vecarea[0] + "%') ";
	    query += " AND TRIM(A.CODIGO) LIKE TRIM('%" + veccod[0] + "%')) SQ1 ";
	    query += " WHERE A.COMP = E.CODIGO";
	    query += " AND A.AREA = F.CODIGO";
	    query += " AND A.TIPVAL = J.CODIGO";
	    query += " AND TRIM(A.COMP) LIKE TRIM('%" + veccomp[0] + "%')";
	    query += " AND TRIM(A.AREA) LIKE TRIM('%" + vecarea[0] + "%')";
	    query += " AND TRIM(A.CODIGO) LIKE TRIM('%" + veccod[0] + "%')";
	    query += " AND A.REGIST = SQ1.REGIST";
	    query += " GROUP BY A.TIPVAL, A.TOLINF, A.FECCAM, E.DESCR, F.DESCR, J.DESCR, A.REGIST";
	    query += " ORDER BY  7";

    pstmt = con.prepareStatement(query);
    //System.out.println(query);
		
    r =  pstmt.executeQuery();
    while (r.next()){
 	Sgc001 select = new Sgc001();
 	select.setZtvalti(r.getString(1)+ " - " + r.getString(6));
 	select.setZtolinf(r.getString(2));
 	select.setZfeccai(r.getString(3));
 	
	//System.out.println(r.getString(1));	
	if ((tolinf+tvalti+sdfecha3.format(feccai)).equals(r.getString(2)+r.getString(1)+ " - " + r.getString(6)+r.getString(3))){		
		//System.out.println(tolinf+tvalti+sdfecha3.format(feccai));
		//System.out.println(r.getString(2)+r.getString(1)+ " - " + r.getString(6)+r.getString(3));
		System.out.println("no hago el insert tolinf, los valores son iguales");
	}
	else {
		//System.out.println(tolinf+tvalti+sdfecha3.format(feccai));
		//System.out.println(r.getString(2)+r.getString(1)+ " - " + r.getString(6)+r.getString(3));
		System.out.println("hago el insert tolinf, los valores son diferentes");	
		insertti();
	}
 			
    }
    //Cierra las conecciones
    pstmt.close();
    con.close();
    
	guardarts();
		
}

public void guardarts() throws NamingException, SQLException{   	
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
     String[] veccod = codigo.split("\\ - ", -1);
	//System.out.println("entre al metodo SELECT");	
	Context initContext = new InitialContext();     
	DataSource ds = (DataSource) initContext.lookup(JNDI);
	con = ds.getConnection();		
	//Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
	DatabaseMetaData databaseMetaData = con.getMetaData();
	productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
	
	//Consulta paginada	
 String query = "SELECT A.TIPVAL, A.TOLSUP, TRIM(TO_CHAR(A.FECCAM,'DD/MM/YYYY')) AS FECCAM, E.DESCR AS DESC4, F.DESCR AS DESC5, J.DESCR AS DESC6, A.REGIST ";
    query += " FROM SGC001C A, SGC005 E, SGC006 F, TUBDER11 J, (SELECT MAX(A.REGIST) AS REGIST ";
    query += " FROM SGC001C A ";
    query += " WHERE TRIM(A.COMP) LIKE TRIM('%" + veccomp[0] + "%') ";
    query += " AND TRIM(A.AREA) LIKE TRIM('%" + vecarea[0] + "%') ";
    query += " AND TRIM(A.CODIGO) LIKE TRIM('%" + veccod[0] + "%')) SQ1 ";
    query += " WHERE A.COMP = E.CODIGO";
    query += " AND A.AREA = F.CODIGO";
    query += " AND A.TIPVAL = J.CODIGO";
    query += " AND TRIM(A.COMP) LIKE TRIM('%" + veccomp[0] + "%')";
    query += " AND TRIM(A.AREA) LIKE TRIM('%" + vecarea[0] + "%')";
    query += " AND TRIM(A.CODIGO) LIKE TRIM('%" + veccod[0] + "%')";
    query += " AND A.REGIST = SQ1.REGIST";
    query += " GROUP BY A.TIPVAL, A.TOLSUP, A.FECCAM, E.DESCR, F.DESCR, J.DESCR, A.REGIST";
    query += " ORDER BY  7";

pstmt = con.prepareStatement(query);
//System.out.println(query);
	
r =  pstmt.executeQuery();
while (r.next()){
	Sgc001 select = new Sgc001();
	select.setZtvalts(r.getString(1)+ " - " + r.getString(6));
	select.setZtolsup(r.getString(2));
	select.setZfeccas(r.getString(3));
	
//System.out.println(r.getString(1));	
if ((tolsup+tvalts+sdfecha3.format(feccas)).equals(r.getString(2)+r.getString(1)+ " - " + r.getString(6)+r.getString(3))){		
	//System.out.println(tolsup+tvalts+sdfecha3.format(feccas));
	//System.out.println(r.getString(2)+r.getString(1)+ " - " + r.getString(6)+r.getString(3));
	System.out.println("no hago el insert tolsup, los valores son iguales");
}
else {
	//System.out.println(tolsup+tvalts+sdfecha3.format(feccas));
    //System.out.println(r.getString(2)+r.getString(1)+ " - " + r.getString(6)+r.getString(3));
	System.out.println("hago el insert tolsup, los valores son diferentes");	
	insertts();
}
			
}
//Cierra las conecciones
pstmt.close();
con.close();

	limpiarValores();
	if(exito=="exito"){
    	msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnUpdate"), "");
    	FacesContext.getCurrentInstance().addMessage(null, msj);
}
}

public void borrar() throws NamingException, SQLException{   	
		delete();
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

		Context initContext = new InitialContext();     
		DataSource ds = (DataSource) initContext.lookup(JNDI);
		con = ds.getConnection();		
		//Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
		DatabaseMetaData databaseMetaData = con.getMetaData();
		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
		
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
  			comp = " - ";
  		}
  		if(comp==""){
  			comp = " - ";
  		}        
         if(area==null){
  			area = " - ";
  		}
         
         String[] veccomp = comp.split("\\ - ", -1);
         String[] vecarea= area.split("\\ - ", -1);
         
		//Consulta paginada	
        String query = "SELECT * FROM"; 
	    query += "(select query.*, rownum as rn from";
		query += "(SELECT A.CODIGO, A.NOMIND, A.TIPVALM, A.META, TO_CHAR(A.FECCAM,'DD/MM/YYYY') AS FECCAM, A.TIPVALTI, A.TOLINF, TO_CHAR(A.FECCATI,'DD/MM/YYYY') AS FECCATI, A.TIPVALTS, A.TOLSUP, TO_CHAR(A.FECCATS,'DD/MM/YYYY') AS FECCATS, A.CALCULO, A.DATOS, A.PROCES, A.PERIOD, A.NIVAPP, A.DESCRI, A.ESTATU, TO_CHAR(A.VIGENC,'DD/MM/YYYY') AS VIGENC, B.DESCR AS DESC1, C.DESCR AS DESC2, D.DESCR AS DESC3, A.COMP, A.AREA, E.DESCR AS DESC4, F.DESCR AS DESC5, J.DESCR AS DESC6, A.COMPOR, A.RESMET ";
	    query += " FROM SGC001 A, TUBDER08 B, TUBDER10 C, TUBDER09 D, SGC005 E, SGC006 F, TUBDER11 J, TUBDER11 K, TUBDER11 L ";
	    query += " WHERE A.PERIOD = D.CODIGO";
	    query += " AND A.NIVAPP = B.CODIGO";
	    query += " AND A.ESTATU = C.CODIGO";
	    query += " AND A.COMP = E.CODIGO";
	    query += " AND A.AREA = F.CODIGO";
	    query += " AND A.TIPVALM = J.CODIGO";
	    query += " AND A.TIPVALTI = K.CODIGO";
	    query += " AND A.TIPVALTS = L.CODIGO";	    
	    query += " AND TRIM(A.COMP) LIKE TRIM('%" + veccomp[0] + "%')";
	    query += " AND TRIM(A.AREA) LIKE TRIM('%" + vecarea[0] + "%')";
	    query += " GROUP BY A.CODIGO, A.NOMIND, A.TIPVALM, A.META, A.FECCAM, A.TIPVALTI, A.TOLINF, A.FECCATI, A.TIPVALTS, A.TOLSUP, A.FECCATS, A.CALCULO, A.DATOS, A.PROCES, A.PERIOD, A.NIVAPP, A.DESCRI, A.ESTATU, A.VIGENC, B.DESCR, C.DESCR, D.DESCR, A.COMP, A.AREA, E.DESCR, F.DESCR, J.DESCR, A.COMPOR, A.RESMET";
	    query += " ORDER BY A.COMP, A.AREA, A.CODIGO ";
	    query += ")query ) " ;
	    query += " WHERE ROWNUM <="+pageSize;
	    query += " AND rn > ("+ first +")";
	    query += " ORDER BY  " + sortField.replace("z", "");

    pstmt = con.prepareStatement(query);
    //System.out.println(query);
    //System.out.println("Usuario ADMINISTRADOR...");
		
    r =  pstmt.executeQuery();
    while (r.next()){
 	Sgc001 select = new Sgc001();
 	select.setZcodigo(r.getString(1));
 	select.setZdesc(r.getString(2)) ;
 	select.setZtvalm(r.getString(3)+ " - " + r.getString(27));
 	select.setZmeta(r.getString(4));
 	select.setZfeccam(r.getString(5));
 	select.setZtvalti(r.getString(6)+ " - " + r.getString(27));
 	select.setZtolinf(r.getString(7));
 	select.setZfeccai(r.getString(8));
 	select.setZtvalts(r.getString(9)+ " - " + r.getString(27));
 	select.setZtolsup(r.getString(10));
 	select.setZfeccas(r.getString(11));
 	select.setZcalcul(r.getString(12));
 	select.setZfuente(r.getString(13));
 	select.setZproces(r.getString(14));
 	select.setZperiod(r.getString(15) + " - " + r.getString(22));
 	select.setZnivapp(r.getString(16) + " - " + r.getString(20));
 	select.setZrespon(r.getString(17));
 	select.setZestatu(r.getString(18) + " - " + r.getString(21));
 	select.setZvigenc(r.getString(19));
 	select.setZdesc1(r.getString(20));
 	select.setZdesc2(r.getString(21));
 	select.setZdesc3(r.getString(22));
 	select.setZcomp(r.getString(23)+ " - " + r.getString(25));
 	select.setZarea(r.getString(24)+ " - " + r.getString(26));
 	select.setZdesc4(r.getString(27));
 	select.setZdelete(r.getString(23)+ "" + r.getString(24)+ "" + r.getString(1));
 	select.setZcompor(r.getString(28));
 	select.setZresmet(r.getString(29));
    	
    	//Agrega la lista
    	list.add(select);
    }
    //Cierra las conecciones
    pstmt.close();
    con.close();

	} else {
		
		if(comp==null){
  			comp = " - ";
  		}
  		if(comp==""){
  			comp = " - ";
  		}        
         if(area==null){
  			area = " - ";
  		}
         
         String[] veccomp = comp.split("\\ - ", -1);
         String[] vecarea= area.split("\\ - ", -1);
         
		//Consulta paginada	
         String query = "SELECT * FROM"; 
 	    query += "(select query.*, rownum as rn from";
 		query += "(SELECT A.CODIGO, A.NOMIND, A.TIPVALM, A.META, TO_CHAR(A.FECCAM,'DD/MM/YYYY') AS FECCAM, A.TIPVALTI, A.TOLINF, TO_CHAR(A.FECCATI,'DD/MM/YYYY') AS FECCATI, A.TIPVALTS, A.TOLSUP, TO_CHAR(A.FECCATS,'DD/MM/YYYY') AS FECCATS, A.CALCULO, A.DATOS, A.PROCES, A.PERIOD, A.NIVAPP, A.DESCRI, A.ESTATU, TO_CHAR(A.VIGENC,'DD/MM/YYYY') AS VIGENC, B.DESCR AS DESC1, C.DESCR AS DESC2, D.DESCR AS DESC3, A.COMP, A.AREA, E.DESCR AS DESC4, F.DESCR AS DESC5, J.DESCR AS DESC6, A.COMPOR, A.RESMET ";
 	    query += " FROM SGC001 A, TUBDER08 B, TUBDER10 C, TUBDER09 D, SGC005 E, SGC006 F, TUBDER11 J, TUBDER11 K, TUBDER11 L, SGC009 M  ";
 	    query += " WHERE A.PERIOD = D.CODIGO";
 	    query += " AND A.NIVAPP = B.CODIGO";
 	    query += " AND A.ESTATU = C.CODIGO";
 	    query += " AND A.COMP = E.CODIGO";
 	    query += " AND A.AREA = F.CODIGO";
 	    query += " AND A.TIPVALM = J.CODIGO";
 	    query += " AND A.TIPVALTI = K.CODIGO";
 	    query += " AND A.TIPVALTS = L.CODIGO";
 		query += " AND A.COMP = M.COMP";
 		query += " AND A.AREA = M.AREA";
 		query += " AND A.CODIGO = M.INDICA";	    
 	    query += " AND TRIM(A.COMP) LIKE TRIM('%" + veccomp[0] + "%')";
 	    query += " AND TRIM(A.AREA) LIKE TRIM('%" + vecarea[0] + "%')";
 	    query += " AND TRIM(M.CODUSER) LIKE ('%" + login.toUpperCase() + "%')";
 	    query += " GROUP BY A.CODIGO, A.NOMIND, A.TIPVALM, A.META, A.FECCAM, A.TIPVALTI, A.TOLINF, A.FECCATI, A.TIPVALTS, A.TOLSUP, A.FECCATS, A.CALCULO, A.DATOS, A.PROCES, A.PERIOD, A.NIVAPP, A.DESCRI, A.ESTATU, A.VIGENC, B.DESCR, C.DESCR, D.DESCR, A.COMP, A.AREA, E.DESCR, F.DESCR, J.DESCR, A.COMPOR, A.RESMET";
 	    query += " ORDER BY A.COMP, A.AREA, A.CODIGO ";
 	    query += ")query ) " ;
 	    query += " WHERE ROWNUM <="+pageSize;
 	    query += " AND rn > ("+ first +")";
 	    query += " ORDER BY  " + sortField.replace("z", "");

    pstmt = con.prepareStatement(query);
    //System.out.println(query);
    //System.out.println("Usuario **** NO ****  ADMINISTRADOR...");
		
    r =  pstmt.executeQuery();
    while (r.next()){
 	Sgc001 select = new Sgc001();
 	select.setZcodigo(r.getString(1));
 	select.setZdesc(r.getString(1)+ " - " +  r.getString(2)) ;
 	select.setZtvalm(r.getString(3)+ " - " + r.getString(27));
 	select.setZmeta(r.getString(4));
 	select.setZfeccam(r.getString(5));
 	select.setZtvalti(r.getString(6)+ " - " + r.getString(27));
 	select.setZtolinf(r.getString(7));
 	select.setZfeccai(r.getString(8));
 	select.setZtvalts(r.getString(9)+ " - " + r.getString(27));
 	select.setZtolsup(r.getString(10));
 	select.setZfeccas(r.getString(11));
 	select.setZcalcul(r.getString(12));
 	select.setZfuente(r.getString(13));
 	select.setZproces(r.getString(14));
 	select.setZperiod(r.getString(15) + " - " + r.getString(22));
 	select.setZnivapp(r.getString(16) + " - " + r.getString(20));
 	select.setZrespon(r.getString(17));
 	select.setZestatu(r.getString(18) + " - " + r.getString(21));
 	select.setZvigenc(r.getString(19));
 	select.setZdesc1(r.getString(20));
 	select.setZdesc2(r.getString(21));
 	select.setZdesc3(r.getString(22));
 	select.setZcomp(r.getString(23)+ " - " + r.getString(25));
 	select.setZarea(r.getString(24)+ " - " + r.getString(26));
 	select.setZdesc4(r.getString(27));
 	select.setZdelete(r.getString(23)+ "" + r.getString(24)+ "" + r.getString(1));
 	select.setZcompor(r.getString(28));
 	select.setZresmet(r.getString(29));
    	
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
      		if(period==null){
      			period = " - ";
      		}
      		if(period==""){
      			period = " - ";
      		}        
             if(nivapp==null){
      			nivapp = " - ";
      		}
      		if(nivapp==""){
      			nivapp = " - ";
      		}        
             if(estatu==null){
      			estatu = " - ";
      		}
      		if(estatu==""){
      			estatu = " - ";
      		}        
             String[] vecperiod = period.split("\\ - ", -1);
             String[] vecnivapp = nivapp.split("\\ - ", -1);
             String[] vecestatu = estatu.split("\\ - ", -1);
       	    Context initContext = new InitialContext();     
      		DataSource ds = (DataSource) initContext.lookup(JNDI);

      		con = ds.getConnection();

     		//Consulta paginada
     		String query = "SELECT COUNT_SGC001('" + ((String) filterValue).toUpperCase() + "','" + vecperiod[0] + "','" + vecnivapp[0] + "','" + vecestatu[0] + "', '" + instancia + "') FROM DUAL";

           
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
  		desc = "";
  		meta = "";
  		tolsup = "";
  		tolinf = "";
  		calcul = "";
  		fuente = "";
  		proces = "";
  		period = "";
  		nivapp = "";
  		respon = "";
  		estatu = "";
  		tvalm = "";
  		tvalti = "";
  		tvalts = "";
  		compor = "";
  		resmet = "";
  		feccam = new Date();
  		feccai = new Date();
  		feccas = new Date();
  		vigenc = new Date();
  		validarOperacion = 0;
	}
    
    public void reset() {
    	// TODO Auto-generated method stub
    	comp = null;
    	area = null;
    	period = null;
  		nivapp = null;
  		estatu = null;
  		respon = null;
  		tvalm = null;
  		tvalti = null;
  		tvalts = null;
    		
    }    
    
}
