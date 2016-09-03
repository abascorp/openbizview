/*
 *  Copyright (C) 2011 - 2016  DVCONSULTORES

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
import java.io.IOException;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

	/**
	 *
	 * @author Andres
	 */
	@ManagedBean
	@ViewScoped
	public class Bvtmenu extends Bd implements Serializable {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		
		private LazyDataModel<Bvtmenu> lazyModel;  
		
		
		/**
		 * @return the lazyModel
		 */
		public LazyDataModel<Bvtmenu> getLazyModel() {
			return lazyModel;
		}	
	
	@PostConstruct	
	public void init(){
		
        if (instancia == null){instancia = "99999";}
		
		//Si no tiene acceso al módulo no puede ingresar
		if (new SeguridadMenuBean().opcmnu("M14")=="false") {
			RequestContext.getCurrentInstance().execute("PF('idleDialogNP').show()");
		}
		
		lazyModel  = new LazyDataModel<Bvtmenu>(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 7217573531435419432L;
			
            @Override
			public List<Bvtmenu> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) { 
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
	
	
	private String codopc = "";
	private String desopc = "";
	private String codvis = "";
	private String b_codrol = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("segrol");
	private int rows;
	private Object filterValue = "";
	private List<Bvtmenu> list = new ArrayList<Bvtmenu>();
	String exito = "exito";
	private String instancia = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("instancia"); //Usuario logeado


	
	/**
	 * @return the codopc
	 */
	public String getCodopc() {
		return codopc;
	}
	
	/**
	 * @param codopc the codopc to set
	 */
	public void setCodopc(String codopc) {
		this.codopc = codopc;
	}
	
	/**
	 * @return the desopc
	 */
	public String getDesopc() {
		return desopc;
	}
	
	/**
	 * @param desopc the desopc to set
	 */
	public void setDesopc(String desopc) {
		this.desopc = desopc;
	}
	
	/**
	 * @return the codvis
	 */
	public String getCodvis() {
		return codvis;
	}
	
	/**
	 * @param codvis the codvis to set
	 */
	public void setCodvis(String codvis) {
		this.codvis = codvis;
	}

	
    

	/**
	 * @return the b_codrol
	 */
	public String getB_codrol() {
		return b_codrol;
	}

	/**
	 * @param b_codrol the b_codrol to set
	 */
	public void setB_codrol(String b_codrol) {
		this.b_codrol = b_codrol;
	}

	

	/**
	 * @return the list
	 */
	public List<Bvtmenu> getList() {
		return list;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(List<Bvtmenu> list) {
		this.list = list;
	}

	/**
	 * @param rows the rows to set
	 */
	public void setRows(int rows) {
		this.rows = rows;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Variables seran utilizadas para capturar mensajes de errores de Oracle y parametros de metodos
	PntGenerica consulta = new PntGenerica();
	boolean vGacc; //Validador de opciones del menú
	private String login = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario"); //Usuario logeado
	FacesMessage msj =  null;
	
	
	//Coneccion a base de datos
	//Pool de conecciones JNDI
	//Coneccion a base de datos
	//Pool de conecciones JNDIFARM
	Connection con;
	PreparedStatement pstmt = null;
	ResultSet r;

	/**
     * Inserta acceso menu.
     * <p>
     * <b>Parametros del Metodo:<b> String b_codrol, String codopc, String desopc, String codvis.<br>
     * String pool, String login.<br><br>
     **/
    public void insertBvTMENU(String rol, String strValores, String pool, String login) throws  NamingException {
       String[] vecValores = strValores.split("\\|", -1);
       String[] veccodrol = rol.split("\\ - ", -1);
        try {
        	Context initContext = new InitialContext();     
     		DataSource ds = (DataSource) initContext.lookup(JNDI);

     		con = ds.getConnection();		
     		
            String query = "INSERT INTO Bvtmenu VALUES (?,?,?,?,?,sysdate,?,sysdate,?)";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, veccodrol[0]);
            pstmt.setString(2, vecValores[0].toUpperCase());
            pstmt.setString(3, vecValores[1]);
            pstmt.setString(4, vecValores[2].toUpperCase());
            pstmt.setString(5, login);
            pstmt.setString(6, login);
            pstmt.setInt(7, Integer.parseInt(instancia));
            System.out.println(query);
            try {
                //Avisando
                pstmt.executeUpdate();
                      
             } catch (SQLException e)  {
            	 exito = "error";
            	msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
            	FacesContext.getCurrentInstance().addMessage(null, msj);
            }
            pstmt.close();
            con.close();
            
        } catch (Exception e) {
        }
    	        
    }
    
    /**
    * Inserta acceso botones.
    * <p>
    * <b>Parametros del Metodo:<b> String b_codrol, String codopc, String desopc, String codvis.<br>
    * String pool, String login.<br><br>
     * @throws SQLException 
     * @throws ClassNotFoundException 
     * @throws IOException 
    **/
   public void insertBvTMENUOPC() throws  NamingException, ClassNotFoundException, SQLException {
	   String[] veccodrol = b_codrol.split("\\ - ", -1);
	   consulta.selectPntGenerica("select * from bvtmenu where b_codrol = '" + veccodrol[0].toUpperCase() + "'" , JNDI);
	   int registros = consulta.getRows();
	   ////System.out.println("Registros: " + registros);
	   
	   if (registros==42){
		   msj = new FacesMessage(FacesMessage.SEVERITY_WARN, getMessage("bvtmenumsj"), ""); 
		   FacesContext.getCurrentInstance().addMessage(null, msj);
		   exito="error";
	   } else {
	   //Menú estandart
	   //Básicos
	   insertBvTMENU(veccodrol[0], "M01|"+getMessage("bas").toUpperCase()+"|0", JNDI, login);
	   insertBvTMENU(veccodrol[0], "M02|"+getMessage("bas1")+"|0", JNDI, login);
	   insertBvTMENU(veccodrol[0], "M03|"+getMessage("bas2")+"|0", JNDI, login);
	   insertBvTMENU(veccodrol[0], "M04|"+getMessage("bas3")+"|0", JNDI, login);
	   insertBvTMENU(veccodrol[0], "M05|"+getMessage("bas4")+"|0", JNDI, login);
	   //Administración
       insertBvTMENU(veccodrol[0], "M06|"+getMessage("adm").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "M07|"+getMessage("adm1")+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "M08|"+getMessage("adm2")+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "M09|"+getMessage("adm3")+"|0", JNDI, login);
       //Seguridad
       insertBvTMENU(veccodrol[0], "M10|"+getMessage("seg").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "M11|"+getMessage("instancias")+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "M12|"+getMessage("seg01")+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "M13|"+getMessage("seg02")+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "M14|"+getMessage("seg03")+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "M15|"+getMessage("seg08")+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "M16|"+getMessage("seg09")+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "M17|"+getMessage("seg04")+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "M18|"+getMessage("seg05")+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "M19|"+getMessage("seg06")+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "M20|"+getMessage("seg07")+"|0", JNDI, login);
       //Tareas
       insertBvTMENU(veccodrol[0], "M21|"+getMessage("mail").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "M22|"+getMessage("mail02")+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "M23|"+getMessage("mail03")+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "M24|"+getMessage("mail04")+"|0", JNDI, login); 
       //Reportes
       insertBvTMENU(veccodrol[0], "R01|"+getMessage("rep").toUpperCase()+"|0", JNDI, login);  
       //Ventas
       insertBvTMENU(veccodrol[0], "V00|"+getMessage("globalVentas").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "V01|"+getMessage("vent").toUpperCase()+"|0", JNDI, login);
       //SGC
       insertBvTMENU(veccodrol[0], "S00|"+getMessage("globalSGC000").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "S01|"+getMessage("sgc001").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "S03|"+getMessage("sgc003").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "S04|"+getMessage("sgc004").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "S05|"+getMessage("mnusgc").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "S07|"+getMessage("seg").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "S08|"+getMessage("Hmeta").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "S09|"+getMessage("Htolinf").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "S10|"+getMessage("Htolsup").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "S11|"+getMessage("Master").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "S12|"+getMessage("Company").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "S13|"+getMessage("Area").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "S14|"+getMessage("Tipvalor").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "S15|"+getMessage("Periodicidad").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "S16|"+getMessage("sgc007").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "S17|"+getMessage("sgc008").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "S18|"+getMessage("sgc009").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "S19|"+getMessage("sgc010").toUpperCase()+"|0", JNDI, login);
       //PARAMETROS PARA REPORTES
       insertBvTMENU(veccodrol[0], "W00|"+getMessage("globalParrep").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "W01|"+getMessage("pre001").toUpperCase()+"|0", JNDI, login);
       //CONTABILIDAD Y COSTOS
       insertBvTMENU(veccodrol[0], "C00|"+getMessage("globalCoyco").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "C01|"+getMessage("mnucont").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "C02|"+getMessage("orf001").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "C03|"+getMessage("coo001").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "C04|"+getMessage("Fabrica").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "C05|"+getMessage("CalendarM").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "C06|"+getMessage("CalendarS").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "C07|"+getMessage("coo002").toUpperCase()+"|0", JNDI, login);
       //INDICADORES DIRECTIVOS
       insertBvTMENU(veccodrol[0], "I00|"+getMessage("globalInd").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "I01|"+getMessage("mnuindt").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "I02|"+getMessage("indt01").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "I03|"+getMessage("indt02").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "I04|"+getMessage("indt03").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "I05|"+getMessage("indt04").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "I06|"+getMessage("indt05").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "I07|"+getMessage("indt06").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "I08|"+getMessage("indt07").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "I09|"+getMessage("indt08").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "I10|"+getMessage("indt09").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "I11|"+getMessage("indt10").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "I12|"+getMessage("indt11").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "I13|"+getMessage("indh").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "I14|"+getMessage("indh01").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "I15|"+getMessage("indh02").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "I16|"+getMessage("indh03").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "I17|"+getMessage("indh0").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "I18|"+getMessage("indh04").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "I19|"+getMessage("indh05").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "I20|"+getMessage("indh06").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "I21|"+getMessage("indh07").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "I22|"+getMessage("indh00").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "I23|"+getMessage("indh08").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "I24|"+getMessage("indh000").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "I25|"+getMessage("indh09").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "I26|"+getMessage("indh0000").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "I27|"+getMessage("indh10").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "I28|"+getMessage("indh11").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "I29|"+getMessage("indh12").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "I30|"+getMessage("indh13").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "I31|"+getMessage("indh14").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "I32|"+getMessage("indh15").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "I33|"+getMessage("indh16").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "I34|"+getMessage("indh17").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "I35|"+getMessage("indh18").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "I36|"+getMessage("indh00000").toUpperCase()+"|0", JNDI, login);
       insertBvTMENU(veccodrol[0], "I37|"+getMessage("indh0").toUpperCase()+"|0", JNDI, login);
	   }
	   if(exito.equals("exito")){
	   msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnInsert"), "");  
	   FacesContext.getCurrentInstance().addMessage(null, msj);
	   }
       limpiarValores();
   }
	   
   
   /**
    * Borra Paises
    * <p>
    * Parametros del metodo: String codpai. Pool de conecciones
    **/
   public void delete() throws  NamingException {  
	        try {
   		Context initContext = new InitialContext();     
    		DataSource ds = (DataSource) initContext.lookup(JNDI);

    		con = ds.getConnection();
    		
    		String[] veccodrol = b_codrol.split("\\ - ", -1);
    		
    		String query = "DELETE from bvtmenu WHERE b_codrol ='" + veccodrol[0].toUpperCase() + "' and instancia = '" + instancia + "'";
           pstmt = con.prepareStatement(query);
           ////System.out.println(query);
           //Antes de insertar verifica si el rol del usuario tiene permisos para insertar
           
           try {
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
			
			FacesContext.getCurrentInstance().addMessage(null, msj); 
		}
   

   
    /**
     * Actualiza bvtmenu
     * <p> Parámetros del método: String codvis, String p_codrol, String codopc
     **/
    public void updateBvtmenu(String param, String codvis) throws  NamingException {
        String[] veccodrol = b_codrol.split("\\ - ", -1);
        ////System.out.println(cadStr);
        try {
        	Context initContext = new InitialContext();     
     		DataSource ds = (DataSource) initContext.lookup(JNDI);

     		con = ds.getConnection();
     		
            String query = "UPDATE BVTMENU SET CODVIS = '" + codvis + "'"
                    + " WHERE B_CODROL = ? AND CODOPC IN ('" + param + "') and instancia = '" + instancia + "'";
            ////System.out.println(query);
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, veccodrol[0].toUpperCase());
            try {
                //Avisando
                pstmt.executeUpdate();
                
            } catch (SQLException e) {
            	msj = new FacesMessage(FacesMessage.SEVERITY_INFO, e.getMessage(), "");
            	FacesContext.getCurrentInstance().addMessage(null, msj);
            }
            pstmt.close();
            con.close();
        } catch (Exception e) {
        }
        
    }
    
    public void update() throws  NamingException, ClassNotFoundException, SQLException { 
    	String vista;
    	HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
    	String[] paramAcc = request.getParameterValues("toAcc");
    	String[] paramDacc = request.getParameterValues("toDacc");
    	if(paramAcc!=null && paramDacc!=null){
    		msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("bvtmenuUp1"), "");
    		FacesContext.getCurrentInstance().addMessage(null, msj);
    		exito = "error";
    	} else 	if(paramAcc!=null && paramDacc==null){
    	for(int i=0;i<paramAcc.length;i++){
    		vista = "0";
    		updateBvtmenu(paramAcc[i], vista);
    		  }
    	} else if (paramAcc==null && paramDacc!=null){
    	for(int i=0;i<paramDacc.length;i++){
          vista = "1";
        	updateBvtmenu(paramDacc[i], vista);
          }	
    	}
		if (exito.equals("exito")) {
			 msj = new FacesMessage(FacesMessage.SEVERITY_INFO,  getMessage("msnUpdate"), "");
			 FacesContext.getCurrentInstance().addMessage(null, msj);
		}
		limpiarValores();
    }
    
    
    public void guardar() throws NamingException, SQLException, ClassNotFoundException{   	
    	HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
    	String[] paramAcc = request.getParameterValues("toAcc");
    	String[] paramDacc = request.getParameterValues("toDacc");
    	////System.out.println(paramAcc);
    	////System.out.println(paramDacc);
    	if(paramAcc==null && paramDacc==null){
    		////System.out.println("Inserta");
    		insertBvTMENUOPC();
    	} else {
    		////System.out.println("Update");
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
 		//Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
 		DatabaseMetaData databaseMetaData = con.getMetaData();
 		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
 		
 		
 		String query = "";
 		
 		if(b_codrol==null){
 			b_codrol = " - ";
 		}
 		if(b_codrol.equals("")){
 			b_codrol = " - ";
 		}
 		
 		String[] veccodrol = b_codrol.split("\\ - ", -1);

  		switch ( productName ) {
        case "Oracle":
        	   query += "  select * from ";
        	   query += " ( select query.*, rownum as rn from";
        	   query += " (SELECT  trim(codopc), trim(desopc), decode(codvis,'0','ACCESO','SIN ACCESO'), trim(b_codrol)";
        	   query += " FROM BVTMENU";
        	   query += " WHERE b_codrol||codopc||desopc like '%" + ((String) filterValue).toUpperCase() + "%'";
        	   query += " and b_codrol like trim('" + veccodrol[0].toUpperCase() +  "%')";
        	   query += " AND   instancia = '" + instancia + "'";
	  		   query += " order by " + sortField + ") query";
	           query += " ) where rownum <= " + pageSize ;
	           query += " and rn > (" + first + ")";
             break;
        case "PostgreSQL":
        	   query += " SELECT  trim(codopc), trim(desopc), case when codvis = '0' then 'ACCESO' else 'SIN ACCESO' end, trim(b_codrol) ";
        	   query += " FROM BVTMENU";
        	   query += " WHERE b_codrol||codopc||desopc like '%" + ((String) filterValue).toUpperCase() + "%'";
        	   query += " and b_codrol like trim('" + veccodrol[0].toUpperCase() +  "%')";
        	   query += " AND   instancia = '" + instancia + "'";
	  		   query += " order by " + sortField + "";
	           query += " LIMIT " + pageSize;
	           query += " OFFSET " + first;
             break;
        case "Microsoft SQL Server":
        	query += " SELECT * ";
        	query += " FROM (SELECT ";
        	query += " 		ROW_NUMBER() OVER (ORDER BY CODOPC ASC) AS ROW_NUM, ";
        	query += " 		CODOPC, "; 
        	query += " 		DESOPC, "; 
        	query += " 		CASE  ";
        	query += " 			WHEN CODVIS = '0' "; 
        	query += " 			THEN 'ACCESO'  ";
        	query += " 			ELSE 'SIN ACCESO'  ";
        	query += " 		END CONOPC,  ";
        	query += " 		B_CODROL ";
        	query += " 		FROM BVTMENU) TOT ";
        	query += " WHERE  ";
        	query += " TOT.B_CODROL + TOT.CODOPC + TOT.DESOPC LIKE '%" + ((String) filterValue).toUpperCase() + "%'";
        	query += " AND TOT.B_CODROL = '" + veccodrol[0].toUpperCase() +  "%)";
        	query += " AND   tot.instancia = '" + instancia + "'";
        	query += " AND TOT.ROW_NUM <= " + pageSize;
        	query += " AND TOT.ROW_NUM > " + first;
        	query += " ORDER BY " + sortField ;
          break;
          }
 		
        
        pstmt = con.prepareStatement(query);
        //System.out.println(query);
  		
        r =  pstmt.executeQuery();
        
        while (r.next()){
        	Bvtmenu select = new Bvtmenu();
     		select.setCodopc(r.getString(1));
     		select.setDesopc(r.getString(2));
     		select.setCodvis(r.getString(3));
     		select.setB_codrol(r.getString(4));
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
  	public void counter(Object filterValue ) throws SQLException, NamingException {
     try {	
    	Context initContext = new InitialContext();     
   		DataSource ds = (DataSource) initContext.lookup(JNDI);
   		con = ds.getConnection();
   	   //Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
 		DatabaseMetaData databaseMetaData = con.getMetaData();
 		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
   	      		
 		String query = "";
 		
 		if(b_codrol==null){
 			b_codrol = " - ";
 		}
 		
 		String[] veccodrol = b_codrol.split("\\ - ", -1);
  		
  		switch ( productName ) {
        case "Oracle":
        	 query = "SELECT count_bvtmenu('" + ((String) filterValue).toUpperCase() + "','" +  veccodrol[0].toUpperCase() + "','" + instancia + "') from dual";
             break;
        case "PostgreSQL":
        	 query = "SELECT count_bvtmenu('" + ((String) filterValue).toUpperCase() + "','" +  veccodrol[0].toUpperCase() + "','" + instancia + "')";
             break;
        case "Microsoft SQL Server":
       	 query = "SELECT DBO.count_bvtmenu('" + ((String) filterValue).toUpperCase() + "','" +  veccodrol[0].toUpperCase() + "','" + instancia + "')";
            break;
            }

        
        pstmt = con.prepareStatement(query);
        //System.out.println(query);

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
	
	public void limpiarValores(){
		codopc = "";
		desopc = "";
		codvis = "";
	}
	
	public void reset() {
  		b_codrol = null;     
    }



}
