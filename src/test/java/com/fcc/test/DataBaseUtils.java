package com.fcc.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.tavant.base.WebPage;
import com.tavant.utils.PropertiesUtil;

public class DataBaseUtils extends WebPage{


	private static DataBaseUtils _instance = null;
	private Connection con = null;
	private final String DB_PROPS_FILE =System.getProperty("user.dir")+"\\"+ "db.properties";
	private static Properties dbprops = new Properties();
	
	public void loadDBProperties() throws IOException {
       /* ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream file = loader.getResourceAsStream(DB_PROPS_FILE);*/
		FileInputStream fileInput = new FileInputStream(DB_PROPS_FILE);
		dbprops.load(fileInput);        
}


	/*
	 * Initiating the Data base connection by using the property values from
	 * resource package.
	 */
	
	public Connection init()
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, IOException {

		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");


			loadDBProperties();
			//String env = getEnvironment();
			String env = dbprops.getProperty("Env").trim();
			String dbserver = dbprops.getProperty("DBserver_"+env).trim();
			String dbname = dbprops.getProperty("DBName_"+env).trim();
			String dbuname = dbprops.getProperty("DBusername_"+env).trim();
			String dbpass = dbprops.getProperty("DBpassword_"+env).trim();
			String url = "jdbc:sqlserver://"+dbserver+";databaseName="+dbname;
			System.out.println("url>>" + url);
			return this.con = DriverManager.getConnection(url, dbuname, dbpass);

		
	}
	public Connection initEncompassDatabase()
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, IOException {

		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");


			//loadDBProperties();
			Properties props = new Properties();		    
			String propFilePath = System.getProperty("user.dir")+"\\"+"db.properties";
			FileInputStream fileInput = new FileInputStream(propFilePath);
			props.load(fileInput);
			
			
						
			String env =props.getProperty("Env").trim();
			String dbserver = props.getProperty("DBserver_"+env).trim();
			//dbserver=dbserver.replace("-","\\");
			String dbname = props.getProperty("DBName_"+env+"_Encompass").trim();
			String dbuname = props.getProperty("DBusername_"+env).trim();
			String dbpass = props.getProperty("DBpassword_"+env).trim();
			String url = "jdbc:sqlserver://"+dbserver+";databaseName="+dbname;
			System.out.println("url>>" + url);
			return this.con = DriverManager.getConnection(url, dbuname, dbpass);

		
	}
	
	/**
	 * Initialize DB for a specific db name. Pass parameter dbname
	 * @return 
	 * 
	 */
	public Connection initForDbName(String dbname)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, IOException {

		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");


		loadDBProperties();
			String env = dbprops.getProperty("Env").trim();
			String dbserver = dbprops.getProperty("DBserver_"+env).trim();
//			String dbname = dbprops.getProperty("DBName_"+env).trim();
			String dbuname = dbprops.getProperty("DBusername_"+env).trim();
			String dbpass = dbprops.getProperty("DBpassword_"+env).trim();
			String url = "jdbc:sqlserver://"+dbserver+";databaseName="+dbname;
			System.out.println("url>>" + url);
			return this.con = DriverManager.getConnection(url, dbuname, dbpass);

		
	}
	
	public String getEnvironment() {
		String url = PropertiesUtil.getProperty("url");
		
		String env = null;
		if (url.contains("qch2o"))
			env = "UAT";
		if (url.contains("uat2h2o"))
			env = "UAT2";
		if(url.contains("cnvd"))
			env = "SIT";
		if(url.contains("uat3h2o"))
			env = "UAT3";
		
		System.out.println("ENVIRONMENT == "+env);
		return env;
	}
	
	public String getDatabaseEnvironment() {
		String url = PropertiesUtil.getProperty("url");
		
		String env = null;
		if (url.contains("qch2o"))
			env = "UAT1";
		if (url.contains("uat2h2o"))
			env = "UAT2";
		if(url.contains("cnvd"))
			env = "SIT";
		if(url.contains("uat3h2o"))
			env = "UAT3";
		
		System.out.println("ENVIRONMENT == "+env);
		return env;
	}
	String getSingleDBValue(Connection con,String query) throws SQLException
	{
	String val = null;
	Statement st = null;
	ResultSet rs = null;
	try{
			if (con != null) 
			{
				st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
				rs = st.executeQuery(query);			
			}
			if(rs.next()) 
			{
				val=rs.getString(1);
			}
		}
	catch(Exception e)
	{
	System.out.println(e);
	}
	finally
	{
		st.close();
		rs.close();		
	}

	return val;
	}
	List<String> getResultList(Connection con,String query) throws SQLException
	{
		Statement st = null;
		ResultSet rs = null;
		ResultSetMetaData mt = null;
		
		List<String> ls=new ArrayList<String>();
		try{
			if (con != null) 
			{
				st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
				rs = st.executeQuery(query);
				mt=rs.getMetaData();
			}
			if(rs.next()) 
			{
				for(int i=1;i<=4;i++)
					ls.add(rs.getString(i));				
			}
		}
	catch(Exception e)
	{
	System.out.println(e);
	}
	finally
	{
		st.close();
		rs.close();		
	}
		return ls;
	}
	@Override
	public void checkPage() {
		// TODO Auto-generated method stub
		
	}

}
