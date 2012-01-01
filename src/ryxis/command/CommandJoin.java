package ryxis.command;

import ryxis.command.core.BaseCommand;
import ryxis.command.core.CommandEvent;
import ryxis.tools.StringTools;

public class CommandJoin extends BaseCommand{

	@Override
	public String onCall(CommandEvent event) {
		if (event.getArgs().length > 0)
			event.getBot().joinChannel(StringTools.parseChannel(event.getArgs()[0]));
		return null;
	}

	@Override
	public String getCommand() {
		return "join";
	}

	@Override
	public String getHelp() {
		return null;
	}
	
	@Override
	public boolean isHidden() {
		return true;
	}
	
	@Override
	public boolean isMasterOnly() {
		return true;
	}
}
