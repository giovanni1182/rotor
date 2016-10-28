package com.gio.crm.model;

import java.util.Collection;
import java.util.Date;

import infra.model.Home;
import infra.sql.SQLQuery;

public class RucCiHomeImpl extends Home implements RucCiHome
{
	/*private Connection connection = null;  
	
	private void conexaoOracle() throws Exception
	{
		try
		{  
		    String driverName = "oracle.jdbc.driver.OracleDriver";  
		    Class.forName(driverName);  
		  
		    String serverName = "10.10.0.26";  
		    String portNumber = "1521";  
		    String sid = "orcl11";
		    String url = "jdbc:oracle:thin:@" + serverName + ":" + portNumber + ":" + sid;
		    String username = "USIS";
		    String password = "Kansas2015";  
		    connection = DriverManager.getConnection(url, username, password);  
		}
		catch (ClassNotFoundException e)
		{  
			System.out.println("1 " + e.getMessage());
			throw new Exception(e.getMessage());  
		}
		catch (SQLException e)
		{  
			System.out.println("2 " + e.getMessage());
			throw new Exception(e.getMessage());
		}
	}*/
	
	/*Función: get_persona_por_documento (<tipo de documento>, <número de documento>)
    Retorna: string delimitado por comillas y punto y coma
    Formato: tipo de persona, país Swift, nombre, apellido, fecha de nacimiento*/
    
	public String obterPessoaPorDoc(String tipoDoc, String numeroDoc) throws Exception
	{
		SQLQuery query = this.getModelManager().createSQLQuery("orcl","select wapps.pkg_get_persona.get_persona_por_documento('"+tipoDoc+"','"+numeroDoc+"') as coluna from dual");
		
		return query.executeAndGetFirstRow().getString("coluna");
	}
	
	/*Función: get_persona_por_nombre (<tipo de persona>,<nombre>,<apellido|NULL>,<fecha de nacimiento|NULL>)
    Retorna: string delimitado por comillas y punto y coma
    Formato: tipo de persona, país Swift, tipo de documento, numero de documento
	Cuando el tipo de persona es 1 (jurídica), apellido y fecha de nacimiento deben ser nulos, caso contrario son obligatorios.*/
	
	public Collection<String> obterPessoaPorNome(String tipoPessoa,String nome, String sobreNome, Date dataNasc) throws Exception
	{
		//wapps.pkg_get_persona.get_persona_por_nombre('1', 'SUPERMERCADO PARANA S.A.', '', null)
		//Resultado:  "1";"PY";"RUC";"80000013-7"
		
		return null;
	}
	
	
}