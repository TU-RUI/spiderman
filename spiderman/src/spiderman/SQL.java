package spiderman;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQL {
	public static Connection c = null;
	public static Statement s = null;
	
	public SQL(){
		try {
			//com.mysql.jdbc.Driver
			
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			c = DriverManager.getConnection("jdbc:mysql://localhost:3306/mysql","root","root");
			System.out.println(c==null?"c is ull":"c is not null");
			s = c.createStatement();
			System.out.println(s==null?"s is ull":"s is not null");
			s.executeUpdate("create database test");
//			Class.forName("org.sqlite.JDBC");
//			c = DriverManager.getConnection("jdbc:sqlite:info.db");
//			s = c.createStatement();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public void CreateTable(){
		String sql = "create table if not exists info(_id integer primary key autoincrement , name text , password text)";
		try {
			System.out.println( (s == null) + "");
			s.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void Insert(String name , String password){
		String sql = "insert into info(name , password) values ('" + name + "'" + " , '" + password +"')";
		try {
			s.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String GetName(int index){
		String sql = "select name from info where _id = " + index;
		String name = "";
		try {
			ResultSet rs = s.executeQuery(sql);
			if(rs.next()){
				name = rs.getString(1);
			}else{
				return null;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return name;
	}
	
	public String GetPassword(int index){
		String sql = "select password from info where _id = " + index;
		String password = "";
		try {
			ResultSet rs = s.executeQuery(sql);
			if(rs.next()){
				password = rs.getString(1);
			}else{
				return null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return password;
	}
	
	
}
