package ryxis.command;

import ryxis.command.core.BaseCommand;
import ryxis.command.core.CommandEvent;
import ryxis.command.core.CommandHandler;
import ryxis.tools.StringTools;

public class CommandHelp extends BaseCommand {

	@Override
	public String onCall(CommandEvent event) {
		if (event.getArgs().length == 0) {
			if (!event.isPrivate())
				event.respond("Sending output in a private message.");
			
			event.getBot().sendMessage(event.getUser(), "Hi! I'm " + event.getBot().getNick() + 
					", a bot made by " + event.getBot().master.getMaster().getNick()  + "! " 
					+ "To use one of my commands, start a message with '" + CommandHandler.COMMAND_CHAR + "'");
			
			String commandsList = "Commands are as follows (!help <command> for details): ";
			for (BaseCommand currentCommand : event.getBot().commands)
				if (!currentCommand.isHidden())
					commandsList += currentCommand.getCommand() + ", ";
			commandsList = commandsList.substring(0, commandsList.length() - 2) + ".";
			event.getBot().sendMessage(event.getUser(), commandsList);
			event.getBot().sendMessage(event.getUser(), "I will also fetch information for you about pages " +
					"on given sites automatically! (FA, YouTube, and others.)");
		} else {
			for (BaseCommand currentCommand : event.getBot().commands)
				if(!currentCommand.isHidden() && currentCommand.getCommand().equalsIgnoreCase(event.getArgs()[0]))
					return StringTools.capitalize(currentCommand.getCommand()) + ": " + currentCommand.getHelp();
			return "Couldn't find a command by the name of " + event.getArgs()[0] + "!";
		}
		return null;
	}

	@Override
	public String getCommand() {
		return "help";
	}

	@Override
	public String getHelp() {
		return "Use with a specific command to get help with that command. Use by itself to get a list of all commands.";
	}
}
