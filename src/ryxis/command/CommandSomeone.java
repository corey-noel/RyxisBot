package ryxis.command;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import org.pircbotx.User;
import ryxis.command.core.BaseCommand;
import ryxis.command.core.CommandEvent;

public class CommandSomeone extends BaseCommand {

	@Override
	public String onCall(CommandEvent event) {
		Random r = new Random();
		if (!event.isPrivate()) {
			if (event.getArgs().length == 0) {
				Set<User> activeUsers = event.getBot().roomHandler.getRoom(event.getChannel()).getActiveUsers(300000L);
				activeUsers.add(event.getUser());
				activeUsers.add(event.getBot().getUserBot());
				if(event.getChannel().getUsers().contains(event.getBot().master.getMaster()));
					activeUsers.add(event.getBot().master.getMaster());
				return ((User) activeUsers.toArray()[(r.nextInt(activeUsers.size()))]).getNick();
			} else if(event.getArgs()[0].toLowerCase().startsWith("a")) {
				Set<User> users = event.getChannel().getUsers();
				return ((User) users.toArray()[(r.nextInt(users.size()))]).getNick();
			} else if(event.getArgs()[0].toLowerCase().startsWith("v")) {
				Set<User> tempUsers = event.getChannel().getUsers();
				Set<User> users = new HashSet<User>(tempUsers);
				for (User user : tempUsers)
					if(!event.getChannel().hasVoice(user))
						users.remove(user);
				if (users.size() == 0)
					return "No users with voice!";
				return ((User) users.toArray()[(r.nextInt(users.size()))]).getNick();
			}
		} 
		return null;
	}

	@Override
	public String getCommand() {
		return "someone";
	}

	@Override
	public String getHelp() {
		return "Pick a random user from the room. Only chooses active users by default. Type \"all\" or \"voice\" for the respective options.";
	}

}
