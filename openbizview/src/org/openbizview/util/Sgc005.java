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
public class Sgc005 extends Bd implements Serializable {

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private LazyDataModel<Sgc005> lazyModel;  
	
	
	/**
	 * @return the lazyModel
	 */
	public LazyDataModel<Sgc005> getLazyModel() {
		return lazyModel;
	}	

@PostConstruct
public void init() {
	//System.out.println("entre al metodo INIT");
	lazyModel  = new LazyDataModel<Sgc005>(){
		/**
		 * 
		 */
		private static final long serialVersionUID = 7217573531435419432L;
		
        @Override
		public List<Sgc005> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) { 
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
	private String codigo = "";
	private String desc = "";
	private Object filterValue = "";
	private List<Sgc005> list = new ArrayList<Sgc005>();
	private int validarOperacion = 0;
	private String instancia = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("instancia"); //Usuario logeado
	private String zcodigo = "";
	private String zdesc = "";

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
                    
        String query = "INSERT INTO SGC005 VALUES (?,?,?,'" + getFecha() + "',?,'" + getFecha() + "',?)";
        pstmt = con.prepareStatement(query);
        pstmt.setString(1, codigo.toUpperCase());
        pstmt.setString(2, desc.toUpperCase());
        pstmt.setString(3, login);
        pstmt.setString(4, login);            
        pstmt.setInt(5, Integer.parseInt(instancia));
        
        //System.out.println(query);
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

        	String query = "DELETE from SGC005 WHERE CODIGO in (" + param + ") and INSTANCIA = " + instancia + "";
        		        	
            pstmt = con.prepareStatement(query);
            //System.out.println(query);

            try {
                //Avisando
                pstmt.executeUpdate();
                msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnDelete"), "");
                limpiarValores();	
            } catch (SQLException e) {
                e.printStackTrace();
                
                if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC001D_FK1) violated - child record found")){
                	
                	//System.out.println("se cumple la condicion y muestro el msg");
                	msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("sgc001dfk1"), "");
                }
                
                if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC001_FK1) violated - child record found")){
                   	
                   	//System.out.println("se cumple la condicion y muestro el msg");
                   	msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("sgc001fk3"), "");
                   }
                   
                   if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC001A_FK1) violated - child record found")){
                      	
                      	//System.out.println("se cumple la condicion y muestro el msg");
                      	msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("sgc001afk4"), "");
                   }
                   
                   if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC001B_FK1) violated - child record found")){
                      	
                      	//System.out.println("se cumple la condicion y muestro el msg");
                      	msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("sgc001bfk4"), "");
                   }
                      
                   if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC001C_FK1) violated - child record found")){
                         	
                         //System.out.println("se cumple la condicion y muestro el msg");
                        msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("sgc001cfk4"), "");
                   }
                      
                   if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC003_FK1) violated - child record found")){
                         	
                        //System.out.println("se cumple la condicion y muestro el msg");
                       	msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("sgc003fk3"), "");
                   }
                         
                   if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC007_FK1) violated - child record found")){
                            	
                     	//System.out.println("se cumple la condicion y muestro el msg");
                        msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("sgc007fk1"), "");
                   }
                   
                   if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC008_FK2) violated - child record found")){
                     	
                     	//System.out.println("se cumple la condicion y muestro el msg");
                     	msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("sgc008fk2"), "");
                  }
                     
                  if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC009_FK2) violated - child record found")){
                        	
                        //System.out.println("se cumple la condicion y muestro el msg");
                       msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("sgc009fk2"), "");
                  }
                     
                  if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC012_FK1) violated - child record found")){
                   	
                   	//System.out.println("se cumple la condicion y muestro el msg");
                   	msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("sgc012fk3"), "");
                }
                   
                if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC012A_FK1) violated - child record found")){
                      	
                      //System.out.println("se cumple la condicion y muestro el msg");
                     msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("sgc012afk3"), "");
                }
                   
                if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC012B_FK1) violated - child record found")){
                      	
                     //System.out.println("se cumple la condicion y muestro el msg");
                    	msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("sgc012bfk3"), "");
                }
                      
                if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.SGC012C_FK1) violated - child record found")){
                         	
                  	//System.out.println("se cumple la condicion y muestro el msg");
                     msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("sgc012cfk3"), "");
                }
                
                if (e.getMessage().trim().equals("ORA-02292: integrity constraint (OPENBIZVIEW.FK_SGC005) violated - child record found")){
                 	
                  	//System.out.println("se cumple la condicion y muestro el msg");
                     msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("sgc005fk"), "");
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
  		
        String query = "UPDATE SGC005";
         query += " SET DESCR = ?, ";
         query += " USRACT = ?,";
         query += " FECACT = '" + getFecha() + "'";
         query += " WHERE CODIGO = ? AND INSTANCIA = " + instancia + "";

        pstmt = con.prepareStatement(query);
        pstmt.setString(1, desc.toUpperCase());
        pstmt.setString(2, login);
        pstmt.setString(3, codigo.toUpperCase());           

        //System.out.println(query);
        //System.out.println(codigo);
        //System.out.println(desc);
        
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
		
		//Consulta paginada
     String query = "SELECT * FROM"; 
	    query += "(select query.*, rownum as rn from";
		query += "(SELECT A.CODIGO, A.DESCR ";
	    query += " FROM SGC005 A";
	    query += " GROUP BY A.CODIGO, A.DESCR";
	    query += ")query ) " ;
	    query += " WHERE ROWNUM <="+pageSize;
	    query += " AND rn > ("+ first +")";
	    query += " ORDER BY  " + sortField.replace("z", "");

    pstmt = con.prepareStatement(query);
    //System.out.println(query);
		
    r =  pstmt.executeQuery();
    
    while (r.next()){
 	Sgc005 select = new Sgc005();
 	select.setZcodigo(r.getString(1));
 	select.setZdesc(r.getString(2));
   	
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

     		//Consulta paginada
     		String query = "SELECT COUNT_SGC005('" + ((String) filterValue).toUpperCase() + "','" + instancia + "') FROM DUAL";

           
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
  		desc = "";
  		validarOperacion = 0;
	}
       
}
