package ryxis.command;

import ryxis.command.core.BaseCommand;
import ryxis.command.core.CommandEvent;
import ryxis.tools.FileTools;

public class CommandFortune extends BaseCommand {

	@Override
	public String onCall(CommandEvent event) {
		return FileTools.randomLine("words/fortune");
	}

	@Override
	public String getCommand() {
		return "fortune";
	}

	@Override
	public String getHelp() {
		return "Pop open a cookie! Get a fortune! And a cookie! :D There isn't really a cookie.";
	}

}
