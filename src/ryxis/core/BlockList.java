package ryxis.core;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import org.pircbotx.User;
import org.pircbotx.hooks.ListenerAdapter;
import ryxis.database.DatabaseUserHandler;

public class BlockList extends ListenerAdapter<RyxisBot> {
	
	private Set<User> blockList = new HashSet<User>();
	private RyxisBot bot;
	private DatabaseUserHandler db;
	
	public BlockList(RyxisBot b) {
		bot = b;
		blockList = new HashSet<User>();
		db = bot.databaseUserHandler;
	}
	
	public boolean addUser(User user) {
		int numChanges = 0;
		db.setUserBlocked(user.getNick(), true);
		if (numChanges > 0) {
			blockList.add(user);
			return true;
		} else return false;
	}
	
	public boolean removeUser(User user) {
		int numChanges = 0;
		db.setUserBlocked(user.getNick(), false);
		if (numChanges > 0) {
			blockList.remove(user);
			return true;
		} else return false;

	}
	
	public void loadBlockList() {
		try {
			ResultSet rs = db.getAllUsers();
			while (rs.next())
				if (rs.getBoolean("blocked"))
					blockList.add(bot.getUser(rs.getString("nickname")));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Set<User> getBlockList() {
		return blockList;
	}
	
	public boolean contains(User user) {
		return blockList.contains(user);
	}
}