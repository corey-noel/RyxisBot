package ryxis.command;

import ryxis.command.core.BaseCommand;
import ryxis.command.core.CommandEvent;
import ryxis.tools.FileTools;

public class CommandSafeword extends BaseCommand {

	@Override
	public String onCall(CommandEvent event) {
		return "Your safeword is: " + FileTools.randomLine("words/safeword");
	}

	@Override
	public String getCommand() {
		return "safeword";
	}

	@Override
	public String getHelp() {
		return "For safety purposes.";
	}

}
