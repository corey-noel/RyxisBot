package ryxis.database;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;
import ryxis.core.Main;
import ryxis.core.RyxisBot;

public class Database {
		
	public static final String link = "jdbc:hsqldb:file:data/db";
	private static Connection con;
	private String server;
	
	public Database(String serverName) {
		server = serverName;
	}
	
	public void connect() {
		if (con == null) {
			RyxisBot.output("Looking for database at " + link + "...");	
			deleteLockFile();
		
			try {
				con = DriverManager.getConnection(link, "SA", "");
				con.setAutoCommit(true);
				RyxisBot.output("Connected to database.");	
			} catch (SQLException e) {
				System.err.println("Could not connect to the database at " + link + ".");
				e.printStackTrace();
				Main.quitAll();
			} 
		
			for (DatabaseAccessor da : accessors)
				try {
					da.prepareStatements(this);
				} 
			catch(SQLException e) { e.printStackTrace(); }
			
			/*
			try {
				executeScript("scripts/createUserTable");
			} catch (FileNotFoundException e) {
				System.err.println("Could not connect to the database at " + link + ".");
				e.printStackTrace();
				bot.quitServer();
				System.exit(1);
			} catch (SQLException e) {
				System.err.println("Could not connect to the database at " + link + ".");
				e.printStackTrace();
				bot.quitServer();
				System.exit(1);
			}
		*/
		} else {
			RyxisBot.output("Already connected!");
		}
	}	
	
	public static void disconnect() {
		RyxisBot.output("Disconnecting from the database at " + link);	
		try {
			con.commit();
			con.close();
			for (DatabaseAccessor da : bot.accessors)
				da.closeStatements();
		} catch (SQLException e) {
			RyxisBot.output("There was an issue disconnecting from the database at " + link + ".");
			e.printStackTrace();
		}			
		deleteLockFile();
	}
		
	public static PreparedStatement prepareStatement(String statement) throws SQLException {
		return con.prepareStatement(statement);
	}
	
	private static boolean deleteLockFile() {
		try {
			File lck = new File(link + ".lck");
			return lck.delete();
		} catch (Exception e) {
			e.printStackTrace();
			return false;	
		}
	}
	
	public static void executeScript(String fileName) throws FileNotFoundException, SQLException {
		String sql = "";
		Scanner sc = new Scanner(new File(fileName));
		while (sc.hasNextLine())
			sql += sc.nextLine();
		con.prepareCall(sql).execute();
	}
}