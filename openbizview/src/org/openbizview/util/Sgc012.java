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
public class Sgc012 extends Bd implements Serializable {

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private LazyDataModel<Sgc012> lazyModel;  
	
	
	/**
	 * @return the lazyModel
	 */
	public LazyDataModel<Sgc012> getLazyModel() {
		return lazyModel;
	}	

@PostConstruct
public void init() {
	//System.out.println("entre al metodo INIT");
	lazyModel  = new LazyDataModel<Sgc012>(){
		/**
		 * 
		 */
		private static final long serialVersionUID = 7217573531435419432L;
		
        @Override
		public List<Sgc012> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) { 
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
	private String codigo = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("codigo"); //Usuario logeado
	public String anocal = "";
	private String mescal = "";
	private String invana = "";
    private String rrhh = "";	
    private String superv = "";
    private String medamb = "";
    private String equipo = "";
    private String infstr = "";
    private String proced = "";
    private String otros = "";
    private String foto = "";
	private Object filterValue = "";
	private List<Sgc012> list = new ArrayList<Sgc012>();
	private int validarOperacion = 0;
	private String instancia = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("instancia"); //Usuario logeado
	private String zcomp = "";
    private String zarea = "";
	private String zcodigo = "";
	private String zanocal = "";
	private String zmescal = "";
	private String zinvana = "";
    private String zrrhh = "";	
    private String zsuperv = "";
    private String zmedamb = "";
    private String zequipo = "";
    private String zinfstr = "";
    private String zproced = "";
    private String zotros = "";
	private String zdesc1 = "";
	private String zdesc2 = "";
	private String zdesc3 = "";
	private String zcoddel = "";
	private String zorderby = "";
	private String zusrcre = "";
	private String zfeccre = "";

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public String getInvana() {
		return invana;
	}

	public void setInvana(String invana) {
		this.invana = invana;
	}

	public String getRrhh() {
		return rrhh;
	}

	public void setRrhh(String rrhh) {
		this.rrhh = rrhh;
	}

	public String getSuperv() {
		return superv;
	}

	public void setSuperv(String superv) {
		this.superv = superv;
	}

	public String getMedamb() {
		return medamb;
	}

	public void setMedamb(String medamb) {
		this.medamb = medamb;
	}	
	
	public String getEquipo() {
		return equipo;
	}

	public void setEquipo(String equipo) {
		this.equipo = equipo;
	}

	public String getInfstr() {
		return infstr;
	}

	public void setInfstr(String infstr) {
		this.infstr = infstr;
	}

	public String getProced() {
		return proced;
	}

	public void setProced(String proced) {
		this.proced = proced;
	}

	public String getOtros() {
		return otros;
	}

	public void setOtros(String otros) {
		this.otros = otros;
	}

	public String getZinvana() {
		return zinvana;
	}

	public void setZinvana(String zinvana) {
		this.zinvana = zinvana;
	}

	public String getZrrhh() {
		return zrrhh;
	}

	public void setZrrhh(String zrrhh) {
		this.zrrhh = zrrhh;
	}

	public String getZsuperv() {
		return zsuperv;
	}

	public void setZsuperv(String zsuperv) {
		this.zsuperv = zsuperv;
	}

	public String getZmedamb() {
		return zmedamb;
	}

	public void setZmedamb(String zmedamb) {
		this.zmedamb = zmedamb;
	}
	
	public String getZequipo() {
		return zequipo;
	}

	public void setZequipo(String zequipo) {
		this.zequipo = zequipo;
	}

	public String getZinfstr() {
		return zinfstr;
	}

	public void setZinfstr(String zinfstr) {
		this.zinfstr = zinfstr;
	}

	public String getZproced() {
		return zproced;
	}

	public void setZproced(String zproced) {
		this.zproced = zproced;
	}

	public String getZotros() {
		return zotros;
	}

	public void setZotros(String zotros) {
		this.zotros = zotros;
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
	//foto = FileUploadControllersgc.archivo;
	//System.out.println(foto);
	//FileUploadControllersgc tfile = new FileUploadControllersgc();
    //System.out.println(FileUploadControllersgc.archivo);  
		//File file = new File(foto);
		//String simpleFileName = file.getName();
		//System.out.println(simpleFileName);
		//String ruta = "C:\\Users\\Consultor3\\Desktop\\Consultor3MJAD\\"+simpleFileName; // ruta de prueba
		//String ruta = "C:\\"+simpleFileName; // ruta de prueba
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

        String query = "INSERT INTO SGC012 VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,SYSDATE,?,SYSDATE,?)";
        pstmt = con.prepareStatement(query);
        pstmt.setString(1, veccomp[0].toUpperCase());
        pstmt.setString(2, vecarea[0].toUpperCase());
        pstmt.setString(3, veccod[0].toUpperCase());
        pstmt.setInt(4,Integer.parseInt(anocal));
        //pstmt.setString(4, anocal.toUpperCase());
        pstmt.setInt(5,Integer.parseInt(mescal));
        //pstmt.setString(5, mescal.toUpperCase());
        pstmt.setString(6, invana.toUpperCase());
        pstmt.setString(7, rrhh.toUpperCase());
        pstmt.setString(8, superv.toUpperCase());
        pstmt.setString(9, medamb.toUpperCase());
        pstmt.setString(10, equipo.toUpperCase());
        pstmt.setString(11, infstr.toUpperCase());
        pstmt.setString(12, proced.toUpperCase());
        pstmt.setString(13, otros.toUpperCase());
        pstmt.setString(14, login);
        pstmt.setString(15, login);            
        pstmt.setInt(16, Integer.parseInt(instancia));
        
        //System.out.println(query);
        
        /*System.out.println("INSERT INTO SGC012 VALUES ('" + veccomp[0] + "','" + 
        													vecarea[0] + "','" + 
        													veccod[0] + "'," + 
        													anocal + "," + 
        													mescal + ",'" + 
        													invana + "','" + 
        													rrhh + "','" + 
        													superv + "','" + 
        													medamb + "','" + 
        													equipo + "','" + 
        													infstr + "','" + 
        													proced + "','" + 
        													otros + "','" + 
        													login + "','" + 
        													getFecha() + "','" + 
        													login + "','" + 
        													getFecha() + "'," + 
        													instancia + ")");
        													
        													*/
        
        //System.out.println(veccomp[0]);
        //System.out.println(vecarea[0]);
        //System.out.println(veccod[0]);
        //System.out.println(anocal);
        //System.out.println(mescal);
        //System.out.println(invana);
        //System.out.println(rrhh);
        //System.out.println(superv);
        //System.out.println(equipo);
        //System.out.println(infstr);
        //System.out.println(proced);
        //System.out.println(otros);
     
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

        	String query = "DELETE from SGC012 WHERE COMP || AREA || CODIGO || ANOCAL || MESCAL in (" + param + ") and INSTANCIA = " + instancia + "";
        		        	
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
  		
        String query = "UPDATE SGC012";
         query += " SET INVANA = ?, ";
         query += " RRHH = ?, ";
         query += " SUPERV = ?, ";
         query += " MEDAMB = ?, ";
         query += " EQUIPO = ?, ";
         query += " INFSTR = ?, ";
         query += " PROCED = ?, ";
         query += " OTROS = ?, ";
         query += " USRACT = ?,";
         query += " FECACT = '" + getFecha() + "'";
         query += " WHERE COMP = ? ";
         query += " AND AREA = ? ";
         query += " AND CODIGO = ? ";
         query += " AND ANOCAL = ? ";
         query += " AND MESCAL = ? ";
         query += " AND INSTANCIA = " + instancia + "";

        pstmt = con.prepareStatement(query);
        pstmt.setString(1, invana.toUpperCase());
        pstmt.setString(2, rrhh.toUpperCase());
        pstmt.setString(3, superv.toUpperCase());
        pstmt.setString(4, medamb.toUpperCase());
        pstmt.setString(5, equipo.toUpperCase());
        pstmt.setString(6, infstr.toUpperCase());
        pstmt.setString(7, proced.toUpperCase());
        pstmt.setString(8, otros.toUpperCase());
        pstmt.setString(9, login);
        pstmt.setString(10, veccomp[0].toUpperCase());
        pstmt.setString(11, vecarea[0].toUpperCase());
        pstmt.setString(12, veccod[0].toUpperCase());
        pstmt.setInt(13,Integer.parseInt(anocal));
        pstmt.setInt(14,Integer.parseInt(mescal));;
        
        //System.out.println(query);
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
  		
		//System.out.println("entre al metodo SELECT");	
		Context initContext = new InitialContext();     
		DataSource ds = (DataSource) initContext.lookup(JNDI);
		con = ds.getConnection();		
		//Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
		DatabaseMetaData databaseMetaData = con.getMetaData();
		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
		
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
		query += "(SELECT A.COMP, A.AREA, A.CODIGO, A.ANOCAL, A.MESCAL, A.INVANA, A.RRHH, A.SUPERV, A.MEDAMB, A.EQUIPO, A.INFSTR, A.PROCED, A.OTROS, A.USRCRE, TO_CHAR(A.FECCRE,'DD/MM/YYYY') AS FECCRE, B.NOMIND AS DESC1, C.DESCR AS DESC2, D.DESCR AS DESC3 ";
	    query += " FROM SGC012 A, SGC001 B, SGC005 C, SGC006 D ";
	    query += " WHERE A.CODIGO = B.CODIGO ";
	    query += " AND A.COMP = C.CODIGO ";
	    query += " AND A.AREA = D.CODIGO ";
	    query += " AND C.CODIGO = D.COMP ";
	    query += " AND TRIM(A.COMP) LIKE TRIM('%" + veccomp[0] + "%')";
	    query += " AND TRIM(A.AREA) LIKE TRIM('%" + vecarea[0] + "%')";
	    query += " AND TRIM(A.CODIGO) LIKE TRIM('%" + veccod[0] + "%')";
	    query += " GROUP BY A.COMP, A.AREA, A.CODIGO, A.ANOCAL, A.MESCAL, A.INVANA, A.RRHH, A.SUPERV, A.MEDAMB, A.EQUIPO, A.INFSTR, A.PROCED, A.OTROS, A.USRCRE,A.FECCRE, B.NOMIND, C.DESCR, D.DESCR";
	    query += ")query ) " ;
	    query += " WHERE ROWNUM <="+pageSize;
	    query += " AND rn > ("+ first +")";
	    query += " ORDER BY COMP, AREA, CODIGO, ANOCAL, MESCAL" + sortField.replace("z", "");

    pstmt = con.prepareStatement(query);
    //System.out.println(query);
		
    r =  pstmt.executeQuery();
    
    while (r.next()){
 	Sgc012 select = new Sgc012();
 	select.setZcomp(r.getString(1) + " - " + r.getString(17));
 	select.setZarea(r.getString(2) + " - " + r.getString(18));
 	select.setZcodigo(r.getString(3) + " - " + r.getString(16));
 	select.setZanocal(r.getString(4));
 	select.setZmescal(r.getString(5));
 	select.setZinvana(r.getString(6));
 	select.setZrrhh(r.getString(7));
 	select.setZsuperv(r.getString(8));
 	select.setZmedamb(r.getString(9));
 	select.setZequipo(r.getString(10));
 	select.setZinfstr(r.getString(11));
 	select.setZproced(r.getString(12));
 	select.setZotros(r.getString(13));
 	select.setZcoddel(r.getString(1) + "" + r.getString(2) + "" + r.getString(3) + "" + r.getString(4) + "" + r.getString(5));
 	select.setZorderby(r.getString(1) + ", " + r.getString(2) + ", " + r.getString(3) + ", " + r.getString(4) + ", " + r.getString(5));
 	select.setZusrcre(r.getString(14));
 	select.setZfeccre(r.getString(15));
   	
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
     		String query = "SELECT COUNT_SGC012('" + ((String) filterValue).toUpperCase() + "','" + veccomp[0] + "','" + vecarea[0] + "','" + veccod[0] + "', '" + instancia + "') FROM DUAL";

           
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
  		invana = "";
  		rrhh = "";
  		superv = "";
  		medamb = "";
  		equipo = "";
  		infstr = "";
  		proced = "";
  		otros = "";
  		foto = "";
  		validarOperacion = 0;
	}
  	
	public void reset() {
		// TODO Auto-generated method stub
  		comp = null;
  		area = null;
  		codigo = null;
	}
       
}
