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

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.primefaces.context.RequestContext;

import org.versiones.util.*;


@ManagedBean
@ViewScoped
public class Db extends Bd implements Serializable  {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void init() {
		if (instancia == null){instancia = "99999";}
		if (login==null) {
			//rq.getCurrentInstance().execute("PF('yourdialogid').show()");
			RequestContext.getCurrentInstance().execute("PF('idleDialog').show()");
		} else {
      // ..
		String[][] vlini = null;
	 //Lee cuantos reportes impresos
	   try {
		q1 = "select count(*) from bvt001 where instancia =  '" + instancia + "' and codrep IN (SELECT B_CODREP FROM BVT007 WHERE B_CODROL IN (SELECT B_CODROL FROM BVT002 WHERE CODUSER = '" + login + "' UNION ALL SELECT B_CODrol FROM BVT008 WHERE CODUSER = '" + login + "'))";
		consulta.selectPntGenerica(q1, JNDI);	
		r1 = consulta.getRows();
		if (r1>0){
			vlini = consulta.getArray();
			r1 = Integer.parseInt(vlini[0][0]);
		}
		//Lee cuantas tareas
		q2 = "select count(*) from t_programacion where instancia =  '" + instancia + "'";
		consulta.selectPntGenerica(q2, JNDI);
		r2 = consulta.getRows();
		if (r2>0){
			vlini = consulta.getArray();
			r2 = Integer.parseInt(vlini[0][0]);
		}
		//Lee cuantos usuarios
		q3 = "select count(*) from bvt002 where instancia =  '" + instancia + "'";
		consulta.selectPntGenerica(q3, JNDI);
		r3 = consulta.getRows();
		if (r3>0){
			vlini = consulta.getArray();
			r3 = Integer.parseInt(vlini[0][0]);
		}
		//Lee cuantos reportes impresos
		q4 = "select count(*) from bvt006 where instancia =  '" + instancia + "'";
		consulta.selectPntGenerica(q4, JNDI);
		r4 = consulta.getRows();
		if (r4>0){
			vlini = consulta.getArray();
			r4 = Integer.parseInt(vlini[0][0]);
		}

		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	   toprep();
		}
	   //System.out.println("instancia: " + instancia);
    }
	
	private String instancia = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("instancia");
	private String login = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("login"); //Usuario logeado
	//private String rol = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("rol"); //Usuario logeado
	private List<Db> list = new ArrayList<Db>();
	
	//Coneccion a base de datos
	//Pool de conecciones JNDI
	//Coneccion a base de datos
	//Pool de conecciones JNDIFARM
	Connection con;
	PreparedStatement pstmt = null;
	PreparedStatement pstmt1 = null;
	ResultSet r;
	
	

	//Querys
	private String q1;
	private String q2;
	private String q3;
	private String q4;
	//Chart
	private String chart1;//Query oracle
	private String chart2;//Query postgres
	private String chart3;//Query sqlserver
    protected int rowschart;
    protected String[][] vlTabla;
	//Registros
	private int r1 = 0;
	private int r2 = 0;
	private int r3 = 0;
	private int r4 = 0;
	PntGenerica consulta = new PntGenerica();
	ControlVersionApp controlv = new ControlVersionApp();
	
	/**
	 * @return the r1
	 */
	public int getR1() {
		return r1;
	}
	/**
	 * @param r1 the r1 to set
	 */
	public void setR1(int r1) {
		this.r1 = r1;
	}
	/**
	 * @return the r2
	 */
	public int getR2() {
		return r2;
	}
	/**
	 * @param r2 the r2 to set
	 */
	public void setR2(int r2) {
		this.r2 = r2;
	}
	/**
	 * @return the r3
	 */
	public int getR3() {
		return r3;
	}
	/**
	 * @param r3 the r3 to set
	 */
	public void setR3(int r3) {
		this.r3 = r3;
	}
	/**
	 * @return the r4
	 */
	public int getR4() {
		return r4;
	}
	/**
	 * @param r4 the r4 to set
	 */
	public void setR4(int r4) {
		this.r4 = r4;
	}
	
	
	/**
	 * @return the instancia
	 */
	public String getInstancia() {
		return instancia;
	}
	/**
	 * @param instancia the instancia to set
	 */
	public void setInstancia(String instancia) {
		this.instancia = instancia;
	}

	
	
	/**
	 * @return the list
	 */
	public List<Db> getList() {
		return list;
	}
	/**
	 * @param list the list to set
	 */
	public void setList(List<Db> list) {
		this.list = list;
	}
	
	
	/**
	 * Tope de los reportes impresos para graficar
	 */
	public void toprep(){

		chart1 = "select * from ( select query.*, rownum as rn from ( select count(b_codrep), b_codrep from bvt006 where instancia = '" + instancia + "' group by b_codrep order by 1 desc) query ) where rownum <= 5 and rn > (0) ";
		chart2 = "select count(b_codrep), b_codrep from bvt006 where instancia = '" + instancia + "' group by b_codrep order by 1 desc LIMIT 5 OFFSET 0";
		chart3 = "SELECT ROW_NUMBER() OVER (ORDER BY B_CODREP ASC) AS ROW_NUM, B_CODREP FROM BVT006 where instancia = '" + instancia + "' HAVING ROW_NUMBER() OVER (ORDER BY B_CODREP ASC) <= 5 AND ROW_NUMBER() OVER (ORDER BY B_CODREP ASC) > 0 ORDER BY 1 DESC";
		try {
			consulta.selectPntGenericaMDB(chart1, chart2, chart3, JNDI);		
			rowschart = consulta.getRows();
			vlTabla = consulta.getArray();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * Retorna el primer reporte impreso
	 */
	public String get1Rep(){
		String rep = "";
		if(rowschart<1){
			rep = "0";
		} else if(rowschart>0){
			rep = vlTabla[0][0];
		}
		return rep;
	}
	
	/**
	 * Retorna el segundo reporte impreso
	 */
	public String get2Rep(){
		String rep = "";
		if(rowschart<2){
			rep = "0";
		} else if(rowschart>0){
			rep = vlTabla[1][0];
		}
		return rep;
	}
	
	/**
	 * Retorna el tercer reporte impreso
	 */
	public String get3Rep(){
		String rep = "";
		if(rowschart<3){
			rep = "0";
		} else if(rowschart>0){
			rep = vlTabla[2][0];
		}
		return rep;
	}
	
	/**
	 * Retorna el cuarto reporte impreso
	 */
	public String get4Rep(){
		String rep = "";
		if(rowschart<4){
			rep = "0";
		} else if(rowschart>0){
			rep = vlTabla[3][0];
		}
		return rep;
	}
	
	/**
	 * Retorna el quinto reporte impreso
	 */
	public String get5Rep(){
		String rep = "";
		if(rowschart<5){
			rep = "0";
		} else if(rowschart>0){
			rep = vlTabla[4][0];
		}
		return rep;
	}
	
	//Descripciones de los reportes
	
	
	/**
	 * Retorna el primer reporte impreso
	 */
	public String get1RepDesc(){
		String rep = "";
		if(rowschart<1){
			rep = "N/A";
		} else if(rowschart>0){
			rep = vlTabla[0][1];
		}
		return rep;
	}
	
	
	/**
	 * Retorna el segundo reporte impreso
	 */
	public String get2RepDesc(){
		String rep = "";
		if(rowschart<2){
			rep = "N/A";
		} else if(rowschart>0){
			rep = vlTabla[1][1];
		}
		return rep;
	}
	
	
	/**
	 * Retorna el tercer reporte impreso
	 */
	public String get3RepDesc(){
		String rep = "";
		if(rowschart<3){
			rep = "N/A";
		} else if(rowschart>0){
			rep = vlTabla[2][1];
		}
		return rep;
	}
	
	
	/**
	 * Retorna el cuarto reporte impreso
	 */
	public String get4RepDesc(){
		String rep = "";
		if(rowschart<4){
			rep = "N/A";
		} else if(rowschart>0){
			rep = vlTabla[3][1];
		}
		return rep;
	}
	
	
	/**
	 * Retorna el quinto reporte impreso
	 */
	public String get5RepDesc(){
		String rep = "";
		if(rowschart<5){
			rep = "N/A";
		} else if(rowschart>0){
			rep = vlTabla[4][1];
		}
		return rep;
	}
	
	
	
	/**
	 * Listar instancias al momento del login
	 * si el usuario no tiene alguna instancia predefinida
	 * muestra el modal para seleccionar la instancia,
	 * lee de las instancias asociadas al usuario
	 */
     public List<String> select() throws NamingException, SQLException   {
  		
  		Context initContext = new InitialContext();     
 		DataSource ds = (DataSource) initContext.lookup(JNDI);
 		con = ds.getConnection();
 		List<String> values = new ArrayList<String>();
 		
 		String query = "SELECT a.instancia||' - '||trim(b.descripcion) ";
	       query += " FROM instancias_usr a, instancias b";
	       query += " where a.instancia=b.instancia";
	       query += " and coduser = '" + login + "'";

  		  		
  		pstmt = con.prepareStatement(query);
        //System.out.println(query);
  		
        r =  pstmt.executeQuery();
        		
        while (r.next()){
 
        values.add(r.getString(1));

        }
        //Cierra las conecciones
        pstmt.close();
        con.close();
        return values;
    }
	
	/**
	 * Indica si retorna muestra el modal o no
	 */
    public String modal(){
    	String modal = "0";
    	if(instancia.equals(null) || instancia.equals("0")){
    		modal = "1";
    	}
    	return modal;
    }
    
     
     /**
      * Chequeo de versiones
      */
     public String ControlV(){
    	 String version = "";
    	 //Si parametriza a cero selecciona
    	 if(CHECK_UPDATE.equals("on")){    	 
    	 version = controlv.selectVersion();
    	 }
    	 return version;
     }
     
     /**
      * Chequeo de versiones
      */
     public String isShow(){
    	 String display = "";
    	 double version = 6.0;
    	 if(CHECK_UPDATE.equals("on")){    	 
        	if(controlv.version()==version){
        		display = "none";
        	} 
    	 } else {
    		 display = "none";
    	 }
    	 return display;
     }
}
