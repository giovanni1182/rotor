package com.gio.crm.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Conexao 
{
		static final String url = "jdbc:Microsoft:sqlserver://Giovanni:1433;DatabaseName=bd_bcp";
		static 
		{
			try 
			{
				Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver");
			}
			catch (ClassNotFoundException ex) 
			{
				System.out.println(ex.getMessage());
			}
		}
		
		public static void main(String[] args) 
		{
			Connection conn = null;
			try 
			{
				conn = DriverManager.getConnection(url,"sa","");
				Statement stmt = conn.createStatement();
				java.sql.ResultSet rs = stmt.executeQuery("select * from entidade");
				while (rs.next()) 
				{
					
					System.out.println(rs.getString(1) + ',' + rs.getString(2) + ',' +
							rs.getString(3));
				}
				rs.close();
				stmt.close();
			}
			catch (SQLException ex) 
			{
				System.out.println("error:" + ex.getMessage());
			}
			finally 
			{
				try 
				{
					conn.close();
				}
				catch (SQLException ignored) 
				{
				}
			}
		}
	}
