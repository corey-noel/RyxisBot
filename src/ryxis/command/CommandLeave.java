package ryxis.command;

import org.pircbotx.Channel;
import ryxis.command.core.BaseCommand;
import ryxis.command.core.CommandEvent;
import ryxis.tools.StringTools;

public class CommandLeave extends BaseCommand{
	
	@Override
	public String onCall(CommandEvent event) {
		if (event.getArgs().length == 0) {
			if(!event.isPrivate())
				event.getBot().partChannel(event.getChannel(), event.getBot().master.getDisconnectReason());
		}
		else {
			if (event.getArgs()[0].equalsIgnoreCase("all")) {
				for(Channel channel : event.getBot().getChannels())
					event.getBot().partChannel(channel, event.getBot().master.getDisconnectReason());
				return "Left all rooms.";
			}
			else
				event.getBot().partChannel(event.getBot().getChannel(StringTools.parseChannel(event.getArgs()[0])), event.getBot().master.getDisconnectReason());
		}
		return null;
	}

	@Override
	public String getCommand() {
		return "leave";
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