/*
 *  Copyright (C) 2011  DVCONSULTORES

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

package org.openbizview.autocomplete;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;

import org.openbizview.util.Bd;
import org.openbizview.util.PntGenerica;
import org.openbizview.util.PntGenericasb;

/*
 * Esta clase permite mostrar las listas de valores en todas las páginas
 * utilizando Primefaces y sustituyendo a JQuery
 */

@ManagedBean
@RequestScoped
public class Autocomplete extends Bd {
	PntGenerica consulta = new PntGenerica();
	PntGenericasb consulta1 = new PntGenericasb();
	int rows;
	String[][] tabla;
	String[][] tabla1;
	private String instancia = (String) FacesContext.getCurrentInstance()
			.getExternalContext().getSessionMap().get("instancia"); // Usuario
																	// logeado
	
	private String login = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario"); //Usuario logeado

	public String getLogin() {
		return login;
	}

	
	/**
	 * Lista grupo de reportes.
	 * 
	 * @throws NamingException
	 * @return List String
	 * @throws IOException
	 **/
	public List<String> completeGrupo(String query) throws NamingException,
			IOException {

		String queryora = "Select codgrup||' - '||desgrup " + " from bvt001a "
				+ " where codgrup||desgrup like '%" + query.toUpperCase() + "%' and instancia = '"
				+ instancia + "' order by codgrup";
		String querypg = "Select codgrup||' - '||desgrup " + " from bvt001a "
				+ " where codgrup||desgrup like '%" + query.toUpperCase() + "%'  and instancia = '"
				+ instancia	+ "' order by codgrup";
		String querysql = "Select codgrup + ' - ' + desgrup "
				+ " from bvt001a " + " where codgrup||desgrup like '%" + query.toUpperCase() + "%' and instancia = '"
				+ instancia
				+ "' order by codgrup";

		List<String> results = new ArrayList<String>();

		consulta.selectPntGenericaMDB(queryora, querypg, querysql, JNDI);

		rows = consulta.getRows();
		tabla = consulta.getArray();
		for (int i = 0; i < rows; i++) {
			results.add(tabla[i][0]);
		}
		return results;
	}

	/**
	 * Lista grupo de reportes.
	 * 
	 * @throws NamingException
	 * @return List String
	 * @throws IOException
	 **/
	public List<String> completeInstancias(String query)
			throws NamingException, IOException {

		String queryora = "Select instancia||' - '||descripcion "
				+ " from instancias  where instancia||descripcion like '%" + query.toUpperCase()
				+ "%' order by instancia";
		String querypg = "Select instancia||' - '||descripcion "
				+ " from instancias  where cast(instancia as text)||descripcion like '%" + query.toUpperCase()
				+ "%' order by instancia";
		String querysql = "Select instancia||' - '||descripcion "
				+ " from instancias  where cast(instancia instancias as text)||descripcion like '%" + query.toUpperCase()
				+ "%' order by instancia";

		List<String> results = new ArrayList<String>();
		// System.out.println(queryora);
		consulta.selectPntGenericaMDB(queryora, querypg, querysql, JNDI);

		rows = consulta.getRows();
		tabla = consulta.getArray();
		for (int i = 0; i < rows; i++) {
			results.add(tabla[i][0]);
		}
		return results;
	}

	/**
	 * Lista Categoria1.
	 * 
	 * @throws NamingException
	 * @return List String
	 * @throws IOException
	 **/
	public List<String> completeCat1(String query) throws NamingException,
			IOException {

		String vlqueryOra = "Select codcat1||' - '||descat1 from bvtcat1 where codcat1||descat1 like '%"
				+ query.toUpperCase()
				+ "%'  and instancia = '"
				+ instancia
				+ "' order by codcat1";

		String vlqueryPg = "Select codcat1||' - '||descat1 from bvtcat1 where codcat1||descat1 like '%"
				+ query.toUpperCase()
				+ "%' and instancia = '"
				+ instancia
				+ "' order by codcat1";

		String vlquerySql = "Select codcat1 + ' - ' + descat1 from bvtcat1 where codcat1 + descat1 like '%"
				+ query.toUpperCase()
				+ "%' and instancia = '"
				+ instancia
				+ "' order by codcat1";

		List<String> results = new ArrayList<String>();

		consulta.selectPntGenericaMDB(vlqueryOra, vlqueryPg, vlquerySql, JNDI);

		rows = consulta.getRows();

		tabla = consulta.getArray();
		for (int i = 0; i < rows; i++) {
			results.add(tabla[i][0]);
		}
		return results;
	}

	/**
	 * Lista Categoria1 en seguridad.
	 * 
	 * @throws NamingException
	 * @return List String
	 * @throws IOException
	 **/
	public List<String> completeCat1AccCat2(String query)
			throws NamingException, IOException {
		String segrol = (String) FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap().get("segrol"); // Usuario
																		// logeado

		if (segrol == null) {
			segrol = " - ";
		}
		if (segrol == "") {
			segrol = " - ";
		}
		String[] vecrol = segrol.split("\\ - ", -1);

		String queryora = "Select codcat1||' - '||descat1 "
				+ " from bvtcat1 "
				+ " where codcat1||descat1 like '%"
				+ query.toUpperCase()
				+ "%' and codcat1 in (select B_CODCAT1 from acccat1 where B_CODROL ='"
				+ vecrol[0].toUpperCase() + "') and instancia = '" + instancia
				+ "' order by codcat1";
		String querypg = "Select codcat1||' - '||descat1 "
				+ " from bvtcat1 "
				+ " where codcat1||descat1 like '%"
				+ query.toUpperCase()
				+ "%' and codcat1 in (select B_CODCAT1 from acccat1 where B_CODROL ='"
				+ vecrol[0].toUpperCase() + "') and instancia = '" + instancia
				+ "' order by codcat1";
		String querysql = "Select codcat1 + ' - ' + descat1 "
				+ " from bvtcat1 "
				+ " where codcat1 + descat1 like '%"
				+ query.toUpperCase()
				+ "%' and codcat1 in (select B_CODCAT1 from acccat1 where B_CODROL ='"
				+ vecrol[0].toUpperCase() + "') and instancia = '" + instancia
				+ "' order by codcat1";

		List<String> results = new ArrayList<String>();

		consulta.selectPntGenericaMDB(queryora, querypg, querysql, JNDI);

		rows = consulta.getRows();

		tabla = consulta.getArray();

		for (int i = 0; i < rows; i++) {
			results.add(tabla[i][0]);
		}
		return results;
	}

	/**
	 * Lista Categoria2.
	 * 
	 * @throws NamingException
	 * @return List String
	 * @throws IOException
	 **/
	public List<String> completeCat2(String query) throws NamingException,
			IOException {
		String cat1 = (String) FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap().get("cat1"); // Usuario
																	// logeado
		if (cat1 == null) {
			cat1 = " - ";
		}
		if (cat1 == "") {
			cat1 = " - ";
		}
		String[] veccat1 = cat1.split("\\ - ", -1);
		String vlqueryOra = "Select codcat2||' - '||descat2 from bvtcat2 where codcat2||descat2 like '%"
				+ query.toUpperCase()
				+ "%' and b_codcat1 = '"
				+ veccat1[0]
				+ "' and instancia = '" + instancia + "' order by codcat2";
		String vlqueryPg = "Select codcat2||' - '||descat2 from bvtcat2 where codcat2||descat2 like '%"
				+ query.toUpperCase()
				+ "%' and b_codcat1 = '"
				+ veccat1[0]
				+ "' and instancia = '" + instancia + "' order by codcat2";
		String vlquerySql = "Select codcat2 + ' - ' + descat2 from bvtcat2 where codcat2 + descat2 like '%"
				+ query.toUpperCase()
				+ "%' and b_codcat1 = '"
				+ veccat1[0]
				+ "' and instancia = '" + instancia + "' order by codcat2";

		// System.out.println(vlqueryOra);
		List<String> results = new ArrayList<String>();
		consulta.selectPntGenericaMDB(vlqueryOra, vlqueryPg, vlquerySql, JNDI);
		rows = consulta.getRows();
		tabla = consulta.getArray();
		for (int i = 0; i < rows; i++) {
			results.add(tabla[i][0]);
		}
		return results;
	}

	/**
	 * Lista Categoria2.
	 * 
	 * @throws NamingException
	 * @return List String
	 * @throws IOException
	 **/
	public List<String> completeCat2AccCat3(String query)
			throws NamingException, IOException {
		String segrol = (String) FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap().get("segrol"); // Usuario
																		// logeado

		String cat1 = (String) FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap().get("cat1"); // Usuario
																	// logeado

		if (segrol == null) {
			segrol = " - ";
		}
		if (segrol == "") {
			segrol = " - ";
		}
		if (cat1 == null) {
			cat1 = " - ";
		}
		if (cat1 == "") {
			cat1 = " - ";
		}
		String[] vecrol = segrol.split("\\ - ", -1);
		String[] veccat1 = cat1.split("\\ - ", -1);

		String queryora = "Select codcat2||' - '||descat2 "
				+ " from bvtcat2 "
				+ " where codcat2||descat2 like '%"
				+ query.toUpperCase()
				+ "%' and codcat2 in (select B_CODCAT2 from acccat2 where B_CODROL ='"
				+ vecrol[0].toUpperCase() + "') and b_codcat1 = '" + veccat1[0]
				+ "' and instancia = '" + instancia + "' order by codcat2";
		String querypg = "Select codcat2||' - '||descat2 "
				+ " from bvtcat2 "
				+ " where codcat2||descat2 like '%"
				+ query.toUpperCase()
				+ "%' and codcat2 in (select B_CODCAT2 from acccat2 where B_CODROL ='"
				+ vecrol[0].toUpperCase() + "') and b_codcat1 = '" + veccat1[0]
				+ "' and instancia = '" + instancia + "' order by codcat2";
		String querysql = "Select codcat2 + ' - ' + descat2 "
				+ " from bvtcat2 "
				+ " where codcat2 + descat2 like '%"
				+ query.toUpperCase()
				+ "%' and codcat2 in (select B_CODCAT2 from acccat2 where B_CODROL ='"
				+ vecrol[0].toUpperCase() + "') and b_codcat1 = '" + veccat1[0]
				+ "' and instancia = '" + instancia + "' order by codcat2";

		List<String> results = new ArrayList<String>();

		consulta.selectPntGenericaMDB(queryora, querypg, querysql, JNDI);

		rows = consulta.getRows();
		tabla = consulta.getArray();
		for (int i = 0; i < rows; i++) {
			results.add(tabla[i][0]);
		}
		return results;
	}

	/**
	 * Lista Categoria2.
	 * 
	 * @throws NamingException
	 * @return List String
	 * @throws IOException
	 **/
	public List<String> completeCat3AccCat4(String query)
			throws NamingException, IOException {
		String segrol = (String) FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap().get("segrol"); // Usuario
																		// logeado
		String cat1 = (String) FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap().get("cat1"); // Usuario
																	// logeado
		String cat2 = (String) FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap().get("cat2"); // Usuario
																	// logeado
		if (segrol == null) {
			segrol = " - ";
		}
		if (segrol == "") {
			segrol = " - ";
		}
		if (cat1 == null) {
			cat1 = " - ";
		}
		if (cat1 == "") {
			cat1 = " - ";
		}
		if (cat2 == null) {
			cat2 = " - ";
		}
		if (cat2 == "") {
			cat2 = " - ";
		}
		String[] vecrol = segrol.split("\\ - ", -1);
		String[] veccat1 = cat1.split("\\ - ", -1);
		String[] veccat2 = cat2.split("\\ - ", -1);

		List<String> results = new ArrayList<String>();
		String queryora = "Select codcat3||' - '||descat3 "
				+ " from bvtcat3 "
				+ " where codcat3||descat3 like '%"
				+ query.toUpperCase()
				+ "%' and codcat3 in (select B_CODCAT3 from acccat3 where B_CODROL ='"
				+ vecrol[0].toUpperCase() + "') and b_codcat1 = '" + veccat1[0]
				+ "' and b_codcat2 = '" + veccat2[0] + "' and instancia = '"
				+ instancia + "' order by codcat3";
		String querypg = "Select codcat3||' - '||descat3 "
				+ " from bvtcat3 "
				+ " where codcat3||descat3 like '%"
				+ query.toUpperCase()
				+ "%' and codcat3 in (select B_CODCAT3 from acccat3 where B_CODROL ='"
				+ vecrol[0].toUpperCase() + "') and b_codcat1 = '" + veccat1[0]
				+ "' and b_codcat2 = '" + veccat2[0] + "' and instancia = '"
				+ instancia + "' order by codcat3";
		String querysql = "Select codcat3 + ' - ' + descat3 "
				+ " from bvtcat3 "
				+ " where codcat3 + descat3 like '%"
				+ query.toUpperCase()
				+ "%' and codcat3 in (select B_CODCAT3 from acccat3 where B_CODROL ='"
				+ vecrol[0].toUpperCase() + "') and b_codcat1 = '" + veccat1[0]
				+ "' and b_codcat2 = '" + veccat2[0] + "' and instancia = '"
				+ instancia + "' order by codcat3";
		consulta.selectPntGenericaMDB(queryora, querypg, querysql, JNDI);
		rows = consulta.getRows();
		tabla = consulta.getArray();
		for (int i = 0; i < rows; i++) {
			results.add(tabla[i][0]);
		}
		return results;
	}

	/**
	 * Lista Categoria3.
	 * 
	 * @throws NamingException
	 * @return List String
	 * @throws IOException
	 **/
	public List<String> completeCat3(String query) throws NamingException,
			IOException {
		String cat1 = (String) FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap().get("cat1"); // Usuario
																	// logeado
		String cat2 = (String) FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap().get("cat2"); // Usuario
																	// logeado

		if (cat1 == null) {
			cat1 = " - ";
		}
		if (cat1 == "") {
			cat1 = " - ";
		}
		if (cat2 == null) {
			cat2 = " - ";
		}
		if (cat2 == "") {
			cat2 = " - ";
		}
		String[] veccat1 = cat1.split("\\ - ", -1);
		String[] veccat2 = cat2.split("\\ - ", -1);
		String vlqueryOra = "Select codcat3||' - '||descat3 from bvtcat3 where codcat3||descat3 like '%"
				+ query.toUpperCase()
				+ "%' and b_codcat1 = '"
				+ veccat1[0]
				+ "' and b_codcat2 = '"
				+ veccat2[0]
				+ "' and instancia = '"
				+ instancia + "' order by  codcat3";
		String vlqueryPg = "Select codcat3||' - '||descat3 from bvtcat3 where codcat3||descat3 like '%"
				+ query.toUpperCase()
				+ "%' and b_codcat1 = '"
				+ veccat1[0]
				+ "' and b_codcat2 = '"
				+ veccat2[0]
				+ "' and instancia = '"
				+ instancia + "' order by  codcat3";
		String vlquerySql = "Select codcat3+' - '+descat3 from bvtcat3 where codcat3+descat3 like '%"
				+ query.toUpperCase()
				+ "%' and b_codcat1 = '"
				+ veccat1[0]
				+ "' and b_codcat2 = '"
				+ veccat2[0]
				+ "' and instancia = '"
				+ instancia + "' order by  codcat3";
		// System.out.println(vlqueryOra);
		// System.out.println(vlqueryPg);
		List<String> results = new ArrayList<String>();
		consulta.selectPntGenericaMDB(vlqueryOra, vlqueryPg, vlquerySql, JNDI);
		rows = consulta.getRows();
		tabla = consulta.getArray();
		for (int i = 0; i < rows; i++) {
			results.add(tabla[i][0]);
		}
		return results;
	}

	/**
	 * Lista Categoria1.
	 * 
	 * @throws NamingException
	 * @return List String
	 * @throws IOException
	 **/
	public List<String> completeCat4(String query) throws NamingException,
			IOException {
		List<String> results = new ArrayList<String>();

		String queryora = "Select codcat4||' - '||descat4 " + " from bvtcat4 "
				+ " where codcat4 like '%" + query + "%' or descat4 like '%"
				+ query.toUpperCase() + "%' and instancia = '" + instancia
				+ "' order by codcat4";
		String querypg = "Select codcat4||' - '||descat4 " + " from bvtcat4 "
				+ " where codcat4 like '%" + query + "%' or descat4 like '%"
				+ query.toUpperCase() + "%' and instancia = '" + instancia
				+ "' order by codcat4";
		String querysql = "Select codcat4+' - '+descat4 " + " from bvtcat4 "
				+ " where codcat4 like '%" + query + "%' or descat4 like '%"
				+ query.toUpperCase() + "%' and instancia = '" + instancia
				+ "' order by codcat4";

		consulta.selectPntGenericaMDB(queryora, querypg, querysql, JNDI);
		rows = consulta.getRows();
		tabla = consulta.getArray();
		for (int i = 0; i < rows; i++) {
			results.add(tabla[i][0]);
		}
		return results;
	}

	/**
	 * Lista Usuario.
	 * 
	 * @throws NamingException
	 * @return List String
	 * @throws IOException
	 **/

	public List<String> completeUser(String query) throws NamingException,
			IOException {

		String queryora = "Select coduser||' - '|| desuser " + " from bvt002 "
				+ " where coduser||desuser like '%"
				+ query.toUpperCase() + "%' and instancia = '" + instancia
				+ "' order by coduser";
		String querypg = "Select coduser||' - '|| desuser " + " from bvt002 "
				+ " where coduser||desuser  like '%"
				+ query.toUpperCase() + "%' and instancia = '" + instancia
				+ "' order by coduser";
		String querysql = "Select coduser + ' - ' + desuser " + " from bvt002 "
				+ " where coduser||desuser  like '%"
				+ query.toUpperCase() + "%' and instancia = '" + instancia
				+ "' order by coduser";

		List<String> results = new ArrayList<String>();
		//System.out.println(querypg);

		consulta.selectPntGenericaMDB(queryora, querypg, querysql, JNDI);

		rows = consulta.getRows();
		tabla = consulta.getArray();
		for (int i = 0; i < rows; i++) {
			results.add(tabla[i][0]);
		}
		return results;
	}
	
	/**
	 * Lista Usuario.
	 * 
	 * @throws NamingException
	 * @return List String
	 * @throws IOException
	 **/

	public List<String> completeUserCod(String query) throws NamingException,
			IOException {

		String queryora = "Select coduser " + " from bvt002 "
				+ " where coduser like '%"
				+ query.toUpperCase() + "%' and instancia = '" + instancia
				+ "' order by coduser";
		String querypg = "Select coduser " + " from bvt002 "
				+ " where coduser  like '%"
				+ query.toUpperCase() + "%' and instancia = '" + instancia
				+ "' order by coduser";
		String querysql = "Select coduser" + " from bvt002 "
				+ " where coduser  like '%"
				+ query.toUpperCase() + "%' and instancia = '" + instancia
				+ "' order by coduser";

		List<String> results = new ArrayList<String>();
		//System.out.println(querypg);

		consulta.selectPntGenericaMDB(queryora, querypg, querysql, JNDI);

		rows = consulta.getRows();
		tabla = consulta.getArray();
		for (int i = 0; i < rows; i++) {
			results.add(tabla[i][0]);
		}
		return results;
	}

	/**
	 * Lista rol.
	 * 
	 * @throws NamingException
	 * @return List String
	 * @throws IOException
	 **/
	public List<String> completeRol(String query) throws NamingException,
			IOException {

		String queryora = "Select codrol||' - '||desrol " + " from bvt003 "
				+ " where codrol||desrol like '%" + query.toUpperCase()
				+ "%'  and instancia = '" + instancia + "' order by codrol ";
		String querypg = "Select codrol||' - '||desrol " + " from bvt003 "
				+ " where codrol||desrol like '%" + query.toUpperCase()
				+ "%'  and instancia = '" + instancia + "' order by codrol ";
		String querysql = "Select codrol + ' - ' + desrol " + " from bvt003 "
				+ " where codrol + desrol like '%" + query.toUpperCase()
				+ "%'  and instancia = '" + instancia + "' order by codrol ";
		 //System.out.println(querypg);
		List<String> results = new ArrayList<String>();
		consulta.selectPntGenericaMDB(queryora, querypg, querysql, JNDI);
		rows = consulta.getRows();
		tabla = consulta.getArray();
		for (int i = 0; i < rows; i++) {
			results.add(tabla[i][0]);
		}
		return results;
	}

	/**
	 * Lista Reportes.
	 * 
	 * @throws NamingException
	 * @return List String
	 * @throws IOException
	 **/
	public List<String> completeRep(String query) throws NamingException,
			IOException {

		List<String> results = new ArrayList<String>();

		String queryora = "Select codrep||' - '||desrep " + " from bvt001 "
				+ " where codrep||desrep like '%" + query.toUpperCase()
				+ "%'  and instancia = '" + instancia + "' order by codrep";

		String querypg = "Select codrep||' - '||desrep " + " from bvt001 "
				+ " where codrep||desrep like '%" + query.toUpperCase()
				+ "%' and instancia = '" + instancia + "' order by codrep";

		String querysql = "Select codrep+' - '+desrep " + " from bvt001 "
				+ " where codrep||desrep like '%" + query.toUpperCase()
				+ "%' and instancia = '" + instancia + "' order by codrep";
		
		//System.out.println(querypg);

		consulta.selectPntGenericaMDB(queryora, querypg, querysql, JNDI);

		rows = consulta.getRows();

		tabla = consulta.getArray();

		for (int i = 0; i < rows; i++) {
			results.add(tabla[i][0]);
		}
		return results;
	}

	/**
	 * Lista grupos de mail.
	 * 
	 * @throws NamingException
	 * @return List String
	 * @throws IOException
	 **/
	public List<String> completeMailGrupos(String query)
			throws NamingException, IOException {
		List<String> results = new ArrayList<String>();

		String queryora = "Select IDGRUPO||' - '||DESGRUPO "
				+ " from mailgrupos " + " where IDGRUPO||DESGRUPO like '%"
				+ query.toUpperCase() + "%' and instancia = '" + instancia
				+ "' order by IDGRUPO";
		String querypg = "Select IDGRUPO||' - '||DESGRUPO "
				+ " from mailgrupos " + " where IDGRUPO||DESGRUPO like '%"
				+ query.toUpperCase() + "%' and instancia = '" + instancia
				+ "' order by IDGRUPO";
		String querysql = "Select LTRIM(RTRIM(CAST(IDGRUPO AS CHAR)))+' - '+DESGRUPO "
				+ " from mailgrupos "
				+ " where LTRIM(RTRIM(cast(IDGRUPO as char)))+DESGRUPO like '%"
				+ query.toUpperCase()
				+ "%' and instancia = '"
				+ instancia
				+ "' order by IDGRUPO";
		consulta.selectPntGenericaMDB(queryora, querypg, querysql, JNDI);
		rows = consulta.getRows();
		tabla = consulta.getArray();
		for (int i = 0; i < rows; i++) {
			results.add(tabla[i][0]);
		}
		return results;
	}
	
	
	/**
	 * Lista frecuencia de repetición.
	 * 
	 * @throws NamingException
	 * @return List String
	 * @throws IOException
	 **/
	public List<String> completeFrecuenciaRepeticion(String query)
			throws NamingException, IOException {
		List<String> results = new ArrayList<String>();

		String queryora = "Select distinct frecuencia||' - '||case frecuencia when '0' then '" + getMessage("mailtareaDiario").toUpperCase() + "' when  '1' then '" + getMessage("mailtareaSemanal").toUpperCase() + "' when '2' then'" + getMessage("mailtareaPersonalizada").toUpperCase() + "' when '3' then '" + getMessage("mailtareaHoraRep").toUpperCase() + "' when '4' then '" + getMessage("mailimes1").toUpperCase() + "' when '5' then '" + getMessage("maillidiasSelect").toUpperCase() + "'  else '" + getMessage("maillidiasSelect1").toUpperCase() + "' end "
				+ " from t_programacion " + " where frecuencia like '%"
				+ query.toUpperCase() + "%' and instancia = '" + instancia
				+ "'";
		String querypg = "Select distinct frecuencia||' - '||case frecuencia when '0' then '" + getMessage("mailtareaDiario").toUpperCase() + "' when  '1' then '" + getMessage("mailtareaSemanal").toUpperCase() + "' when '2' then'" + getMessage("mailtareaPersonalizada").toUpperCase() + "' when '3' then '" + getMessage("mailtareaHoraRep").toUpperCase() + "' when '4' then '" + getMessage("mailimes1").toUpperCase() + "' when '5' then '" + getMessage("maillidiasSelect").toUpperCase() + "'  else '" + getMessage("maillidiasSelect1").toUpperCase() + "' end "
				+ " from t_programacion " + " where frecuencia like '%"
				+ query.toUpperCase() + "%' and instancia = '" + instancia
				+ "'";
		String querysql = "Select LTRIM(RTRIM(CAST(IDGRUPO AS CHAR)))+' - '+DESGRUPO "
				+ " from mailgrupos "
				+ " where LTRIM(RTRIM(cast(IDGRUPO as char)))+DESGRUPO like '%"
				+ query.toUpperCase()
				+ "%' and instancia = '"
				+ instancia
				+ "' order by IDGRUPO";
		consulta.selectPntGenericaMDB(queryora, querypg, querysql, JNDI);
		//System.out.println(querypg);
		rows = consulta.getRows();
		tabla = consulta.getArray();
		for (int i = 0; i < rows; i++) {
			results.add(tabla[i][0]);
		}
		return results;
	}

	/**
	 * Listas prueba para Sybase.
	 * 
	 * @throws NamingException
	 * @return List String
	 * @throws IOException
	**/ 
	public List<String> completeTestsb(String query) throws NamingException,IOException {
		//System.out.println("entre al metodo testsb");
		
		String querysb = "SELECT VN.PERNR + ' - ' + VN.NCHMC + ' ' + VN.VNAMC CLAVE " + " FROM R3P.SAPSR3.M_PREMN VN "
				+ " WHERE VN.MANDT='400'" + " AND VN.PERNR LIKE '%00666%'"
				+ " order by 1";
		//System.out.println(querysb);
		//System.out.println(JNDISB);
		List<String> results = new ArrayList<String>();

		consulta1.selectPntGenericasb(querysb, JNDISB);

		rows = consulta1.getRows();
		tabla1 = consulta1.getArray();
		for (int i = 0; i < rows; i++) {
			results.add(tabla1[i][0]);
		}
		return results;
	}	

	
	/**
	 * Listas nivel de app.
	 * 
	 * @throws NamingException
	 * @return List String
	 * @throws IOException
	 **/
	public List<String> completeNivapp(String query) throws NamingException,IOException {
		//System.out.println("entre al metodo nivapp");

		String querysb = "SELECT CODIGO || ' - ' || DESCR " 
		        + " FROM TUBDER08 "
				+ " GROUP BY CODIGO, DESCR "
				+ " ORDER BY 1";
		//System.out.println(querysb);
		//System.out.println(JNDISB);
		List<String> results = new ArrayList<String>();

		consulta.selectPntGenerica(querysb, JNDI);

		rows = consulta.getRows();
		tabla = consulta.getArray();
		for (int i = 0; i < rows; i++) {
			results.add(tabla[i][0]);
		}
		return results;
	}
	
	/**
	 * Listas nivel de app.
	 * 
	 * @throws NamingException
	 * @return List String
	 * @throws IOException
	 **/
	public List<String> completePeriod(String query) throws NamingException,IOException {
		//System.out.println("entre al metodo nivapp");

		String querysb = "SELECT CODIGO || ' - ' || DESCR " 
		        + " FROM TUBDER09 "
				+ " GROUP BY CODIGO, DESCR "
				+ " ORDER BY 1";
		//System.out.println(querysb);
		//System.out.println(JNDISB);
		List<String> results = new ArrayList<String>();

		consulta.selectPntGenerica(querysb, JNDI);

		rows = consulta.getRows();
		tabla = consulta.getArray();
		for (int i = 0; i < rows; i++) {
			results.add(tabla[i][0]);
		}
		return results;
	}
	
	/**
	 * Listas nivel de app.
	 * 
	 * @throws NamingException
	 * @return List String
	 * @throws IOException
	 **/
	public List<String> completeEstatu(String query) throws NamingException,IOException {
		//System.out.println("entre al metodo nivapp");

		String querysb = "SELECT CODIGO || ' - ' || DESCR " 
		        + " FROM TUBDER10 "
				+ " GROUP BY CODIGO, DESCR "
				+ " ORDER BY 1";
		//System.out.println(querysb);
		//System.out.println(JNDISB);
		List<String> results = new ArrayList<String>();

		consulta.selectPntGenerica(querysb, JNDI);

		rows = consulta.getRows();
		tabla = consulta.getArray();
		for (int i = 0; i < rows; i++) {
			results.add(tabla[i][0]);
		}
		return results;
	}

	/**
	 * Listas compa;ias.
	 * @return 
	 * 
	 * @throws NamingException
	 * @throws IOException
	 **/
	public List<String> completeComp(String query) throws NamingException,IOException {
		//System.out.println("entre al metodo nivapp");
		
			String validar = "1";
			String querycon = "SELECT BI_SGC014('" + login.toUpperCase() + "') AS VALIDAR FROM DUAL";
		
			//System.out.println(querycon);
			//System.out.println(JNDI);
			
			consulta.selectPntGenerica(querycon, JNDI);
			
			rows = consulta.getRows();
			tabla = consulta.getArray();
			//System.out.println(tabla[0][0]);
			
			if (tabla[0][0].equals(validar)) {
	    		//System.out.println(tabla[0][0]);
	    		//System.out.println(validar);
	    		//System.out.println("EL USUARIO ES IGUAL");
	    		
	    		String querysb = "SELECT A.CODIGO || ' - ' || A.DESCR " 
				        + " FROM SGC005 A "
				        + " GROUP BY A.CODIGO, A.DESCR "
						+ " ORDER BY 1";
				//System.out.println(querysb);
				//System.out.println(JNDI);
				List<String> results = new ArrayList<String>();

				consulta.selectPntGenerica(querysb, JNDI);

				rows = consulta.getRows();
				tabla= consulta.getArray();
				for (int i = 0; i < rows; i++) {
					results.add(tabla[i][0]);
				}
				return results;

	    }
	    	else {
	    		//System.out.println(tabla[0][0]);
	    		//System.out.println(validar);
	    		//System.out.println("EL USUARIO NO ES IGUAL");
	    		
	    		String querysb = "SELECT A.CODIGO || ' - ' || A.DESCR " 
				        + " FROM SGC005 A,  SGC007 B "
	    				+ " WHERE A.CODIGO = B.COMP "
				        + " AND TRIM(B.CODUSER) like '%" + login.toUpperCase() + "%'"
				        + " GROUP BY A.CODIGO, A.DESCR "
						+ " ORDER BY 1";
				//System.out.println(querysb);
				//System.out.println(JNDI;
				List<String> results = new ArrayList<String>();

				consulta.selectPntGenerica(querysb, JNDI);

				rows = consulta.getRows();
				tabla = consulta.getArray();
				for (int i = 0; i < rows; i++) {
					results.add(tabla[i][0]);
				}
				return results;
	    		
	    	}			

	}
	
	/**
	 * Listas tipo de valor.
	 * 
	 * @throws NamingException
	 * @return List String
	 * @throws IOException
	 **/
	public List<String> completeTval(String query) throws NamingException,IOException {
		//System.out.println("entre al metodo nivapp");
		
			String querysb = "SELECT A.CODIGO || ' - ' || A.DESCR " 
			        + " FROM TUBDER11 A "
			        + " GROUP BY A.CODIGO, A.DESCR "
					+ " ORDER BY 1";
			//System.out.println(querysb);
			//System.out.println(JNDISB);
			List<String> results = new ArrayList<String>();

			consulta.selectPntGenerica(querysb, JNDI);

			rows = consulta.getRows();
			tabla = consulta.getArray();
			for (int i = 0; i < rows; i++) {
				results.add(tabla[i][0]);
			}
			return results;
	}
	
	/**
	 * Listas areas.
	 * 
	 * @throws NamingException
	 * @return List String
	 * @throws IOException
	 **/
	public List<String> completeArea(String query) throws NamingException,IOException {
		//System.out.println("entre al metodo COMPLETEAREA");
		String comp = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("comp"); // Usuario logeado

		if (comp == null) {
			comp = " - ";
		}
		if (comp == "") {
			comp = " - ";
		}
		String[] veccomp = comp.split("\\ - ", -1);			
		String validar = "1";
		String querycon = "SELECT BI_SGC014('" + login.toUpperCase() + "') AS VALIDAR FROM DUAL";
		
			//System.out.println(querycon);
			//System.out.println(JNDISB);
			
			consulta.selectPntGenerica(querycon, JNDI);
			
			rows = consulta.getRows();
			tabla = consulta.getArray();
			//System.out.println(tabla1[0][0]);
			
			if (tabla[0][0].equals(validar)) {
	    		//System.out.println(tabla1[0][0]);
	    		//System.out.println(validar);
	    		//System.out.println("EL USUARIO ES IGUAL");
	    		
	    		String querysb = "SELECT A.CODIGO || ' - ' || A.DESCR " 
		        + " FROM SGC006 A "
		        + " WHERE A.COMP = '" + veccomp[0].toUpperCase() + "'"
				+ " GROUP BY A.CODIGO, A.DESCR "
				+ " ORDER BY 1";
				//System.out.println(querysb);
				//System.out.println(JNDISB);
				List<String> results = new ArrayList<String>();

				consulta.selectPntGenerica(querysb, JNDI);

				rows = consulta.getRows();
				tabla = consulta.getArray();
				for (int i = 0; i < rows; i++) {
					results.add(tabla[i][0]);
				}
				return results;

	    }
	    	else {
	    		//System.out.println(tabla1[0][0]);
	    		//System.out.println(validar);
	    		//System.out.println("EL USUARIO NO ES IGUAL");
	    		
	    		String querysb = "SELECT A.CODIGO || ' - ' || A.DESCR " 
				        + " FROM SGC006 A, SGC008 B "
	    				+ " WHERE A.COMP = B.COMP AND A.CODIGO = B.AREA"
				        + " AND TRIM(B.CODUSER) like '%" + login.toUpperCase() + "%'"
				        + " AND TRIM(B.COMP) like '%" + veccomp[0].toUpperCase() + "%'"
				        + " GROUP BY A.CODIGO, A.DESCR "
						+ " ORDER BY 1";
				//System.out.println(querysb);
				//System.out.println(JNDISB);
				List<String> results = new ArrayList<String>();

				consulta.selectPntGenerica(querysb, JNDI);

				rows = consulta.getRows();
				tabla = consulta.getArray();
				for (int i = 0; i < rows; i++) {
					results.add(tabla[i][0]);
				}
				return results;
	    		
	    	}		
	}
	
	/**
	 * Listas indicadores.
	 * 
	 * @throws NamingException
	 * @return List String
	 * @throws IOException
	 **/
	public List<String> completeInd(String query) throws NamingException,IOException {

		//System.out.println("entre al metodo nivapp");
		String comp = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("comp"); // Usuario logeado
		String area = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("area"); // Usuario logeado

		if (comp == null) {
			comp = " - ";
		}
		if (comp == "") {
			comp = " - ";
		}
		if (area == null) {
			area = " - ";
		}
		if (area == "") {
			area = " - ";
		}
		String[] veccomp = comp.split("\\ - ", -1);
		String[] vecarea = area.split("\\ - ", -1);		
		String validar = "1";
		
		String querycon = "SELECT BI_SGC014('" + login.toUpperCase() + "') AS VALIDAR FROM DUAL";
		
			//System.out.println(querycon);
			
			//System.out.println(JNDISB);
			
			consulta.selectPntGenerica(querycon, JNDI);
			
			rows = consulta.getRows();
			tabla = consulta.getArray();
			//System.out.println(tabla1[0][0]);
			
			if (tabla[0][0].equals(validar)) {
	    		//System.out.println(tabla1[0][0]);
	    		//System.out.println(validar);
	    		//System.out.println("EL USUARIO ES IGUAL");
	    		
				String querysb = "SELECT CODIGO || ' - ' || NOMIND " 
				        + " FROM SGC001 "
				        + " WHERE COMP = '" + veccomp[0].toUpperCase() + "'"
				        + " AND AREA = '" + vecarea[0].toUpperCase() + "'"
						+ " GROUP BY CODIGO, NOMIND "
						+ " ORDER BY 1";
				//System.out.println(querysb);
				//System.out.println("Usuario Administrador");
				List<String> results = new ArrayList<String>();

				consulta.selectPntGenerica(querysb, JNDI);

				rows = consulta.getRows();
				tabla = consulta.getArray();
				for (int i = 0; i < rows; i++) {
					results.add(tabla[i][0]);
				}
				return results;

	    } else {
	    		String querysb = " SELECT "
				+ " DISTINCT "
				+ " TOT.CODIND || ' - ' || TOT.NOMIND INDICADOR "
				+ " FROM (SELECT "
				  + " B.COMP, "
				  + " B.AREA, "
				  + " TRIM(B.CODIGO) CODIND, "
				  + " TRIM(B.NOMIND) NOMIND "
				  + " FROM "
				  + " SGC009 A, SGC001 B "
				  + " WHERE " 
				  + " TRIM(A.COMP) = TRIM(B.COMP) "
				  + " AND TRIM(A.AREA) = TRIM(B.AREA) "
				  + " AND TRIM(A.INDICA) = TRIM(B.CODIGO) "
				  + " AND TRIM(A.CODUSER) = '" + login.toUpperCase() + "'"
				  + " UNION ALL "
				  + " SELECT "
				  + " B.COMP, "
				  + " B.AREA, "
				  + " TRIM(B.CODIGO) CODIND, "
				  + " TRIM(B.NOMIND) NOMIND "
				  + " FROM " 
				  + " SGC001 B   "
				  + " WHERE " 
				  + " TRIM(B.COMP)||TRIM(B.AREA) IN (SELECT COMP||AREA FROM SGC008 WHERE CODUSER = '" + login.toUpperCase() + "' AND DUEPRO = '1')) TOT "
				+ " WHERE "
				+ " TRIM(TOT.COMP) = '" + veccomp[0].toUpperCase() + "'"
				+ " AND TRIM(TOT.AREA) = '" + vecarea[0].toUpperCase() + "'"
	    		+ " ORDER BY 1 ";	    	
	    	
	    /*		String querysb = " SELECT "
	    				+ " TRIM(B.CODIGO) || ' - ' || TRIM(B.NOMIND)  INDICADOR "
	    				+ " FROM  "
	    				+ " SGC009 A, SGC001 B   "
	    				+ " WHERE  "
	    				+ " TRIM(A.COMP) = TRIM(B.COMP) "
	    				+ " AND TRIM(A.AREA) = TRIM(B.AREA) "
	    				+ " AND TRIM(A.INDICA) = TRIM(B.CODIGO) "
	    				+ " AND TRIM(A.CODUSER) = '" + login.toUpperCase() + "'"
	    				+ " AND TRIM(A.COMP) = '" + veccomp[0].toUpperCase() + "'"
	    				+ " AND TRIM(A.AREA) = '" + vecarea[0].toUpperCase() + "'"
	    				+ " GROUP BY B.CODIGO, B.NOMIND   "
	    				+ " ORDER BY 1 ";
	    				*/
	    				
	    		//System.out.println(querysb);
	    		//System.out.println("Usuario *** NO *** Administrador");
	    		List<String> results = new ArrayList<String>();

				consulta.selectPntGenerica(querysb, JNDI);

				rows = consulta.getRows();
				tabla = consulta.getArray();
				for (int i = 0; i < rows; i++) {
					results.add(tabla[i][0]);
				}
				return results;
	    		
	    	}	
	}
	

	//INICIO DE LOS AUTOCOMPLETES EXCLUSIVOS PARA LA TABLA SGC012A
	
	/**
	 * Listas compa;ias.
	 * 
	 * @throws NamingException
	 * @return List String
	 * @throws IOException
	 **/
	public List<String> completeComp1(String query) throws NamingException,IOException {
		//System.out.println("entre al metodo nivapp");
		String validar = "1";
		String querycon = "SELECT BI_SGC014('" + login.toUpperCase() + "') AS VALIDAR FROM DUAL";
		
			//System.out.println(querycon);
			//System.out.println(JNDISB);
			
			consulta.selectPntGenerica(querycon, JNDI);
			
			rows = consulta.getRows();
			tabla = consulta.getArray();
			//System.out.println(tabla1[0][0]);
			
			if (tabla[0][0].equals(validar)) {
	    		//System.out.println(tabla1[0][0]);
	    		//System.out.println(validar);
	    		//System.out.println("EL USUARIO ES IGUAL");
	    		
				String querysb = "SELECT A.COMP || ' - ' || B.DESCR " 
				        + " FROM SGC012 A, SGC005 B "
				        + " WHERE A.COMP = B.CODIGO "
				        + " GROUP BY A.COMP, B.DESCR "
						+ " ORDER BY 1";
				//System.out.println(querysb);
				//System.out.println(JNDISB);
				List<String> results = new ArrayList<String>();

				consulta.selectPntGenerica(querysb, JNDI);

				rows = consulta.getRows();
				tabla = consulta.getArray();
				for (int i = 0; i < rows; i++) {
					results.add(tabla[i][0]);
				}
				return results;

	    }
	    	else {
	    		//System.out.println(tabla1[0][0]);
	    		//System.out.println(validar);
	    		//System.out.println("EL USUARIO NO ES IGUAL");
	    		
	    		String querysb = "SELECT A.COMP || ' - ' || B.DESCR " 
				        + " FROM SGC012 A, SGC005 B, SGC007 C "
				        + " WHERE A.COMP = B.CODIGO "
				        + " AND A.COMP = C.COMP "
				        + " AND TRIM(C.CODUSER) like '%" + login.toUpperCase() + "%'"
				        + " GROUP BY A.COMP, B.DESCR "
						+ " ORDER BY 1";
				//System.out.println(querysb);
				//System.out.println(JNDISB);
				List<String> results = new ArrayList<String>();

				consulta.selectPntGenerica(querysb, JNDI);

				rows = consulta.getRows();
				tabla = consulta.getArray();
				for (int i = 0; i < rows; i++) {
					results.add(tabla[i][0]);
				}
				return results;
	    		
	    	}			
	}
	
	/**
	 * Listas areas.
	 * 
	 * @throws NamingException
	 * @return List String
	 * @throws IOException
	 **/
	public List<String> completeArea1(String query) throws NamingException,IOException {
		//System.out.println("entre al metodo COMPLETEAREA");
		String comp = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("comp"); // Usuario logeado

		if (comp == null) {
			comp = " - ";
		}
		if (comp == "") {
			comp = " - ";
		}
		String[] veccomp = comp.split("\\ - ", -1);			
		String validar = "1";
		String querycon = "SELECT BI_SGC014('" + login.toUpperCase() + "') AS VALIDAR FROM DUAL";
		
			//System.out.println(querycon);
			//System.out.println(JNDISB);
			
			consulta.selectPntGenerica(querycon, JNDI);
			
			rows = consulta.getRows();
			tabla = consulta.getArray();
			//System.out.println(tabla1[0][0]);
			
			if (tabla[0][0].equals(validar)) {
	    		//System.out.println(tabla1[0][0]);
	    		//System.out.println(validar);
	    		//System.out.println("EL USUARIO ES IGUAL");
	    		
				String querysb = "SELECT A.AREA || ' - ' || B.DESCR " 
				        + " FROM SGC012 A, SGC006 B "
				        + " WHERE A.AREA = B.CODIGO"
				        + " AND A.COMP = '" + veccomp[0].toUpperCase() + "'"
						+ " GROUP BY A.AREA, B.DESCR "
						+ " ORDER BY 1";
				//System.out.println(querysb);
				//System.out.println(JNDISB);
				List<String> results = new ArrayList<String>();

				consulta.selectPntGenerica(querysb, JNDI);

				rows = consulta.getRows();
				tabla = consulta.getArray();
				for (int i = 0; i < rows; i++) {
					results.add(tabla[i][0]);
				}
				return results;

	    }
	    	else {
	    		//System.out.println(tabla1[0][0]);
	    		//System.out.println(validar);
	    		//System.out.println("EL USUARIO NO ES IGUAL");
	    		String querysb = "SELECT A.AREA || ' - ' || B.DESCR " 
	    		        + " FROM SGC012 A, SGC006 B, SGC008 C "
	    		        + " WHERE A.AREA = B.CODIGO"
	    		        + " AND A.COMP = C.COMP"
	    		        + " AND A.AREA = C.AREA"
	    		        + " AND TRIM(C.CODUSER) like '%" + login.toUpperCase() + "%'"
	    		        + " AND A.COMP = '" + veccomp[0].toUpperCase() + "'"
	    				+ " GROUP BY A.AREA, B.DESCR "
	    				+ " ORDER BY 1";
				//System.out.println(querysb);
				//System.out.println(JNDISB);
				List<String> results = new ArrayList<String>();

				consulta.selectPntGenerica(querysb, JNDI);

				rows = consulta.getRows();
				tabla = consulta.getArray();
				for (int i = 0; i < rows; i++) {
					results.add(tabla[i][0]);
				}
				return results;
	    		
	    	}		
	}
	
	/**
	 * Listas indicadores.
	 * 
	 * @throws NamingException
	 * @return List String
	 * @throws IOException
	 **/
	public List<String> completeInd1(String query) throws NamingException,IOException {
		//System.out.println("entre al metodo nivapp");
		String comp = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("comp"); // Usuario logeado
		String area = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("area"); // Usuario logeado

		if (comp == null) {
			comp = " - ";
		}
		if (comp == "") {
			comp = " - ";
		}
		if (area == null) {
			area = " - ";
		}
		if (area == "") {
			area = " - ";
		}
		String[] veccomp = comp.split("\\ - ", -1);
		String[] vecarea = area.split("\\ - ", -1);		
		String validar = "1";
		String querycon = "SELECT BI_SGC014('" + login.toUpperCase() + "') AS VALIDAR FROM DUAL";
		
			//System.out.println(querycon);
			//System.out.println(JNDISB);
			
			consulta.selectPntGenerica(querycon, JNDI);
			
			rows = consulta.getRows();
			tabla = consulta.getArray();
			//System.out.println(tabla1[0][0]);
			
			if (tabla[0][0].equals(validar)) {
	    		//System.out.println(tabla1[0][0]);
	    		//System.out.println(validar);
	    		//System.out.println("EL USUARIO ES IGUAL");
	    		
				String querysb = "SELECT A.CODIGO || ' - ' || B.NOMIND " 
				        + " FROM SGC012 A, SGC001 B "
				        + " WHERE A.CODIGO = B.CODIGO"
				        + " AND A.COMP = '" + veccomp[0].toUpperCase() + "'"
				        + " AND A.AREA = '" + vecarea[0].toUpperCase() + "'"
						+ " GROUP BY A.CODIGO, B.NOMIND "
						+ " ORDER BY 1";
				//System.out.println(querysb);
				//System.out.println(JNDISB);
				List<String> results = new ArrayList<String>();

				consulta.selectPntGenerica(querysb, JNDI);

				rows = consulta.getRows();
				tabla = consulta.getArray();
				for (int i = 0; i < rows; i++) {
					results.add(tabla[i][0]);
				}
				return results;

	    }
	    	else {
	    		//System.out.println(tabla1[0][0]);
	    		//System.out.println(validar);
	    		//System.out.println("EL USUARIO NO ES IGUAL");
	    		String querysb = " SELECT " 
	    				+ " DISTINCT " 
	    				+ " RES.INDICADOR " 
	    				+ " FROM (SELECT  " 
	    				+ "       A.COMP, A.AREA, A.CODIGO || ' - ' || B.NOMIND INDICADOR  " 
	    				+ "       FROM  " 
	    				+ "       SGC012 A, SGC001 B, SGC009 C   " 
	    				+ "       WHERE  " 
	    				+ "       A.COMP = B.COMP   " 
	    				+ "       AND A.AREA = B.AREA   " 
	    				+ "       AND A.CODIGO = B.CODIGO  " 
	    				+ "       AND A.COMP = C.COMP   " 
	    				+ "       AND A.AREA = C.AREA   " 
	    				+ "       AND TRIM(C.CODUSER) = '%" + login.toUpperCase() + "%'  " 
	    				+ "       UNION ALL " 
	    				+ "       SELECT  " 
	    				+ "       A.COMP, A.AREA, A.CODIGO || ' - ' || B.NOMIND INDICADOR " 
	    				+ "       FROM  " 
	    				+ "       SGC012 A, SGC001 B  " 
	    				+ "       WHERE  " 
	    				+ "       A.COMP = B.COMP   " 
	    				+ "       AND A.AREA = B.AREA   " 
	    				+ "       AND A.CODIGO = B.CODIGO  " 
	    				+ "       AND A.COMP||A.AREA IN (SELECT COMP||AREA FROM SGC008 WHERE CODUSER = '" + login.toUpperCase() + "' AND DUEPRO = '1')) RES " 
	    				+ " WHERE " 
	    				+ " RES.COMP = '" + veccomp[0].toUpperCase() + "'" 
	    				+ " AND RES.AREA = '" + vecarea[0].toUpperCase() + "'"
	    				+ " ORDER BY 1 " ;


				//System.out.println(querysb);
				//System.out.println(JNDISB);
				List<String> results = new ArrayList<String>();

				consulta.selectPntGenerica(querysb, JNDI);

				rows = consulta.getRows();
				tabla = consulta.getArray();
				for (int i = 0; i < rows; i++) {
					results.add(tabla[i][0]);
				}
				return results;
	    		
	    	}	
	}

	/**
	 * Listas ANOCAL.
	 * 
	 * @throws NamingException
	 * @return List String
	 * @throws IOException
	 **/
	public List<String> completeAnocal(String query) throws NamingException,IOException {
		//System.out.println("entre al metodo nivapp");
		String comp = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("comp"); // Usuario logeado
		String area = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("area"); // Usuario logeado
		String codigo = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("codigo"); // Usuario logeado

		if (comp == null) {
			comp = " - ";
		}
		if (comp == "") {
			comp = " - ";
		}
		if (area == null) {
			area = " - ";
		}
		if (area == "") {
			area = " - ";
		}
		if (codigo == null) {
			codigo= " - ";
		}
		if (codigo == "") {
			codigo = " - ";
		}
		String[] veccomp = comp.split("\\ - ", -1);
		String[] vecarea = area.split("\\ - ", -1);
		String[] veccod = codigo.split("\\ - ", -1);
			
		String querysb = "SELECT A.ANOCAL " 
		        + " FROM SGC012 A "
		        + " WHERE A.COMP = '" + veccomp[0].toUpperCase() + "'"
		        + " AND A.AREA = '" + vecarea[0].toUpperCase() + "'"
		        + " AND A.CODIGO = '" + veccod[0].toUpperCase() + "'"
				+ " GROUP BY A.ANOCAL"
				+ " ORDER BY 1";
		//System.out.println(querysb);
		//System.out.println(login.toUpperCase());
		//System.out.println(JNDISB);
		List<String> results = new ArrayList<String>();

		consulta.selectPntGenerica(querysb, JNDI);

		rows = consulta.getRows();
		tabla = consulta.getArray();
		for (int i = 0; i < rows; i++) {
			results.add(tabla[i][0]);
		}
		return results;
	}
	
	/**
	 * Listas MESCAL.
	 * 
	 * @throws NamingException
	 * @return List String
	 * @throws IOException
	 **/
	public List<String> completeMescal(String query) throws NamingException,IOException {
		//System.out.println("entre al metodo nivapp");
		String comp = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("comp"); // Usuario logeado
		String area = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("area"); // Usuario logeado
		String codigo = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("codigo"); // Usuario logeado
		String anocal = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("anocal"); // Usuario logeado

		if (comp == null) {
			comp = " - ";
		}
		if (comp == "") {
			comp = " - ";
		}
		if (area == null) {
			area = " - ";
		}
		if (area == "") {
			area = " - ";
		}
		if (codigo == null) {
			codigo= " - ";
		}
		if (codigo == "") {
			codigo = " - ";
		}
		if (anocal == null) {
			anocal= " - ";
		}
		if (anocal == "") {
			anocal = " - ";
		}
		String[] veccomp = comp.split("\\ - ", -1);
		String[] vecarea = area.split("\\ - ", -1);
		String[] veccod = codigo.split("\\ - ", -1);
			
		String querysb = "SELECT A.MESCAL " 
		        + " FROM SGC012 A "
		        + " WHERE A.COMP = '" + veccomp[0].toUpperCase() + "'"
		        + " AND A.AREA = '" + vecarea[0].toUpperCase() + "'"
		        + " AND A.CODIGO = '" + veccod[0].toUpperCase() + "'"
		        + " AND A.ANOCAL = '" + anocal.toUpperCase() + "'"
				+ " GROUP BY A.MESCAL"
				+ " ORDER BY 1";
		//System.out.println(querysb);
		//System.out.println(login.toUpperCase());
		//System.out.println(JNDISB);
		List<String> results = new ArrayList<String>();

		consulta.selectPntGenerica(querysb, JNDI);

		rows = consulta.getRows();
		tabla = consulta.getArray();
		for (int i = 0; i < rows; i++) {
			results.add(tabla[i][0]);
		}
		return results;
	}

	/**
	 * Listas MESCAL.
	 * 
	 * @throws NamingException
	 * @return List String
	 * @throws IOException
	 **/
	public List<String> completeAccion(String query) throws NamingException,IOException {
		//System.out.println("entre al metodo nivapp");
		String comp = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("comp"); // Usuario logeado
		String area = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("area"); // Usuario logeado
		String codigo = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("codigo"); // Usuario logeado
		String anocal = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("anocal"); // Usuario logeado
		String mescal = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("mescal"); // Usuario logeado
		
		if (comp == null) {
			comp = " - ";
		}
		if (comp == "") {
			comp = " - ";
		}
		if (area == null) {
			area = " - ";
		}
		if (area == "") {
			area = " - ";
		}
		if (codigo == null) {
			codigo= " - ";
		}
		if (codigo == "") {
			codigo = " - ";
		}
		if (anocal == null) {
			anocal= " - ";
		}
		if (anocal == "") {
			anocal = " - ";
		}
		if (mescal == null) {
			mescal= " - ";
		}
		if (mescal == "") {
			mescal = " - ";
		}
		String[] veccomp = comp.split("\\ - ", -1);
		String[] vecarea = area.split("\\ - ", -1);
		String[] veccod = codigo.split("\\ - ", -1);
			
		String querysb = "SELECT A.ACCION " 
		        + " FROM SGC012A A "
		        + " WHERE A.COMP = '" + veccomp[0].toUpperCase() + "'"
		        + " AND A.AREA = '" + vecarea[0].toUpperCase() + "'"
		        + " AND A.CODIGO = '" + veccod[0].toUpperCase() + "'"
		        + " AND A.ANOCAL = '" + anocal.toUpperCase() + "'"
		        + " AND A.MESCAL = '" + mescal.toUpperCase() + "'"
				+ " GROUP BY A.ACCION"
				+ " ORDER BY 1";
		//System.out.println(querysb);
		//System.out.println(login.toUpperCase());
		//System.out.println(JNDISB);
		List<String> results = new ArrayList<String>();

		consulta.selectPntGenerica(querysb, JNDI);

		rows = consulta.getRows();
		tabla = consulta.getArray();
		for (int i = 0; i < rows; i++) {
			results.add(tabla[i][0]);
		}
		return results;
	}
	
	/**
	 * Lista Reportes.
	 * 
	 * @throws NamingException
	 * @return List String
	 * @throws IOException
	 **/
	public List<String> completeRepo(String query) throws NamingException,
			IOException {

		List<String> results = new ArrayList<String>();

		String queryora = " SELECT C.CODREP||' - '||C.DESREP AS REPORT" 
				        + " FROM BVT001 C, BVT002 A, BVT007 B "
				        + " WHERE A.B_CODROL = B.B_CODROL "
				        + " AND C.CODREP = B.B_CODREP"
						+ " AND A.CODUSER like '%" + login.toUpperCase() + "%'"
						+ " AND C.CODREP || C.DESREP like '%" + query.toUpperCase() + "%'"
						+ " AND C.INSTANCIA = '" + instancia + "'"
						+ " GROUP BY C.CODREP, C.DESREP"
			         	+ " ORDER BY C.CODREP";

		//System.out.println(queryora);

		consulta.selectPntGenerica(queryora,JNDI);

		rows = consulta.getRows();

		tabla = consulta.getArray();

		for (int i = 0; i < rows; i++) {
			results.add(tabla[i][0]);
		}
		return results;
	}
	
	/**
	 * Lista Reportes.
	 * 
	 * @throws NamingException
	 * @return List String
	 * @throws IOException
	 **/
	public List<String> completeTipv(String query) throws NamingException,
			IOException {

		List<String> results = new ArrayList<String>();

		String querysb = " SELECT CODIGO +' - '+ DESCR " 
				       + " FROM R3P.dbo.TUBDER12 "
					   + " WHERE CODIGO + DESCR LIKE '%" + query.toUpperCase() + "%'"
					   + " ORDER BY CODIGO";
		
		//System.out.println(querysb);

		consulta1.selectPntGenericasb(querysb,JNDISB);

		rows = consulta1.getRows();

		tabla1 = consulta1.getArray();

		for (int i = 0; i < rows; i++) {
			results.add(tabla1[i][0]);
		}
		return results;
	}
	
	/**
	 * Lista Reportes.
	 * 
	 * @throws NamingException
	 * @return List String
	 * @throws IOException
	 **/
	public List<String> completeCeno(String query) throws NamingException,
			IOException {

		List<String> results = new ArrayList<String>();

		String querysb = " SELECT CODIGO ||' - '|| DESCR " 
				       + " FROM CST001 "
					   + " WHERE CODIGO || DESCR LIKE '%" + query.toUpperCase() + "%'"
					   + " ORDER BY CODIGO";
		
		//System.out.println(querysb);

		consulta.selectPntGenerica(querysb,JNDI);

		rows = consulta.getRows();

		tabla = consulta.getArray();

		for (int i = 0; i < rows; i++) {
			results.add(tabla[i][0]);
		}
		return results;
	}
	
	/**
	 * Lista Reportes.
	 * 
	 * @throws NamingException
	 * @return List String
	 * @throws IOException
	 **/
	public List<String> completeOrdf(String query) throws NamingException,
			IOException {

		List<String> results = new ArrayList<String>();

		String querysb = " SELECT CODIGO ||' - '|| DESCR " 
				       + " FROM CST002 "
					   + " WHERE CODIGO || DESCR LIKE '%" + query.toUpperCase() + "%'"
					   + " ORDER BY CODIGO";
		
		//System.out.println(querysb);

		consulta.selectPntGenerica(querysb,JNDI);

		rows = consulta.getRows();

		tabla = consulta.getArray();

		for (int i = 0; i < rows; i++) {
			results.add(tabla[i][0]);
		}
		return results;
	}
	
	/**
	 * Lista Reportes.
	 * 
	 * @throws NamingException
	 * @return List String
	 * @throws IOException
	 **/
	public List<String> completeAnoc(String query) throws NamingException,
			IOException {

		List<String> results = new ArrayList<String>();

		String querysb = " SELECT ANOCAL " 
				       + " FROM TUBDER03 "
					   + " WHERE ANOCAL LIKE '%" + query.toUpperCase() + "%'"
					   + " GROUP BY ANOCAL"
					   + " ORDER BY ANOCAL";
		
		//System.out.println(querysb);

		consulta.selectPntGenerica(querysb,JNDI);

		rows = consulta.getRows();

		tabla = consulta.getArray();

		for (int i = 0; i < rows; i++) {
			results.add(tabla[i][0]);
		}
		return results;
	}
	
	/**
	 * Lista Reportes.
	 * 
	 * @throws NamingException
	 * @return List String
	 * @throws IOException
	 **/
	public List<String> completeMesc(String query) throws NamingException,IOException {
		String anocal = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("anocal"); // Usuario logeado
		if (anocal == null) {
			anocal= " - ";
		}
		if (anocal == "") {
			anocal = " - ";
		}

		List<String> results = new ArrayList<String>();

		String querysb = " SELECT MESCAL " 
				       + " FROM TUBDER03 "
					   + " WHERE MESCAL LIKE '%" + query.toUpperCase() + "%'"
					   + " AND ANOCAL = '" + anocal.toUpperCase() + "'"
					   + " GROUP BY MESCAL"
					   + " ORDER BY MESCAL";
		
		//System.out.println(querysb);

		consulta.selectPntGenerica(querysb,JNDI);

		rows = consulta.getRows();

		tabla = consulta.getArray();

		for (int i = 0; i < rows; i++) {
			results.add(tabla[i][0]);
		}
		return results;
	}
	
	/**
	 * Lista Reportes.
	 * 
	 * @throws NamingException
	 * @return List String
	 * @throws IOException
	 **/
	public List<String> completeCia(String query) throws NamingException,IOException {

		List<String> results = new ArrayList<String>();

		String querysb = " SELECT CODCIA ||' - '|| DESCIA " 
				       + " FROM INDT03 "
					   + " WHERE CODCIA || DESCIA LIKE '%" + query.toUpperCase() + "%'"
					   + " GROUP BY CODCIA, DESCIA"
					   + " ORDER BY CODCIA";
		
		//System.out.println(querysb);

		consulta.selectPntGenerica(querysb,JNDI);

		rows = consulta.getRows();

		tabla = consulta.getArray();

		for (int i = 0; i < rows; i++) {
			results.add(tabla[i][0]);
		}
		return results;
	}
	
	/**
	 * Lista Reportes.
	 * 
	 * @throws NamingException
	 * @return List String
	 * @throws IOException
	 **/
	public List<String> completeUbica(String query) throws NamingException,IOException {
	
		String codcia = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("codcia"); // Usuario logeado
		if (codcia == null) {
			codcia= " - ";
		}
		if (codcia == "") {
			codcia = " - ";
		}
		String[] veccia = codcia.split("\\ - ", -1);
		
		List<String> results = new ArrayList<String>();

		String querysb = " SELECT A.CODUBI ||' - '|| A.DESUBI " 
				       + " FROM INDT04 A, INDT06 B "
					   + " WHERE A.CODUBI = B.CODUBI"
					   + " AND A.CODUBI || A.DESUBI LIKE '%" + query.toUpperCase() + "%'"
					   + " AND B.CODCIA = '" + veccia[0].toUpperCase() + "'"
					   + " GROUP BY A.CODUBI, A.DESUBI"
					   + " ORDER BY A.CODUBI";
		
		//System.out.println(querysb);

		consulta.selectPntGenerica(querysb,JNDI);

		rows = consulta.getRows();

		tabla = consulta.getArray();

		for (int i = 0; i < rows; i++) {
			results.add(tabla[i][0]);
		}
		return results;
	}
	
	/**
	 * Lista Reportes.
	 * 
	 * @throws NamingException
	 * @return List String
	 * @throws IOException
	 **/
	public List<String> completeUbicacion(String query) throws NamingException,IOException {
		
		List<String> results = new ArrayList<String>();

		String querysb = " SELECT A.CODUBI ||' - '|| A.DESUBI " 
				       + " FROM INDT04 A"
					   + " WHERE  A.CODUBI || A.DESUBI LIKE '%" + query.toUpperCase() + "%'"
					   + " GROUP BY A.CODUBI, A.DESUBI"
					   + " ORDER BY A.CODUBI";
		
		//System.out.println(querysb);

		consulta.selectPntGenerica(querysb,JNDI);

		rows = consulta.getRows();

		tabla = consulta.getArray();

		for (int i = 0; i < rows; i++) {
			results.add(tabla[i][0]);
		}
		return results;
	}	
	
	/**
	 * Lista Reportes.
	 * 
	 * @throws NamingException
	 * @return List String
	 * @throws IOException
	 **/
	public List<String> completeAnoca(String query) throws NamingException,
			IOException {

		List<String> results = new ArrayList<String>();

		String querysb = " SELECT ANOCAL " 
				       + " FROM TUBDER03A "
					   + " WHERE ANOCAL LIKE '%" + query.toUpperCase() + "%'"
					   + " GROUP BY ANOCAL"
					   + " ORDER BY ANOCAL";
		
		//System.out.println(querysb);

		consulta.selectPntGenerica(querysb,JNDI);

		rows = consulta.getRows();

		tabla = consulta.getArray();

		for (int i = 0; i < rows; i++) {
			results.add(tabla[i][0]);
		}
		return results;
	}
	
	/**
	 * Lista Reportes.
	 * 
	 * @throws NamingException
	 * @return List String
	 * @throws IOException
	 **/
	public List<String> completeSemca(String query) throws NamingException,IOException {
		String anocal = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("anocal"); // Usuario logeado
		if (anocal == null) {
			anocal= "";
		}

		List<String> results = new ArrayList<String>();

		String querysb = " SELECT SEMCAL || ' - ' || TO_CHAR(FECINI,'DD/MM/YY') || ' al ' || TO_CHAR(FECFIN,'DD/MM/YY')" 
				       + " FROM TUBDER03A "
					   + " WHERE SEMCAL LIKE '%" + query.toUpperCase() + "%'"
					   + " AND ANOCAL = '" + anocal.toUpperCase() + "'"
					   + " ORDER BY SEMCAL";
		
		//System.out.println(querysb);

		consulta.selectPntGenerica(querysb,JNDI);

		rows = consulta.getRows();

		tabla = consulta.getArray();

		for (int i = 0; i < rows; i++) {
			results.add(tabla[i][0]);
		}
		return results;
	}
	
	/**
	 * Lista Lineas de produccion.
	 * 
	 * @throws NamingException
	 * @return List String
	 * @throws IOException
	 **/
	public List<String> completeCodlin(String query) throws NamingException,
			IOException {

		List<String> results = new ArrayList<String>();

		String querysb = " SELECT CODLIN||' - '||DESLIN" 
				       + " FROM INDT07 "
					   + " WHERE CODLIN||DESLIN LIKE '%" + query.toUpperCase() + "%'"
					   + " GROUP BY CODLIN, DESLIN"
					   + " ORDER BY CODLIN";
		
		//System.out.println(querysb);

		consulta.selectPntGenerica(querysb,JNDI);

		rows = consulta.getRows();

		tabla = consulta.getArray();

		for (int i = 0; i < rows; i++) {
			results.add(tabla[i][0]);
		}
		return results;
	}
	
	/**
	 * Lista Lineas de rendimiento.
	 * 
	 * @throws NamingException
	 * @return List String
	 * @throws IOException
	 **/
	public List<String> completeCodren(String query) throws NamingException,
			IOException {

		List<String> results = new ArrayList<String>();

		String querysb = " SELECT CODREN||' - '||DESREN" 
				       + " FROM INDT11 "
					   + " WHERE CODREN||DESREN LIKE '%" + query.toUpperCase() + "%'"
					   + " GROUP BY CODREN, DESREN"
					   + " ORDER BY CODREN";
		
		//System.out.println(querysb);

		consulta.selectPntGenerica(querysb,JNDI);

		rows = consulta.getRows();

		tabla = consulta.getArray();

		for (int i = 0; i < rows; i++) {
			results.add(tabla[i][0]);
		}
		return results;
	}
	
	/**
	 * Lista Lineas de rendimiento.
	 * 
	 * @throws NamingException
	 * @return List String
	 * @throws IOException
	 **/
	public List<String> completeSector(String query) throws NamingException,
			IOException {

		List<String> results = new ArrayList<String>();

		String querysb = " SELECT CODSEC||' - '||DESSEC" 
				       + " FROM INDT08 "
					   + " WHERE CODSEC||DESSEC LIKE '%" + query.toUpperCase() + "%'"
					   + " GROUP BY CODSEC, DESSEC"
					   + " ORDER BY CODSEC";
		
		//System.out.println(querysb);

		consulta.selectPntGenerica(querysb,JNDI);

		rows = consulta.getRows();

		tabla = consulta.getArray();

		for (int i = 0; i < rows; i++) {
			results.add(tabla[i][0]);
		}
		return results;
	}
	
	/**
	 * Lista Lineas de rendimiento.
	 * 
	 * @throws NamingException
	 * @return List String
	 * @throws IOException
	 **/
	public List<String> completeCodven(String query) throws NamingException,
			IOException {

		List<String> results = new ArrayList<String>();

		String querysb = " SELECT CODVEN||' - '||DESVEN" 
				       + " FROM INDT09 "
					   + " WHERE CODVEN||DESVEN LIKE '%" + query.toUpperCase() + "%'"
					   + " GROUP BY CODVEN, DESVEN"
					   + " ORDER BY CODVEN";
		
		//System.out.println(querysb);

		consulta.selectPntGenerica(querysb,JNDI);

		rows = consulta.getRows();

		tabla = consulta.getArray();

		for (int i = 0; i < rows; i++) {
			results.add(tabla[i][0]);
		}
		return results;
	}
	
	/**
	 * Lista Lineas de rendimiento.
	 * 
	 * @throws NamingException
	 * @return List String
	 * @throws IOException
	 **/
	public List<String> completeCodinv(String query) throws NamingException,
			IOException {

		List<String> results = new ArrayList<String>();

		String querysb = " SELECT CODINV||' - '||DESINV" 
				       + " FROM INDT10 "
					   + " WHERE CODINV||DESINV LIKE '%" + query.toUpperCase() + "%'"
					   + " GROUP BY CODINV, DESINV"
					   + " ORDER BY CODINV";
		
		//System.out.println(querysb);

		consulta.selectPntGenerica(querysb,JNDI);

		rows = consulta.getRows();

		tabla = consulta.getArray();

		for (int i = 0; i < rows; i++) {
			results.add(tabla[i][0]);
		}
		return results;
	}
	
	public List<String> completeSino(String query) throws NamingException,
	IOException {

List<String> results = new ArrayList<String>();

String querysb = " SELECT '1'||' - '||'SI' MOV FROM DUAL "
			   + " UNION ALL "
			   + " SELECT '0'||' - '||'NO' MOV FROM DUAL ";

//System.out.println(querysb);

consulta.selectPntGenerica(querysb,JNDI);

rows = consulta.getRows();

tabla = consulta.getArray();

for (int i = 0; i < rows; i++) {
	results.add(tabla[i][0]);
}
return results;
}	
	
}
