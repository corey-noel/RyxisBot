package ryxis.command;

import java.util.Set;
import org.pircbotx.User;
import ryxis.command.core.BaseCommand;
import ryxis.command.core.CommandEvent;


public class CommandIgnore extends BaseCommand {
	
	@Override
	public String onCall(CommandEvent event) {
		if (event.getArgs()[0].equalsIgnoreCase("add")) {
			if (event.getArgs().length < 2)
				return getHelp();
			boolean success = event.getBot().blockList.addUser(event.getBot().getUser(event.getArgs()[1]));
			return success ? "User " + event.getArgs()[1] + " will now be ignored!" : "User " + event.getArgs()[1] + " was not ignored!";
		} else if (event.getArgs()[0].equalsIgnoreCase("remove")) {
			if (event.getArgs().length < 2)
				return getHelp();
			boolean success = event.getBot().blockList.removeUser(event.getBot().getUser(event.getArgs()[1]));
			return success ? "User " + event.getArgs()[1] + " will no longer be ignored!" : "User " + event.getArgs()[1] + " was not unignored!";
		} else if (event.getArgs()[0].equalsIgnoreCase("list")) {
			Set<User> blockedUsers = event.getBot().blockList.getBlockList();
			if (blockedUsers.size() == 0)
				return "No users blocked!";
			String userList = "Blocked users: ";
			for (User user : blockedUsers)
				userList += user.getNick() + " ";
			return userList;
		} return getHelp();
	}
	
	@Override
	public String getCommand() {
		return "ignore";
	}
	
	@Override
	public String getHelp() {
		return "Usage: !ignore [add|remove|list] <user>";
	}
	
	@Override
	public boolean isMasterOnly() {
		return true;
	}
	
	@Override
	public boolean isHidden() {
		return true;
	}
	
	@Override
	public int getMinArgs() {
		return 1;
	}
	
	@Override
	public String getUnderArgsMessage() {
		return getHelp();
	}	
}
