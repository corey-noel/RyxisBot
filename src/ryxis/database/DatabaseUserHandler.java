package ryxis.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.hsqldb.jdbc.JDBCArrayBasic;


public class DatabaseUserHandler implements DatabaseAccessor {
	
	// nickname, description, species, dispatch, blocked, respondWithNick, useNotice

	private PreparedStatement createUser, getAllUsers, deleteUser, userExists;
	
	private PreparedStatement setUserDescription, setUserSpecies, setUserDispatch, 
			setUserBlocked, setUserrespondWithNick, setUserUseNotice;
	
	private PreparedStatement getUserDescription, getUserSpecies, getUserDispatch, 
			getUserBlocked, getUserrespondWithNick, getUserUseNotice;
	
	private final String serverName;

	
	public DatabaseUserHandler(String server) {	
		serverName = server;
	}
	
	public void setUserDescription(String nick, String value) {
		if (!userExists(nick))
			createUser(nick);	
		try {
			setUserDescription.setString(1, value);
			setUserDescription.setString(2, nick);
			setUserDescription.execute();
			setUserDescription.clearParameters();
		} catch (SQLException e) { e.printStackTrace(); }
	}
	
	public void setUserSpecies(String nick, String value) {
		if (!userExists(nick))
			createUser(nick);
		try {
			setUserSpecies.setString(1, value);
			setUserSpecies.setString(2, nick);
			setUserSpecies.execute();
			setUserSpecies.clearParameters();
		} catch (SQLException e) { e.printStackTrace(); }
	}
	
	public void setUserDispatch(String nick, boolean[] value) {
		if (!userExists(nick))
			createUser(nick);
		Boolean[] objectData = new Boolean[value.length];
		for (int i = 0; i < value.length; i++)
			objectData[i] = value[i];
		try {
			setUserDispatch.setArray(1, new JDBCArrayBasic(objectData, org.hsqldb.types.Type.SQL_VARCHAR_DEFAULT));
			setUserDispatch.setString(2, nick);
			setUserDispatch.execute();
			setUserDispatch.clearParameters();
		} catch (SQLException e) { e.printStackTrace(); }
	}
	
	public void setUserBlocked(String nick, boolean value) {
		if (!userExists(nick))
			createUser(nick);
		try {
			setUserBlocked.setBoolean(1, value);
			setUserBlocked.setString(2, nick);
			setUserBlocked.execute();
			setUserBlocked.clearParameters();
		} catch (SQLException e) { e.printStackTrace(); }
	}
	
	public void setUserrespondWithNick(String nick, boolean value) {
		if (!userExists(nick))
			createUser(nick);
		try {
			setUserrespondWithNick.setBoolean(1, value);
			setUserrespondWithNick.setString(2, nick);
			setUserrespondWithNick.execute();
			setUserrespondWithNick.clearParameters();
		} catch (SQLException e) { e.printStackTrace(); }
	}
	
	public void setUserUseNotice(String nick, boolean value) {
		if (!userExists(nick))
			createUser(nick);
		try {
			setUserUseNotice.setBoolean(1, value);
			setUserUseNotice.setString(2, nick);
			setUserUseNotice.execute();
			setUserUseNotice.clearParameters();
		} catch (SQLException e) { e.printStackTrace(); }
	}
	
	public String getUserDescription(String nick) {
		if (!userExists(nick))
			createUser(nick);
		try {
			getUserDescription.setString(1, nick);
			ResultSet rs = getUserDescription.executeQuery();
			rs.next();
			getUserDescription.clearParameters();
			return rs.getString("description");
		} catch (SQLException e) { 
			e.printStackTrace(); 
			return null;
		}
	}
	
	public String getUserSpecies(String nick) {
		if (!userExists(nick))
			createUser(nick);
		try {
			getUserSpecies.setString(1, nick);
			ResultSet rs = getUserSpecies.executeQuery();
			rs.next();
			getUserSpecies.clearParameters();
			return rs.getString("species");
		} catch (SQLException e) { 
			e.printStackTrace(); 
			return null;
		}
	}
	
	public boolean[] getUserDispatch(String nick) {
		if (!userExists(nick))
			createUser(nick);
		try {
			getUserDispatch.setString(1, nick);
			ResultSet rs = getUserDispatch.executeQuery();
			rs.next();
			getUserDispatch.clearParameters();
			Object[] arr = (Object[]) rs.getArray("dispatch").getArray();
			boolean[] boolArr = new boolean[arr.length];
			for (int i = 0; i < arr.length; i++)
				boolArr[i] = (Boolean) arr[i];
			return boolArr;
		} catch (SQLException e) { 
			e.printStackTrace(); 
			return null;
		}
	}
	
	public boolean getUserBlocked(String nick) {
		if (!userExists(nick))
			createUser(nick);
		try {
			getUserBlocked.setString(1, nick);
			ResultSet rs = getUserBlocked.executeQuery();
			rs.next();
			getUserBlocked.clearParameters();
			return rs.getBoolean("blocked");
		} catch (SQLException e) { 
			e.printStackTrace(); 
			return false;
		}
	}
	
	public boolean getUserRespondWithNick(String nick) {
		if (!userExists(nick))
			createUser(nick);
		try {
			getUserrespondWithNick.setString(1, nick);
			ResultSet rs = getUserrespondWithNick.executeQuery();
			rs.next();
			getUserrespondWithNick.clearParameters();
			return rs.getBoolean("respondWithNick");
		} catch (SQLException e) { 
			e.printStackTrace(); 
			return false;
		}
	}
	
	public boolean getUserUseNotice(String nick) {
		if (!userExists(nick))
			createUser(nick);
		try {
			getUserUseNotice.setString(1, nick);
			ResultSet rs = getUserUseNotice.executeQuery();
			rs.next();
			getUserUseNotice.clearParameters();
			return rs.getBoolean("useNotice");
		} catch (SQLException e) { 
			e.printStackTrace(); 
			return false;
		}
	}
	
	public void createUser(String nick) {
		try {
			createUser.setString(1, nick);
			createUser.execute();
			createUser.clearParameters();
		} catch (SQLException e) { e.printStackTrace(); }
	}
	
	public boolean userExists(String nick) {
		try {
			userExists.setString(1, nick);
			ResultSet rs = userExists.executeQuery();
			userExists.clearParameters();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public ResultSet getAllUsers() {
		try {
			return getAllUsers.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void prepareStatements(Database db) throws SQLException {
		createUser = db.prepareStatement("insert into " + serverName + ".users values(?, '', '', false, false, false, " +
				"array[false, false, false, false, false, false, false, false, false, false, false, false, " +
				"false, false, false, false])");
		getAllUsers = db.prepareStatement("select * from " + serverName + ".users");
		deleteUser = db.prepareStatement("delete from " + serverName + ".users where nickname = ?");
		userExists = db.prepareStatement("select * from " + serverName + ".users where nickname = ?");
		setUserDescription = db.prepareStatement("update " + serverName + ".users set description = ? where nickname = ?");
		setUserSpecies = db.prepareStatement("update " + serverName + ".users set species = ? where nickname = ?");
		setUserDispatch = db.prepareStatement("update " + serverName + ".users set dispatch = ? where nickname = ?");
		setUserBlocked = db.prepareStatement("update " + serverName + ".users set blocked = ? where nickname = ?");
		setUserrespondWithNick = db.prepareStatement("update " + serverName + ".users set respondWithNick = ? where nickname = ?");
		setUserUseNotice = db.prepareStatement("update " + serverName + ".users set useNotice = ? where nickname = ?");
		getUserDescription = db.prepareStatement("select description from " + serverName + ".users where nickname = ?");
		getUserSpecies = db.prepareStatement("select species from " + serverName + ".users where nickname = ?");
		getUserDispatch = db.prepareStatement("select dispatch from " + serverName + ".users where nickname = ?");
		getUserBlocked = db.prepareStatement("select blocked from " + serverName + ".users where nickname = ?");
		getUserrespondWithNick = db.prepareStatement("select respondWithNick from " + serverName + ".users where nickname = ?");
		getUserUseNotice = db.prepareStatement("select useNotice from " + serverName + ".users where nickname = ?");
	}

	@Override
	public void closeStatements() throws SQLException {
		createUser.close();
		getAllUsers.close();
		deleteUser.close();
		userExists.close();
		setUserDescription.close();
		setUserSpecies.close();
		setUserDispatch.close();
		setUserBlocked.close();
		setUserrespondWithNick.close();
		setUserUseNotice.close();
		getUserDescription.close();
		getUserSpecies.close();
		getUserDispatch.close();
		getUserBlocked.close();
		getUserrespondWithNick.close();
		getUserUseNotice.close();
		createUser = null;
		getAllUsers = null;
		deleteUser = null;
		userExists = null;
		setUserDescription = null;
		setUserSpecies = null;
		setUserDispatch = null;
		setUserBlocked = null;
		setUserrespondWithNick = null;
		setUserUseNotice = null;
		getUserDescription = null;
		getUserSpecies = null;
		getUserDispatch = null;
		getUserBlocked = null;
		getUserrespondWithNick = null;
		getUserUseNotice = null;
	}
}