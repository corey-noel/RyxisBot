package ryxis.command;

import ryxis.command.core.BaseCommand;
import ryxis.command.core.CommandEvent;

public class CommandHi extends BaseCommand {

	@Override
	public String onCall(CommandEvent event) {
		return "Hi" + (event.getUser().equals(event.getBot().master.getMaster()) ? ", Master" : "") + "!";
	}

	@Override
	public String getCommand() {
		return "hi";
	}

	@Override
	public String getHelp() {
		return "Say hi!";
	}
}
