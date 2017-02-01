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
public class Sgc006 extends Bd implements Serializable {

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private LazyDataModel<Sgc006> lazyModel;  
	
	
	/**
	 * @return the lazyModel
	 */
	public LazyDataModel<Sgc006> getLazyModel() {
		return lazyModel;
	}	

@PostConstruct
public void init() {
	//System.out.println("entre al metodo INIT");
	lazyModel  = new LazyDataModel<Sgc006>(){
		/**
		 * 
		 */
		private static final long serialVersionUID = 7217573531435419432L;
		
        @Override
		public List<Sgc006> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) { 
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
	private String codigo = "";
	private String desc = "";
	private String ccosto = "";
	//private String mail1 = "";
	//private String mail2 = "";
	//private String mail3 = "";
	//private String mail4 = "";
	//private String mail5 = "";
	private Object filterValue = "";
	private List<Sgc006> list = new ArrayList<Sgc006>();
	private int validarOperacion = 0;
	private String instancia = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("instancia"); //Usuario logeado
	private String zcomp = "";
	private String zcodigo = "";
	private String zdesc = "";
	private String zccosto = "";
	private String zdesc1 = "";
	private String zcompa = "";
	//private String zmail1 = "";
	//private String zmail2 = "";
	//private String zmail3 = "";
	//private String zmail4 = "";
	//private String zmail5 = "";

	public String getCcosto() {
		return ccosto;
	}

	public void setCcosto(String ccosto) {
		this.ccosto = ccosto;
	}

	public String getZccosto() {
		return zccosto;
	}

	public void setZccosto(String zccosto) {
		this.zccosto = zccosto;
	}

/*	public String getMail1() {
		return mail1;
	}

	public void setMail1(String mail1) {
		this.mail1 = mail1;
	}

	public String getMail2() {
		return mail2;
	}

	public void setMail2(String mail2) {
		this.mail2 = mail2;
	}

	public String getMail3() {
		return mail3;
	}

	public void setMail3(String mail3) {
		this.mail3 = mail3;
	}

	public String getMail4() {
		return mail4;
	}

	public void setMail4(String mail4) {
		this.mail4 = mail4;
	}

	public String getMail5() {
		return mail5;
	}

	public void setMail5(String mail5) {
		this.mail5 = mail5;
	}

	public String getZmail1() {
		return zmail1;
	}

	public void setZmail1(String zmail1) {
		this.zmail1 = zmail1;
	}

	public String getZmail2() {
		return zmail2;
	}

	public void setZmail2(String zmail2) {
		this.zmail2 = zmail2;
	}

	public String getZmail3() {
		return zmail3;
	}

	public void setZmail3(String zmail3) {
		this.zmail3 = zmail3;
	}

	public String getZmail4() {
		return zmail4;
	}

	public void setZmail4(String zmail4) {
		this.zmail4 = zmail4;
	}

	public String getZmail5() {
		return zmail5;
	}

	public void setZmail5(String zmail5) {
		this.zmail5 = zmail5;
	}

*/
	public String getZcompa() {
		return zcompa;
	}

	public void setZcompa(String zcompa) {
		this.zcompa = zcompa;
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

	public String getZdesc1() {
		return zdesc1;
	}

	public void setZdesc1(String zdesc1) {
		this.zdesc1 = zdesc1;
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
        
        if(comp==null){
 			comp= " - ";
 		}
 		if(comp==""){
 			comp = " - ";
 		}        
        String[] veccomp = comp.split("\\ - ", -1);
        
        String query = "INSERT INTO SGC006 VALUES (?,?,?,?,NULL,NULL,NULL,NULL,NULL,?,'" + getFecha() + "',?,'" + getFecha() + "',?)";
        pstmt = con.prepareStatement(query);
        pstmt.setString(1, veccomp[0].toUpperCase());
        pstmt.setString(2, codigo.toUpperCase());
        pstmt.setString(3, desc.toUpperCase());
        pstmt.setString(4, ccosto.toUpperCase());
        //pstmt.setString(5, mail1.toUpperCase());
        //pstmt.setString(6, mail2.toUpperCase());
        //pstmt.setString(7, mail3.toUpperCase());
        //pstmt.setString(8, mail4.toUpperCase());
        //pstmt.setString(9, mail5.toUpperCase());
        pstmt.setString(5, login);
        pstmt.setString(6, login);            
        pstmt.setInt(7, Integer.parseInt(instancia));
        
        //System.out.println(query);
        //System.out.println(veccomp[0]);
      //System.out.println(codigo);
        //System.out.println(desc);
     
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

        	String query = "DELETE from SGC006 WHERE COMP || CODIGO in (" + param + ") and INSTANCIA = " + instancia + "";
        		        	
            pstmt = con.prepareStatement(query);
            //System.out.println(query);

            try {
                //Avisando
                pstmt.executeUpdate();
                msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnDelete"), "");
                limpiarValores();	
            } catch (SQLException e) {
                e.printStackTrace();
                
                if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC001D_FK2) violated - child record found")){
                	
                	//System.out.println("se cumple la condicion y muestro el msg");
                	msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("sgc001dfk1"), "");
                }
                
                else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC001_FK2) violated - child record found")){
                   	
                   	//System.out.println("se cumple la condicion y muestro el msg");
                   	msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("sgc001fk3"), "");
                   }
                   
                else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC001A_FK2) violated - child record found")){
                      	
                      	//System.out.println("se cumple la condicion y muestro el msg");
                      	msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("sgc001afk4"), "");
                   }
                   
                else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC001B_FK2) violated - child record found")){
                      	
                      	//System.out.println("se cumple la condicion y muestro el msg");
                      	msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("sgc001bfk4"), "");
                   }
                      
                else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC001C_FK2) violated - child record found")){
                         	
                         //System.out.println("se cumple la condicion y muestro el msg");
                        msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("sgc001cfk4"), "");
                   }
                   
                else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC012_FK2) violated - child record found")){
                      	
                      	//System.out.println("se cumple la condicion y muestro el msg");
                      	msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("sgc012fk3"), "");
                   }
                      
                else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC012A_FK2) violated - child record found")){
                         	
                         //System.out.println("se cumple la condicion y muestro el msg");
                        msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("sgc012afk3"), "");
                   }
                      
                else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC012B_FK2) violated - child record found")){
                         	
                        //System.out.println("se cumple la condicion y muestro el msg");
                       	msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("sgc012bfk3"), "");
                   }
                         
                else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC012C_FK2) violated - child record found")){
                            	
                     	//System.out.println("se cumple la condicion y muestro el msg");
                        msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("sgc012cfk3"), "");
                   }
                   
                else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC003_FK2) violated - child record found")){
                    	
                       //System.out.println("se cumple la condicion y muestro el msg");
                      	msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("sgc003fk3"), "");
                  }
                                          
                else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC008_FK3) violated - child record found")){
                    	
                    	//System.out.println("se cumple la condicion y muestro el msg");
                    	msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("sgc008fk2"), "");
                 }
                    
                else if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC009_FK3) violated - child record found")){
                       	
                       //System.out.println("se cumple la condicion y muestro el msg");
                      msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("sgc009fk2"), "");
                 }
                
                else {
                	
                	//System.out.println("no se cumple la condicion y muestro otro msg");
                	msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");       	
                }
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
        String[] veccomp = comp.split("\\ - ", -1);
  		
        String query = "UPDATE SGC006";
         query += " SET DESCR = ?, ";
         query += " CCOSTO = ?, ";
         //query += " MAIL1 = ?, ";
         //query += " MAIL2 = ?, ";
         //query += " MAIL3 = ?, ";
         //query += " MAIL4 = ?, ";
         //query += " MAIL5 = ?, ";
         query += " USRACT = ?,";
         query += " FECACT = '" + getFecha() + "'";
         query += " WHERE CODIGO = ? ";
         query += " AND COMP = ? ";
         query += " AND INSTANCIA = " + instancia + "";

        pstmt = con.prepareStatement(query);
        pstmt.setString(1, desc.toUpperCase());
        pstmt.setString(2, ccosto.toUpperCase());
        //pstmt.setString(3, mail1.toUpperCase());
        //pstmt.setString(4, mail2.toUpperCase());
        //pstmt.setString(5, mail3.toUpperCase());
        //pstmt.setString(6, mail4.toUpperCase());
        //pstmt.setString(7, mail5.toUpperCase());
        pstmt.setString(3, login);
        pstmt.setString(4, codigo.toUpperCase());   
        pstmt.setString(5, veccomp[0].toUpperCase()); 

        //System.out.println(query);
        //System.out.println(codigo);
        //System.out.println(desc);
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
		
		if(comp==null){
 			comp= " - ";
 		}
 		if(comp==""){
 			comp = " - ";
 		}        
        String[] veccomp = comp.split("\\ - ", -1);
		
		//Consulta paginada
     String query = "SELECT * FROM"; 
	    query += "(select query.*, rownum as rn from";
		query += "(SELECT A.COMP, A.CODIGO, A.DESCR, B.DESCR AS DESC1, A.MAIL1, A.MAIL2, A.MAIL3,A.MAIL4, A.MAIL5, A.CCOSTO ";
	    query += " FROM SGC006 A, SGC005 B ";
	    query += " WHERE A.COMP = B.CODIGO ";
	    query += " AND TRIM(A.COMP) LIKE TRIM('%" + veccomp[0] + "%')";
	    query += " GROUP BY A.COMP, A.CODIGO, A.DESCR, B.DESCR, A.MAIL1, A.MAIL2, A.MAIL3, A.MAIL4, A.MAIL5, A.CCOSTO";
	    query += ")query ) " ;
	    query += " WHERE ROWNUM <="+pageSize;
	    query += " AND rn > ("+ first +")";
	    query += " ORDER BY 2";

    pstmt = con.prepareStatement(query);
    //System.out.println(query);
		
    r =  pstmt.executeQuery();
    
    while (r.next()){
 	Sgc006 select = new Sgc006();
 	select.setZcomp(r.getString(1) + " - " + r.getString(4));
 	select.setZcodigo(r.getString(2));
 	select.setZdesc(r.getString(3));
 	select.setZdesc1(r.getString(4));
 	select.setZcompa(r.getString(1));
 	//select.setZmail1(r.getString(5));
 	//select.setZmail2(r.getString(6));
 	//select.setZmail3(r.getString(7));
 	//select.setZmail4(r.getString(8));
 	//select.setZmail5(r.getString(9));
 	select.setZccosto(r.getString(10));
   	
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
            String[] veccomp = comp.split("\\ - ", -1);

     		//Consulta paginada
     		String query = "SELECT COUNT_SGC006('" + ((String) filterValue).toUpperCase() + "','" + veccomp[0] + "','" + instancia + "') FROM DUAL";

           
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
  		codigo = "";
  		desc = "";
  		ccosto = "";
  		//mail1 = "";
  		//mail2 = "";
  		//mail3 = "";
  		//mail4 = "";
  		//mail5 = "";
  		validarOperacion = 0;
	}
  	
	public void reset() {
		// TODO Auto-generated method stub
  		comp = null;
	}
       
}
