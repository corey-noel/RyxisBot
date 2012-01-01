package ryxis.database;

import java.sql.SQLException;

public interface DatabaseAccessor{	
	public void prepareStatements(Database db) throws SQLException;
	public void closeStatements() throws SQLException;
}
