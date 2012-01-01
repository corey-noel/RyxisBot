package ryxis.command;

import ryxis.command.core.BaseCommand;
import ryxis.command.core.CommandEvent;
import ryxis.core.RyxisBot;

public class CommandQuit extends BaseCommand {

	@Override
	public String onCall(CommandEvent event) {
		RyxisBot.output("Quitting the server from command.");	
		event.getBot().disconnect(); 
		return null;
	}

	@Override
	public String getCommand() {
		return "quit";
	}

	@Override
	public String getHelp() {
		return null;
	}
	
	@Override
	public boolean isMasterOnly() {
		return true;
	}
	
	@Override
	public boolean isHidden() {
		return true;
	}
}