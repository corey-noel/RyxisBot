package ryxis.command;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import ryxis.command.core.BaseCommand;
import ryxis.command.core.CommandEvent;
import ryxis.database.DatabaseUserHandler;
import ryxis.tools.CollectionTools;
import ryxis.tools.CollectionTools.EmptyListException;
import ryxis.tools.CollectionTools.ListException;
import ryxis.tools.CollectionTools.ListLengthException;


public class CommandDescription extends BaseCommand {
	
	private static String ERR = "There was an issue! D:";
	private static final int PAGE_SIZE = 8;	
	
	@Override
	public String onCall(CommandEvent event) {
		String nick = event.getUser().getNick();
		DatabaseUserHandler db = event.getBot().databaseUserHandler;
			
		// del subcommand
		if (event.getArgs()[0].equalsIgnoreCase("del") || event.getArgs()[0].equalsIgnoreCase("delete")) {
			if (db.getUserDescription(nick) == null)
				return "No description with your nick found!";
			db.setUserDescription(nick, null);
			return "Description deleted!";
			
		// edit subcommand	
		} else if (event.getArgs()[0].equalsIgnoreCase("edit")) {
			db.setUserDescription(nick, event.getMessageSansCommand().substring(5));
			return "Description edited!";
			
		// fetch subcommand
		} else if (event.getArgs()[0].equalsIgnoreCase("fetch") || event.getArgs()[0].equalsIgnoreCase("get")) {
			if (event.getArgs().length < 2)
				return getHelp();
			if (!db.userExists(event.getArgs()[1]))
				return "No such description found! (" + event.getArgs()[1] + ")";
			String desc = db.getUserDescription(event.getArgs()[1]);
			if (desc == null)
				return "No such description found! (" + event.getArgs()[1] + ")";
			return desc;
			
		} else if (event.getArgs()[0].equalsIgnoreCase("list")) {
			if (event.getArgs().length < 2)
				return list(1, db.getAllUsers());
			else 
				try {
					return list(Integer.parseInt(event.getArgs()[1]), db.getAllUsers());
				} catch (NumberFormatException e) {}
		}
		return getHelp();
	}

	@Override
	public String getCommand() {
		return "description";
	}

	@Override
	public String getHelp() {
		return "Store and fetch user information! Usage: [del|edit|fetch|list] (You do not need to enter your nick for any sub-command.)";
	}
	
	@Override
	public int getMinArgs() {
		return 1;
	}
	
	@Override
	public String getUnderArgsMessage() {
		return getHelp();
	}
		
	private String list(int page, ResultSet rs) {
		ArrayList<String> descriptions = new ArrayList<String>();
		try {
			while (rs.next())
				descriptions.add(rs.getString("nickname") + ": " + rs.getString("description"));
		} catch (SQLException e) {
			e.printStackTrace();
			return ERR;
		}
		
		Collections.sort(descriptions);
		
		try {
			return CollectionTools.list(descriptions, page, PAGE_SIZE);
		} catch (ListException e) {
			if (e instanceof EmptyListException)
				return "There are no descriptions entered!";
			if (e instanceof ListLengthException)
				return "There aren't that many descriptions!";
			return "Really? Page " + page + "?";
		}
	}
}